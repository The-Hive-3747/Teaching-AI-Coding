package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Autonomous OpMode that shoots immediately (0s delay), then drives away from the wall.
 */
@Autonomous(name = "Auto: Shoot First", group = "Autonomous")
public class AutoShootFirst extends BaseAuto {

    @Override
    protected double getInitialDelaySeconds() {
        return 0.0; // 0 seconds delay -> Shoot immediately
    }
}
