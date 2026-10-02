package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Subsystem class representing the Intake mechanism.
 * Controls intake motor and continuous rotation intake servos:
 * - Control Hub Port 1: "intake_servo_left"
 * - Expansion Hub Port 5: "intake_servo_right"
 * Controls:
 * - A Button: Toggle Collect Mode (0.5 power)
 * - Right Bumper (RB) during Phase 3 (Flywheel Running): Pulsed Feed Mode (100 ms ON at 0.5 power / 200 ms OFF at 0.0 power)
 * - Flywheel 100 ms Bump: Reverse intake (-0.5 power) alongside flywheel startup, and STOP intake collect mode so it does NOT resume
 * - Left Bumper (LB) / X: Reject Mode (-0.5 power)
 */
public class Intake {
    private DcMotor intakeMotor;
    private CRServo intakeServoLeft;  // Control Hub Servo Port 1
    private CRServo intakeServoRight; // Expansion Hub Servo Port 5

    private final double collectPower = 0.5;
    private final double feedPower = 0.5;
    private final double reversePower = -0.5;

    private boolean isCollectOn = false;
    private boolean isFeeding = false;
    private boolean previousRBState = false;
    private boolean previousAState = false;

    // Pulse Timer for 100 ms ON / 200 ms OFF shooting pulse
    private ElapsedTime shootPulseTimer;
    private static final double PULSE_PERIOD_SEC = 0.300;     // 300 ms total cycle
    private static final double PULSE_ON_DURATION_SEC = 0.100; // 100 ms ON duration

    // Working variables declared as fields to avoid local re-declaration
    private boolean buttonPressed = false;
    private double currentAppliedPower = 0.0;
    private double cycleTime = 0.0;

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

        shootPulseTimer = new ElapsedTime();
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
     * flywheel startup sequence phases, and reverse mode.
     *
     * @param gamepad Gamepad input to check (A for Collect, RB for Pulsed Feed)
     * @param isReversed True if reverse mode (LB / X) is active
     * @param isFlywheelReversing True during Phase 1 (Flywheel 100 ms reverse bump)
     * @param isFlywheelPausing True during Phase 2 (Flywheel 300 ms pause)
     * @param isFlywheelOn True during Phase 3 (Flywheel running forward)
     * @return true if intake control button (A) was pressed on this cycle
     */
    public boolean update(Gamepad gamepad, boolean isReversed, boolean isFlywheelReversing, boolean isFlywheelPausing, boolean isFlywheelOn) {
        buttonPressed = false;

        // Toggle Collect Mode with A button (rising edge)
        if (gamepad.a && !previousAState) {
            isCollectOn = !isCollectOn;
            buttonPressed = true;
        }
        previousAState = gamepad.a;

        // Check if Right Bumper (RB) is held for Feed Mode
        isFeeding = gamepad.right_bumper;

        if (isFeeding && !previousRBState) {
            // Reset pulse timer on initial press of RB
            shootPulseTimer.reset();
        }
        previousRBState = isFeeding;

        // Determine target power priority
        if (isFlywheelReversing) {
            // 100 ms Flywheel Startup Bump: Reverse intake at -0.5 power AND turn off collect mode so it stays stopped after startup
            isCollectOn = false;
            currentAppliedPower = reversePower;
        } else if (isReversed) {
            // Full Rejection Mode (LB / X): Reverse at -0.5 power
            currentAppliedPower = reversePower;
        } else if (isFlywheelPausing) {
            // Phase 2 (300 ms Flywheel Pause): Stopped at 0.0 power
            currentAppliedPower = 0.0;
        } else if (isFeeding && isFlywheelOn) {
            // Phase 3 (Flywheel Running Forward) + RB held:
            // Pulse Intake: 100 ms ON at 0.5 power / 200 ms OFF at 0.0 power
            cycleTime = shootPulseTimer.seconds() % PULSE_PERIOD_SEC;
            if (cycleTime < PULSE_ON_DURATION_SEC) {
                currentAppliedPower = feedPower; // 100 ms ON (0.5 power)
            } else {
                currentAppliedPower = 0.0;       // 200 ms OFF (0.0 power)
            }
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
