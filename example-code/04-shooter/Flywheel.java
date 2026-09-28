package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Checkpoint 4 — Flywheel subsystem, first version.
 *
 * Derived from The Hive's final Flywheel.java, rolled back to before the Checkpoint 5 refinements:
 *   - reverse bump is still 200 ms (later shortened to 100 ms: "it is spitting balls out")
 *   - target power is still 1.0 (later 0.95)
 *   - speed tuning is on gamepad 1's D-pad, which collides with precision drive
 *     (later moved to gamepad 2: "I thought gamepad 1 dpad was slow mode, not tuning the flywheel")
 *
 * Pressing B runs a 3-phase startup:
 *   1. REVERSE_BUMP  flywheel (and intake) backward at -0.5 to clear a stuck ball
 *   2. PAUSE         everything stopped for 300 ms
 *   3. RUNNING       flywheel forward at target power
 * Pressing B again stops it.
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
     * Updates the flywheel state and motor power.
     * B toggles the startup sequence. D-pad up/down adjusts target speed by 0.05.
     *
     * @param gamepad1   driver gamepad
     * @param isReversed true if reject mode (LB / X) is active
     * @return true if a flywheel control button was pressed this cycle
     */
    public boolean update(Gamepad gamepad1, boolean isReversed) {
        Gamepad gamepad2 = gamepad1;   // tuning is on the same gamepad for now
        buttonPressed = false;

        // Gamepad 1 B Button Press: Handle 3-phase startup sequence -> Turn Flywheel ON / OFF
        if (gamepad1 != null && gamepad1.b && !previousBState) {
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
        previousBState = (gamepad1 != null) && gamepad1.b;

        // D-pad Up / Down tunes flywheel target speed
        if (gamepad2 != null) {
            // Increase set power with Dpad UP (rising edge, max limit 1.0)
            if (gamepad2.dpad_up && !previousDpadUpState) {
                targetPower = Math.min(1.0, targetPower + 0.05);
                targetPower = Math.round(targetPower * 100.0) / 100.0;
                buttonPressed = true;
            }
            previousDpadUpState = gamepad2.dpad_up;

            // Decrease set power with Dpad DOWN (rising edge, min limit 0.0)
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
