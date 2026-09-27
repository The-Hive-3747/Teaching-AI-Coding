package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Checkpoint 2 — Mecanum drive only.
 *
 * Left stick:  forward/back (Y) and strafe (X)
 * Right stick: rotate (X)
 *
 * Motor names must match the Robot Controller configuration exactly.
 */
@TeleOp(name = "HiveTeleOp", group = "Hive")
public class HiveTeleOp extends LinearOpMode {

    // --- Hardware ---
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;

    @Override
    public void runOpMode() {

        // 1. Hardware mapping — the strings must match the config names.
        frontLeft  = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft   = hardwareMap.get(DcMotor.class, "backLeft");
        backRight  = hardwareMap.get(DcMotor.class, "backRight");

        // 2. Reversing — the left-side motors are mounted mirrored.
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        // Brake when the sticks are released so the robot doesn't coast.
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            // 3. Reading the gamepad.
            // Pushing the stick forward gives a NEGATIVE y value, so negate it.
            double forward = -gamepad1.left_stick_y;
            double strafe  =  gamepad1.left_stick_x;
            double rotate  =  gamepad1.right_stick_x;

            // Standard mecanum mixing.
            double frontLeftPower  = forward + strafe + rotate;
            double frontRightPower = forward - strafe - rotate;
            double backLeftPower   = forward - strafe + rotate;
            double backRightPower  = forward + strafe - rotate;

            // Scale so no motor is asked for more than 1.0.
            double max = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
            max = Math.max(max, Math.abs(backLeftPower));
            max = Math.max(max, Math.abs(backRightPower));
            if (max > 1.0) {
                frontLeftPower  /= max;
                frontRightPower /= max;
                backLeftPower   /= max;
                backRightPower  /= max;
            }

            // 4. Setting power.
            frontLeft.setPower(frontLeftPower);
            frontRight.setPower(frontRightPower);
            backLeft.setPower(backLeftPower);
            backRight.setPower(backRightPower);

            telemetry.addData("Front L/R", "%.2f / %.2f", frontLeftPower, frontRightPower);
            telemetry.addData("Back  L/R", "%.2f / %.2f", backLeftPower, backRightPower);
            telemetry.update();
        }
    }
}
