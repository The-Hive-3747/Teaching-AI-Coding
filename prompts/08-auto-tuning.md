# Checkpoint 8 — under 30 seconds

> **SIMULATED** — the guide's prompt, not yet The Hive's original. Replace when recovered.

Guide page: `../guide/08-auto-tuning.md`
Produces: `../example-code/08-auto-tuning/HiveAutoShoot.java`

Same conversation as Checkpoints 6–7.

## Prompt 1 — check the math

> Add up the total duration of all states in `HiveAutoShoot` and tell me the worst-case run time.

**Produced:** 0.9 + 2.0 + 3 × 0.5 + 0.6 + 2.0 = **7.0 s**. Well under 30. So this checkpoint is about reliability, not speed.

**Who typed it:** mechanical team

## Prompt 2 — safety timeout

> Add a safety timeout to `HiveAutoShoot`. Use a separate `ElapsedTime` that starts when the OpMode starts. If it ever passes 28 seconds, regardless of the current state, stop all motors and go to DONE. Show the total elapsed time on telemetry. Don't change any of the existing states.

**Produced:** a `runTimer`, reset right after `waitForStart()`, and a check at the top of the loop before the `switch`. One new constant. Nothing else touched.

## Prompt 3 — only if reliability runs show drift

Five runs from the same spot. If the park distance varies:

> DRIVE_TO_PARK ends in a different spot each run. Change it to 0.35 power for 2.8 seconds so it's slower and more consistent.

If it's worse on a low battery:

> Read the battery voltage from the hardware map's voltage sensor at the start of each state. Scale the drive powers in LEAVE_WALL, TURN_TO_PARK and DRIVE_TO_PARK by 12.5 divided by the current voltage, so the robot moves the same distance on a low battery. Cap the scaled power at 1.0.

(This one is real SDK code Gemini knows: `hardwareMap.voltageSensor.iterator().next().getVoltage()`. Ask it to explain the line if the team wants to know what it does.)

## What went wrong

**Nothing in the simulation** — the example code only adds the timeout. On a real robot, expect Prompt 3 to be needed on at least one of the two axes (turn or drive).

## Final commit and tag

Commit message: "Auto final — 5/5, 7 s." Tag: `week1-final`.
