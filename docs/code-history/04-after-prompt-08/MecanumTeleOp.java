package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Iterative TeleOp OpMode for a Mecanum drive FTC robot.
 * Controls drive motors with gamepad1 joysticks, flywheel & intake subsystems
 * with gamepad1 buttons (A, B, X, DPad Up, DPad Down), continuous rotation intake servos,
 * goBILDA undercarriage LED lights for state feedback, and match time warnings with gamepad rumble.
 */
@TeleOp(name = "Mecanum TeleOp", group = "Iterative Opmode")
public class MecanumTeleOp extends OpMode {

    // Drive Motors
    private DcMotor frontLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor backRightDrive = null;

    // Subsystems & Controllers
    private Flywheel flywheel;
    private Intake intake;
    private LedController ledController;

    // Reverse Mode State (X Button)
    private boolean isReversed = false;
    private boolean previousXState = false;

    // Match Timer & Warning States
    private ElapsedTime matchTimer;
    private boolean warned30Sec = false;
    private boolean warned10Sec = false;
    private double matchSeconds = 0.0;
    private double targetLedColor = LedController.COLOR_BLUE;

    // Loop variables declared as class fields to prevent re-allocation each loop
    private double y = 0.0;
    private double x = 0.0;
    private double rx = 0.0;

    private double frontLeftPower = 0.0;
    private double backLeftPower = 0.0;
    private double frontRightPower = 0.0;
    private double backRightPower = 0.0;
    private double maxPower = 0.0;

    private boolean xPressed = false;
    private boolean flywheelButtonPressed = false;
    private boolean intakeButtonPressed = false;

    @Override
    public void init() {
        // Initialize LED controller (Sets LEDs to Yellow for "Init Started")
        ledController = new LedController();
        ledController.init(hardwareMap);

        // Initialize drive motors using required names
        frontLeftDrive = hardwareMap.get(DcMotor.class, "front_left_drive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "front_right_drive");
        backLeftDrive = hardwareMap.get(DcMotor.class, "back_left_drive");
        backRightDrive = hardwareMap.get(DcMotor.class, "back_right_drive");

        // Set motor directions (Left side reversed for forward driving)
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);

        // Set zero power behavior for precise stopping
        frontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Initialize flywheel and intake subsystems
        flywheel = new Flywheel();
        flywheel.init(hardwareMap);

        intake = new Intake();
        intake.init(hardwareMap);

        // Initialize Match Timer
        matchTimer = new ElapsedTime();

        // Signal "Init Completed" with Green LEDs
        ledController.setColor(LedController.COLOR_GREEN);
        telemetry.addData("Status", "Init Completed (Ready for Start)");
    }

    @Override
    public void start() {
        // Reset match timer and warning flags when START is pressed
        matchTimer.reset();
        warned30Sec = false;
        warned10Sec = false;

        // Set LEDs to Blue for TeleOp active
        ledController.setColor(LedController.COLOR_BLUE);
    }

