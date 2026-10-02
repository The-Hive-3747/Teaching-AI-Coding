package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Base Experimental Autonomous OpMode for the 4-ball trick:
 * 1. Shoots initial 3 balls.
 * 2. Drives backward with intake ON to drop 4th ball resting on top of intake.
 * 3. Drives forward with intake ON to scoop 4th ball and gently touch wall.
 * 4. Spools flywheel and shoots 4th ball.
 * 5. Drives backward away from wall for movement points.
 */
public abstract class BaseAutoExperimental extends OpMode {

    public enum AutoExpState {
        DELAY,
        STATE_1_START_FLYWHEEL_FIRST_3,
        STATE_2_PULSE_SHOOT_FIRST_3,
        STATE_3_STOP_FIRST_SHOOT,
        STATE_4_DRIVE_BACK_DROP_4TH_BALL,
        STATE_5_DRIVE_FORWARD_INTAKE_4TH_BALL,
        STATE_6_START_FLYWHEEL_4TH_BALL,
        STATE_7_PULSE_SHOOT_4TH_BALL,
        STATE_8_STOP_SECOND_SHOOT,
        STATE_9_DRIVE_BACK_PARK,
        STATE_10_ALL_STOP
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
                    currentState = AutoExpState.STATE_1_START_FLYWHEEL_FIRST_3;
                    stateTimer.reset();
                    autoGamepad.b = true; // Trigger flywheel startup sequence
                } else {
                    ledController.setColor(LedController.COLOR_YELLOW);
                }
                break;

            case STATE_1_START_FLYWHEEL_FIRST_3:
                // State 1: Flywheel starts moving for first 3 balls (1.0 sec)
                if (stateTime >= 1.0) {
                    currentState = AutoExpState.STATE_2_PULSE_SHOOT_FIRST_3;
                    stateTimer.reset();
                }
                break;

            case STATE_2_PULSE_SHOOT_FIRST_3:
                // State 2: Shoot first 3 balls by pulsing intake (5.0 sec)
                autoGamepad.right_bumper = true;

                if (stateTime >= 5.0) {
                    currentState = AutoExpState.STATE_3_STOP_FIRST_SHOOT;
                    stateTimer.reset();
                }
                break;

            case STATE_3_STOP_FIRST_SHOOT:
                // State 3: Stop shooting and turn off intake & flywheel
                flywheel.stop();
                intake.stop();

                currentState = AutoExpState.STATE_4_DRIVE_BACK_DROP_4TH_BALL;
                stateTimer.reset();
                break;

            case STATE_4_DRIVE_BACK_DROP_4TH_BALL:
                // State 4: Drive backward (-0.4 power) with Intake ON to drop 4th ball (1.0 sec)
                frontLeftPower = -0.4;
                backLeftPower = -0.4;
                frontRightPower = -0.4;
                backRightPower = -0.4;
                autoGamepad.a = true; // Turn ON Intake Collect to help drop ball in front

                if (stateTime >= 1.0) {
                    currentState = AutoExpState.STATE_5_DRIVE_FORWARD_INTAKE_4TH_BALL;
                    stateTimer.reset();
                }
                break;

            case STATE_5_DRIVE_FORWARD_INTAKE_4TH_BALL:
                // State 5: Drive forward (+0.3 power) with Intake ON to scoop 4th ball & touch wall (1.2 sec)
                frontLeftPower = 0.3;
                backLeftPower = 0.3;
                frontRightPower = 0.3;
                backRightPower = 0.3;
                autoGamepad.a = true; // Keep Intake ON collecting

                if (stateTime >= 1.2) {
                    currentState = AutoExpState.STATE_6_START_FLYWHEEL_4TH_BALL;
                    stateTimer.reset();
                    autoGamepad.b = true; // Trigger flywheel startup sequence for 4th ball
                }
                break;

            case STATE_6_START_FLYWHEEL_4TH_BALL:
                // State 6: Spool flywheel for 4th ball (0.8 sec)
                if (stateTime >= 0.8) {
                    currentState = AutoExpState.STATE_7_PULSE_SHOOT_4TH_BALL;
                    stateTimer.reset();
                }
                break;

            case STATE_7_PULSE_SHOOT_4TH_BALL:
                // State 7: Shoot 4th ball by pulsing intake (2.5 sec)
                autoGamepad.right_bumper = true;

                if (stateTime >= 2.5) {
                    currentState = AutoExpState.STATE_8_STOP_SECOND_SHOOT;
                    stateTimer.reset();
                }
                break;

            case STATE_8_STOP_SECOND_SHOOT:
                // State 8: Stop second shoot
                flywheel.stop();
                intake.stop();

                currentState = AutoExpState.STATE_9_DRIVE_BACK_PARK;
                stateTimer.reset();
                break;

            case STATE_9_DRIVE_BACK_PARK:
                // State 9: Drive backward (-0.4 power) away from wall for movement points (1.0 sec)
                frontLeftPower = -0.4;
                backLeftPower = -0.4;
                frontRightPower = -0.4;
                backRightPower = -0.4;

                if (stateTime >= 1.0) {
                    currentState = AutoExpState.STATE_10_ALL_STOP;
                    stateTimer.reset();
                }
                break;

            case STATE_10_ALL_STOP:
                // State 10: All stop -> End State Machine
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
        if (currentState != AutoExpState.DELAY && currentState != AutoExpState.STATE_10_ALL_STOP) {
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
