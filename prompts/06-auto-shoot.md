# Checkpoint 6 — autonomous: back off the wall and shoot

> **RECONSTRUCTED** — see the README for what that means.

Guide page: `../guide/06-auto-shoot.md`

## Prompt (as used in the guide)

> **Prompt:**
>
> Create an Autonomous OpMode in TeamCode called `AutoShootFirst`, iterative `OpMode` style like `MecanumTeleOp`. Annotate it `@Autonomous(name = "Auto: Shoot First")`. Same four drive motors, same reversals and brake settings as `MecanumTeleOp` — read them from that file. Reuse the existing `Flywheel` and `Intake` classes; don't modify them.
>
> The robot starts with the shooter facing the goal and its back against the wall. Preloaded balls are already in the intake.
>
> Implement a state machine with an enum and `ElapsedTime`:
>
> 1. `STATE_1_BACK_UP` — all four drive motors at −0.3 for 0.30 seconds, to back away from the wall to shooting distance.
> 2. `STATE_1B_SETTLE_1000MS` — drive motors at 0 for 1.0 second so the robot stops rocking.
> 3. `STATE_2_START_FLYWHEEL` — add a `startDirect()` method to `Flywheel` that goes straight to RUNNING at target power, skipping the reverse bump (balls are preloaded; reversing would eject them). Call it, and wait 2.0 seconds.
> 4. `STATE_3_PULSE_SHOOTING` — create a `Gamepad` object in the OpMode as a simulated gamepad. In this state set its `right_bumper` to true and pass it to `intake.update(...)` so the intake pulses exactly as in TeleOp. Stay here 10 seconds.
> 5. `STATE_4_STOP_SHOOTING` — `flywheel.stop()`, `intake.stop()`.
> 6. `STATE_6_ALL_STOP` — everything at zero. Stay here.
>
> Reset the state timer on every transition. Call `flywheel.update(autoGamepad, false)` and `intake.update(...)` every loop, after the switch, so the subsystems run their own state machines. Show the current state, state time, total time, and flywheel phase on telemetry. No `sleep()`.

## What it produced

`AutoShootFirst.java` with a six-state machine, a simulated `Gamepad`, and a new `Flywheel.startDirect()`. Compare `../example-code/06-auto-shoot/`.

## Why reconstructed

Gemini's transcript (Session Iteration 2) summarizes the first autonomous session: *"Implement standard and experimental 4-ball autonomous state machines"* and the key learning *"Reversing intake while preloaded with balls will eject them. Preloaded balls require direct forward spooling (`startDirect()`)."* The prompt is written to reach the same design in the guide's smaller steps.

## The lesson that came from testing

The first autonomous ran the normal B-button startup — reverse bump included — and ejected the preloaded balls. The fix was `startDirect()`: go straight to RUNNING. If your prompt doesn't say "balls are preloaded, skip the reverse bump," expect the same thing.
