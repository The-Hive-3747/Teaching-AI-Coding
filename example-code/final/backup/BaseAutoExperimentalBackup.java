package org.firstinspires.ftc.teamcode.backup;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Backup copy of BaseAutoExperimental.
 */
public abstract class BaseAutoExperimentalBackup extends OpMode {

    public enum AutoExpState {
        DELAY,
        STATE_1_BACK_UP_DROP_4TH_BALL,
        STATE_1B_SETTLE_1000MS_1,
        STATE_2_START_FLYWHEEL_FIRST_3,
        STATE_3_PULSE_SHOOT_FIRST_3,
        STATE_4_STOP_FIRST_SHOOT_BRAKE_250MS,
        STATE_5_DRIVE_FORWARD_INTAKE_4TH_BALL,
        STATE_6_BACK_UP_AND_REVERSE_BUMP_4TH_BALL,
        STATE_6B_SETTLE_1000MS_2,
        STATE_7_START_FLYWHEEL_4TH_BALL,
        STATE_8_PULSE_SHOOT_4TH_BALL,
        STATE_9_STOP_SECOND_SHOOT,
        STATE_9B_PARK_BACK_UP,
        STATE_10_PARK_TURN_LEFT,
        STATE_10B_PARK_DRIVE_FORWARD,
        STATE_10C_PARK_BACK_OFF_WALL,
        STATE_11_ALL_STOP
    }

    protected DcMotor frontLeftDrive = null;
    protected DcMotor frontRightDrive = null;
    protected DcMotor backLeftDrive = null;
    protected DcMotor backRightDrive = null;

    protected FlywheelBackup flywheel;
    protected IntakeBackup intake;
    protected LedControllerBackup ledController;

    protected AutoExpState currentState = AutoExpState.DELAY;
    protected ElapsedTime stateTimer;
    protected ElapsedTime totalAutoTimer;

    protected Gamepad autoGamepad;

    protected double frontLeftPower = 0.0;
    protected double backLeftPower = 0.0;
    protected double frontRightPower = 0.0;
    protected double backRightPower = 0.0;
    protected double stateTime = 0.0;

    protected abstract double getInitialDelaySeconds();

    protected double getFirstShootDurationSeconds() {
        return (getInitialDelaySeconds() > 0) ? 2.6 : 4.0;
    }

    protected boolean shouldParkTurnAndDrive() {
        return getInitialDelaySeconds() > 0.0;
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
        currentState = AutoExpState.DELAY;

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
                    currentState = AutoExpState.STATE_1_BACK_UP_DROP_4TH_BALL;
                    stateTimer.reset();
                } else {
                    ledController.setColor(LedControllerBackup.COLOR_YELLOW);
                }
                break;

            case STATE_1_BACK_UP_DROP_4TH_BALL:
                frontLeftPower = -0.3;
                backLeftPower = -0.3;
                frontRightPower = -0.3;
                backRightPower = -0.3;

                if (stateTime >= 0.30) {
                    currentState = AutoExpState.STATE_1B_SETTLE_1000MS_1;
                    stateTimer.reset();
                }
                break;

            case STATE_1B_SETTLE_1000MS_1:
                intake.stop();
                frontLeftPower = 0.0;
                backLeftPower = 0.0;
                frontRightPower = 0.0;
                backRightPower = 0.0;

                if (stateTime >= 1.000) {
                    currentState = AutoExpState.STATE_2_START_FLYWHEEL_FIRST_3;
                    stateTimer.reset();
                    flywheel.startDirect();
                }
                break;

            case STATE_2_START_FLYWHEEL_FIRST_3:
                intake.stop();
                flywheel.startDirect();

                if (stateTime >= 2.0) {
                    currentState = AutoExpState.STATE_3_PULSE_SHOOT_FIRST_3;
                    stateTimer.reset();
                }
                break;

            case STATE_3_PULSE_SHOOT_FIRST_3:
                autoGamepad.right_bumper = true;

                if (stateTime >= getFirstShootDurationSeconds()) {
                    currentState = AutoExpState.STATE_4_STOP_FIRST_SHOOT_BRAKE_250MS;
                    stateTimer.reset();
                }
                break;

            case STATE_4_STOP_FIRST_SHOOT_BRAKE_250MS:
                flywheel.stop();
                intake.stop();

                if (stateTime >= 0.250) {
                    currentState = AutoExpState.STATE_5_DRIVE_FORWARD_INTAKE_4TH_BALL;
                    stateTimer.reset();
                }
                break;

