# Checkpoint 7 — autonomous: park

> **SIMULATED** — the guide's prompt, not yet The Hive's original. Replace when recovered. The park path (turn right ~90°, drive ~4 ft) is a placeholder for The Hive's real path.

Guide page: `../guide/07-auto-park.md`
Produces: `../example-code/07-auto-park/HiveAutoShoot.java`

Same conversation as Checkpoint 6.

## Prompt (as used)

> Add parking to `HiveAutoShoot`. Don't change LEAVE_WALL, SPIN_UP, or SHOOT — they work.
>
> Insert two new states between SHOOT and DONE:
>
> - `TURN_TO_PARK` — turn off the flywheels and intake. Rotate the robot clockwise in place: left motors at +0.5, right motors at −0.5, for 0.6 seconds.
> - `DRIVE_TO_PARK` — all four drive motors forward at 0.5 for 2.0 seconds. Then stop the drive motors.
>
> Then go to DONE as before. Reset the timer on each state change, same as the others. Add the new states to the telemetry.

**Who typed it:** mechanical team

## What it produced

Two new enum values, SHOOT's exit changed from DONE to TURN_TO_PARK, a second `setDrive(left, right)` overload for the turn, and the two new `case` blocks. Earlier states untouched.

## What to expect on the first runs

The two things most likely to be off, and the one-line prompt for each. (The example code keeps the starting numbers; yours will change.)

**Turns the wrong way.** "Clockwise" in the prompt is unambiguous, but which motors are "left" depends on where you stand. Watch from above.

> TURN_TO_PARK rotates the wrong way. Swap the signs: left motors −0.5, right motors +0.5. Nothing else changes.

**Overshoots or undershoots the zone.**

> Change DRIVE_TO_PARK from 2.0 to 1.7 seconds.

One change, then three runs. Commit when three in a row end in the zone.

## Note on time-based turns

A 0.6-second turn at 0.5 power varies with battery voltage and floor. If the angle drifts between runs, slow it down and lengthen it — 0.35 power for 0.9 seconds is more repeatable than 0.5 for 0.6. Encoders or an IMU heading fix this properly; that's a later checkpoint.
