# Checkpoint 8 — Autonomous: Under 30 Seconds

**At the end of this checkpoint:** The autonomous runs reliably, finishes with time to spare inside the 30-second period, and is ready for a match.

**Time:** 30–60 minutes of runs.

---

## 8.1 Add up the time

Write down the duration of every state and total them:

| State | Duration (s) |
|---|---|
| LEAVE_WALL | 0.9 |
| SPIN_UP | 1.5 |
| SHOOT (3 × 0.5) | 1.5 |
| TURN_TO_PARK | 0.6 |
| DRIVE_TO_PARK | 2.0 |
| **Total** | **6.5** |

If you're under 30 with margin, you're fine and this checkpoint is about *reliability*, not speed. Most teams doing this will be well under. If you're close to 30, the fixes are below.

Ask Gemini to do the sum if you want a sanity check:
> "Add up the total duration of all states in `HiveAutoShoot` and tell me the worst-case run time."

## 8.2 Add a safety timer

Whatever the total, add a hard stop so the robot can never run past the period:

> **Prompt:**
>
> Add a safety timeout to `HiveAutoShoot`. Use a separate `ElapsedTime` that starts when the OpMode starts. If it ever passes 28 seconds, regardless of the current state, stop all motors and go to DONE. Show the total elapsed time on telemetry.

The Driver Station has its own 30-second autonomous timer that stops the OpMode, but the safety timer is still worth having: it stops the robot *cleanly*, from your own code, before the hard cutoff — and it protects you during practice runs where the Driver Station timer may not be on.

## 8.3 Reliability runs

Run the full autonomous **five times** from the same starting position. For each run write down:

| Run | Pieces scored | Ended in park zone? | Total time (s) | Notes |
|---|---|---|---|---|
| 1 | | | | |
| 2 | | | | |
| 3 | | | | |
| 4 | | | | |
| 5 | | | | |

Five out of five is the target. Four is acceptable if the miss was small. Fewer means something needs tuning.

## 8.4 If it's inconsistent

Time-based autonomous varies because of battery voltage, wheel slip, and where the robot started. Fixes, in order of how often they help:

| Problem | Fix |
|---|---|
| Drive distance varies run to run | Lower the power and lengthen the time. "Change DRIVE_TO_PARK to 0.35 power for 2.8 seconds." Slower is more consistent. |
| Worse when battery is low | Test with a fresh battery and note the voltage. Consider: "Scale all drive powers by 12.5 divided by the current battery voltage, so the robot moves the same distance on a low battery." (This reads `hardwareMap.voltageSensor`.) |
| Turn angle varies | Same fix as drive: slower turn, longer time. |
| First shot inconsistent | "Increase SPIN_UP from 1.5 to 2.0 seconds." |
| Starting position varies | Not a code fix. Make a physical jig or tape marks for the starting position. |
| Piece jams | "Add a 0.3 second reverse pulse of the intake before SHOOT starts, to unjam." |

## 8.5 If it's too slow

Only if you're near 30 seconds:

| Where to save time | Prompt |
|---|---|
| Spin-up | "Start the flywheels during LEAVE_WALL instead of after, so spin-up overlaps with driving." |
| Shooting | "Reduce the pulse-off time from 0.2 to 0.1 seconds." |
| Driving | "Increase DRIVE_TO_PARK power to 0.7 and reduce the time to 1.4 seconds." (Then re-check reliability — faster is less consistent.) |
| Turning | "Increase TURN_TO_PARK power to 0.7 and reduce time to 0.4 seconds." |

## 8.6 Match-day checklist

- [ ] Battery charged; note the voltage you tuned at
- [ ] Wheels clean
- [ ] Starting position marked or jigged
- [ ] Right OpMode selected on the Driver Hub (`HiveAutoShoot`, not TeleOp)
- [ ] Pieces preloaded the same way every time
- [ ] Field clear of people

## 8.7 Save it — and tag it

Commit: "Auto final — 5/5 reliable, X seconds."

In Git, also tag it: **Git → New Tag**, name it `auto-v1` or `week1-final`. When you start adding odometry or changing the robot later, you can always get this version back.

> **Ben:** link the final version → `../example-code/08-Auto-Final.java`

## Checkpoint 8 test

- [ ] Safety timeout is in
- [ ] 5 runs logged; at least 4 succeed
- [ ] Total time is well under 30 seconds
- [ ] Reader can explain the safety timeout and every state
- [ ] Saved and tagged

## What's next

You have a working robot with an autonomous, and a team that can change any of it by describing what they want. Some things to describe next, using the same loop:

- **Encoders** — "Use the drive motor encoders instead of time so LEAVE_WALL drives exactly 24 inches." (You'll need the wheel diameter and gear ratio.)
- **Odometry** — once it's wired, describe it the same way you described the motors in Checkpoint 1.
- **A second autonomous** for the other starting position — copy `HiveAutoShoot`, describe the differences.
- **Driver-assist features** in TeleOp — "When I press X, run the shooter sequence once automatically."

Whatever you add, one change per prompt, test after every change, save what works.
