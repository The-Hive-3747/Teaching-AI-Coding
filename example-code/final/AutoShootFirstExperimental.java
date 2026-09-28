package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Experimental Autonomous OpMode that shoots initial 3 balls immediately (0s delay),
 * drops the 4th ball from top of intake by driving backward, intakes the 4th ball,
 * shoots it, and parks away from the wall.
 */
@Autonomous(name = "Auto: Shoot First (Experimental 4-Ball)", group = "Autonomous")
public class AutoShootFirstExperimental extends BaseAutoExperimental {

    @Override
    protected double getInitialDelaySeconds() {
        return 0.0; // 0 seconds delay -> Shoot immediately
    }
}
