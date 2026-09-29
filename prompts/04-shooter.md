# Checkpoint 4 — flywheel startup sequence and pulse feed

> **RECONSTRUCTED** — see the README for what that means.

Guide page: `../guide/04-shooter.md`

## Prompt (as used in the guide)

> **Prompt:**
>
> Add the flywheel to `MecanumTeleOp`. Don't change the drive or intake code.
>
> - Put it in its own class, `Flywheel`, like `Intake`: `init(hardwareMap)`, `update(gamepad, isReversed)`, `stop()`.
> - One motor named `flywheel`, reversed, brake at zero power.
> - Use an enum with the states IDLE, REVERSE_BUMP, PAUSE, RUNNING.
> - Pressing B (edge-detected) when IDLE starts the sequence: REVERSE_BUMP runs the flywheel at −0.5 for 200 ms; PAUSE stops it for 300 ms; RUNNING sets it to the target power, 1.0, and stays there. Pressing B in any other state stops the flywheel and returns to IDLE.
> - D-pad up/down adjusts the target power by ±0.05, capped at 0 and 1.
> - The intake needs to know the flywheel's state. Give `Flywheel` methods `isReversing()`, `isPausing()`, `isOn()`, and change `Intake.update` to take those three booleans:
>   - While the flywheel is in REVERSE_BUMP, the intake also runs at −0.5.
>   - While it's in PAUSE, the intake stops.
>   - While it's RUNNING and right bumper is held, pulse the intake: 100 ms on at 0.5, 200 ms off, repeating. Reset the pulse timer when the bumper is first pressed.
>   - Otherwise the intake behaves as before (collect / reject / off).
> - Reject mode (LB/X) also runs the flywheel at −0.5.
> - All timing with `ElapsedTime`, no `sleep()` — the drive must keep responding through the whole sequence.
> - Telemetry: flywheel phase, target power, intake power, whether feeding is active.

## What it should produce

*(Snapshot derived from the team's final code. This reconstructed prompt has not been run; Gemini's output will differ in names and layout.)*

New `Flywheel.java` with `enum StartPhase { IDLE, REVERSE_BUMP, PAUSE, RUNNING }`, B edge-detect, two timed transitions, D-pad tuning; `Intake.update` gains the three flywheel-state parameters and the 100/200 ms pulse. Compare `../example-code/04-shooter/` (pre-refinement: 200 ms bump, 1.0 target, tuning on gamepad 1).

## What went wrong

Three things, each fixed in Checkpoint 5 with a verbatim prompt:

1. The 200 ms reverse bump spat balls out the front → *"it is spitting balls out, so i would like to shorten it"*
2. D-pad tuning collided with D-pad precision drive → *"I thought gamepad 1 dpad was slow mode, not tuning the flywheel."*
3. Collect resumed after the bump and jammed the spool-up → *"But then I don't want the intake to resume."*

See `05-refine.md`.
