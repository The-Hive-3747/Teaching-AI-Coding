package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Checkpoint 3 — Mecanum drive + intake.
 *
 * Left stick:   forward/back (Y) and strafe (X)
 * Right stick:  rotate (X)
 * Right bumper: intake in (hold)
 * Left bumper:  intake out (hold)
 */
@TeleOp(name = "HiveTeleOp", group = "Hive")
public class HiveTeleOp extends LinearOpMode {

    // --- Hardware ---
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor intake;

    // --- Tuning ---
    private static final double INTAKE_POWER = 1.0;

    @Override
    public void runOpMode() {

        frontLeft  = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft   = hardwareMap.get(DcMotor.class, "backLeft");
        backRight  = hardwareMap.get(DcMotor.class, "backRight");
        intake     = hardwareMap.get(DcMotor.class, "intake");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);
        intake.setDirection(DcMotor.Direction.FORWARD);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            // ---------- Drive (unchanged from Checkpoint 2) ----------
            double forward = -gamepad1.left_stick_y;
            double strafe  =  gamepad1.left_stick_x;
            double rotate  =  gamepad1.right_stick_x;

            double frontLeftPower  = forward + strafe + rotate;
            double frontRightPower = forward - strafe - rotate;
            double backLeftPower   = forward - strafe + rotate;
            double backRightPower  = forward + strafe - rotate;

            double max = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
            max = Math.max(max, Math.abs(backLeftPower));
            max = Math.max(max, Math.abs(backRightPower));
            if (max > 1.0) {
                frontLeftPower  /= max;
                frontRightPower /= max;
                backLeftPower   /= max;
                backRightPower  /= max;
            }

            frontLeft.setPower(frontLeftPower);
            frontRight.setPower(frontRightPower);
            backLeft.setPower(backLeftPower);
            backRight.setPower(backRightPower);

            // ---------- Intake ----------
            double intakePower;
            if (gamepad1.right_bumper) {
                intakePower = INTAKE_POWER;        // pull pieces in
            } else if (gamepad1.left_bumper) {
                intakePower = -INTAKE_POWER;       // push pieces out
            } else {
                intakePower = 0.0;                 // neither held: stop
            }
            intake.setPower(intakePower);

            telemetry.addData("Front L/R", "%.2f / %.2f", frontLeftPower, frontRightPower);
            telemetry.addData("Back  L/R", "%.2f / %.2f", backLeftPower, backRightPower);
            telemetry.addData("Intake", "%.2f", intakePower);
            telemetry.update();
        }
    }
}
