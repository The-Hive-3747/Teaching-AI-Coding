package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Iterative TeleOp OpMode for a Mecanum drive FTC robot.
 * Controls drive motors with Tank Drive + Strafe kinematics (Left Stick Y = Left Motors,
 * Right Stick Y = Right Motors, Both Sticks X = Strafe Left/Right), subsystems (flywheel & intake)
 * with gamepad1 controls (A for Intake Collect, B for Flywheel Startup Sequence [200ms reverse -> 300ms pause -> forward spool],
 * Right Bumper for Feed, Left Bumper/X for Reject, DPad Up/Down for Flywheel Speed),
 * goBILDA undercarriage LED lights for state feedback, and FTC rule-compliant 2Hz flashing endgame warnings.
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

    // Reverse / Reject Mode State (Left Bumper or X Button)
    private boolean isReversed = false;
    private boolean previousReverseButtonState = false;

    // Match Timer & Warning States
    private ElapsedTime matchTimer;
    private boolean warned30Sec = false;
    private boolean warned10Sec = false;

    // Working variables declared as class fields to prevent re-allocation inside loop
    private double matchSeconds = 0.0;
    private double targetLedColor = LedController.COLOR_BLUE;
    private boolean flashState = false;
    private boolean reverseButtonPressed = false;

    // Tank Drive Kinematic Variables
    private double leftY = 0.0;
    private double rightY = 0.0;
    private double strafe = 0.0;

    private double frontLeftPower = 0.0;
    private double backLeftPower = 0.0;
    private double frontRightPower = 0.0;
    private double backRightPower = 0.0;
    private double maxPower = 0.0;

    private boolean flywheelButtonPressed = false;
    private boolean intakeButtonPressed = false;

    @Override
    public void init() {
        // Initialize LED controller (Sets LEDs to Yellow for "Init Started")
        ledController = new LedController();
        ledController.init(hardwareMap);

        // Initialize drive motors
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

        // Initialize subsystems
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

        // Explicitly stop subsystems when START is hit
        if (flywheel != null) {
            flywheel.stop();
        }
        if (intake != null) {
            intake.stop();
        }

        // Set LEDs to Blue for TeleOp active
        ledController.setColor(LedController.COLOR_BLUE);
    }

    @Override
    public void loop() {
        // --- 1. Mecanum Tank Drive + Strafe Control ---
        leftY = -gamepad1.left_stick_y;     // Note: gamepad Y stick is inverted (negative up)
        rightY = -gamepad1.right_stick_y;    // Note: gamepad Y stick is inverted (negative up)
        strafe = ((gamepad1.left_stick_x + gamepad1.right_stick_x) / 2.0) * 1.1; // Average X input with 1.1 multiplier

        frontLeftPower = leftY + strafe;
        backLeftPower = leftY - strafe;
        frontRightPower = rightY - strafe;
        backRightPower = rightY + strafe;

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

        frontLeftPower *= 0.8;
        frontRightPower *= 0.8;
        backLeftPower *= 0.8;
        backRightPower *= 0.8;

        frontLeftDrive.setPower(frontLeftPower);
        frontRightDrive.setPower(frontRightPower);
        backLeftDrive.setPower(backLeftPower);
        backRightDrive.setPower(backRightPower);

        // --- 2. Reverse / Reject Mode (Left Bumper or X Button) Handling ---
        reverseButtonPressed = (gamepad1.left_bumper || gamepad1.x);
        if (reverseButtonPressed && !previousReverseButtonState) {
            isReversed = !isReversed;
        }
        previousReverseButtonState = reverseButtonPressed;

        // --- 3. Subsystem Updates ---
        flywheelButtonPressed = flywheel.update(gamepad1, isReversed);
        intakeButtonPressed = intake.update(gamepad1, isReversed, flywheel.isReversing());

        // If another button (A, B, DPad UP, DPad DOWN) is hit while in reverse mode,
        // reverse mode stops and that button's action takes effect immediately.
        if (isReversed && (flywheelButtonPressed || intakeButtonPressed)) {
            isReversed = false;
            flywheel.update(gamepad1, false);
            intake.update(gamepad1, false, false);
        }

        // --- 4. Match Time Warnings & Rumble Alerts ---
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
        // 2 Hz flashing calculation (250 ms ON, 250 ms OFF) complying with FTC safety rules
        flashState = ((int)(matchSeconds * 4) % 2) == 0;

        if (matchSeconds >= 110.0 && matchSeconds < 114.0) {
            // 10s Warning: Flash Red / Orange at 2 Hz for 4 seconds
            targetLedColor = flashState ? LedController.COLOR_RED : LedController.COLOR_ORANGE;
        } else if (matchSeconds >= 90.0 && matchSeconds < 94.0) {
            // 30s Warning: Flash Orange / White at 2 Hz for 4 seconds
            targetLedColor = flashState ? LedController.COLOR_ORANGE : LedController.COLOR_WHITE;
        } else if (intake.isFeeding()) {
            // Feeding / Shooting into Flywheel (Right Bumper held): White
            targetLedColor = LedController.COLOR_WHITE;
        } else if (isReversed || flywheel.isReversing()) {
            // Reverse Mode or 200 ms Flywheel Reverse Bump Active: Red
            targetLedColor = LedController.COLOR_RED;
        } else if (flywheel.isPausing()) {
            // 300 ms Pause Phase: Orange
            targetLedColor = LedController.COLOR_ORANGE;
        } else if (flywheel.isOn() && intake.isCollectOn()) {
            // Both Flywheel & Intake Collect active: Orange
            targetLedColor = LedController.COLOR_ORANGE;
        } else if (flywheel.isOn()) {
            // Flywheel running forward: Purple
            targetLedColor = LedController.COLOR_PURPLE;
        } else if (intake.isCollectOn()) {
            // Intake Collect active: Green
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
        telemetry.addData("Feed / Shoot (RB)", intake.isFeeding() ? "ACTIVE (0.50)" : "OFF");
        telemetry.addData("Reverse Mode (LB/X)", isReversed ? "ACTIVE (-0.3)" : "OFF");
        telemetry.addData("Flywheel Phase", "%s | Target: %.2f",
                flywheel.getPhase().toString(), flywheel.getTargetPower());
        telemetry.addData("Intake Power", "%.2f", intake.getCurrentPower());
        telemetry.addData("LED Color Position", "%.2f", ledController.getCurrentPosition());
        telemetry.update();
    }
}
