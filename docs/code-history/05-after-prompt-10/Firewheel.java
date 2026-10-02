package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Subsystem class representing the Firewheels mechanism.
 * Controls the left and right feed servos that transfer balls from the intake into the flywheel to shoot.
 * - Control Hub Port 5: "firewheel_left"
 * - Expansion Hub Port 1: "firewheel_right"
 */
public class Firewheel {
    private CRServo firewheelLeft;
    private CRServo firewheelRight;

    private final double shootPower = 0.8;
    private final double reversePower = -0.3;
    private boolean isShooting = false;

    // Field variables declared to prevent local re-declarations in update loop
    private double currentAppliedPower = 0.0;

    /**
     * Initializes the firewheel continuous rotation servos from the HardwareMap.
     *
     * @param hardwareMap HardwareMap reference from the OpMode
     */
    public void init(HardwareMap hardwareMap) {
        // Control Hub Port 5
        firewheelLeft = hardwareMap.get(CRServo.class, "firewheel_left");
        // Expansion Hub Port 1
        firewheelRight = hardwareMap.get(CRServo.class, "firewheel_right");

        // Reverse right servo if needed so both feed balls forward
        if (firewheelRight != null) {
            firewheelRight.setDirection(CRServo.Direction.REVERSE);
        }

        setPowerToHardware(0.0);
    }

    /**
     * Updates the firewheel servo powers based on gamepad input (Right Bumper) and reverse mode.
     *
     * @param gamepad Gamepad input to check (right_bumper for shoot)
     * @param isReversed True if reverse mode (Left Bumper / X) is active
     * @return true if shoot bumper or reverse mode was active on this cycle
     */
    public boolean update(Gamepad gamepad, boolean isReversed) {
        isShooting = gamepad.right_bumper;

        if (isReversed) {
            currentAppliedPower = reversePower;
        } else if (isShooting) {
            currentAppliedPower = shootPower;
        } else {
            currentAppliedPower = 0.0;
        }

        setPowerToHardware(currentAppliedPower);

        return isShooting;
    }

    private void setPowerToHardware(double pwr) {
        if (firewheelLeft != null) {
            firewheelLeft.setPower(pwr);
        }
        if (firewheelRight != null) {
            firewheelRight.setPower(pwr);
        }
    }

    public boolean isShooting() {
        return isShooting;
    }

    public double getCurrentPower() {
        return currentAppliedPower;
    }
}
