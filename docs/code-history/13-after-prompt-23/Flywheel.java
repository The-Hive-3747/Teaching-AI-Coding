package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Subsystem class representing the Flywheel mechanism.
 * Handles target power adjustments via D-Pad and toggling via B button.
 * Pressing B executes a 200 ms intake reverse bump first, then turns on the flywheel.
 */
public class Flywheel {
    private DcMotor flywheelMotor;
    private double targetPower = 0.6;
    private boolean isOn = false;

    // 200 ms Reverse Bump state machine
    private boolean isBumping = false;
    private ElapsedTime bumpTimer;
    private static final double BUMP_DURATION_SEC = 0.200; // 200 ms

    private boolean previousBState = false;
    private boolean previousDpadUpState = false;
    private boolean previousDpadDownState = false;

    // Working variable declared as field to avoid local re-declaration
    private boolean buttonPressed = false;

    /**
     * Initializes the flywheel motor from the HardwareMap.
     *
     * @param hardwareMap HardwareMap reference from the OpMode
     */
    public void init(HardwareMap hardwareMap) {
        flywheelMotor = hardwareMap.get(DcMotor.class, "flywheel");
        flywheelMotor.setDirection(DcMotor.Direction.REVERSE);
        flywheelMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        bumpTimer = new ElapsedTime();
        stop();
    }

    /**
     * Explicitly stops the flywheel and cancels any active bump sequence.
     */
    public void stop() {
        isOn = false;
        isBumping = false;
        if (flywheelMotor != null) {
            flywheelMotor.setPower(0.0);
        }
    }

    /**
     * Updates the flywheel state and motor power based on gamepad input and reverse mode.
     * Pressing B button initiates a 200 ms reverse bump before turning on the flywheel.
     *
     * @param gamepad Gamepad input to check
     * @param isReversed True if reverse mode (Left Bumper / X) is active
     * @return true if a flywheel control button (B, DPad UP, DPad DOWN) was pressed on this cycle
     */
    public boolean update(Gamepad gamepad, boolean isReversed) {
        buttonPressed = false;

        // B Button Press: Handle 200 ms bump sequence -> Turn Flywheel ON / OFF
        if (gamepad.b && !previousBState) {
            if (isOn) {
                // If flywheel is currently ON, turn it OFF immediately
                isOn = false;
                isBumping = false;
            } else if (!isBumping) {
                // If flywheel is OFF, start 200 ms reverse bump sequence
                isBumping = true;
                bumpTimer.reset();
            }
            buttonPressed = true;
        }
        previousBState = gamepad.b;

        // Increase set power with Dpad UP (rising edge)
        if (gamepad.dpad_up && !previousDpadUpState) {
            targetPower = Math.min(0.9, targetPower + 0.05);
            targetPower = Math.round(targetPower * 100.0) / 100.0;
            buttonPressed = true;
        }
        previousDpadUpState = gamepad.dpad_up;

        // Decrease set power with Dpad DOWN (rising edge)
        if (gamepad.dpad_down && !previousDpadDownState) {
            targetPower = Math.max(0.0, targetPower - 0.05);
            targetPower = Math.round(targetPower * 100.0) / 100.0;
            buttonPressed = true;
        }
        previousDpadDownState = gamepad.dpad_down;

        // Handle 200 ms Bump Timing State Machine
        if (isBumping) {
            if (bumpTimer.seconds() >= BUMP_DURATION_SEC) {
                // 200 ms bump finished -> Turn Flywheel ON!
                isBumping = false;
                isOn = true;
            }
        }

        // Determine motor power
        if (isReversed) {
            flywheelMotor.setPower(-0.3);
        } else if (isOn) {
            flywheelMotor.setPower(targetPower);
        } else {
            flywheelMotor.setPower(0.0);
        }

        return buttonPressed;
    }

    public boolean isBumping() {
        return isBumping;
    }

    public double getTargetPower() {
        return targetPower;
    }

    public boolean isOn() {
        return isOn;
    }

    public double getCurrentPower() {
        return flywheelMotor != null ? flywheelMotor.getPower() : 0.0;
    }
}
