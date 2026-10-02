package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Subsystem class representing the Flywheel mechanism.
 * Handles target power adjustments via D-Pad, toggling via B button,
 * and reverse motor behavior during ball rejection.
 */
public class Flywheel {
    private DcMotor flywheelMotor;
    private double targetPower = 0.6;
    private boolean isOn = false;

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
        flywheelMotor.setDirection(DcMotor.Direction.FORWARD);
        flywheelMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        flywheelMotor.setPower(0.0);
    }

    /**
     * Updates the flywheel state and motor power based on gamepad input and reverse mode.
     *
     * @param gamepad Gamepad input to check
     * @param isReversed True if reverse mode (X button) is active
     * @return true if a flywheel control button (B, DPad UP, DPad DOWN) was pressed on this cycle
     */
    public boolean update(Gamepad gamepad, boolean isReversed) {
        buttonPressed = false;

        // Toggle ON/OFF with B button (rising edge)
        if (gamepad.b && !previousBState) {
            isOn = !isOn;
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
