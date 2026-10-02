package org.firstinspires.ftc.teamcode.backup;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Backup copy of BaseAuto.
 */
public abstract class BaseAutoBackup extends OpMode {

    public enum AutoState {
        DELAY,
        STATE_1_BACK_UP,
        STATE_1B_SETTLE_1000MS,
        STATE_2_START_FLYWHEEL,
        STATE_3_PULSE_SHOOTING,
        STATE_4_STOP_SHOOTING,
        STATE_4B_PARK_BACK_UP,
        STATE_5_PARK_TURN_LEFT,
        STATE_5B_PARK_DRIVE_FORWARD,
        STATE_5C_PARK_BACK_OFF_WALL,
        STATE_6_ALL_STOP
    }

    protected DcMotor frontLeftDrive = null;
    protected DcMotor frontRightDrive = null;
    protected DcMotor backLeftDrive = null;
    protected DcMotor backRightDrive = null;

    protected FlywheelBackup flywheel;
    protected IntakeBackup intake;
    protected LedControllerBackup ledController;

    protected AutoState currentState = AutoState.DELAY;
    protected ElapsedTime stateTimer;
    protected ElapsedTime totalAutoTimer;

    protected Gamepad autoGamepad;

    protected double frontLeftPower = 0.0;
    protected double backLeftPower = 0.0;
    protected double frontRightPower = 0.0;
    protected double backRightPower = 0.0;
    protected double stateTime = 0.0;

    protected abstract double getInitialDelaySeconds();

    protected boolean shouldParkTurnAndDrive() {
        return getInitialDelaySeconds() > 0.0;
    }

    protected double getShootDurationSeconds() {
        return (getInitialDelaySeconds() > 0.0) ? 8.3 : 10.0;
    }

    @Override
    public void init() {
        ledController = new LedControllerBackup();
        ledController.init(hardwareMap);

        frontLeftDrive = hardwareMap.get(DcMotor.class, "front_left_drive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "front_right_drive");
        backLeftDrive = hardwareMap.get(DcMotor.class, "back_left_drive");
        backRightDrive = hardwareMap.get(DcMotor.class, "back_right_drive");

        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);

        frontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        flywheel = new FlywheelBackup();
        flywheel.init(hardwareMap);

        intake = new IntakeBackup();
        intake.init(hardwareMap);

        stateTimer = new ElapsedTime();
        totalAutoTimer = new ElapsedTime();
        autoGamepad = new Gamepad();

        ledController.setColor(LedControllerBackup.COLOR_GREEN);
        telemetry.addData("Status", "Init Completed");
    }

    @Override
    public void start() {
        totalAutoTimer.reset();
        stateTimer.reset();
        currentState = AutoState.DELAY;

        flywheel.stop();
        intake.stop();

        ledController.setColor(LedControllerBackup.COLOR_BLUE);
    }