            case STATE_5_DRIVE_FORWARD_INTAKE_4TH_BALL:
                flywheel.stop();
                frontLeftPower = 0.3;
                backLeftPower = 0.3;
                frontRightPower = 0.3;
                backRightPower = 0.3;
                autoGamepad.a = true;

                if (stateTime >= 0.30) {
                    currentState = AutoExpState.STATE_6_BACK_UP_AND_REVERSE_BUMP_4TH_BALL;
                    stateTimer.reset();
                }
                break;

            case STATE_6_BACK_UP_AND_REVERSE_BUMP_4TH_BALL:
                frontLeftPower = -0.3;
                backLeftPower = -0.3;
                frontRightPower = -0.3;
                backRightPower = -0.3;

                if (stateTime < 0.100) {
                    autoGamepad.left_bumper = true;
                } else {
                    intake.stop();
                }

                if (stateTime >= 0.30) {
                    currentState = AutoExpState.STATE_6B_SETTLE_1000MS_2;
                    stateTimer.reset();
                }
                break;

            case STATE_6B_SETTLE_1000MS_2:
                flywheel.stop();
                intake.stop();
                frontLeftPower = 0.0;
                backLeftPower = 0.0;
                frontRightPower = 0.0;
                backRightPower = 0.0;

                if (stateTime >= 1.000) {
                    currentState = AutoExpState.STATE_7_START_FLYWHEEL_4TH_BALL;
                    stateTimer.reset();
                    flywheel.startDirect();
                }
                break;

            case STATE_7_START_FLYWHEEL_4TH_BALL:
                intake.stop();
                flywheel.startDirect();

                if (stateTime >= 2.0) {
                    currentState = AutoExpState.STATE_8_PULSE_SHOOT_4TH_BALL;
                    stateTimer.reset();
                }
                break;

            case STATE_8_PULSE_SHOOT_4TH_BALL:
                autoGamepad.right_bumper = true;

                if (stateTime >= 2.0) {
                    currentState = AutoExpState.STATE_9_STOP_SECOND_SHOOT;
                    stateTimer.reset();
                }
                break;

            case STATE_9_STOP_SECOND_SHOOT:
                flywheel.stop();
                intake.stop();

                if (shouldParkTurnAndDrive()) {
                    currentState = AutoExpState.STATE_9B_PARK_BACK_UP;
                } else {
                    currentState = AutoExpState.STATE_11_ALL_STOP;
                }
                stateTimer.reset();
                break;

            case STATE_9B_PARK_BACK_UP:
                frontLeftPower = -0.3;
                backLeftPower = -0.3;
                frontRightPower = -0.3;
                backRightPower = -0.3;

                if (stateTime >= 0.45) {
                    currentState = AutoExpState.STATE_10_PARK_TURN_LEFT;
                    stateTimer.reset();
                }
                break;

            case STATE_10_PARK_TURN_LEFT:
                frontLeftPower = -0.5 * 0.8;
                backLeftPower = -0.5 * 0.8;
                frontRightPower = 0.5 * 0.8;
                backRightPower = 0.5 * 0.8;

                if (stateTime >= 0.55) {
                    currentState = AutoExpState.STATE_10B_PARK_DRIVE_FORWARD;
                    stateTimer.reset();
                }
                break;

            case STATE_10B_PARK_DRIVE_FORWARD:
                frontLeftPower = 0.4 * 0.8;
                backLeftPower = 0.4 * 0.8;
                frontRightPower = 0.4 * 0.8;
                backRightPower = 0.4 * 0.8;

                if (stateTime >= 3.0) {
                    currentState = AutoExpState.STATE_10C_PARK_BACK_OFF_WALL;
                    stateTimer.reset();
                }
                break;

            case STATE_10C_PARK_BACK_OFF_WALL:
                frontLeftPower = -0.5;
                backLeftPower = -0.5;
                frontRightPower = -0.5;
                backRightPower = -0.5;

                if (stateTime >= 0.100) {
                    currentState = AutoExpState.STATE_11_ALL_STOP;
                    stateTimer.reset();
                }
                break;

            case STATE_11_ALL_STOP:
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

        if (currentState != AutoExpState.DELAY && currentState != AutoExpState.STATE_11_ALL_STOP) {
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

        telemetry.addData("Auto Exp State", currentState.toString());
        telemetry.addData("State Time", "%.2f sec", stateTime);
        telemetry.addData("Total Auto Time", "%.1f sec", totalAutoTimer.seconds());
        telemetry.addData("Flywheel Phase", flywheel.getPhase().toString());
        telemetry.addData("Intake Power", "%.2f", intake.getCurrentPower());
        telemetry.update();
    }
}
