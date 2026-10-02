package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Subsystem class representing the Flywheel mechanism.
 * Handles target power adjustments via Gamepad 2 D-Pad Up/Down (Speed Up / Speed Down)
 * and toggling via Gamepad 1 B button.
 * Uses DcMotor.ZeroPowerBehavior.BRAKE for active fast motor braking.
 * Default target power is set to 0.95 (max limit 1.0).
 * Pressing B executes a startup sequence:
 * 1. Reverses flywheel and intake at -0.5 power for 100 ms (shortened time) to clear stuck balls.
 * 2. Pauses both for 300 ms.
 * 3. Spins flywheel forward at set target power.
 */
public class Flywheel {
    public enum StartPhase { IDLE, REVERSE_BUMP, PAUSE, RUNNING }

    private DcMotor flywheelMotor;
    private double targetPower = 0.95; // Default target power updated to 0.95
    private StartPhase phase = StartPhase.IDLE;

    private ElapsedTime phaseTimer;
    private static final double REVERSE_DURATION_SEC = 0.100; // 100 ms reverse bump
    private static final double PAUSE_DURATION_SEC = 0.300;   // 300 ms pause

    private boolean previousBState = false;
    private boolean previousDpadUpState = false;
    private boolean previousDpadDownState = false;

    // Working variables declared as fields to avoid local re-declaration
    private boolean buttonPressed = false;
    private double currentAppliedPower = 0.0;

    /**
     * Initializes the flywheel motor from the HardwareMap with BRAKE zero power behavior.
     *
     * @param hardwareMap HardwareMap reference from the OpMode
     */
    public void init(HardwareMap hardwareMap) {
        flywheelMotor = hardwareMap.get(DcMotor.class, "flywheel");
        flywheelMotor.setDirection(DcMotor.Direction.REVERSE);
        flywheelMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

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
     * Starts the flywheel spooling forward directly without running the reverse bump sequence.
     * Useful for Autonomous when balls are preloaded and reversing the intake would spit them out.
     */
    public void startDirect() {
        phase = StartPhase.RUNNING;
        targetPower = 0.95;
        currentAppliedPower = targetPower;
        if (flywheelMotor != null) {
            flywheelMotor.setPower(currentAppliedPower);
        }
    }

    /**
     * Updates the flywheel state and motor power.
     * Gamepad 1 B button toggles flywheel startup sequence.
     * Gamepad 2 DPad Up/Down adjusts flywheel target speed.
     *
     * @param gamepad1 Primary driver gamepad (B button for Flywheel toggle)
     * @param gamepad2 Co-driver gamepad (DPad Up/Down for Flywheel target tuning)
     * @param isReversed True if reverse/rejection mode (Left Bumper / X) is active
     * @return true if a flywheel control button was pressed on this cycle
     */
    public boolean update(Gamepad gamepad1, Gamepad gamepad2, boolean isReversed) {
        buttonPressed = false;

        // Gamepad 1 B Button Press: Handle 3-phase startup sequence -> Turn Flywheel ON / OFF
        if (gamepad1 != null && gamepad1.b && !previousBState) {
            if (phase != StartPhase.IDLE) {
                // If flywheel is currently in startup or running, turn it OFF
                stop();
            } else {
                // Start sequence: Phase 1 = Reverse bump for 100 ms
                phase = StartPhase.REVERSE_BUMP;
                phaseTimer.reset();
            }
            buttonPressed = true;
        }
        previousBState = (gamepad1 != null) && gamepad1.b;

        // Gamepad 2 DPad Up / Down strictly tunes flywheel target speed (leaving Gamepad 1 DPad 100% for slow drive)
        if (gamepad2 != null) {
            // Increase set power with Gamepad 2 Dpad UP (rising edge, max limit 1.0)
            if (gamepad2.dpad_up && !previousDpadUpState) {
                targetPower = Math.min(1.0, targetPower + 0.05);
                targetPower = Math.round(targetPower * 100.0) / 100.0;
                buttonPressed = true;
            }
            previousDpadUpState = gamepad2.dpad_up;

            // Decrease set power with Gamepad 2 Dpad DOWN (rising edge, min limit 0.0)
            if (gamepad2.dpad_down && !previousDpadDownState) {
                targetPower = Math.max(0.0, targetPower - 0.05);
                targetPower = Math.round(targetPower * 100.0) / 100.0;
                buttonPressed = true;
            }
            previousDpadDownState = gamepad2.dpad_down;
        }

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

        // Determine motor power (-0.5 power during reverse bump or rejection)
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

    /**
     * Single gamepad overload for backwards compatibility or autonomous.
     */
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
