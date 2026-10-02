package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Subsystem class representing the Intake mechanism.
 * Handles toggling via A button and reverse motor behavior during ball rejection.
 */
public class Intake {
    private DcMotor intakeMotor;
    private final double power = 0.5;
    private boolean isOn = false;

    private boolean previousAState = false;

    // Working variable declared as field to avoid local re-declaration
    private boolean buttonPressed = false;

    /**
     * Initializes the intake motor from the HardwareMap.
     *
     * @param hardwareMap HardwareMap reference from the OpMode
     */
    public void init(HardwareMap hardwareMap) {
        intakeMotor = hardwareMap.get(DcMotor.class, "intake");
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeMotor.setPower(0.0);
    }

    /**
     * Updates the intake state and motor power based on gamepad input and reverse mode.
     *
     * @param gamepad Gamepad input to check
     * @param isReversed True if reverse mode (X button) is active
     * @return true if intake control button (A) was pressed on this cycle
     */
    public boolean update(Gamepad gamepad, boolean isReversed) {
        buttonPressed = false;

        // Toggle ON/OFF with A button (rising edge)
        if (gamepad.a && !previousAState) {
            isOn = !isOn;
            buttonPressed = true;
        }
        previousAState = gamepad.a;

        // Determine motor power
        if (isReversed) {
            intakeMotor.setPower(-0.3);
        } else if (isOn) {
            intakeMotor.setPower(power);
        } else {
            intakeMotor.setPower(0.0);
        }

        return buttonPressed;
    }

    public double getPower() {
        return power;
    }

    public boolean isOn() {
        return isOn;
    }

    public double getCurrentPower() {
        return intakeMotor != null ? intakeMotor.getPower() : 0.0;
    }
}
