package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Autonomous OpMode that waits 15 seconds before shooting, then drives away from the wall.
 */
@Autonomous(name = "Auto: Shoot Delayed (15s)", group = "Autonomous")
public class AutoShootDelayed extends BaseAuto {

    @Override
    protected double getInitialDelaySeconds() {
        return 15.0; // 15 seconds delay before shooting
    }
}
