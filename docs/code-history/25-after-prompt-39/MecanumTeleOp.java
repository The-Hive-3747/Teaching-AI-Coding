package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Iterative TeleOp OpMode for a Mecanum drive FTC robot.
 * Controls drive motors with Tank Drive + Strafe kinematics (Left Stick Y = Left Motors, Right Stick Y = Right Motors),
 * Gamepad 1 D-Pad Creep Mode (0.2 power forward/back/strafe), subsystems (flywheel & intake) with gamepad1 controls
 * (A for Intake Collect, B for Flywheel Startup Sequence, Right Bumper for Pulsed Feed, Left Bumper/X for Reject at -0.5 power),
 * Gamepad 2 D-Pad for Flywheel Speed tuning, goBILDA undercarriage LED lights for state feedback, and FTC rule-compliant 2Hz flashing endgame warnings.
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
        // --- 1. Mecanum Tank Drive + Strafe / Gamepad 1 D-Pad Creep Mode ---
        leftY = -gamepad1.left_stick_y;     // Note: gamepad Y stick is inverted (negative up)
        rightY = -gamepad1.right_stick_y;    // Note: gamepad Y stick is inverted (negative up)
        strafe = ((gamepad1.left_stick_x + gamepad1.right_stick_x) / 2.0) * 1.1; // Average X input with 1.1 multiplier

        // Gamepad 1 D-Pad Creep / Precision Drive (0.2 power override for fine alignment)
        if (gamepad1.dpad_up) {
            leftY = 0.2;
            rightY = 0.2;
            strafe = 0.0;
        } else if (gamepad1.dpad_down) {
            leftY = -0.2;
            rightY = -0.2;
            strafe = 0.0;
        } else if (gamepad1.dpad_left) {
            leftY = 0.0;
            rightY = 0.0;
            strafe = -0.2;
        } else if (gamepad1.dpad_right) {
            leftY = 0.0;
            rightY = 0.0;
            strafe = 0.2;
        }

        // Calculate individual motor powers for mecanum tank kinematics
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
        // Pass gamepad1 (controls) and gamepad2 (flywheel speed tuning) to flywheel
        flywheelButtonPressed = flywheel.update(gamepad1, gamepad2, isReversed);
        intakeButtonPressed = intake.update(gamepad1, isReversed, flywheel.isReversing(), flywheel.isPausing(), flywheel.isOn());

        // If another button (A, B, DPad UP, DPad DOWN) is hit while in reverse mode,
        // reverse mode stops and that button's action takes effect immediately.
        if (isReversed && (flywheelButtonPressed || intakeButtonPressed)) {
            isReversed = false;
            flywheel.update(gamepad1, gamepad2, false);
            intake.update(gamepad1, false, false, false, false);
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
        // 1 Hz flashing calculation (500 ms ON, 500 ms OFF) for clean, distinct color display
        flashState = ((int)(matchSeconds * 2) % 2) == 0;

        if (matchSeconds >= 110.0 && matchSeconds < 114.0) {
            // 10s Warning: Pulse Solid Red at 1 Hz for 4 seconds
            targetLedColor = flashState ? LedController.COLOR_RED : LedController.COLOR_BLUE;
        } else if (matchSeconds >= 90.0 && matchSeconds < 94.0) {
            // 30s Warning: Pulse Solid Orange at 1 Hz for 4 seconds
            targetLedColor = flashState ? LedController.COLOR_ORANGE : LedController.COLOR_BLUE;
        } else if (intake.isFeeding()) {
            // Phase 3 Feeding / Shooting into Flywheel (Right Bumper held): White
            targetLedColor = LedController.COLOR_WHITE;
        } else if (isReversed || flywheel.isReversing()) {
            // Reverse Mode or Phase 1 Flywheel Reverse Bump Active: Red
            targetLedColor = LedController.COLOR_RED;
        } else if (flywheel.isPausing()) {
            // Phase 2 Flywheel Pause (300 ms): Orange
            targetLedColor = LedController.COLOR_ORANGE;
        } else if (flywheel.isOn() && intake.isCollectOn()) {
            // Phase 3 Flywheel running & Intake Collect active: Orange
            targetLedColor = LedController.COLOR_ORANGE;
        } else if (flywheel.isOn()) {
            // Phase 3 Flywheel running forward: Purple
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
        telemetry.addData("Phase 3 Pulsed Feed (RB)", intake.isFeeding() ? "ACTIVE (100ms ON / 200ms OFF)" : "OFF");
        telemetry.addData("Reverse Mode (LB/X)", isReversed ? "ACTIVE (-0.5)" : "OFF");
        telemetry.addData("Flywheel Phase", "%s | Target: %.2f",
                flywheel.getPhase().toString(), flywheel.getTargetPower());
        telemetry.addData("Intake Power", "%.2f", intake.getCurrentPower());
        telemetry.addData("LED Color Position", "%.2f", ledController.getCurrentPosition());
        telemetry.update();
    }
}
