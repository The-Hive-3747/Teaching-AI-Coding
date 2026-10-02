package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Experimental Autonomous OpMode that waits 1.0 second (testing delay) before shooting initial 3 balls,
 * drops the 4th ball from top of intake by driving backward, intakes the 4th ball,
 * shoots it, turns left, and drives forward to park.
 * Note: Change delay back to 15.0s after testing.
 */
@Autonomous(name = "Auto: Shoot Delayed 1s Test (Experimental 4-Ball)", group = "Autonomous")
public class AutoShootDelayedExperimental extends BaseAutoExperimental {

    @Override
    protected double getInitialDelaySeconds() {
        return 1.0; // 1.0 second testing delay before shooting sequence (change back to 15.0s later)
    }
}
