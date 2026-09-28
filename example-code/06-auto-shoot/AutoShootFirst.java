package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Checkpoint 6 — Autonomous v1: back off the wall and shoot the preloaded balls.
 *
 * Derived from The Hive's final BaseAuto.java: LEDs, the start delay, the park sequence
 * and the base-class split all removed. One OpMode, one state machine:
 *
 *   STATE_1_BACK_UP           drive backward at -0.3 for 0.30 s to set the shooting distance
 *   STATE_1B_SETTLE_1000MS    stop for 1.0 s so the robot isn't rocking when it shoots
 *   STATE_2_START_FLYWHEEL    spool the flywheel directly (no reverse bump — balls are preloaded) for 2.0 s
 *   STATE_3_PULSE_SHOOTING    hold the "feed" input so the intake pulses 100 ms on / 200 ms off, for 10 s
 *   STATE_4_STOP_SHOOTING     flywheel and intake off
 *   STATE_6_ALL_STOP          everything off, stay here
 *
 * The flywheel and intake are driven through the same Flywheel and Intake classes TeleOp uses,
 * by feeding them a simulated gamepad whose buttons the state machine sets.
 */
@Autonomous(name = "Auto: Shoot First", group = "Autonomous")
public class AutoShootFirst extends OpMode {

    public enum AutoState {
        STATE_1_BACK_UP,
        STATE_1B_SETTLE_1000MS,
        STATE_2_START_FLYWHEEL,
        STATE_3_PULSE_SHOOTING,
        STATE_4_STOP_SHOOTING,
        STATE_6_ALL_STOP
    }

    // Hardware
    protected DcMotor frontLeftDrive = null;
    protected DcMotor frontRightDrive = null;
    protected DcMotor backLeftDrive = null;
    protected DcMotor backRightDrive = null;

    protected Flywheel flywheel;
    protected Intake intake;

    // State Machine Variables
    protected AutoState currentState = AutoState.STATE_1_BACK_UP;
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

    @Override
    public void init() {
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

        telemetry.addData("Status", "Init Completed (Ready for Start)");
    }

    @Override
    public void start() {
        totalAutoTimer.reset();
        stateTimer.reset();
        currentState = AutoState.STATE_1_BACK_UP;

        // Ensure subsystems are stopped
        flywheel.stop();
        intake.stop();

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
            case STATE_1_BACK_UP:
                // State 1: Drive backward (-0.3 power) for 0.30 sec (300 ms) to set shooting distance
                frontLeftPower = -0.3;
                backLeftPower = -0.3;
                frontRightPower = -0.3;
                backRightPower = -0.3;

                if (stateTime >= 0.30) {
                    currentState = AutoState.STATE_1B_SETTLE_1000MS;
                    stateTimer.reset();
                }
                break;

            case STATE_1B_SETTLE_1000MS:
                // State 1B: Pause 1.0 second with drive motors stopped (0.0 power) to dissipate robot inertia
                frontLeftPower = 0.0;
                backLeftPower = 0.0;
                frontRightPower = 0.0;
                backRightPower = 0.0;

                if (stateTime >= 1.000) {
                    currentState = AutoState.STATE_2_START_FLYWHEEL;
                    stateTimer.reset();
                    // Spool flywheel directly forward for preloaded balls without running intake reverse bump
                    flywheel.startDirect();
                }
                break;

            case STATE_2_START_FLYWHEEL:
                // State 2: Flywheel spools directly forward to 1.0 power (2.0 sec)
                flywheel.startDirect();

                if (stateTime >= 2.0) {
                    currentState = AutoState.STATE_3_PULSE_SHOOTING;
                    stateTimer.reset();
                }
                break;

            case STATE_3_PULSE_SHOOTING:
                // State 3: Shoot preloaded balls by pulsing intake (100ms ON / 200ms OFF) for 10 s
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

                currentState = AutoState.STATE_6_ALL_STOP;
                stateTimer.reset();
                break;

            case STATE_6_ALL_STOP:
                // State 6: All stop -> End State Machine
                frontLeftPower = 0.0;
                backLeftPower = 0.0;
                frontRightPower = 0.0;
                backRightPower = 0.0;

                flywheel.stop();
                intake.stop();

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

        // Telemetry Output
        telemetry.addData("Auto State", currentState.toString());
        telemetry.addData("State Time", "%.2f sec", stateTime);
        telemetry.addData("Total Auto Time", "%.1f sec", totalAutoTimer.seconds());
        telemetry.addData("Flywheel Phase", flywheel.getPhase().toString());
        telemetry.addData("Intake Power", "%.2f", intake.getCurrentPower());
        telemetry.update();
    }
}
