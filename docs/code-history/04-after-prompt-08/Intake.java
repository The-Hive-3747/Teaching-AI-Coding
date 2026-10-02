package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Subsystem class representing the Intake mechanism.
 * Controls intake motor and continuous rotation intake servos.
 * Handles toggling via A button and reverse behavior during ball rejection.
 */
public class Intake {
    private DcMotor intakeMotor;
    private CRServo intakeServoExpansion; // Expansion Hub Servo Port 5
    private CRServo intakeServoControl;   // Control Hub Servo Port 1

    private final double power = 0.5;
    private boolean isOn = false;

    private boolean previousAState = false;

    // Working variables declared as fields to avoid local re-declaration
    private boolean buttonPressed = false;
    private double currentAppliedPower = 0.0;

    /**
     * Initializes the intake motor and continuous rotation servos from the HardwareMap.
     *
     * @param hardwareMap HardwareMap reference from the OpMode
     */
    public void init(HardwareMap hardwareMap) {
        intakeMotor = hardwareMap.get(DcMotor.class, "intake");
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Continuous rotation servos
        intakeServoExpansion = hardwareMap.get(CRServo.class, "intake_servo_expansion");
        intakeServoControl = hardwareMap.get(CRServo.class, "intake_servo_control");

        setPowerToHardware(0.0);
    }

    /**
     * Updates the intake state, motor power, and servo powers based on gamepad input and reverse mode.
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

        // Determine target power
        if (isReversed) {
            currentAppliedPower = -0.3;
        } else if (isOn) {
            currentAppliedPower = power;
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
        if (intakeServoExpansion != null) {
            intakeServoExpansion.setPower(pwr);
        }
        if (intakeServoControl != null) {
            intakeServoControl.setPower(pwr);
        }
    }

    public double getPower() {
        return power;
    }

    public boolean isOn() {
        return isOn;
    }

    public double getCurrentPower() {
        return currentAppliedPower;
    }
}
