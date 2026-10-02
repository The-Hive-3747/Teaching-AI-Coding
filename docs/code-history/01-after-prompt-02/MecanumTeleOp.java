package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Iterative TeleOp OpMode for a Mecanum drive FTC robot.
 * Controls drive motors with gamepad1 joysticks, and flywheel & intake subsystems
 * with gamepad1 buttons (A, B, X, DPad Up, DPad Down).
 */
@TeleOp(name = "Mecanum TeleOp", group = "Iterative Opmode")
public class MecanumTeleOp extends OpMode {

    // Drive Motors
    private DcMotor frontLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor backRightDrive = null;

    // Subsystems
    private Flywheel flywheel;
    private Intake intake;

    // Reverse Mode State (X Button)
    private boolean isReversed = false;
    private boolean previousXState = false;

    @Override
    public void init() {
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

        // Initialize flywheel and intake subsystem instances
        flywheel = new Flywheel();
        flywheel.init(hardwareMap);

        intake = new Intake();
        intake.init(hardwareMap);

        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void loop() {
        // --- 1. Mecanum Drive Control ---
        // Left joystick: forward/back (Y) & strafe left/right (X)
        // Right joystick: turn left/right (X)
        double y = -gamepad1.left_stick_y;     // Note: gamepad Y stick is inverted (negative up)
        double x = gamepad1.left_stick_x * 1.1; // 1.1 multiplier counteracts imperfect strafing
        double rx = gamepad1.right_stick_x;

        // Calculate individual motor powers for mecanum kinematics
        double frontLeftPower = y + x + rx;
        double backLeftPower = y - x + rx;
        double frontRightPower = y - x - rx;
        double backRightPower = y + x - rx;

        // Normalize motor powers so none exceed 1.0
        double max = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
        max = Math.max(max, Math.abs(backLeftPower));
        max = Math.max(max, Math.abs(backRightPower));

        if (max > 1.0) {
            frontLeftPower /= max;
            frontRightPower /= max;
            backLeftPower /= max;
            backRightPower /= max;
        }

        // Output power to drive motors
        frontLeftDrive.setPower(frontLeftPower);
        frontRightDrive.setPower(frontRightPower);
        backLeftDrive.setPower(backLeftPower);
        backRightDrive.setPower(backRightPower);

        // --- 2. Reverse Mode (X Button) Handling ---
        boolean xPressed = gamepad1.x && !previousXState;
        previousXState = gamepad1.x;

        if (xPressed) {
            isReversed = !isReversed;
        }

        // --- 3. Subsystem Updates ---
        // Pass gamepad and isReversed state to flywheel and intake
        boolean flywheelButtonPressed = flywheel.update(gamepad1, isReversed);
        boolean intakeButtonPressed = intake.update(gamepad1, isReversed);

        // If another button (A, B, DPad UP, DPad DOWN) is hit while in reverse mode,
        // reverse mode stops and that button's action takes effect immediately.
        if (isReversed && (flywheelButtonPressed || intakeButtonPressed)) {
            isReversed = false;
            // Re-update subsystems without reverse mode active to apply their normal target powers
            flywheel.update(gamepad1, false);
            intake.update(gamepad1, false);
        }

        // --- 4. Telemetry Output ---
        telemetry.addData("Status", "Running");
        telemetry.addData("Drive Motors", "FL: %.2f | FR: %.2f | BL: %.2f | BR: %.2f",
                frontLeftPower, frontRightPower, backLeftPower, backRightPower);
        telemetry.addData("Reverse Mode (X)", isReversed ? "ACTIVE (-0.3)" : "OFF");
        telemetry.addData("Flywheel", "ON: %b | Target: %.2f | Current: %.2f",
                flywheel.isOn(), flywheel.getTargetPower(), flywheel.getCurrentPower());
        telemetry.addData("Intake", "ON: %b | Power: %.2f | Current: %.2f",
                intake.isOn(), intake.getPower(), intake.getCurrentPower());
        telemetry.update();
    }
}
