package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Subsystem class representing the Flywheel mechanism.
 * Handles target power adjustments via D-Pad and toggling via B button.
 * Default target power is set to 1.0 (max limit 1.0).
 * Pressing B executes a startup sequence:
 * 1. Reverses flywheel and intake at -0.3 power for 200 ms to clear stuck balls.
 * 2. Pauses both for 300 ms.
 * 3. Spins flywheel forward at set target power.
 */
public class Flywheel {
    public enum StartPhase { IDLE, REVERSE_BUMP, PAUSE, RUNNING }

    private DcMotor flywheelMotor;
    private double targetPower = 1.0;
    private StartPhase phase = StartPhase.IDLE;

    private ElapsedTime phaseTimer;
    private static final double REVERSE_DURATION_SEC = 0.200; // 200 ms reverse bump
    private static final double PAUSE_DURATION_SEC = 0.300;   // 300 ms pause

    private boolean previousBState = false;
    private boolean previousDpadUpState = false;
    private boolean previousDpadDownState = false;

    // Working variables declared as fields to avoid local re-declaration
    private boolean buttonPressed = false;
    private double currentAppliedPower = 0.0;

    /**
     * Initializes the flywheel motor from the HardwareMap.
     *
     * @param hardwareMap HardwareMap reference from the OpMode
     */
    public void init(HardwareMap hardwareMap) {
        flywheelMotor = hardwareMap.get(DcMotor.class, "flywheel");
        flywheelMotor.setDirection(DcMotor.Direction.REVERSE);
        flywheelMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        phaseTimer = new ElapsedTime();
        stop();
    }

    /**
     * Explicitly stops the flywheel and cancels any active startup sequence.
     */
    public void stop() {
        phase = StartPhase.IDLE;
        currentAppliedPower = 0.0;
        if (flywheelMotor != null) {
            flywheelMotor.setPower(0.0);
        }
    }

    /**
     * Updates the flywheel state and motor power based on gamepad input and reverse mode.
     * Pressing B button initiates the 200 ms reverse bump -> 300 ms pause -> forward spool sequence.
     *
     * @param gamepad Gamepad input to check
     * @param isReversed True if reverse mode (Left Bumper / X) is active
     * @return true if a flywheel control button (B, DPad UP, DPad DOWN) was pressed on this cycle
     */
    public boolean update(Gamepad gamepad, boolean isReversed) {
        buttonPressed = false;

        // B Button Press: Handle 3-phase startup sequence -> Turn Flywheel ON / OFF
        if (gamepad.b && !previousBState) {
            if (phase != StartPhase.IDLE) {
                // If flywheel is currently in startup or running, turn it OFF
                stop();
            } else {
                // Start sequence: Phase 1 = Reverse bump for 200 ms
                phase = StartPhase.REVERSE_BUMP;
                phaseTimer.reset();
            }
            buttonPressed = true;
        }
        previousBState = gamepad.b;

        // Increase set power with Dpad UP (rising edge, max limit 1.0)
        if (gamepad.dpad_up && !previousDpadUpState) {
            targetPower = Math.min(1.0, targetPower + 0.05);
            targetPower = Math.round(targetPower * 100.0) / 100.0;
            buttonPressed = true;
        }
        previousDpadUpState = gamepad.dpad_up;

        // Decrease set power with Dpad DOWN (rising edge, min limit 0.0)
        if (gamepad.dpad_down && !previousDpadDownState) {
            targetPower = Math.max(0.0, targetPower - 0.05);
            targetPower = Math.round(targetPower * 100.0) / 100.0;
            buttonPressed = true;
        }
        previousDpadDownState = gamepad.dpad_down;

        // Startup Phase State Machine
        if (phase == StartPhase.REVERSE_BUMP) {
            if (phaseTimer.seconds() >= REVERSE_DURATION_SEC) {
                // Transition to Phase 2: Pause for 300 ms
                phase = StartPhase.PAUSE;
                phaseTimer.reset();
            }
        } else if (phase == StartPhase.PAUSE) {
            if (phaseTimer.seconds() >= PAUSE_DURATION_SEC) {
                // Transition to Phase 3: Spool Forward at Target Power
                phase = StartPhase.RUNNING;
            }
        }

        // Determine motor power
        if (isReversed || phase == StartPhase.REVERSE_BUMP) {
            currentAppliedPower = -0.3;
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
