package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Base Experimental Autonomous OpMode for the 4-ball trick:
 * 1. Drives backward 6 inches (-0.3 power for 1.0s) with Intake ON to drop 4th ball in front & set 6-inch shooting distance.
 * 2. Spools flywheel directly forward and shoots first 3 balls.
 * 3. Drives forward into wall (+0.3 power for 1.2s) with Intake ON to scoop 4th ball.
 * 4. Drives backward 6 inches (-0.3 power for 1.0s) to reset 6-inch shooting distance.
 * 5. Spools flywheel directly forward and shoots 4th ball.
 * 6. Strafes Right for 3 seconds at 0.3 power to park in box near wall (Delayed Auto only).
 */
public abstract class BaseAutoExperimental extends OpMode {

    public enum AutoExpState {
        DELAY,
        STATE_1_BACK_UP_DROP_4TH_BALL,
        STATE_2_START_FLYWHEEL_FIRST_3,
        STATE_3_PULSE_SHOOT_FIRST_3,
        STATE_4_STOP_FIRST_SHOOT,
        STATE_5_DRIVE_FORWARD_INTAKE_4TH_BALL,
        STATE_6_BACK_UP_FOR_4TH_SHOOT,
        STATE_7_START_FLYWHEEL_4TH_BALL,
        STATE_8_PULSE_SHOOT_4TH_BALL,
        STATE_9_STOP_SECOND_SHOOT,
        STATE_10_PARK_STRAFE_RIGHT,
        STATE_11_ALL_STOP
    }

    // Hardware
    protected DcMotor frontLeftDrive = null;
    protected DcMotor frontRightDrive = null;
    protected DcMotor backLeftDrive = null;
    protected DcMotor backRightDrive = null;

    protected Flywheel flywheel;
    protected Intake intake;
    protected LedController ledController;

    // State Machine Variables
    protected AutoExpState currentState = AutoExpState.DELAY;
    protected ElapsedTime stateTimer;
    protected ElapsedTime totalAutoTimer;

    // Simulated Gamepad for programmatic subsystem driving
    protected Gamepad autoGamepad;

    // Working variables declared as class fields to prevent re-allocation inside loop
    protected double frontLeftPower = 0.0;
    protected double backLeftPower = 0.0;
    protected double frontRightPower = 0.0;
    protected double backRightPower = 0.0;
    protected double stateTime = 0.0;

    /**
     * Subclasses override this to specify initial delay in seconds (0s or 15s).
     *
     * @return Initial delay time in seconds
     */
    protected abstract double getInitialDelaySeconds();

    /**
     * Shooting duration for first 3 balls (4.5s for delayed, 5.0s for shoot first).
     *
     * @return Duration in seconds
     */
    protected double getFirstShootDurationSeconds() {
        return (getInitialDelaySeconds() > 0) ? 4.5 : 5.0;
    }

    /**
     * Only Delayed Autonomous OpModes strafe right to park in the box near the wall.
     *
     * @return True if this Auto should strafe right to park
     */
    protected boolean shouldParkStrafeRight() {
        return getInitialDelaySeconds() > 0.0;
    }

    @Override
    public void init() {
        // Initialize LED controller (Yellow for "Init Started")
        ledController = new LedController();
        ledController.init(hardwareMap);

        // Initialize drive motors
        frontLeftDrive = hardwareMap.get(DcMotor.class, "front_left_drive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "front_right_drive");
        backLeftDrive = hardwareMap.get(DcMotor.class, "back_left_drive");
        backRightDrive = hardwareMap.get(DcMotor.class, "back_right_drive");

        // Set motor directions (Left side reversed for forward driving)
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);

        // Set zero power behavior for precise stopping
        frontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Initialize subsystems
        flywheel = new Flywheel();
        flywheel.init(hardwareMap);

        intake = new Intake();
        intake.init(hardwareMap);

        // Initialize timers & simulated gamepad
        stateTimer = new ElapsedTime();
        totalAutoTimer = new ElapsedTime();
        autoGamepad = new Gamepad();

        // Signal "Init Completed" with Green LEDs
        ledController.setColor(LedController.COLOR_GREEN);
        telemetry.addData("Status", "Init Completed (Ready for Start)");
    }

