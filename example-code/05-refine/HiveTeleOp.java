package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Checkpoint 5 — Refined TeleOp.
 *
 * Changes from Checkpoint 4, each from one prompt after a driver complaint:
 *   - flywheelRight reversed (it was pulling backward on the first test)
 *   - spin-up 1.5 s -> 2.0 s (first shot was weak)
 *   - joystick inputs cubed (too sensitive at small movements)
 *   - rotation scaled by 0.7 (turning too fast)
 *   - slow mode: hold left trigger to scale drive by 0.4 (can't line up)
 *   - 0.05 deadzone on sticks (robot crept when released)
 *
 * Controls:
 *   Left stick     forward/back (Y), strafe (X)
 *   Right stick    rotate (X)
 *   Left trigger   slow mode (hold)
 *   Right bumper   intake in (hold)
 *   Left bumper    intake out (hold)
 *   Right trigger  spin up flywheels, then feed (hold)
 */
@TeleOp(name = "HiveTeleOp", group = "Hive")
public class HiveTeleOp extends LinearOpMode {

    // --- Hardware ---
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor intake;
    private DcMotor flywheelLeft;
    private DcMotor flywheelRight;

    // --- Tuning: drive ---
    private static final double DEADZONE        = 0.05;
    private static final double ROTATION_SCALE  = 0.7;
    private static final double SLOW_MODE_SCALE = 0.4;

    // --- Tuning: intake / shooter ---
    private static final double INTAKE_POWER      = 1.0;
    private static final double FLYWHEEL_POWER    = 0.8;
    private static final double SPIN_UP_SECONDS   = 2.0;
    private static final double PULSE_ON_SECONDS  = 0.3;
    private static final double PULSE_OFF_SECONDS = 0.2;
    private static final double TRIGGER_THRESHOLD = 0.5;

    // --- Shooter state ---
    private enum ShooterState { IDLE, SPINNING_UP, FEEDING }
    private ShooterState shooterState = ShooterState.IDLE;
    private final ElapsedTime shooterTimer = new ElapsedTime();
    private final ElapsedTime pulseTimer   = new ElapsedTime();
    private boolean pulseOn = false;

    @Override
    public void runOpMode() {

        frontLeft     = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight    = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft      = hardwareMap.get(DcMotor.class, "backLeft");
        backRight     = hardwareMap.get(DcMotor.class, "backRight");
        intake        = hardwareMap.get(DcMotor.class, "intake");
        flywheelLeft  = hardwareMap.get(DcMotor.class, "flywheelLeft");
        flywheelRight = hardwareMap.get(DcMotor.class, "flywheelRight");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);
        intake.setDirection(DcMotor.Direction.FORWARD);
        flywheelLeft.setDirection(DcMotor.Direction.FORWARD);
        flywheelRight.setDirection(DcMotor.Direction.REVERSE);   // reversed after testing

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flywheelLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        flywheelRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            // ---------- Drive ----------
            double forward = shapeInput(-gamepad1.left_stick_y);
            double strafe  = shapeInput( gamepad1.left_stick_x);
            double rotate  = shapeInput( gamepad1.right_stick_x) * ROTATION_SCALE;

            boolean slowMode = gamepad1.left_trigger > TRIGGER_THRESHOLD;
            double driveScale = slowMode ? SLOW_MODE_SCALE : 1.0;

            double frontLeftPower  = (forward + strafe + rotate) * driveScale;
            double frontRightPower = (forward - strafe - rotate) * driveScale;
            double backLeftPower   = (forward - strafe + rotate) * driveScale;
            double backRightPower  = (forward + strafe - rotate) * driveScale;

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

            // ---------- Shooter state machine ----------
            boolean triggerHeld = gamepad1.right_trigger > TRIGGER_THRESHOLD;
            double flywheelPower = 0.0;
            double intakePower   = 0.0;

            switch (shooterState) {
                case IDLE:
                    if (triggerHeld) {
                        shooterState = ShooterState.SPINNING_UP;
                        shooterTimer.reset();
                    }
                    break;

                case SPINNING_UP:
                    flywheelPower = FLYWHEEL_POWER;
                    if (!triggerHeld) {
                        shooterState = ShooterState.IDLE;
                    } else if (shooterTimer.seconds() > SPIN_UP_SECONDS) {
                        shooterState = ShooterState.FEEDING;
                        pulseOn = true;
                        pulseTimer.reset();
                    }
                    break;

                case FEEDING:
                    flywheelPower = FLYWHEEL_POWER;
                    if (!triggerHeld) {
                        shooterState = ShooterState.IDLE;
                        break;
                    }
                    if (pulseOn && pulseTimer.seconds() > PULSE_ON_SECONDS) {
                        pulseOn = false;
                        pulseTimer.reset();
                    } else if (!pulseOn && pulseTimer.seconds() > PULSE_OFF_SECONDS) {
                        pulseOn = true;
                        pulseTimer.reset();
                    }
                    intakePower = pulseOn ? INTAKE_POWER : 0.0;
                    break;
            }

            // ---------- Intake bumpers (only when the shooter isn't using the intake) ----------
            if (shooterState == ShooterState.IDLE) {
                if (gamepad1.right_bumper) {
                    intakePower = INTAKE_POWER;
                } else if (gamepad1.left_bumper) {
                    intakePower = -INTAKE_POWER;
                }
            }

            flywheelLeft.setPower(flywheelPower);
            flywheelRight.setPower(flywheelPower);
            intake.setPower(intakePower);

            telemetry.addData("Drive", slowMode ? "SLOW" : "normal");
            telemetry.addData("Front L/R", "%.2f / %.2f", frontLeftPower, frontRightPower);
            telemetry.addData("Back  L/R", "%.2f / %.2f", backLeftPower, backRightPower);
            telemetry.addData("Shooter", "%s (%.1f s)", shooterState, shooterTimer.seconds());
            telemetry.addData("Flywheel", "%.2f", flywheelPower);
            telemetry.addData("Intake", "%.2f", intakePower);
            telemetry.update();
        }
    }

    /** Apply a deadzone, then cube the input for finer control near the center. */
    private double shapeInput(double raw) {
        if (Math.abs(raw) < DEADZONE) {
            return 0.0;
        }
        return raw * raw * raw;
    }
}
