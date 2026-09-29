# Checkpoint 7 — two autos; the delayed one parks

> **RECONSTRUCTED** — see the README for what that means.

Guide page: `../guide/07-auto-park.md`

## Prompt (as used in the guide)

> **Prompt:**
>
> Refactor `AutoShootFirst` into a base class plus two OpModes. Don't change the behavior of the existing states.
>
> - Move everything into an abstract class `BaseAuto extends OpMode` with an abstract method `getInitialDelaySeconds()`.
> - Add a `DELAY` state at the start that waits `getInitialDelaySeconds()` before going to `STATE_1_BACK_UP`.
> - `AutoShootFirst extends BaseAuto` returns 0.0 and keeps its `@Autonomous(name = "Auto: Shoot First")`. `AutoShootDelayed extends BaseAuto` returns 15.0, annotated `@Autonomous(name = "Auto: Shoot Delayed (15s)")`.
> - Add a method `shouldParkTurnAndDrive()` that returns true only when the delay is greater than 0. After `STATE_4_STOP_SHOOTING`, if it's true go to the park states below; otherwise go to `STATE_6_ALL_STOP` as before.
> - Park states, in order:
>   - `STATE_4B_PARK_BACK_UP` — all four drive motors at −0.3 for 0.45 seconds.
>   - `STATE_5_PARK_TURN_LEFT` — turn left in place: left motors −0.5, right motors +0.5, both scaled by the 0.8 drive cap, for 0.55 seconds.
>   - `STATE_5B_PARK_DRIVE_FORWARD` — all four at 0.4 scaled by 0.8, for 3.0 seconds.
>   - then `STATE_6_ALL_STOP`.
> - Same timer reset on every transition. Add the new states to telemetry.

## What it should produce

*(Snapshot derived from the team's final code. This reconstructed prompt has not been run; Gemini's output will differ in names and layout.)*

`BaseAuto.java` (abstract, holds the state machine, `DELAY` state, `getInitialDelaySeconds()`, `shouldParkTurnAndDrive()`, three park states) plus `AutoShootFirst.java` and `AutoShootDelayed.java`, each ~12 lines. Compare `../example-code/07-auto-park/`.

## Likely first-test failures

| Symptom | Prompt |
|---|---|
| Turns the wrong way | "STATE_5_PARK_TURN_LEFT turns right. Swap the signs: left motors +0.5, right motors −0.5." |
| Overshoots the zone | "Change STATE_5B_PARK_DRIVE_FORWARD from 3.0 to 2.5 seconds." |
| Shoot First parks too | "`shouldParkTurnAndDrive()` should return false when the delay is 0." |
