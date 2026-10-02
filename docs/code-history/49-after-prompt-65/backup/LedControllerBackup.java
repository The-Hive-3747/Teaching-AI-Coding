package org.firstinspires.ftc.teamcode.backup;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Backup copy of LedController subsystem.
 */
public class LedControllerBackup {

    public static final double COLOR_RED = 0.00;
    public static final double COLOR_ORANGE = 0.17;
    public static final double COLOR_YELLOW = 0.33;
    public static final double COLOR_GREEN = 0.50;
    public static final double COLOR_BLUE = 0.67;
    public static final double COLOR_PURPLE = 0.83;
    public static final double COLOR_WHITE = 1.00;

    private Servo ledLeft;
    private Servo ledRight;

    private double currentPosition = COLOR_OFF();

    public static double COLOR_OFF() {
        return COLOR_WHITE;
    }

    public void init(HardwareMap hardwareMap) {
        ledLeft = hardwareMap.get(Servo.class, "led_left");
        ledRight = hardwareMap.get(Servo.class, "led_right");

        if (ledLeft instanceof PwmControl) {
            ((PwmControl) ledLeft).setPwmRange(new PwmControl.PwmRange(500, 2500));
        }
        if (ledRight instanceof PwmControl) {
            ((PwmControl) ledRight).setPwmRange(new PwmControl.PwmRange(500, 2500));
        }

        if (ledLeft != null) {
            ledLeft.setDirection(Servo.Direction.FORWARD);
        }
        if (ledRight != null) {
            ledRight.setDirection(Servo.Direction.FORWARD);
        }

        setColor(COLOR_YELLOW);
    }

    public void setColor(double colorPosition) {
        currentPosition = colorPosition;
        if (ledLeft != null) {
            ledLeft.setPosition(colorPosition);
        }
        if (ledRight != null) {
            ledRight.setPosition(colorPosition);
        }
    }

    public double getCurrentPosition() {
        return currentPosition;
    }
}
