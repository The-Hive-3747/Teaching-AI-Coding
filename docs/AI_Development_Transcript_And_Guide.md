# FTC Java Robotics — AI-Assisted Engineering Transcript & Curriculum Guide

> **Project:** FIRST Tech Challenge (FTC) Mecanum Drive, 3-Phase Flywheel, Pulsed Feed Intake, goBILDA Under-Carriage LED Driver, and Non-Blocking Autonomous Systems
> **Language:** Java (FTC Robot Controller SDK)
> **Goal:** Complete record of prompt engineering, iterative debugging, state machine design, and best practices for teaching AI-assisted programming to robotics students.

---

## 1. Executive Summary & Architecture Overview

This project showcases a complete end-to-end software suite for an FTC robot built using modern non-blocking iterative state machine patterns (`OpMode`).

### Hardware Configuration
* **Drive Train:** 4 Motors (`front_left_drive`, `front_right_drive`, `back_left_drive`, `back_right_drive`) configured for Mecanum Tank Drive + Strafe kinematics, capped at `0.8` max power with `BRAKE` zero-power behavior.
* **Shooter Subsystem:** Single motor (`flywheel`), `REVERSE` direction, `BRAKE` behavior, defaulting to `0.95` target power with non-blocking 3-phase startup (100 ms reverse bump $\rightarrow$ 300 ms pause $\rightarrow$ forward spool).
* **Intake Subsystem:** 1 DC Motor (`intake`) + 2 Continuous Rotation Servos (`intake_servo_left` reversed, `intake_servo_right`), operating in Collect mode (`0.5`), Pulsed Feed mode (`100 ms ON / 200 ms OFF`), and Reject mode (`-0.5`).
* **LED Feedback Controller:** 2 Servos (`led_left`, `led_right`) utilizing expanded `500 µs - 2500 µs` PWM signal ranges to display solid diagnostic status colors and 1 Hz flashing endgame rumble alerts.

---

## 2. Chronological AI Engineering Transcript & Prompt Walkthrough

Below is the step-by-step transcript of developer prompts, problem analyses, AI solutions, and system iterations.

