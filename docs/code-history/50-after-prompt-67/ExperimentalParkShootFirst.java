package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Autonomous OpMode: Experimental Park Shoot First (4-Ball Trick + Backward Park).
 * Sequence:
 * 1. Drives backward (-0.3 power for 300 ms) with Intake OFF to drop 4th ball in front.
 * 2. Settles for 1.0 second.
 * 3. Spools flywheel directly forward (2.0s).
 * 4. Shoots first 3 balls by pulsing intake (2.6s).
 * 5. Stops flywheel completely with BRAKE mode and pauses 250 ms.
 * 6. Drives forward (+0.3 power for 300 ms) with Intake ON to scoop 4th ball.
 * 7. Single combined move: Drives backward (-0.3 power) AND reverses intake (-0.5) for 300 ms (100ms intake bump) to back 4th ball away from flywheel.
 * 8. Stops intake, settles for 1.0 second.
 * 9. Spools flywheel directly forward for 2.0s.
 * 10. Shoots 4th ball by pulsing intake (2.0s).
 * 11. Turns 45 degrees Left (0.275s at 0.5 power).
 * 12. Drives backward for 1.75 seconds at -0.4 power.
 * 13. Turns 45 degrees Right to correct heading (0.275s at 0.5 power).
 * 14. Drives BACKWARD for 2.3 seconds at -0.4 power into parking box.
 * 15. All stop (parked).
 */
@Autonomous(name = "Experimental Park Shoot First", group = "Autonomous")
public class ExperimentalParkShootFirst extends OpMode {

    public enum ExpParkState {
        STATE_1_BACK_UP_DROP_4TH_BALL,
        STATE_1B_SETTLE_1,
        STATE_2_START_FLYWHEEL_FIRST_3,
        STATE_3_PULSE_SHOOT_FIRST_3,
        STATE_4_STOP_FIRST_SHOOT_BRAKE_250MS,
        STATE_5_DRIVE_FORWARD_INTAKE_4TH_BALL,
        STATE_6_BACK_UP_AND_REVERSE_BUMP_4TH_BALL,
        STATE_6B_SETTLE_2,
        STATE_7_START_FLYWHEEL_4TH_BALL,
        STATE_8_PULSE_SHOOT_4TH_BALL,
        STATE_9_STOP_SECOND_SHOOT,
        STATE_10_TURN_45_LEFT,
        STATE_11_DRIVE_BACKWARD_1750MS,
        STATE_12_TURN_45_RIGHT_CORRECT,
        STATE_13_DRIVE_BACKWARD_2300MS,
        STATE_14_ALL_STOP
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
    private ExpParkState currentState = ExpParkState.STATE_1_BACK_UP_DROP_4TH_BALL;
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
        currentState = ExpParkState.STATE_1_BACK_UP_DROP_4TH_BALL;

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
            case STATE_1_BACK_UP_DROP_4TH_BALL:
                // State 1: Drive backward (-0.3 power) for 0.30s (300 ms) to move 6 inches from wall
                // Keep Intake OFF (0.0 power) so preloaded balls stay inside while 4th ball tumbles off top
                frontLeftPower = -0.3;
                backLeftPower = -0.3;
                frontRightPower = -0.3;
                backRightPower = -0.3;

                if (stateTime >= 0.30) {
                    currentState = ExpParkState.STATE_1B_SETTLE_1;
                    stateTimer.reset();
                }
                break;

            case STATE_1B_SETTLE_1:
                // State 1B: Pause 1.0 second with drive motors & intake stopped (0.0 power) to let inertia dissipate
                intake.stop();
                frontLeftPower = 0.0;
                backLeftPower = 0.0;
                frontRightPower = 0.0;
                backRightPower = 0.0;

                if (stateTime >= 1.000) {
                    currentState = ExpParkState.STATE_2_START_FLYWHEEL_FIRST_3;
                    stateTimer.reset();
                    flywheel.startDirect();
                }
                break;

            case STATE_2_START_FLYWHEEL_FIRST_3:
                // State 2: Spool flywheel directly forward for first 3 balls (2.0 sec) while Intake is strictly STOPPED
                intake.stop();
                flywheel.startDirect();

                if (stateTime >= 2.0) {
                    currentState = ExpParkState.STATE_3_PULSE_SHOOT_FIRST_3;
                    stateTimer.reset();
                }
                break;

            case STATE_3_PULSE_SHOOT_FIRST_3:
                // State 3: Shoot first 3 balls AFTER 2.0s flywheel delay by pulsing intake (100ms ON / 200ms OFF)
                autoGamepad.right_bumper = true;

                if (stateTime >= 2.6) {
                    currentState = ExpParkState.STATE_4_STOP_FIRST_SHOOT_BRAKE_250MS;
                    stateTimer.reset();
                }
                break;

            case STATE_4_STOP_FIRST_SHOOT_BRAKE_250MS:
                // State 4: Stop first shoot & engage BRAKE mode on flywheel for 250 ms before intaking 4th ball
                flywheel.stop();
                intake.stop();

                if (stateTime >= 0.250) {
                    currentState = ExpParkState.STATE_5_DRIVE_FORWARD_INTAKE_4TH_BALL;
                    stateTimer.reset();
                }
                break;

