package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Controller class for goBILDA LED light modules.
 * Uses PWM servo positions based on the goBILDA LED color mapping chart:
 * - Red: 0.00 (500 µs)
 * - Orange: 0.17 (~833 µs)
 * - Yellow: 0.33 (~1166 µs)
 * - Green: 0.50 (1500 µs)
 * - Blue: 0.67 (~1833 µs)
 * - Purple: 0.83 (~2166 µs)
 * - White / Special: 1.00 (2500 µs)
 */
public class LedController {

    // Color constants for goBILDA LED modules
    public static final double COLOR_RED = 0.00;
    public static final double COLOR_ORANGE = 0.17;
    public static final double COLOR_YELLOW = 0.33;
    public static final double COLOR_GREEN = 0.50;
    public static final double COLOR_BLUE = 0.67;
    public static final double COLOR_PURPLE = 0.83;
    public static final double COLOR_WHITE = 1.00;

    private Servo ledExpansion;
    private Servo ledControl;

    private double currentPosition = COLOR_OFF();

    public static double COLOR_OFF() {
        return COLOR_WHITE;
    }

    /**
     * Initializes the LED servo outputs from the HardwareMap.
     *
     * @param hardwareMap HardwareMap reference from the OpMode
     */
    public void init(HardwareMap hardwareMap) {
        // Expansion Hub Servo Port 2
        ledExpansion = hardwareMap.get(Servo.class, "led_expansion");
        // Control Hub Servo Port 3
        ledControl = hardwareMap.get(Servo.class, "led_control");

        // Set initial color (Yellow for Init Started)
        setColor(COLOR_YELLOW);
    }

    /**
     * Sets the color position for both LED lights.
     *
     * @param colorPosition Servo position (0.00 to 1.00) corresponding to desired color
     */
    public void setColor(double colorPosition) {
        currentPosition = colorPosition;
        if (ledExpansion != null) {
            ledExpansion.setPosition(colorPosition);
        }
        if (ledControl != null) {
            ledControl.setPosition(colorPosition);
        }
    }

    public double getCurrentPosition() {
        return currentPosition;
    }
}
