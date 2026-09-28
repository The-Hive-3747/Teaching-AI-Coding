package org.firstinspires.ftc.teamcode.backup;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Backup copy of Flywheel subsystem.
 */
public class FlywheelBackup {
    public enum StartPhase { IDLE, REVERSE_BUMP, PAUSE, RUNNING }

    private DcMotor flywheelMotor;
    private double targetPower = 1.0;
    private StartPhase phase = StartPhase.IDLE;

    private ElapsedTime phaseTimer;
    private static final double REVERSE_DURATION_SEC = 0.100;
    private static final double PAUSE_DURATION_SEC = 0.300;

    private boolean previousBState = false;
    private boolean previousRightTriggerState = false;
    private boolean previousLeftTriggerState = false;
    private static final double TRIGGER_THRESHOLD = 0.5;

    private boolean buttonPressed = false;
    private double currentAppliedPower = 0.0;

    public void init(HardwareMap hardwareMap) {
        flywheelMotor = hardwareMap.get(DcMotor.class, "flywheel");
        flywheelMotor.setDirection(DcMotor.Direction.REVERSE);
        flywheelMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        phaseTimer = new ElapsedTime();
        stop();
    }

    public void stop() {
        phase = StartPhase.IDLE;
        currentAppliedPower = 0.0;
        if (flywheelMotor != null) {
            flywheelMotor.setPower(0.0);
        }
    }

    public void startDirect() {
        phase = StartPhase.RUNNING;
        targetPower = 1.0;
        currentAppliedPower = targetPower;
        if (flywheelMotor != null) {
            flywheelMotor.setPower(currentAppliedPower);
        }
    }

    public boolean update(Gamepad gamepad1, Gamepad gamepad2, boolean isReversed) {
        buttonPressed = false;

        if (gamepad1 != null && gamepad1.b && !previousBState) {
            if (phase != StartPhase.IDLE) {
                stop();
            } else {
                phase = StartPhase.REVERSE_BUMP;
                phaseTimer.reset();
            }
            buttonPressed = true;
        }
        previousBState = (gamepad1 != null) && gamepad1.b;

        Gamepad tuningGamepad = (gamepad2 != null) ? gamepad2 : gamepad1;

        if (tuningGamepad != null) {
            boolean rightTriggerPressed = tuningGamepad.right_trigger > TRIGGER_THRESHOLD;
            boolean leftTriggerPressed = tuningGamepad.left_trigger > TRIGGER_THRESHOLD;

            if (rightTriggerPressed && !previousRightTriggerState) {
                targetPower = Math.min(1.0, targetPower + 0.05);
                targetPower = Math.round(targetPower * 100.0) / 100.0;
                buttonPressed = true;
            }
            previousRightTriggerState = rightTriggerPressed;

            if (leftTriggerPressed && !previousLeftTriggerState) {
                targetPower = Math.max(0.0, targetPower - 0.05);
                targetPower = Math.round(targetPower * 100.0) / 100.0;
                buttonPressed = true;
            }
            previousLeftTriggerState = leftTriggerPressed;
        }

        if (phase == StartPhase.REVERSE_BUMP) {
            if (phaseTimer.seconds() >= REVERSE_DURATION_SEC) {
                phase = StartPhase.PAUSE;
                phaseTimer.reset();
            }
        } else if (phase == StartPhase.PAUSE) {
            if (phaseTimer.seconds() >= PAUSE_DURATION_SEC) {
                phase = StartPhase.RUNNING;
            }
        }

        if (isReversed || phase == StartPhase.REVERSE_BUMP) {
            currentAppliedPower = -0.5;
        } else if (phase == StartPhase.PAUSE) {
            currentAppliedPower = 0.0;
        } else if (phase == StartPhase.RUNNING) {
            currentAppliedPower = targetPower;
        } else {
            currentAppliedPower = 0.0;
        }

        flywheelMotor.setPower(currentAppliedPower);

        return buttonPressed;
    }

    public boolean update(Gamepad gamepad, boolean isReversed) {
        return update(gamepad, gamepad, isReversed);
    }

    public boolean isReversing() {
        return phase == StartPhase.REVERSE_BUMP;
    }

    public boolean isPausing() {
        return phase == StartPhase.PAUSE;
    }

    public boolean isOn() {
        return phase == StartPhase.RUNNING;
    }

    public StartPhase getPhase() {
        return phase;
    }

    public double getTargetPower() {
        return targetPower;
    }

    public double getCurrentPower() {
        return currentAppliedPower;
    }
}
