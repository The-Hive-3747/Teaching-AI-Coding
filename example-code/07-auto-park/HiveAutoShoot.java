package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Checkpoint 7 — Autonomous v2: leave the wall, shoot, park.
 *
 * Time-based (no encoders, no odometry). A state machine:
 *
 *   LEAVE_WALL     drive forward 0.9 s at 0.5 power, then stop
 *   SPIN_UP        flywheels on, wait 2.0 s
 *   SHOOT          pulse the intake 3 times (0.3 s on / 0.2 s off), flywheels stay on
 *   TURN_TO_PARK   flywheels off; rotate clockwise in place 0.6 s
 *   DRIVE_TO_PARK  drive forward 2.0 s at 0.5 power, then stop
 *   DONE           everything off
 *
 * Same hardware, reversals, and shooter numbers as HiveTeleOp.
 */
@Autonomous(name = "HiveAutoShoot", group = "Hive", preselectTeleOp = "HiveTeleOp")
public class HiveAutoShoot extends LinearOpMode {

    // --- Hardware ---
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor intake;
    private DcMotor flywheelLeft;
    private DcMotor flywheelRight;

    // --- Tuning (start here, then adjust after testing) ---
    private static final double LEAVE_WALL_POWER   = 0.5;
    private static final double LEAVE_WALL_SECONDS = 0.9;
    private static final double FLYWHEEL_POWER     = 0.8;
    private static final double SPIN_UP_SECONDS    = 2.0;
    private static final double INTAKE_POWER       = 1.0;
    private static final double PULSE_ON_SECONDS   = 0.3;
    private static final double PULSE_OFF_SECONDS  = 0.2;
    private static final int    SHOOT_PULSES       = 3;
    private static final double TURN_POWER         = 0.5;
    private static final double TURN_SECONDS       = 0.6;
    private static final double PARK_POWER         = 0.5;
    private static final double PARK_SECONDS       = 2.0;

    // --- State machine ---
    private enum State { LEAVE_WALL, SPIN_UP, SHOOT, TURN_TO_PARK, DRIVE_TO_PARK, DONE }
    private State state = State.LEAVE_WALL;
    private final ElapsedTime stateTimer = new ElapsedTime();   // time since entering current state
    private final ElapsedTime pulseTimer = new ElapsedTime();   // time in current pulse phase
    private boolean pulseOn = false;
    private int pulsesDone = 0;

    @Override
    public void runOpMode() {

        frontLeft     = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight    = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft      = hardwareMap.get(DcMotor.class, "backLeft");
        backRight     = hardwareMap.get(DcMotor.class, "backRight");
        intake        = hardwareMap.get(DcMotor.class, "intake");
        flywheelLeft  = hardwareMap.get(DcMotor.class, "flywheelLeft");
        flywheelRight = hardwareMap.get(DcMotor.class, "flywheelRight");

        // Same reversals as HiveTeleOp.
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);
        intake.setDirection(DcMotor.Direction.FORWARD);
        flywheelLeft.setDirection(DcMotor.Direction.FORWARD);
        flywheelRight.setDirection(DcMotor.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flywheelLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        flywheelRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        telemetry.addData("Status", "Initialized — waiting for start");
        telemetry.update();

        waitForStart();

        // Enter the first state.
        state = State.LEAVE_WALL;
        stateTimer.reset();

        while (opModeIsActive()) {

            switch (state) {

                case LEAVE_WALL:
                    setDrive(LEAVE_WALL_POWER);
                    if (stateTimer.seconds() > LEAVE_WALL_SECONDS) {
                        setDrive(0.0);
                        changeState(State.SPIN_UP);
                    }
                    break;

                case SPIN_UP:
                    setFlywheels(FLYWHEEL_POWER);
                    intake.setPower(0.0);
                    if (stateTimer.seconds() > SPIN_UP_SECONDS) {
                        pulseOn = true;
                        pulsesDone = 0;
                        pulseTimer.reset();
                        changeState(State.SHOOT);
                    }
                    break;

                case SHOOT:
                    setFlywheels(FLYWHEEL_POWER);
                    if (pulseOn && pulseTimer.seconds() > PULSE_ON_SECONDS) {
                        pulseOn = false;
                        pulsesDone++;
                        pulseTimer.reset();
                    } else if (!pulseOn && pulseTimer.seconds() > PULSE_OFF_SECONDS) {
                        pulseOn = true;
                        pulseTimer.reset();
                    }
                    intake.setPower(pulseOn ? INTAKE_POWER : 0.0);
                    if (pulsesDone >= SHOOT_PULSES) {
                        changeState(State.TURN_TO_PARK);
                    }
                    break;

                case TURN_TO_PARK:
                    setFlywheels(0.0);
                    intake.setPower(0.0);
                    // Clockwise in place: left side forward, right side backward.
                    setDrive(TURN_POWER, -TURN_POWER);
                    if (stateTimer.seconds() > TURN_SECONDS) {
                        setDrive(0.0);
                        changeState(State.DRIVE_TO_PARK);
                    }
                    break;

                case DRIVE_TO_PARK:
                    setDrive(PARK_POWER);
                    if (stateTimer.seconds() > PARK_SECONDS) {
                        setDrive(0.0);
                        changeState(State.DONE);
                    }
                    break;

                case DONE:
                    setDrive(0.0);
                    setFlywheels(0.0);
                    intake.setPower(0.0);
                    break;
            }

            telemetry.addData("State", state);
            telemetry.addData("State time", "%.1f s", stateTimer.seconds());
            telemetry.addData("Pulses", "%d / %d", pulsesDone, SHOOT_PULSES);
            telemetry.update();
        }
    }

    private void changeState(State next) {
        state = next;
        stateTimer.reset();
    }

    /** All four drive motors at the same power: forward if positive. */
    private void setDrive(double power) {
        setDrive(power, power);
    }

    /** Left-side and right-side power separately (for turning in place). */
    private void setDrive(double leftPower, double rightPower) {
        frontLeft.setPower(leftPower);
        backLeft.setPower(leftPower);
        frontRight.setPower(rightPower);
        backRight.setPower(rightPower);
    }

    private void setFlywheels(double power) {
        flywheelLeft.setPower(power);
        flywheelRight.setPower(power);
    }
}
