package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Base Autonomous OpMode containing shared state machine logic for
 * shooting and driving away from the target wall.
 */
public abstract class BaseAuto extends OpMode {

    public enum AutoState {
        DELAY,
        STATE_1_START_FLYWHEEL,
        STATE_2_PULSE_SHOOTING,
        STATE_3_STOP_SHOOTING,
        STATE_4_DRIVE_BACKWARDS,
        STATE_5_ALL_STOP
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
    protected AutoState currentState = AutoState.DELAY;
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
        currentState = AutoState.DELAY;

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
                    // Transition to State 1: Start Flywheel
                    currentState = AutoState.STATE_1_START_FLYWHEEL;
                    stateTimer.reset();
                    // Trigger flywheel start sequence (Simulate pressing B button)
                    autoGamepad.b = true;
                } else {
                    // Yellow LEDs during delay
                    ledController.setColor(LedController.COLOR_YELLOW);
                }
                break;

            case STATE_1_START_FLYWHEEL:
                // State 1: Flywheel starts moving (reverse bump -> pause -> forward spool)
                if (stateTime >= 1.0) {
                    // Exit to State 2 after 1 second passes
                    currentState = AutoState.STATE_2_PULSE_SHOOTING;
                    stateTimer.reset();
                }
                break;

            case STATE_2_PULSE_SHOOTING:
                // State 2: Start shooting by pulsing intake (Right Bumper held)
                autoGamepad.right_bumper = true;

                if (stateTime >= 10.0) {
                    // Exit to State 3 after 10 seconds passing
                    currentState = AutoState.STATE_3_STOP_SHOOTING;
                    stateTimer.reset();
                }
                break;

            case STATE_3_STOP_SHOOTING:
                // State 3: Stop shooting and turn off intake and flywheel
                flywheel.stop();
                intake.stop();

                // Directly transition to State 4
                currentState = AutoState.STATE_4_DRIVE_BACKWARDS;
                stateTimer.reset();
                break;

            case STATE_4_DRIVE_BACKWARDS:
                // State 4: Drive backwards away from wall slowly at 0.4 power
                frontLeftPower = -0.4;
                backLeftPower = -0.4;
                frontRightPower = -0.4;
                backRightPower = -0.4;

                if (stateTime >= 1.0) {
                    // Exit to State 5 after 1 second passes
                    currentState = AutoState.STATE_5_ALL_STOP;
                    stateTimer.reset();
                }
                break;

            case STATE_5_ALL_STOP:
                // State 5: All stop -> End State Machine
                frontLeftPower = 0.0;
                backLeftPower = 0.0;
                frontRightPower = 0.0;
                backRightPower = 0.0;

                flywheel.stop();
                intake.stop();

                // Signal Auto Complete with Green LEDs
                ledController.setColor(LedController.COLOR_GREEN);
                break;
        }

        // Apply drive motor powers
        frontLeftDrive.setPower(frontLeftPower);
        frontRightDrive.setPower(frontRightPower);
        backLeftDrive.setPower(backLeftPower);
        backRightDrive.setPower(backRightPower);

        // Update Flywheel & Intake subsystems with simulated gamepad
        flywheel.update(autoGamepad, false);
        intake.update(autoGamepad, false, flywheel.isReversing(), flywheel.isPausing(), flywheel.isOn());

        // Update LEDs if not in DELAY or ALL_STOP overrides
        if (currentState != AutoState.DELAY && currentState != AutoState.STATE_5_ALL_STOP) {
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
        telemetry.addData("Auto State", currentState.toString());
        telemetry.addData("State Time", "%.1f / %.1f sec", stateTime, getTargetStateDuration());
        telemetry.addData("Total Auto Time", "%.1f sec", totalAutoTimer.seconds());
        telemetry.addData("Flywheel Phase", flywheel.getPhase().toString());
        telemetry.addData("Intake Power", "%.2f", intake.getCurrentPower());
        telemetry.update();
    }

    private double getTargetStateDuration() {
        switch (currentState) {
            case DELAY: return getInitialDelaySeconds();
            case STATE_1_START_FLYWHEEL: return 1.0;
            case STATE_2_PULSE_SHOOTING: return 10.0;
            case STATE_3_STOP_SHOOTING: return 0.0;
            case STATE_4_DRIVE_BACKWARDS: return 1.0;
            case STATE_5_ALL_STOP: return 0.0;
            default: return 0.0;
        }
    }
}