### Session Iteration 1: TeleOp & Core Subsystem Architecture
* **Developer Intent:** Build a non-blocking TeleOp with Tank Drive + Strafe, a 3-phase Flywheel startup, Pulsed Intake shooting, Rejection mode, and goBILDA LED status indicators.
* **AI Solution:** Created modular subsystem classes ([`Flywheel.java`](file:///C:/Users/3747h/StudioProjects/FtcRobotController/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/Flywheel.java), [`Intake.java`](file:///C:/Users/3747h/StudioProjects/FtcRobotController/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/Intake.java), [`LedController.java`](file:///C:/Users/3747h/StudioProjects/FtcRobotController/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/LedController.java)) and the primary [`MecanumTeleOp.java`](file:///C:/Users/3747h/StudioProjects/FtcRobotController/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/MecanumTeleOp.java).
* **Key Learning:** Using `ElapsedTime` instead of `Thread.sleep()` is mandatory in FTC `OpMode` loops to keep drive controls responsive.

### Session Iteration 2: Initial Autonomous OpModes & 4-Ball Experimental Trick
* **Developer Intent:** Implement standard and experimental 4-ball autonomous state machines.
* **AI Solution:** Created [`BaseAuto.java`](file:///C:/Users/3747h/StudioProjects/FtcRobotController/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/BaseAuto.java) and [`BaseAutoExperimental.java`](file:///C:/Users/3747h/StudioProjects/FtcRobotController/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/BaseAutoExperimental.java) with `startDirect()` flywheel spooling (skipping the reverse bump during preloaded auto start to prevent spitting balls out).
* **Key Learning:** Reversing intake while preloaded with balls will eject them. Preloaded balls require direct forward spooling (`startDirect()`).

### Session Iteration 3: Mecanum Kinematics Alignment & Precision Creep Mode
* **Developer Prompt:** *"hey, in the latest push, holding both joysticks left made the robot strafe right, and vice versa. please fix"*
* **AI Solution:** Corrected wheel power signs in [`MecanumTeleOp.java`](file:///C:/Users/3747h/StudioProjects/FtcRobotController/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/MecanumTeleOp.java):
  ```java
  frontLeftPower  = leftY + strafe;
  backLeftPower   = leftY - strafe;
  frontRightPower = rightY - strafe;
  backRightPower  = rightY + strafe;
  ```

### Session Iteration 4: Flywheel Reverse Bump Fine-Tuning
* **Developer Prompt:** *"what is the current time of spinning the motors back before starting the flywheel? it is spitting balls out, so i would like to shorten it"*
* **AI Solution:** Shortened `REVERSE_DURATION_SEC` from `0.200` (200 ms) down to **`0.100` (100 ms)** in [`Flywheel.java`](file:///C:/Users/3747h/StudioProjects/FtcRobotController/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/Flywheel.java).

### Session Iteration 5: Non-Contact Wall Parking Points
* **Developer Prompt:** *"On the delayed autonomous, we end by bumping into the wall to get park points. However, we get points for not touching the wall. Can we add a backup after the turn and drive forward?"*
* **AI Solution:** Added `STATE_PARK_BACK_OFF_WALL` at the end of the parking sequence, driving **FORWARD for 100 ms at `0.5` power** to pull the chassis slightly off the wall after touching down, ensuring non-contact parking points.

### Session Iteration 6: Dual Gamepad Control Mapping
* **Developer Prompt:** *"I thought gamepad 1 dpad was slow mode, not tuning the flywheel."*
* **AI Solution:** Separated gamepad mappings cleanly:
  * **Gamepad 1 D-Pad:** Dedicated 100% to **Precision Creep Driving** (`0.5` power).
  * **Gamepad 2 D-Pad:** Dedicated 100% to **Flywheel Speed Tuning** (`±0.05` per press).

### Session Iteration 7: Flywheel Auto-Intake Stop Behavior
* **Developer Prompt:** *"When I start up the flywheel, I want the process to continue to do the reverse function. But then I don't want the intake to resume. I want the intake to stop (whether it was stopped or running)."*
* **AI Solution:** In [`Intake.java`](file:///C:/Users/3747h/StudioProjects/FtcRobotController/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/Intake.java#L100-L105), added `isCollectOn = false;` during the reverse bump phase. This ensures that after the 100 ms reverse bump, the intake stays completely stopped (`0.0` power) when the flywheel spools forward until the driver explicitly feeds or collects.

### Session Iteration 8: Default Speed Set to 0.95 & New Experimental OpMode
* **Developer Prompt:** *"Let's set the default speed to 0.95"* and create `ExperimentalParkShootFirst.java`.
* **AI Solution:** Updated `targetPower = 0.95;` in [`Flywheel.java`](file:///C:/Users/3747h/StudioProjects/FtcRobotController/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/Flywheel.java#L21) and generated [`ExperimentalParkShootFirst.java`](file:///C:/Users/3747h/StudioProjects/FtcRobotController/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/ExperimentalParkShootFirst.java).

---

## 3. How to Teach FTC Coding with AI (Pedagogical Guide)

When teaching robotics students how to write code using AI assistants, emphasize these 4 principles:

1. **Be Specific About Physical Subsystems:** Instead of saying *"make the intake shoot"*, specify motor names, direction (`REVERSE`), power limits (`0.5`), and pulse timings (`100 ms ON / 200 ms OFF`).
2. **Use Non-Blocking Timers:** Never let AI use `Thread.sleep()` inside `loop()` in an FTC OpMode. Always demand state machines using `ElapsedTime` and `switch/case`.
3. **Iterate Incrementally:** Tackle one subsystem or trajectory phase at a time (e.g., test drive controls $\rightarrow$ test flywheel startup $\rightarrow$ test combined auto).
4. **Maintain Field Backups:** Always ask the AI to save backup snapshots before major architectural refactors.

---

## 4. Complete Project File Manifest

The provided ZIP file `FtcRobotController_SourceCode_And_AI_Transcript.zip` contains:

```
FtcRobotController/
├── AI_Development_Transcript_And_Guide.md
└── TeamCode/src/main/java/org/firstinspires/ftc/teamcode/
    ├── MecanumTeleOp.java
    ├── Flywheel.java
    ├── Intake.java
    ├── LedController.java
    ├── BaseAuto.java
    ├── AutoShootFirst.java
    ├── AutoShootDelayed.java
    ├── BaseAutoExperimental.java
    ├── AutoShootFirstExperimental.java
    ├── AutoShootDelayedExperimental.java
    ├── ExperimentalParkShootFirst.java
    └── backup/
        ├── MecanumTeleOpBackup.java
        ├── FlywheelBackup.java
        ├── IntakeBackup.java
        ├── LedControllerBackup.java
        ├── BaseAutoBackup.java
        ├── AutoShootFirstBackup.java
        ├── AutoShootDelayedBackup.java
        ├── BaseAutoExperimentalBackup.java
        ├── AutoShootFirstExperimentalBackup.java
        └── AutoShootDelayedExperimentalBackup.java
```