            case STATE_5_DRIVE_FORWARD_INTAKE_4TH_BALL:
                // State 5: Flywheel is stopped. Drive forward (+0.3 power) for 0.30s with Intake ON (A=true) to scoop 4th ball
                flywheel.stop();
                frontLeftPower = 0.3;
                backLeftPower = 0.3;
                frontRightPower = 0.3;
                backRightPower = 0.3;
                autoGamepad.a = true; // Intake ON collecting 4th ball

                if (stateTime >= 0.30) {
                    currentState = ExpParkState.STATE_6_BACK_UP_AND_REVERSE_BUMP_4TH_BALL;
                    stateTimer.reset();
                }
                break;

            case STATE_6_BACK_UP_AND_REVERSE_BUMP_4TH_BALL:
                // State 6: Drive backward (-0.3 power) for 0.30s AND reverse intake (-0.5 power) for 100 ms
                frontLeftPower = -0.3;
                backLeftPower = -0.3;
                frontRightPower = -0.3;
                backRightPower = -0.3;

                if (stateTime < 0.100) {
                    autoGamepad.left_bumper = true;
                } else {
                    intake.stop();
                }

                if (stateTime >= 0.30) {
                    currentState = ExpParkState.STATE_6B_SETTLE_2;
                    stateTimer.reset();
                }
                break;

            case STATE_6B_SETTLE_2:
                // State 6B: Turn OFF Intake & pause 1.0 second with all motors stopped (0.0 power) to let inertia dissipate
                flywheel.stop();
                intake.stop();
                frontLeftPower = 0.0;
                backLeftPower = 0.0;
                frontRightPower = 0.0;
                backRightPower = 0.0;

                if (stateTime >= 1.000) {
                    currentState = ExpParkState.STATE_7_START_FLYWHEEL_4TH_BALL;
                    stateTimer.reset();
                    flywheel.startDirect();
                }
                break;

            case STATE_7_START_FLYWHEEL_4TH_BALL:
                // State 7: Flywheel spools directly forward to 1.0 power (2.0 sec) while Intake is strictly STOPPED
                intake.stop();
                flywheel.startDirect();

                if (stateTime >= 2.0) {
                    currentState = ExpParkState.STATE_8_PULSE_SHOOT_4TH_BALL;
                    stateTimer.reset();
                }
                break;

            case STATE_8_PULSE_SHOOT_4TH_BALL:
                // State 8: Shoot 4th ball AFTER 2.0s flywheel delay by pulsing intake (2.0 sec)
                autoGamepad.right_bumper = true;

                if (stateTime >= 2.0) {
                    currentState = ExpParkState.STATE_9_STOP_SECOND_SHOOT;
                    stateTimer.reset();
                }
                break;

            case STATE_9_STOP_SECOND_SHOOT:
                // State 9: Stop second shoot and transition to parking sequence
                flywheel.stop();
                intake.stop();

                currentState = ExpParkState.STATE_10_TURN_45_LEFT;
                stateTimer.reset();
                break;

            case STATE_10_TURN_45_LEFT:
                // State 10: Turn 45 degrees Left in place at 0.5 power for 0.275s (275 ms)
                frontLeftPower = -0.5 * 0.8;
                backLeftPower = -0.5 * 0.8;
                frontRightPower = 0.5 * 0.8;
                backRightPower = 0.5 * 0.8;

                if (stateTime >= 0.275) {
                    currentState = ExpParkState.STATE_11_DRIVE_BACKWARD_1750MS;
                    stateTimer.reset();
                }
                break;

            case STATE_11_DRIVE_BACKWARD_1750MS:
                // State 11: Drive backward for 1.75 seconds at -0.4 power
                frontLeftPower = -0.4 * 0.8;
                backLeftPower = -0.4 * 0.8;
                frontRightPower = -0.4 * 0.8;
                backRightPower = -0.4 * 0.8;

                if (stateTime >= 1.75) {
                    currentState = ExpParkState.STATE_12_TURN_45_RIGHT_CORRECT;
                    stateTimer.reset();
                }
                break;

            case STATE_12_TURN_45_RIGHT_CORRECT:
                // State 12: Turn 45 degrees Right to correct heading in place at 0.5 power for 0.275s (275 ms)
                frontLeftPower = 0.5 * 0.8;
                backLeftPower = 0.5 * 0.8;
                frontRightPower = -0.5 * 0.8;
                backRightPower = -0.5 * 0.8;

                if (stateTime >= 0.275) {
                    currentState = ExpParkState.STATE_13_DRIVE_BACKWARD_2300MS;
                    stateTimer.reset();
                }
                break;

            case STATE_13_DRIVE_BACKWARD_2300MS:
                // State 13: Drive BACKWARD for 2.3 seconds at -0.4 power into parking box
                frontLeftPower = -0.4 * 0.8;
                backLeftPower = -0.4 * 0.8;
                frontRightPower = -0.4 * 0.8;
                backRightPower = -0.4 * 0.8;

                if (stateTime >= 2.3) {
                    currentState = ExpParkState.STATE_14_ALL_STOP;
                    stateTimer.reset();
                }
                break;

            case STATE_14_ALL_STOP:
                // State 14: All stop -> End State Machine (Parked in box)
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
        if (currentState != ExpParkState.STATE_14_ALL_STOP) {
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