    @Override
    public void loop() {
        stateTime = stateTimer.seconds();

        autoGamepad.a = false;
        autoGamepad.b = false;
        autoGamepad.right_bumper = false;
        autoGamepad.left_bumper = false;
        autoGamepad.x = false;

        frontLeftPower = 0.0;
        backLeftPower = 0.0;
        frontRightPower = 0.0;
        backRightPower = 0.0;

        switch (currentState) {
            case DELAY:
                if (stateTime >= getInitialDelaySeconds()) {
                    currentState = AutoState.STATE_1_BACK_UP;
                    stateTimer.reset();
                } else {
                    ledController.setColor(LedControllerBackup.COLOR_YELLOW);
                }
                break;

            case STATE_1_BACK_UP:
                frontLeftPower = -0.3;
                backLeftPower = -0.3;
                frontRightPower = -0.3;
                backRightPower = -0.3;

                if (stateTime >= 0.30) {
                    currentState = AutoState.STATE_1B_SETTLE_1000MS;
                    stateTimer.reset();
                }
                break;

            case STATE_1B_SETTLE_1000MS:
                frontLeftPower = 0.0;
                backLeftPower = 0.0;
                frontRightPower = 0.0;
                backRightPower = 0.0;

                if (stateTime >= 1.000) {
                    currentState = AutoState.STATE_2_START_FLYWHEEL;
                    stateTimer.reset();
                    flywheel.startDirect();
                }
                break;

            case STATE_2_START_FLYWHEEL:
                flywheel.startDirect();

                if (stateTime >= 2.0) {
                    currentState = AutoState.STATE_3_PULSE_SHOOTING;
                    stateTimer.reset();
                }
                break;

            case STATE_3_PULSE_SHOOTING:
                autoGamepad.right_bumper = true;

                if (stateTime >= getShootDurationSeconds()) {
                    currentState = AutoState.STATE_4_STOP_SHOOTING;
                    stateTimer.reset();
                }
                break;

            case STATE_4_STOP_SHOOTING:
                flywheel.stop();
                intake.stop();

                if (shouldParkTurnAndDrive()) {
                    currentState = AutoState.STATE_4B_PARK_BACK_UP;
                } else {
                    currentState = AutoState.STATE_6_ALL_STOP;
                }
                stateTimer.reset();
                break;

            case STATE_4B_PARK_BACK_UP:
                frontLeftPower = -0.3;
                backLeftPower = -0.3;
                frontRightPower = -0.3;
                backRightPower = -0.3;

                if (stateTime >= 0.45) {
                    currentState = AutoState.STATE_5_PARK_TURN_LEFT;
                    stateTimer.reset();
                }
                break;

            case STATE_5_PARK_TURN_LEFT:
                frontLeftPower = -0.5 * 0.8;
                backLeftPower = -0.5 * 0.8;
                frontRightPower = 0.5 * 0.8;
                backRightPower = 0.5 * 0.8;

                if (stateTime >= 0.55) {
                    currentState = AutoState.STATE_5B_PARK_DRIVE_FORWARD;
                    stateTimer.reset();
                }
                break;

            case STATE_5B_PARK_DRIVE_FORWARD:
                frontLeftPower = 0.4 * 0.8;
                backLeftPower = 0.4 * 0.8;
                frontRightPower = 0.4 * 0.8;
                backRightPower = 0.4 * 0.8;

                if (stateTime >= 3.0) {
                    currentState = AutoState.STATE_5C_PARK_BACK_OFF_WALL;
                    stateTimer.reset();
                }
                break;

            case STATE_5C_PARK_BACK_OFF_WALL:
                frontLeftPower = -0.5;
                backLeftPower = -0.5;
                frontRightPower = -0.5;
                backRightPower = -0.5;

                if (stateTime >= 0.100) {
                    currentState = AutoState.STATE_6_ALL_STOP;
                    stateTimer.reset();
                }
                break;

            case STATE_6_ALL_STOP:
                frontLeftPower = 0.0;
                backLeftPower = 0.0;
                frontRightPower = 0.0;
                backRightPower = 0.0;

                flywheel.stop();
                intake.stop();

                ledController.setColor(LedControllerBackup.COLOR_GREEN);
                break;
        }

        frontLeftDrive.setPower(frontLeftPower);
        frontRightDrive.setPower(frontRightPower);
        backLeftDrive.setPower(backLeftPower);
        backRightDrive.setPower(backRightPower);

        flywheel.update(autoGamepad, false);
        intake.update(autoGamepad, false, flywheel.isReversing(), flywheel.isPausing(), flywheel.isOn());

        if (currentState != AutoState.DELAY && currentState != AutoState.STATE_6_ALL_STOP) {
            if (intake.isFeeding()) {
                ledController.setColor(LedControllerBackup.COLOR_WHITE);
            } else if (flywheel.isReversing()) {
                ledController.setColor(LedControllerBackup.COLOR_RED);
            } else if (flywheel.isPausing()) {
                ledController.setColor(LedControllerBackup.COLOR_ORANGE);
            } else if (flywheel.isOn()) {
                ledController.setColor(LedControllerBackup.COLOR_PURPLE);
            } else {
                ledController.setColor(LedControllerBackup.COLOR_BLUE);
            }
        }

        telemetry.addData("Auto State", currentState.toString());
        telemetry.addData("State Time", "%.2f sec", stateTime);
        telemetry.addData("Total Auto Time", "%.1f sec", totalAutoTimer.seconds());
        telemetry.addData("Flywheel Phase", flywheel.getPhase().toString());
        telemetry.addData("Intake Power", "%.2f", intake.getCurrentPower());
        telemetry.update();
    }
}
