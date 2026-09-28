package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Checkpoint 5 — Refined TeleOp.
 *
 * The Hive's final MecanumTeleOp.java with the LED controller and match-timer rumble removed
 * (those are Checkpoint 9 extras). Flywheel.java and Intake.java are the final versions verbatim.
 *
 * Changes from Checkpoint 4, each from one real prompt:
 *   - strafe signs corrected            "holding both joysticks left made the robot strafe right"
 *   - reverse bump 200 ms -> 100 ms     "it is spitting balls out, so i would like to shorten it"
 *   - flywheel tuning moved to gamepad 2 "I thought gamepad 1 dpad was slow mode, not tuning the flywheel"
 *   - collect stops after the bump      "I don't want the intake to resume"
 *   - default flywheel power 0.95       "Let's set the default speed to 0.95"
 *
 * Gamepad 1: sticks = tank drive + strafe, D-pad = precision 0.5, A = collect, B = flywheel, RB = feed, LB/X = reject
 * Gamepad 2: D-pad up/down = flywheel target +/- 0.05
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

    // Reverse / Reject Mode State (Left Bumper or X Button)
    private boolean isReversed = false;
    private boolean previousReverseButtonState = false;

    // Working variables declared as class fields to prevent re-allocation inside loop
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

        telemetry.addData("Status", "Init Completed (Ready for Start)");
    }

    @Override
    public void start() {
        // Explicitly stop subsystems when START is hit
        if (flywheel != null) {
            flywheel.stop();
        }
        if (intake != null) {
            intake.stop();
        }
    }

    @Override
    public void loop() {
        // --- 1. Mecanum Tank Drive + Strafe / Gamepad 1 D-Pad Precision Mode ---
        leftY = -gamepad1.left_stick_y;     // Note: gamepad Y stick is inverted (negative up)
        rightY = -gamepad1.right_stick_y;    // Note: gamepad Y stick is inverted (negative up)
        strafe = ((gamepad1.left_stick_x + gamepad1.right_stick_x) / 2.0) * 1.1; // Average X input with 1.1 multiplier

        // Gamepad 1 D-Pad Precision Drive (0.5 power override for fine alignment)
        if (gamepad1.dpad_up) {
            leftY = 0.5;
            rightY = 0.5;
            strafe = 0.0;
        } else if (gamepad1.dpad_down) {
            leftY = -0.5;
            rightY = -0.5;
            strafe = 0.0;
        } else if (gamepad1.dpad_left) {
            leftY = 0.0;
            rightY = 0.0;
            strafe = -0.5;
        } else if (gamepad1.dpad_right) {
            leftY = 0.0;
            rightY = 0.0;
            strafe = 0.5;
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

        // --- 4. Telemetry Output ---
        telemetry.addData("Status", "Running");
        telemetry.addData("Drive Motors", "FL: %.2f | FR: %.2f | BL: %.2f | BR: %.2f",
                frontLeftPower, frontRightPower, backLeftPower, backRightPower);
        telemetry.addData("Phase 3 Pulsed Feed (RB)", intake.isFeeding() ? "ACTIVE (100ms ON / 200ms OFF)" : "OFF");
        telemetry.addData("Reverse Mode (LB/X)", isReversed ? "ACTIVE (-0.5)" : "OFF");
        telemetry.addData("Flywheel Phase", "%s | Target: %.2f",
                flywheel.getPhase().toString(), flywheel.getTargetPower());
        telemetry.addData("Intake Power", "%.2f", intake.getCurrentPower());
        telemetry.update();
    }
}
