package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Base Autonomous OpMode containing shared state machine logic for
 * backing up 6 inches, shooting, and strafing right for 3 seconds to park in the box near the wall.
 */
public abstract class BaseAuto extends OpMode {

    public enum AutoState {
        DELAY,
        STATE_1_BACK_UP_6_INCHES,
        STATE_2_START_FLYWHEEL,
        STATE_3_PULSE_SHOOTING,
        STATE_4_STOP_SHOOTING,
        STATE_5_PARK_STRAFE_RIGHT,
        STATE_6_ALL_STOP
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
                    currentState = AutoState.STATE_1_BACK_UP_6_INCHES;
                    stateTimer.reset();
                } else {
                    ledController.setColor(LedController.COLOR_YELLOW);
                }
                break;

            case STATE_1_BACK_UP_6_INCHES:
                // State 1: Drive backward (-0.3 power) for 1.0 sec to back up 6 inches from wall
                frontLeftPower = -0.3;
                backLeftPower = -0.3;
                frontRightPower = -0.3;
                backRightPower = -0.3;

                if (stateTime >= 1.0) {
                    currentState = AutoState.STATE_2_START_FLYWHEEL;
                    stateTimer.reset();
                    autoGamepad.b = true; // Trigger flywheel startup sequence
                }
                break;

            case STATE_2_START_FLYWHEEL:
                // State 2: Flywheel starts moving (reverse bump -> pause -> forward spool)
                if (stateTime >= 1.0) {
                    currentState = AutoState.STATE_3_PULSE_SHOOTING;
                    stateTimer.reset();
                }
                break;

            case STATE_3_PULSE_SHOOTING:
                // State 3: Shoot from 6 inches away by pulsing intake (100ms ON / 200ms OFF) for 10 seconds
                autoGamepad.right_bumper = true;

                if (stateTime >= 10.0) {
                    currentState = AutoState.STATE_4_STOP_SHOOTING;
                    stateTimer.reset();
                }
                break;

            case STATE_4_STOP_SHOOTING:
                // State 4: Stop shooting and turn off intake and flywheel
                flywheel.stop();
                intake.stop();

                currentState = AutoState.STATE_5_PARK_STRAFE_RIGHT;
                stateTimer.reset();
                break;

            case STATE_5_PARK_STRAFE_RIGHT:
                // State 5: Strafe Right for 3 seconds at 0.3 power to park in box near wall
                frontLeftPower = 0.3 * 0.8;
                backLeftPower = -0.3 * 0.8;
                frontRightPower = -0.3 * 0.8;
                backRightPower = 0.3 * 0.8;

                if (stateTime >= 3.0) {
                    currentState = AutoState.STATE_6_ALL_STOP;
                    stateTimer.reset();
                }
                break;

            case STATE_6_ALL_STOP:
                // State 6: All stop -> End State Machine (Parked in box)
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
        if (currentState != AutoState.DELAY && currentState != AutoState.STATE_6_ALL_STOP) {
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
        telemetry.addData("State Time", "%.1f sec", stateTime);
        telemetry.addData("Total Auto Time", "%.1f sec", totalAutoTimer.seconds());
        telemetry.addData("Flywheel Phase", flywheel.getPhase().toString());
        telemetry.addData("Intake Power", "%.2f", intake.getCurrentPower());
        telemetry.update();
    }
}
