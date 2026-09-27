# Checkpoint 5 — direction, timing, pulsing fixes

> **SIMULATED** — the guide's prompts, not yet The Hive's originals. Replace when recovered. This is the checkpoint where the real prompts matter most: they're the mechanical team's own words.

Guide page: `../guide/05-refine.md`
Produces: `../example-code/05-refine/HiveTeleOp.java`

Each of these is one driver complaint → one prompt → one test. Only the kept ones are listed; the log in the guide has the reverted ones.

## 5a — "It's too twitchy"

> The drive is too sensitive at small stick movements. Cube the joystick inputs so the first 30% of stick travel gives finer control. Also add a deadzone of 0.05 so the robot doesn't creep when the sticks are released. Don't change the shooter or intake.

**Produced:** a `shapeInput()` helper applied to all three stick axes.
**Who:** mechanical team

## 5b — "Turning is too fast"

> Scale the rotation input by 0.7 so turning is slower than driving. Only change the rotation term.

**Produced:** `* ROTATION_SCALE` on the rotate variable, and a constant at the top.
**Who:** mechanical team

## 5c — "I can't line up on the goal"

> Add a slow mode: while the left trigger is held past 0.5, scale all four drive powers by 0.4. Show "SLOW" on telemetry when it's active.

**Produced:** a `driveScale` multiplier on the four power lines, before normalization.
**Who:** mechanical team

## 5d — what went wrong during 5c

Gemini applied the 0.4 scale *after* normalization in one version, which is fine, and *to the shooter power* in another, which is not. Symptom: flywheels ran slow while slow mode was held.

> Slow mode should only affect the four drive motors. The flywheel and intake powers must not change when the left trigger is held.

## Explain-before-changing

Used once when the team wasn't sure what a change would touch:

> Before you change anything: explain in plain English what happens when the right trigger is pressed, step by step, including what the bumpers do at each step.

The answer matched the robot, so nothing was changed. Worth doing whenever a prompt would touch the shooter.
