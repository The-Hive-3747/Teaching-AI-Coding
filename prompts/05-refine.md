# Checkpoint 5 — the four refinement prompts

> **VERBATIM** — see the README for what that means.

Guide page: `../guide/05-refine.md`

These are the mechanical team's words, from Gemini's transcript (Session Iterations 4, 6, 7, 8). Each is one prompt, one change, one test.

## "It is spitting balls out"

> **VERBATIM:** *what is the current time of spinning the motors back before starting the flywheel? it is spitting balls out, so i would like to shorten it*

**Changed:** `REVERSE_DURATION_SEC` 0.200 → 0.100 in `Flywheel.java`.
**Style:** asks the question first, names the symptom, says the direction of the change but not the number. The transcript records the change (200 → 100 ms), not the reply.

## "I thought gamepad 1 dpad was slow mode"

> **VERBATIM:** *I thought gamepad 1 dpad was slow mode, not tuning the flywheel.*

**Changed:** `Flywheel.update` now takes two gamepads; D-pad tuning reads gamepad 2 only. Gamepad 1's D-pad is left to precision drive.
**Style:** states the expectation. A complete bug report in one sentence.

## "I don't want the intake to resume"

> **VERBATIM:** *When I start up the flywheel, I want the process to continue to do the reverse function. But then I don't want the intake to resume. I want the intake to stop (whether it was stopped or running).*

**Changed:** `isCollectOn = false;` inside the `isFlywheelReversing` branch of `Intake.update`.
**Style:** describes the sequence step by step — keep this, then don't do that, regardless of prior state. That precision is why it was a one-line fix.

## "Let's set the default speed to 0.95"

> **VERBATIM:** *Let's set the default speed to 0.95*

**Changed:** `targetPower = 0.95` (was 1.0) in `Flywheel.java`, and in `startDirect()`.
**Style:** the tuning loop closing. The drivers had the gamepad 2 D-pad to try speeds in practice; this bakes their pick in.

## Compare

`../example-code/05-refine/` — final `Flywheel.java` and `Intake.java` verbatim.
