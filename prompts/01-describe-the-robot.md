# Checkpoint 1 — the one-time robot description

> **RECONSTRUCTED** — see the README for what that means.

Guide page: `../guide/01-describe-the-robot.md`

## Prompt (as used in the guide)

> **Prompt:**
>
> I'm writing code for a FIRST Tech Challenge robot using the FtcRobotController SDK (this project). All my code goes in the TeamCode module. Here's the robot.
>
> **Drive:** Mecanum, four motors named `front_left_drive`, `front_right_drive`, `back_left_drive`, `back_right_drive`. The left-side motors are mounted mirrored and need to be reversed. I want tank-style driving: left stick Y drives the left wheels, right stick Y drives the right wheels, and the X of either stick strafes. Cap all drive power at 0.8. The D-pad should do slow precision moves at 0.5 power.
>
> **Flywheel:** One motor named `flywheel`, mounted so it needs to be reversed. It launches balls.
>
> **Intake:** One motor named `intake` plus two continuous-rotation servos, `intake_servo_left` and `intake_servo_right`, that run together with it. The left servo is mounted mirrored and needs reversing. Positive power pulls balls in. The intake is also what feeds balls into the flywheel.
>
> **Controls (gamepad 1):** A toggles the intake on and off for collecting. B toggles the flywheel. Right bumper, held, feeds balls into the flywheel. Left bumper or X toggles a reject mode that runs the intake and flywheel backward.
>
> **Flywheel startup sequence, when B is pressed:** run the flywheel and intake backward at −0.5 for 200 ms to clear a jammed ball, stop everything for 300 ms, then run the flywheel forward at full power and keep it there. Pressing B again stops it.
>
> **Feeding, when right bumper is held and the flywheel is running:** pulse the intake, 100 ms on at 0.5 power, 200 ms off, repeating, so balls feed one at a time.
>
> Everything must be non-blocking: use `ElapsedTime` and state machines, never `sleep()`, so driving stays responsive during the flywheel sequence.
>
> Don't write any code yet. Just confirm you understand the robot and tell me if anything is unclear.

## What it produced

A restatement of the robot. No code — the last line asks for confirmation first.

## Why reconstructed

The Hive's first session built the whole TeleOp — drive, flywheel, intake, LEDs — from one description. Gemini's summary of it (Session Iteration 1 in the transcript) records the intent, not the words. The prompt above is written to produce the same code, split the way the guide teaches it: describe everything once, then ask for one subsystem at a time.

## What to watch for in the reply

- It assumes encoder modes ("I'll use RUN_USING_ENCODER"). Say: *"No encoders for now. Plain power control."*
- It proposes its own control scheme. Say: *"Use exactly the controls I listed."*
- It offers to write everything now. Say: *"Not yet. Next message I'll ask for the drive only."*