    @Override
    public void start() {
        totalAutoTimer.reset();
        stateTimer.reset();
        currentState = AutoExpState.DELAY;

        // Ensure subsystems are stopped
        flywheel.stop();
        intake.stop();

        // Set LEDs to Blue for Auto active
        ledController.setColor(LedController.COLOR_BLUE);
    }

    @Override
    public void loop() {
        stateTime = stateTimer.seconds();

        // Zero out simulated gamepad inputs by default each cycle
        autoGamepad.a = false;
        autoGamepad.b = false;
        autoGamepad.right_bumper = false;
        autoGamepad.left_bumper = false;
        autoGamepad.x = false;

        frontLeftPower = 0.0;
        backLeftPower = 0.0;
        frontRightPower = 0.0;
        backRightPower = 0.0;

        // --- State Machine Transition & Execution Logic ---
        switch (currentState) {
            case DELAY:
                // Waiting initial delay (0s for ShootFirst, 15s for ShootDelayed)
                if (stateTime >= getInitialDelaySeconds()) {
                    currentState = AutoExpState.STATE_1_BACK_UP_DROP_4TH_BALL;
                    stateTimer.reset();
                } else {
                    ledController.setColor(LedController.COLOR_YELLOW);
                }
                break;

            case STATE_1_BACK_UP_DROP_4TH_BALL:
                // State 1: Drive backward (-0.3 power) for 1.0s to move 6 inches from wall
                // and turn Intake ON (A=true) so 4th ball on top tumbles off in front of robot
                frontLeftPower = -0.3;
                backLeftPower = -0.3;
                frontRightPower = -0.3;
                backRightPower = -0.3;
                autoGamepad.a = true; // Intake ON to help tumble 4th ball down

                if (stateTime >= 1.0) {
                    currentState = AutoExpState.STATE_2_START_FLYWHEEL_FIRST_3;
                    stateTimer.reset();
                    // Spool flywheel directly forward for preloaded balls without running intake reverse bump
                    flywheel.startDirect();
                }
                break;

            case STATE_2_START_FLYWHEEL_FIRST_3:
                // State 2: Spool flywheel directly forward for first 3 balls (1.0 sec)
                flywheel.startDirect();

                if (stateTime >= 1.0) {
                    currentState = AutoExpState.STATE_3_PULSE_SHOOT_FIRST_3;
                    stateTimer.reset();
                }
                break;

            case STATE_3_PULSE_SHOOT_FIRST_3:
                // State 3: Shoot first 3 balls from 6 inches away by pulsing intake (4.5s or 5.0s)
                autoGamepad.right_bumper = true;

                if (stateTime >= getFirstShootDurationSeconds()) {
                    currentState = AutoExpState.STATE_4_STOP_FIRST_SHOOT;
                    stateTimer.reset();
                }
                break;

            case STATE_4_STOP_FIRST_SHOOT:
                // State 4: Stop first shoot
                flywheel.stop();
                intake.stop();

                currentState = AutoExpState.STATE_5_DRIVE_FORWARD_INTAKE_4TH_BALL;
                stateTimer.reset();
                break;

            case STATE_5_DRIVE_FORWARD_INTAKE_4TH_BALL:
                // State 5: Drive forward (+0.3 power) for 1.2s with Intake ON to scoop 4th ball & gently touch wall
                frontLeftPower = 0.3;
                backLeftPower = 0.3;
                frontRightPower = 0.3;
                backRightPower = 0.3;
                autoGamepad.a = true; // Keep Intake ON collecting

                if (stateTime >= 1.2) {
                    currentState = AutoExpState.STATE_6_BACK_UP_FOR_4TH_SHOOT;
                    stateTimer.reset();
                }
                break;

            case STATE_6_BACK_UP_FOR_4TH_SHOOT:
                // State 6: Drive backward (-0.3 power) for 1.0s to move 6 inches away from wall again
                frontLeftPower = -0.3;
                backLeftPower = -0.3;
                frontRightPower = -0.3;
                backRightPower = -0.3;

                if (stateTime >= 1.0) {
                    currentState = AutoExpState.STATE_7_START_FLYWHEEL_4TH_BALL;
                    stateTimer.reset();
                    // Spool flywheel directly forward for 4th ball
                    flywheel.startDirect();
                }
                break;

            case STATE_7_START_FLYWHEEL_4TH_BALL:
                // State 7: Spool flywheel directly forward for 4th ball (0.8 sec)
                flywheel.startDirect();

                if (stateTime >= 0.8) {
                    currentState = AutoExpState.STATE_8_PULSE_SHOOT_4TH_BALL;
                    stateTimer.reset();
                }
                break;

            case STATE_8_PULSE_SHOOT_4TH_BALL:
                // State 8: Shoot 4th ball from 6 inches away by pulsing intake (2.5 sec)
                autoGamepad.right_bumper = true;

                if (stateTime >= 2.5) {
                    currentState = AutoExpState.STATE_9_STOP_SECOND_SHOOT;
                    stateTimer.reset();
                }
                break;

            case STATE_9_STOP_SECOND_SHOOT:
                // State 9: Stop second shoot
                flywheel.stop();
                intake.stop();

                if (shouldParkStrafeRight()) {
                    // Delayed Auto strafes right to park in box
                    currentState = AutoExpState.STATE_10_PARK_STRAFE_RIGHT;
                } else {
                    // Normal Auto stops in place
                    currentState = AutoExpState.STATE_11_ALL_STOP;
                }
                stateTimer.reset();
                break;

            case STATE_10_PARK_STRAFE_RIGHT:
                // State 10: Strafe Right for 3 seconds at 0.3 power to park in box near wall (Delayed Auto only)
                frontLeftPower = 0.3 * 0.8;
                backLeftPower = -0.3 * 0.8;
                frontRightPower = -0.3 * 0.8;
                backRightPower = 0.3 * 0.8;

                if (stateTime >= 3.0) {
                    currentState = AutoExpState.STATE_11_ALL_STOP;
                    stateTimer.reset();
                }
                break;

            case STATE_11_ALL_STOP:
                // State 11: All stop -> End State Machine
                frontLeftPower = 0.0;
                backLeftPower = 0.0;
                frontRightPower = 0.0;
                backRightPower = 0.0;

                flywheel.stop();
                intake.stop();

                ledController.setColor(LedController.COLOR_GREEN);
                break;
        }

        // Apply drive motor powers
        frontLeftDrive.setPower(frontLeftPower);
        frontRightDrive.setPower(frontRightPower);
        backLeftDrive.setPower(backLeftPower);
        backRightDrive.setPower(backRightPower);

        // Update Flywheel & Intake subsystems
        flywheel.update(autoGamepad, false);
        intake.update(autoGamepad, false, flywheel.isReversing(), flywheel.isPausing(), flywheel.isOn());

        // Update LEDs if not in DELAY or ALL_STOP overrides
        if (currentState != AutoExpState.DELAY && currentState != AutoExpState.STATE_11_ALL_STOP) {
            if (intake.isFeeding()) {
                ledController.setColor(LedController.COLOR_WHITE);
            } else if (flywheel.isReversing()) {
                ledController.setColor(LedController.COLOR_RED);
            } else if (flywheel.isPausing()) {
                ledController.setColor(LedController.COLOR_ORANGE);
            } else if (flywheel.isOn()) {
                ledController.setColor(LedController.COLOR_PURPLE);
            } else {
                ledController.setColor(LedController.COLOR_BLUE);
            }
        }

        // Telemetry Output
        telemetry.addData("Auto Exp State", currentState.toString());
        telemetry.addData("State Time", "%.1f sec", stateTime);
        telemetry.addData("Total Auto Time", "%.1f sec", totalAutoTimer.seconds());
        telemetry.addData("Flywheel Phase", flywheel.getPhase().toString());
        telemetry.addData("Intake Power", "%.2f", intake.getCurrentPower());
        telemetry.update();
    }
}
