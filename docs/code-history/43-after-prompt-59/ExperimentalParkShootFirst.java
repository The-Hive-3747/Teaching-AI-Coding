package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Autonomous OpMode: Experimental Park Shoot First.
 * Sequence:
 * 1. Drives backward (-0.3 power for 300 ms) to set 6-inch shooting distance.
 * 2. Pauses 1.0s for inertia to dissipate.
 * 3. Spools flywheel directly forward (2.0s).
 * 4. Shoots preloaded balls by pulsing intake (100ms ON / 200ms OFF for 10.0s).
 * 5. Turns 45 degrees Left (0.275s at 0.5 power).
 * 6. Drives backward for 1.5 seconds at -0.4 power.
 * 7. Turns 45 degrees Right to correct heading (0.275s at 0.5 power).
 * 8. Drives forward for 3.0 seconds at 0.4 power into parking box.
 * 9. Backs off wall for 100 ms at -0.5 power to earn non-contact parking points.
 */
@Autonomous(name = "Experimental Park Shoot First", group = "Autonomous")
public class ExperimentalParkShootFirst extends OpMode {

    public enum ExpParkState {
        STATE_1_BACK_UP,
        STATE_1B_SETTLE,
        STATE_2_START_FLYWHEEL,
        STATE_3_PULSE_SHOOT,
        STATE_4_STOP_SHOOT,
        STATE_5_TURN_45_LEFT,
        STATE_6_DRIVE_BACKWARD_1500MS,
        STATE_7_TURN_45_RIGHT_CORRECT,
        STATE_8_DRIVE_FORWARD_3000MS,
        STATE_9_BACK_OFF_WALL,
        STATE_10_ALL_STOP
    }

    // Hardware
    private DcMotor frontLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor backRightDrive = null;

    private Flywheel flywheel;
    private Intake intake;
    private LedController ledController;

    // State Machine Variables
    private ExpParkState currentState = ExpParkState.STATE_1_BACK_UP;
    private ElapsedTime stateTimer;
    private ElapsedTime totalAutoTimer;

    // Simulated Gamepad for programmatic subsystem driving
    private Gamepad autoGamepad;

    // Working variables declared as class fields to prevent re-allocation inside loop
    private double frontLeftPower = 0.0;
    private double backLeftPower = 0.0;
    private double frontRightPower = 0.0;
    private double backRightPower = 0.0;
    private double stateTime = 0.0;

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
        currentState = ExpParkState.STATE_1_BACK_UP;

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

        // --- State Machine Execution ---
        switch (currentState) {
            case STATE_1_BACK_UP:
                // State 1: Drive backward (-0.3 power) for 0.30 sec (300 ms)
                frontLeftPower = -0.3;
                backLeftPower = -0.3;
                frontRightPower = -0.3;
                backRightPower = -0.3;

                if (stateTime >= 0.30) {
                    currentState = ExpParkState.STATE_1B_SETTLE;
                    stateTimer.reset();
                }
                break;

            case STATE_1B_SETTLE:
                // State 1B: Pause 1.0 second with drive motors stopped (0.0 power) to dissipate robot inertia
                frontLeftPower = 0.0;
                backLeftPower = 0.0;
                frontRightPower = 0.0;
                backRightPower = 0.0;

                if (stateTime >= 1.000) {
                    currentState = ExpParkState.STATE_2_START_FLYWHEEL;
                    stateTimer.reset();
                    flywheel.startDirect();
                }
                break;

            case STATE_2_START_FLYWHEEL:
                // State 2: Flywheel spools directly forward to 1.0 power (2.0 sec)
                flywheel.startDirect();

                if (stateTime >= 2.0) {
                    currentState = ExpParkState.STATE_3_PULSE_SHOOT;
                    stateTimer.reset();
                }
                break;

            case STATE_3_PULSE_SHOOT:
                // State 3: Shoot preloaded balls by pulsing intake (100ms ON / 200ms OFF for 10.0s)
                autoGamepad.right_bumper = true;

                if (stateTime >= 10.0) {
                    currentState = ExpParkState.STATE_4_STOP_SHOOT;
                    stateTimer.reset();
                }
                break;

            case STATE_4_STOP_SHOOT:
                // State 4: Stop flywheel & intake
                flywheel.stop();
                intake.stop();

                currentState = ExpParkState.STATE_5_TURN_45_LEFT;
                stateTimer.reset();
                break;

            case STATE_5_TURN_45_LEFT:
                // State 5: Turn 45 degrees Left in place at 0.5 power for 0.275s (275 ms)
                frontLeftPower = -0.5 * 0.8;
                backLeftPower = -0.5 * 0.8;
                frontRightPower = 0.5 * 0.8;
                backRightPower = 0.5 * 0.8;

                if (stateTime >= 0.275) {
                    currentState = ExpParkState.STATE_6_DRIVE_BACKWARD_1500MS;
                    stateTimer.reset();
                }
                break;

            case STATE_6_DRIVE_BACKWARD_1500MS:
                // State 6: Drive backward for 1.5 seconds at -0.4 power
                frontLeftPower = -0.4 * 0.8;
                backLeftPower = -0.4 * 0.8;
                frontRightPower = -0.4 * 0.8;
                backRightPower = -0.4 * 0.8;

                if (stateTime >= 1.5) {
                    currentState = ExpParkState.STATE_7_TURN_45_RIGHT_CORRECT;
                    stateTimer.reset();
                }
                break;

            case STATE_7_TURN_45_RIGHT_CORRECT:
                // State 7: Turn 45 degrees Right to correct heading in place at 0.5 power for 0.275s (275 ms)
                frontLeftPower = 0.5 * 0.8;
                backLeftPower = 0.5 * 0.8;
                frontRightPower = -0.5 * 0.8;
                backRightPower = -0.5 * 0.8;

                if (stateTime >= 0.275) {
                    currentState = ExpParkState.STATE_8_DRIVE_FORWARD_3000MS;
                    stateTimer.reset();
                }
                break;

            case STATE_8_DRIVE_FORWARD_3000MS:
                // State 8: Drive forward for 3.0 seconds at 0.4 power into parking box
                frontLeftPower = 0.4 * 0.8;
                backLeftPower = 0.4 * 0.8;
                frontRightPower = 0.4 * 0.8;
                backRightPower = 0.4 * 0.8;

                if (stateTime >= 3.0) {
                    currentState = ExpParkState.STATE_9_BACK_OFF_WALL;
                    stateTimer.reset();
                }
                break;

            case STATE_9_BACK_OFF_WALL:
                // State 9: Drive backward at -0.5 power for 100 ms (0.100s) to back off wall and earn non-contact points
                frontLeftPower = -0.5;
                backLeftPower = -0.5;
                frontRightPower = -0.5;
                backRightPower = -0.5;

                if (stateTime >= 0.100) {
                    currentState = ExpParkState.STATE_10_ALL_STOP;
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

        // Update LEDs if not in ALL_STOP
        if (currentState != ExpParkState.STATE_10_ALL_STOP) {
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
        telemetry.addData("State Time", "%.2f sec", stateTime);
        telemetry.addData("Total Auto Time", "%.1f sec", totalAutoTimer.seconds());
        telemetry.addData("Flywheel Phase", flywheel.getPhase().toString());
        telemetry.addData("Intake Power", "%.2f", intake.getCurrentPower());
        telemetry.update();
    }
}
