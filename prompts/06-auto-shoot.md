# Checkpoint 6 — autonomous: leave wall and shoot

> **SIMULATED** — the guide's prompt, not yet The Hive's original. Replace when recovered.

Guide page: `../guide/06-auto-shoot.md`
Produces: `../example-code/06-auto-shoot/HiveAutoShoot.java`

**Fresh Gemini conversation.** The robot description from Checkpoint 1 was pasted first, then:

## Prompt (as used)

> Create a new Autonomous OpMode in TeamCode called `HiveAutoShoot`. Use the `@Autonomous` annotation. It uses the same hardware as `HiveTeleOp` — same motor names and exactly the same motor reversals as `HiveTeleOp` has now (check that file). Use the same flywheel power, spin-up time, and pulse timing that `HiveTeleOp` uses.
>
> Implement it as a state machine using an enum and `ElapsedTime`. The states are:
>
> 1. `LEAVE_WALL` — the robot starts with its back against the wall, facing the field. All four drive motors forward at 0.5 power for 0.9 seconds. Then stop the drive motors.
> 2. `SPIN_UP` — flywheels on. Wait for the spin-up time. Intake off.
> 3. `SHOOT` — keep flywheels on. Pulse the intake with the same on/off timing as TeleOp. Do this 3 times.
> 4. `DONE` — everything off. Stay here.
>
> Reset the timer every time the state changes. Show the current state and the timer on telemetry. Don't use `sleep()` — use the timer so telemetry keeps updating.
>
> Start with these exact numbers; I'll tune them after testing.

**Who typed it:** mechanical team

## What it produced

A `LinearOpMode` with `enum State { LEAVE_WALL, SPIN_UP, SHOOT, DONE }`, a `changeState()` helper that resets the timer, `setDrive()` and `setFlywheels()` helpers, and a pulse counter in SHOOT. It read the numbers out of `HiveTeleOp` (0.8, 2.0 s, 0.3/0.2) and `flywheelRight` reversed — because the prompt told it to look.

## What went wrong (first run)

Nothing, on this robot — because the prompt says which way the robot faces at the start. An earlier draft of the prompt didn't, and "forward" then depends on how the robot is placed. If your robot starts *facing* the wall, the fix is:

> At the start of autonomous the robot is facing the wall, so LEAVE_WALL should drive backward (negative power) to move away from it. Everything else stays the same.

## Second and third runs

Three pieces each, all three runs. Committed. The tuning numbers carried over from TeleOp, which is why there was nothing to fix here — Checkpoint 5's work paid off.
