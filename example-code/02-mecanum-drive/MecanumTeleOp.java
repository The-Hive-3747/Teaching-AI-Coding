package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Checkpoint 2 — Mecanum drive only (tank drive + strafe).
 *
 * Derived from The Hive's final MecanumTeleOp.java by removing everything except the drive.
 *
 * Left stick Y  = left-side motors
 * Right stick Y = right-side motors
 * Either stick X = strafe (the two X values are averaged)
 * D-pad          = precision moves at 0.5 power
 */
@TeleOp(name = "Mecanum TeleOp", group = "Iterative Opmode")
public class MecanumTeleOp extends OpMode {

    // Drive Motors
    private DcMotor frontLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor backRightDrive = null;

    // Tank Drive Kinematic Variables
    private double leftY = 0.0;
    private double rightY = 0.0;
    private double strafe = 0.0;

    private double frontLeftPower = 0.0;
    private double backLeftPower = 0.0;
    private double frontRightPower = 0.0;
    private double backRightPower = 0.0;
    private double maxPower = 0.0;

    @Override
    public void init() {
        // 1. Hardware mapping — the strings must match the Robot Controller config exactly.
        frontLeftDrive = hardwareMap.get(DcMotor.class, "front_left_drive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "front_right_drive");
        backLeftDrive = hardwareMap.get(DcMotor.class, "back_left_drive");
        backRightDrive = hardwareMap.get(DcMotor.class, "back_right_drive");

        // 2. Set motor directions (left side reversed for forward driving)
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);

        // Brake when power is zero, for precise stopping
        frontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addData("Status", "Init Completed (Ready for Start)");
    }

    @Override
    public void loop() {
        // 3. Read the gamepad. The Y sticks are inverted (negative = pushed forward).
        leftY = -gamepad1.left_stick_y;
        rightY = -gamepad1.right_stick_y;
        strafe = ((gamepad1.left_stick_x + gamepad1.right_stick_x) / 2.0) * 1.1;

        // D-pad precision drive (0.5 power override for fine alignment)
        if (gamepad1.dpad_up) {
            leftY = 0.5;  rightY = 0.5;  strafe = 0.0;
        } else if (gamepad1.dpad_down) {
            leftY = -0.5; rightY = -0.5; strafe = 0.0;
        } else if (gamepad1.dpad_left) {
            leftY = 0.0;  rightY = 0.0;  strafe = -0.5;
        } else if (gamepad1.dpad_right) {
            leftY = 0.0;  rightY = 0.0;  strafe = 0.5;
        }

        // Mecanum tank kinematics
        frontLeftPower = leftY + strafe;
        backLeftPower = leftY - strafe;
        frontRightPower = rightY - strafe;
        backRightPower = rightY + strafe;

        // Normalize so no motor exceeds 1.0, then cap at 0.8
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

        // 4. Set power
        frontLeftDrive.setPower(frontLeftPower);
        frontRightDrive.setPower(frontRightPower);
        backLeftDrive.setPower(backLeftPower);
        backRightDrive.setPower(backRightPower);

        telemetry.addData("Status", "Running");
        telemetry.addData("Drive Motors", "FL: %.2f | FR: %.2f | BL: %.2f | BR: %.2f",
                frontLeftPower, frontRightPower, backLeftPower, backRightPower);
        telemetry.update();
    }
}
