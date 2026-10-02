# Checkpoint 5 — the four refinement prompts

> **VERBATIM** — see the README for what that means.

Guide page: `../guide/05-refine.md`

These are the mechanical mentor's words — "spitting balls out" typed by Asim; the gamepad, intake-resume and 0.95 prompts called out by Asim at the robot and typed by Ben. Each is one prompt, one change, one test. Authors per prompt are in `../docs/verbatim-transcript.md`.

## "It is spitting balls out"

> **VERBATIM:** *what is the current time of spinning the motors back before starting the flywheel? it is spitting balls out, so i would like to shorten it*

**Changed:** `REVERSE_DURATION_SEC` 0.200 → 0.100 in `Flywheel.java` — plus an unrequested power reduction, reverted by the next prompt:

> **VERBATIM:** *don't bump down the power, keep it the same. let's just try the shortened time.*

**Style:** asks the question first, names the symptom, says the direction of the change but not the number. Then holds Gemini to one change.

## "I thought gamepad 1 dpad was slow mode"

> **VERBATIM:** *I thought gamepad 1 dpad was slow mode, not tuning the flywheel.*

(Second time of asking. The night before: *"Let's move the up/down on the dpad for tuning the flywheel to gamepad2. Let's make up dpad controls move the robot at 0.2 speed forward/backward and s…"* — clipped in the log.)

**Changed:** `Flywheel.update` now takes two gamepads; D-pad tuning reads gamepad 2 only. Gamepad 1's D-pad is left to precision drive.
**Style:** states the expectation. A complete bug report in one sentence.

## "I don't want the intake to resume"

> **VERBATIM:** *When I start up the flywheel, I want the process to continue to do the reverse function. But then I don't want the intake to resume. I want the intake to stop (whether it was stopped or running).*

**Changed:** `isCollectOn = false;` inside the `isFlywheelReversing` branch of `Intake.update`.
**Style:** describes the sequence step by step — keep this, then don't do that, regardless of prior state. That precision is why it was a one-line fix.

## "Let's set the default speed to 0.95"

Preceded, as usual, by a question: *"What is the current default speed of the flywheel?"* — Gemini: 1.0 — then:

> **VERBATIM:** *Let's set the default speed to 0.95*

**Changed:** `targetPower = 0.95` (was 1.0) in `Flywheel.java`, and in `startDirect()`.
**Style:** the tuning loop closing. The drivers had the gamepad 2 D-pad to try speeds in practice; this bakes their pick in.

## Compare

`../example-code/05-refine/` — final `Flywheel.java` and `Intake.java` verbatim.