    @Override
    public void loop() {
        // --- 1. Mecanum Drive Control ---
        // Left joystick: forward/back (Y) & strafe left/right (X)
        // Right joystick: turn left/right (X)
        y = -gamepad1.left_stick_y;     // Note: gamepad Y stick is inverted (negative up)
        x = gamepad1.left_stick_x * 1.1; // 1.1 multiplier counteracts imperfect strafing
        rx = gamepad1.right_stick_x;

        // Calculate individual motor powers for mecanum kinematics
        frontLeftPower = y + x + rx;
        backLeftPower = y - x + rx;
        frontRightPower = y - x - rx;
        backRightPower = y + x - rx;

        // Normalize motor powers so none exceed 1.0, then scale to max 0.8 power
        maxPower = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
        maxPower = Math.max(maxPower, Math.abs(backLeftPower));
        maxPower = Math.max(maxPower, Math.abs(backRightPower));

        if (maxPower > 1.0) {
            frontLeftPower /= maxPower;
            frontRightPower /= maxPower;
            backLeftPower /= maxPower;
            backRightPower /= maxPower;
        }

        // Scale drive motor powers to cap max speed at 0.8
        frontLeftPower *= 0.8;
        frontRightPower *= 0.8;
        backLeftPower *= 0.8;
        backRightPower *= 0.8;

        // Output power to drive motors
        frontLeftDrive.setPower(frontLeftPower);
        frontRightDrive.setPower(frontRightPower);
        backLeftDrive.setPower(backLeftPower);
        backRightDrive.setPower(backRightPower);

        // --- 2. Reverse Mode (X Button) Handling ---
        xPressed = gamepad1.x && !previousXState;
        previousXState = gamepad1.x;

        if (xPressed) {
            isReversed = !isReversed;
        }

        // --- 3. Subsystem Updates ---
        // Pass gamepad and isReversed state to flywheel and intake
        flywheelButtonPressed = flywheel.update(gamepad1, isReversed);
        intakeButtonPressed = intake.update(gamepad1, isReversed);

        // If another button (A, B, DPad UP, DPad DOWN) is hit while in reverse mode,
        // reverse mode stops and that button's action takes effect immediately.
        if (isReversed && (flywheelButtonPressed || intakeButtonPressed)) {
            isReversed = false;
            // Re-update subsystems without reverse mode active to apply their normal target powers
            flywheel.update(gamepad1, false);
            intake.update(gamepad1, false);
        }

        // --- 4. Match Time Warnings & Rumble ---
        matchSeconds = matchTimer.seconds();

        // 30-second warning (at 90 seconds in 120s match)
        if (matchSeconds >= 90.0 && !warned30Sec) {
            gamepad1.rumbleBlips(2);
            warned30Sec = true;
        }

        // 10-second warning (at 110 seconds in 120s match)
        if (matchSeconds >= 110.0 && !warned10Sec) {
            gamepad1.rumble(1000); // Rumble for 1 second
            warned10Sec = true;
        }

        // --- 5. Global Undercarriage LED Feedback ---
        if (matchSeconds >= 110.0) {
            // Final 10 seconds warning: Red
            targetLedColor = LedController.COLOR_RED;
        } else if (matchSeconds >= 90.0) {
            // 30 seconds remaining endgame alert: Orange
            targetLedColor = LedController.COLOR_ORANGE;
        } else if (isReversed) {
            // Ball Rejection / Reverse Mode active: Red
            targetLedColor = LedController.COLOR_RED;
        } else if (flywheel.isOn() && intake.isOn()) {
            // Both subsystems active: Orange
            targetLedColor = LedController.COLOR_ORANGE;
        } else if (flywheel.isOn()) {
            // Flywheel active only: Purple
            targetLedColor = LedController.COLOR_PURPLE;
        } else if (intake.isOn()) {
            // Intake active only: Green
            targetLedColor = LedController.COLOR_GREEN;
        } else {
            // Idle / Normal driving: Blue
            targetLedColor = LedController.COLOR_BLUE;
        }

        ledController.setColor(targetLedColor);

        // --- 6. Telemetry Output ---
        telemetry.addData("Status", "Running");
        telemetry.addData("Time Remaining", "%d sec", Math.max(0, 120 - (int) matchSeconds));
        telemetry.addData("Drive Motors", "FL: %.2f | FR: %.2f | BL: %.2f | BR: %.2f",
                frontLeftPower, frontRightPower, backLeftPower, backRightPower);
        telemetry.addData("Reverse Mode (X)", isReversed ? "ACTIVE (-0.3)" : "OFF");
        telemetry.addData("Flywheel", "ON: %b | Target: %.2f | Current: %.2f",
                flywheel.isOn(), flywheel.getTargetPower(), flywheel.getCurrentPower());
        telemetry.addData("Intake", "ON: %b | Power: %.2f | Current: %.2f",
                intake.isOn(), intake.getPower(), intake.getCurrentPower());
        telemetry.addData("LED Color Position", "%.2f", ledController.getCurrentPosition());
        telemetry.update();
    }
}
