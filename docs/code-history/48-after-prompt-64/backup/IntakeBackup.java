package org.firstinspires.ftc.teamcode.backup;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Backup copy of Intake subsystem.
 */
public class IntakeBackup {
    private DcMotor intakeMotor;
    private CRServo intakeServoLeft;
    private CRServo intakeServoRight;

    private final double collectPower = 0.5;
    private final double feedPower = 0.5;
    private final double reversePower = -0.5;

    private boolean isCollectOn = false;
    private boolean isFeeding = false;
    private boolean previousRBState = false;
    private boolean previousAState = false;

    private ElapsedTime shootPulseTimer;
    private static final double PULSE_PERIOD_SEC = 0.300;
    private static final double PULSE_ON_DURATION_SEC = 0.100;

    private boolean buttonPressed = false;
    private double currentAppliedPower = 0.0;
    private double cycleTime = 0.0;

    public void init(HardwareMap hardwareMap) {
        intakeMotor = hardwareMap.get(DcMotor.class, "intake");
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        intakeServoLeft = hardwareMap.get(CRServo.class, "intake_servo_left");
        intakeServoRight = hardwareMap.get(CRServo.class, "intake_servo_right");

        if (intakeServoLeft != null) {
            intakeServoLeft.setDirection(CRServo.Direction.REVERSE);
        }

        shootPulseTimer = new ElapsedTime();
        stop();
    }

    public void stop() {
        isCollectOn = false;
        isFeeding = false;
        currentAppliedPower = 0.0;
        setPowerToHardware(0.0);
    }

    public boolean update(Gamepad gamepad, boolean isReversed, boolean isFlywheelReversing, boolean isFlywheelPausing, boolean isFlywheelOn) {
        buttonPressed = false;

        if (gamepad.a && !previousAState) {
            isCollectOn = !isCollectOn;
            buttonPressed = true;
        }
        previousAState = gamepad.a;

        isFeeding = gamepad.right_bumper;

        if (isFeeding && !previousRBState) {
            shootPulseTimer.reset();
        }
        previousRBState = isFeeding;

        if (isReversed || isFlywheelReversing) {
            currentAppliedPower = reversePower;
        } else if (isFlywheelPausing) {
            currentAppliedPower = 0.0;
        } else if (isFeeding && isFlywheelOn) {
            cycleTime = shootPulseTimer.seconds() % PULSE_PERIOD_SEC;
            if (cycleTime < PULSE_ON_DURATION_SEC) {
                currentAppliedPower = feedPower;
            } else {
                currentAppliedPower = 0.0;
            }
        } else if (isCollectOn) {
            currentAppliedPower = collectPower;
        } else {
            currentAppliedPower = 0.0;
        }

        setPowerToHardware(currentAppliedPower);

        return buttonPressed;
    }

    private void setPowerToHardware(double pwr) {
        if (intakeMotor != null) {
            intakeMotor.setPower(pwr);
        }
        if (intakeServoLeft != null) {
            intakeServoLeft.setPower(pwr);
        }
        if (intakeServoRight != null) {
            intakeServoRight.setPower(pwr);
        }
    }

    public boolean isCollectOn() {
        return isCollectOn;
    }

    public boolean isFeeding() {
        return isFeeding;
    }

    public double getCurrentPower() {
        return currentAppliedPower;
    }
}
