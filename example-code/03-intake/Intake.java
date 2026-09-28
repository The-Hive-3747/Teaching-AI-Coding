package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Checkpoint 3 — Intake subsystem: collect and reject only. (Feed pulsing comes with the flywheel.)
 *
 * Derived from The Hive's final Intake.java.
 *
 * Hardware: one motor "intake" plus two continuous-rotation servos
 * "intake_servo_left" (reversed) and "intake_servo_right". All three run together.
 *
 * Controls:
 *   A button (press)      toggle Collect mode (0.5 power)
 *   reject mode (from TeleOp, LB or X)  run everything at -0.5
 */
public class Intake {
    private DcMotor intakeMotor;
    private CRServo intakeServoLeft;   // Control Hub servo port 1
    private CRServo intakeServoRight;  // Expansion Hub servo port 5

    private final double collectPower = 0.5;
    private final double reversePower = -0.5;

    private boolean isCollectOn = false;
    private boolean previousAState = false;

    private boolean buttonPressed = false;
    private double currentAppliedPower = 0.0;

    public void init(HardwareMap hardwareMap) {
        intakeMotor = hardwareMap.get(DcMotor.class, "intake");
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        intakeServoLeft = hardwareMap.get(CRServo.class, "intake_servo_left");
        intakeServoRight = hardwareMap.get(CRServo.class, "intake_servo_right");

        // Reverse the left servo so both pull balls in
        if (intakeServoLeft != null) {
            intakeServoLeft.setDirection(CRServo.Direction.REVERSE);
        }
        stop();
    }

    public void stop() {
        isCollectOn = false;
        currentAppliedPower = 0.0;
        setPowerToHardware(0.0);
    }

    /**
     * @param gamepad    gamepad 1 (A toggles collect)
     * @param isReversed true while reject mode is active
     * @return true if A was pressed this cycle
     */
    public boolean update(Gamepad gamepad, boolean isReversed) {
        buttonPressed = false;

        // A toggles Collect on the rising edge (so holding A doesn't flicker it)
        if (gamepad.a && !previousAState) {
            isCollectOn = !isCollectOn;
            buttonPressed = true;
        }
        previousAState = gamepad.a;

        if (isReversed) {
            currentAppliedPower = reversePower;   // Reject: push balls out
        } else if (isCollectOn) {
            currentAppliedPower = collectPower;   // Collect: pull balls in
        } else {
            currentAppliedPower = 0.0;            // Idle
        }

        setPowerToHardware(currentAppliedPower);
        return buttonPressed;
    }

    private void setPowerToHardware(double pwr) {
        if (intakeMotor != null) intakeMotor.setPower(pwr);
        if (intakeServoLeft != null) intakeServoLeft.setPower(pwr);
        if (intakeServoRight != null) intakeServoRight.setPower(pwr);
    }

    public boolean isCollectOn() { return isCollectOn; }
    public double getCurrentPower() { return currentAppliedPower; }
}
