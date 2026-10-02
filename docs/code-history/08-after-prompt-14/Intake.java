package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Subsystem class representing the Intake mechanism.
 * Controls intake motor and continuous rotation intake servos:
 * - Control Hub Port 1: "intake_servo_left"
 * - Expansion Hub Port 5: "intake_servo_right"
 * Handles toggling via A button and reverse behavior during ball rejection.
 */
public class Intake {
    private DcMotor intakeMotor;
    private CRServo intakeServoLeft;  // Control Hub Servo Port 1
    private CRServo intakeServoRight; // Expansion Hub Servo Port 5

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
        intakeServoLeft = hardwareMap.get(CRServo.class, "intake_servo_left");
        intakeServoRight = hardwareMap.get(CRServo.class, "intake_servo_right");

        // Reverse left intake servo so both servos rotate to pull in balls
        if (intakeServoLeft != null) {
            intakeServoLeft.setDirection(CRServo.Direction.REVERSE);
        }

        stop();
    }

    /**
     * Explicitly stops the intake motor and servos (0.0 power).
     */
    public void stop() {
        isOn = false;
        currentAppliedPower = 0.0;
        setPowerToHardware(0.0);
    }

    /**
     * Updates the intake state, motor power, and servo powers based on gamepad input and reverse mode.
     *
     * @param gamepad Gamepad input to check (A button for toggle)
     * @param isReversed True if reverse mode (Left Bumper / X) is active
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
        if (intakeServoLeft != null) {
            intakeServoLeft.setPower(pwr);
        }
        if (intakeServoRight != null) {
            intakeServoRight.setPower(pwr);
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
