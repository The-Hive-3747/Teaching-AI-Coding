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
 * Controls:
 * - A Button: Toggle Collect Mode (0.5 power)
 * - Right Bumper (RB): Hold Slow Feed Mode (0.25 power) to feed balls into flywheel
 * - Flywheel 200 ms Bump: Reverses intake (-0.3 power) before flywheel turns on
 * - Left Bumper (LB) / X: Reject Mode (-0.3 power)
 */
public class Intake {
    private DcMotor intakeMotor;
    private CRServo intakeServoLeft;  // Control Hub Servo Port 1
    private CRServo intakeServoRight; // Expansion Hub Servo Port 5

    private final double collectPower = 0.5;
    private final double slowFeedPower = 0.25;
    private final double reversePower = -0.3;

    private boolean isCollectOn = false;
    private boolean isFeeding = false;

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
        isCollectOn = false;
        isFeeding = false;
        currentAppliedPower = 0.0;
        setPowerToHardware(0.0);
    }

    /**
     * Updates the intake state, motor power, and servo powers based on gamepad input,
     * flywheel bump state, and reverse mode.
     *
     * @param gamepad Gamepad input to check (A for Collect, RB for Slow Feed)
     * @param isReversed True if reverse mode (LB / X) is active
     * @param isFlywheelBumping True if flywheel 200 ms reverse bump is active
     * @return true if intake control button (A) was pressed on this cycle
     */
    public boolean update(Gamepad gamepad, boolean isReversed, boolean isFlywheelBumping) {
        buttonPressed = false;

        // Toggle Collect Mode with A button (rising edge)
        if (gamepad.a && !previousAState) {
            isCollectOn = !isCollectOn;
            buttonPressed = true;
        }
        previousAState = gamepad.a;

        // Check if Right Bumper (RB) is held for Slow Feed
        isFeeding = gamepad.right_bumper;

        // Determine target power priority
        if (isReversed || isFlywheelBumping) {
            // Rejection Mode OR 200 ms Flywheel Bump: Reverse at -0.3 power
            currentAppliedPower = reversePower;
        } else if (isFeeding) {
            // Slow Feed Mode (RB held): Feed balls slowly into flywheel at 0.25 power
            currentAppliedPower = slowFeedPower;
        } else if (isCollectOn) {
            // Collect Mode (A toggled ON): Intake balls at 0.5 power
            currentAppliedPower = collectPower;
        } else {
            // Idle: Off
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
