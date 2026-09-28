package org.firstinspires.ftc.teamcode.backup;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Backup copy of MecanumTeleOp.
 */
public class MecanumTeleOpBackup extends OpMode {

    private DcMotor frontLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor backRightDrive = null;

    private FlywheelBackup flywheel;
    private IntakeBackup intake;
    private LedControllerBackup ledController;

    private boolean isReversed = false;
    private boolean previousReverseButtonState = false;

    private ElapsedTime matchTimer;
    private boolean warned30Sec = false;
    private boolean warned10Sec = false;

    private double matchSeconds = 0.0;
    private double targetLedColor = LedControllerBackup.COLOR_BLUE;
    private boolean flashState = false;
    private boolean reverseButtonPressed = false;

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

        matchTimer = new ElapsedTime();

        ledController.setColor(LedControllerBackup.COLOR_GREEN);
        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void start() {
        matchTimer.reset();
        warned30Sec = false;
        warned10Sec = false;

        if (flywheel != null) {
            flywheel.stop();
        }
        if (intake != null) {
            intake.stop();
        }

        ledController.setColor(LedControllerBackup.COLOR_BLUE);
    }

    @Override
    public void loop() {
        leftY = -gamepad1.left_stick_y;
        rightY = -gamepad1.right_stick_y;
        strafe = ((gamepad1.left_stick_x + gamepad1.right_stick_x) / 2.0) * 1.1;

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

        frontLeftPower = leftY + strafe;
        backLeftPower = leftY - strafe;
        frontRightPower = rightY - strafe;
        backRightPower = rightY + strafe;

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

        reverseButtonPressed = (gamepad1.left_bumper || gamepad1.x);
        if (reverseButtonPressed && !previousReverseButtonState) {
            isReversed = !isReversed;
        }
        previousReverseButtonState = reverseButtonPressed;

        flywheelButtonPressed = flywheel.update(gamepad1, gamepad2, isReversed);
        intakeButtonPressed = intake.update(gamepad1, isReversed, flywheel.isReversing(), flywheel.isPausing(), flywheel.isOn());

        if (isReversed && (flywheelButtonPressed || intakeButtonPressed)) {
            isReversed = false;
            flywheel.update(gamepad1, gamepad2, false);
            intake.update(gamepad1, false, false, false, false);
        }

        matchSeconds = matchTimer.seconds();

        if (matchSeconds >= 90.0 && !warned30Sec) {
            gamepad1.rumbleBlips(2);
            warned30Sec = true;
        }

        if (matchSeconds >= 110.0 && !warned10Sec) {
            gamepad1.rumble(1000);
            warned10Sec = true;
        }

        flashState = ((int)(matchSeconds * 2) % 2) == 0;

        if (matchSeconds >= 110.0 && matchSeconds < 114.0) {
            targetLedColor = flashState ? LedControllerBackup.COLOR_RED : LedControllerBackup.COLOR_BLUE;
        } else if (matchSeconds >= 90.0 && matchSeconds < 94.0) {
            targetLedColor = flashState ? LedControllerBackup.COLOR_ORANGE : LedControllerBackup.COLOR_BLUE;
        } else if (intake.isFeeding()) {
            targetLedColor = LedControllerBackup.COLOR_WHITE;
        } else if (isReversed || flywheel.isReversing()) {
            targetLedColor = LedControllerBackup.COLOR_RED;
        } else if (flywheel.isPausing()) {
            targetLedColor = LedControllerBackup.COLOR_ORANGE;
        } else if (flywheel.isOn() && intake.isCollectOn()) {
            targetLedColor = LedControllerBackup.COLOR_ORANGE;
        } else if (flywheel.isOn()) {
            targetLedColor = LedControllerBackup.COLOR_PURPLE;
        } else if (intake.isCollectOn()) {
            targetLedColor = LedControllerBackup.COLOR_GREEN;
        } else {
            targetLedColor = LedControllerBackup.COLOR_BLUE;
        }

        ledController.setColor(targetLedColor);

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
