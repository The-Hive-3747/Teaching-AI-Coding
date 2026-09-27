# Checkpoint 7 — Autonomous: Park

**At the end of this checkpoint:** After shooting, the robot drives to the parking zone and stops there.

**Time:** 20–30 minutes.

---

## 7.1 Add states, don't rewrite

The autonomous from Checkpoint 6 works. Parking is one or two more states between `SHOOT` and `DONE`. Adding to a working state machine is the easiest kind of change — say exactly where the new states go and what they do.

```
  ...
┌──────────────┐
│    SHOOT     │
└──────┬───────┘
       ▼
┌──────────────┐  flywheels off
│  TURN_TO_PARK│  rotate: left motors +0.5, right motors -0.5, for 0.6 s
└──────┬───────┘
       ▼
┌──────────────┐  all motors forward at 0.5
│ DRIVE_TO_PARK│  for 2.0 s
└──────┬───────┘
       ▼
┌──────────────┐
│     DONE     │
└──────────────┘
```

> **Ben:** replace with The Hive's actual park path. If it's a strafe instead of turn-then-drive, describe that. Diagram of the field with the path drawn on it goes here: `[DIAGRAM: top-down field sketch, start position on wall, shooting spot, arrow to park zone]`

## 7.2 Work out the path before prompting

Walk the robot through it by hand. Which way does it need to turn? How far? Rough numbers are fine; you'll tune. Write it as a list:

1. After shooting, turn right about 90°.
2. Drive forward about 4 feet.
3. Stop.

## 7.3 Ask for it

Same conversation as Checkpoint 6.

> **Prompt:**
>
> Add parking to `HiveAutoShoot`. Don't change LEAVE_WALL, SPIN_UP, or SHOOT — they work.
>
> Insert two new states between SHOOT and DONE:
>
> - `TURN_TO_PARK` — turn off the flywheels and intake. Rotate the robot clockwise in place: left motors at +0.5, right motors at −0.5, for 0.6 seconds.
> - `DRIVE_TO_PARK` — all four drive motors forward at 0.5 for 2.0 seconds. Then stop the drive motors.
>
> Then go to DONE as before. Reset the timer on each state change, same as the others. Add the new states to the telemetry.

`[SCREENSHOT: the updated enum and the two new case blocks]`

## 7.4 Read what it wrote

Find:
1. Two new values in the enum.
2. `SHOOT` now transitions to `TURN_TO_PARK` instead of `DONE`.
3. Flywheels get set to 0 at the start of `TURN_TO_PARK`.
4. `DRIVE_TO_PARK` transitions to `DONE`.

**Compare with:** [`../example-code/07-auto-park/HiveAutoShoot.java`](../example-code/07-auto-park/HiveAutoShoot.java) — a reference version written against the same prompt (simulated until the real one replaces it).

## 7.5 Test

Same setup as Checkpoint 6. Mark the park zone with tape if you don't have field elements.

| Step | Expected | Actual |
|---|---|---|
| Shoots (unchanged) | Same as Checkpoint 6 | |
| Turns | About 90°, correct direction | |
| Drives | Ends in the park zone | |
| Done | Stops, stays | |
| Total time | Under 15 seconds | |

Three runs. Note where the robot ends up each time — use tape marks.

## 7.6 If it's wrong

| Symptom | Prompt |
|---|---|
| Turns the wrong way | "TURN_TO_PARK rotates the wrong way. Swap the signs: left motors −0.5, right motors +0.5." |
| Turns too far / not enough | "Change TURN_TO_PARK from 0.6 to 0.5 seconds." |
| Overshoots the park zone | "Change DRIVE_TO_PARK from 2.0 to 1.7 seconds." |
| Turns while still shooting | Flywheels didn't stop, or SHOOT exited early. "Make sure SHOOT completes all 3 pulses before moving to TURN_TO_PARK." |
| Robot drifts during the drive | "In DRIVE_TO_PARK, run the right motors slightly faster: right 0.55, left 0.5." |
| Ends up in the right spot only sometimes | Time-based variance. Lower the power and lengthen the time: slower is more repeatable. "Change DRIVE_TO_PARK to 0.35 power for 2.8 seconds." |
| Shooting broke | Gemini touched earlier states. "Restore LEAVE_WALL, SPIN_UP and SHOOT exactly as they were." Or revert to the saved Checkpoint 6 file. |

Tuning table:

| TURN (s) | TURN power | DRIVE (s) | DRIVE power | Ended in zone? |
|---|---|---|---|---|
| 0.6 | 0.5 | 2.0 | 0.5 | |
| | | | | |

## 7.7 Save it

Commit: "Auto shoots and parks."

## Checkpoint 7 test

- [ ] Three runs in a row end in the park zone
- [ ] Shooting still works
- [ ] Reader can trace the full state sequence from START to DONE
- [ ] Saved

Go to [Checkpoint 8 — Under 30 seconds](08-auto-tuning.md).
