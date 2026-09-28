package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Autonomous OpMode that waits 15.0 seconds before shooting, then turns left and drives forward to park.
 */
@Autonomous(name = "Auto: Shoot Delayed (15s)", group = "Autonomous")
public class AutoShootDelayed extends BaseAuto {

    @Override
    protected double getInitialDelaySeconds() {
        return 15.0; // 15.0 seconds delay before shooting
    }
}
