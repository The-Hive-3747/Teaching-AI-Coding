# Checkpoint 1 — the one-time robot description

> **SIMULATED** — the guide's prompt, not yet The Hive's original. Replace when recovered.

Guide page: `../guide/01-describe-the-robot.md`

## Prompt (as used)

> I'm writing code for a FIRST Tech Challenge robot using the FtcRobotController SDK. All my code goes in the TeamCode module. Here's the robot.
>
> **Drive:** Mecanum drive, four motors. Names in the hardware config: `frontLeft`, `frontRight`, `backLeft`, `backRight`. The left-side motors are mounted mirrored, so `frontLeft` and `backLeft` need to be reversed for the robot to drive forward when all four get positive power.
>
> **Intake:** One motor named `intake`. Positive power pulls game pieces into the robot.
>
> **Shooter:** Two flywheel motors named `flywheelLeft` and `flywheelRight`, one on each side of the launch path, so the game piece is pushed out between them. One of them will probably need to be reversed; I'll tell you which after testing. The intake feeds pieces into the flywheels.
>
> **Controls (gamepad 1):** Left stick moves the robot (Y = forward/back, X = strafe). Right stick X rotates. Right bumper held = intake in. Left bumper held = intake out. Right trigger held = spin up the flywheels, then feed.
>
> Don't write any code yet. Just confirm you understand the robot and tell me if anything is unclear.

**Who typed it:** coordinator

## What it produced

A restatement of the robot. No code. The reply is the check: read it for anything wrong.

## What to watch for in the reply

- It says the flywheels "spin in opposite directions" or "the same direction" as a fact. Correct it: *"We don't know yet. Leave both un-reversed until I test."*
- It assumes a motor type or encoder use ("I'll use RUN_USING_ENCODER"). Say: *"No encoders for now. Plain power control."*
- It proposes gamepad 2 controls you didn't ask for. Say: *"Gamepad 1 only for now."*

## Why the last line matters

Without "don't write any code yet," Gemini in Agent mode will often go straight to creating a full TeleOp with everything in it — which is exactly the all-at-once approach the workshop avoids.
