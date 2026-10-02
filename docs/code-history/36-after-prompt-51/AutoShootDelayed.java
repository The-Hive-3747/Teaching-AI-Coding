package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Autonomous OpMode that waits 1.0 second (testing delay) before shooting, then turns left and drives forward to park.
 * Note: Change delay back to 15.0s after testing.
 */
@Autonomous(name = "Auto: Shoot Delayed (1s Test)", group = "Autonomous")
public class AutoShootDelayed extends BaseAuto {

    @Override
    protected double getInitialDelaySeconds() {
        return 1.0; // 1.0 second testing delay before shooting (change back to 15.0s later)
    }
}
