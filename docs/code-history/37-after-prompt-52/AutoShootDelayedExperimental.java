package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Experimental Autonomous OpMode that waits 15.0 seconds before shooting initial 3 balls,
 * drops the 4th ball from top of intake by driving backward, intakes the 4th ball,
 * shoots it, turns left, and drives forward to park.
 */
@Autonomous(name = "Auto: Shoot Delayed 15s (Experimental 4-Ball)", group = "Autonomous")
public class AutoShootDelayedExperimental extends BaseAutoExperimental {

    @Override
    protected double getInitialDelaySeconds() {
        return 15.0; // 15.0 seconds delay before shooting sequence
    }
}
