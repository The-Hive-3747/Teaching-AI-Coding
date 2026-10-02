# Checkpoint 8 — Autonomous: Under 30 Seconds

**At the end of this checkpoint:** Both autos run reliably, finish inside the 30-second period, and keep the LEAVE points by ending clear of the wall.

**Time:** 30–60 minutes of runs.

---

## 8.1 Add up the time

Write down the duration of every state and total them. Do it for **each** OpMode, because the delayed one has 15 seconds less to work with.

**Shoot First:**

| State | Duration (s) |
|---|---|
| STATE_1_BACK_UP | 0.30 |
| STATE_1B_SETTLE_1000MS | 1.0 |
| STATE_2_START_FLYWHEEL | 2.0 |
| STATE_3_PULSE_SHOOTING | 10.0 |
| **Total** | **13.3** |

Fine.

**Shoot Delayed, as it left Checkpoint 7:**

| State | Duration (s) |
|---|---|
| DELAY | 15.0 |
| STATE_1_BACK_UP | 0.30 |
| STATE_1B_SETTLE_1000MS | 1.0 |
| STATE_2_START_FLYWHEEL | 2.0 |
| STATE_3_PULSE_SHOOTING | 10.0 |
| STATE_4B_PARK_BACK_UP | 0.45 |
| STATE_5_PARK_TURN_LEFT | 0.55 |
| STATE_5B_PARK_DRIVE_FORWARD | 3.0 |
| **Total** | **32.3** |

**Over.** The Driver Station stops the OpMode at 30 s, so at this total the robot gets cut off in the park drive — which is what you'd see in the Checkpoint 7 test if you watched the DS timer. What testing *doesn't* tell you is by how much, or which state to shorten. That's what the column is for.

The Hive did think about this. The prompt that added the park, verbatim: *"On the delayed side, I want to strafe to the right for 3 seconds at 0.3 power. If that exceeds our 30 second time for autonomous, let's cut down shoot[ing] … [minim]um of 5 seconds shooting."* Gemini cut the delayed shoot to 8.3 s — and the total was still 30.7. Telling Gemini the limit isn't the same as seeing the sum. Ask for it:

> "Add up the total duration of all states in `BaseAuto` for the delayed auto and tell me the worst-case run time."

## 8.2 Where the time goes

The only state with slack is shooting: 10 seconds to launch three balls is generous. The Hive cut the delayed auto's shoot time to 8.3 s, leaving Shoot First at 10.

> **Prompt:**
>
> In `BaseAuto`, make the shoot duration depend on the delay: 10.0 seconds when the delay is 0, 8.3 seconds when the delay is greater than 0. Add a `getShootDurationSeconds()` method for it. Don't change anything else.

That brings Shoot Delayed to **30.6 s** — still over by 0.6. (The Hive's final code ships like this, plus the 0.1 s step added in 8.3, for 30.7 s on paper — and on the field it finished, off the wall. So the cutoff isn't as sharp as the arithmetic suggests. Don't bank on that: a run that only works because the timer is generous is one battery-voltage dip from not working. Add it up, and leave real margin.) This is why you add it up: the fix is one more number.

> **Prompt:**
>
> The delayed auto totals more than 30 seconds and the period is 30. Reduce the delayed shoot duration from 8.3 to 6.5 seconds so the whole run finishes with a full second to spare.

Or shorten the delay itself, if the alliance partner doesn't need the full 15. Either way, re-add the column until it's under 29 — including the 0.1 s step you're about to add.

## 8.3 Keeping the LEAVE points — a real prompt

After the park drive, the robot ends pressed against the wall. In BIOBUZZ, PARK (5 points) only needs the robot in the loading zone — but LEAVE (3 points) requires the robot to *not be touching the perimeter wall*, and both are assessed at the end of AUTO. A robot that parks by driving into the wall earns PARK and forfeits LEAVE. The mechanical team spotted this. Their prompt, verbatim:

> *On the delayed autonomous, we end by bumping into the wall to get park points. However, we get points for not touching the wall. Can we add a backup after the turn and drive forward? I'm thinking 100ms for 0.5 power backwards at the end should move us away.*

Gemini added `STATE_5C_PARK_BACK_OFF_WALL`: all four motors at −0.5 for 100 ms, between the park drive and ALL_STOP. Notice the shape: the rule ("we get points for not touching the wall"), the ask ("a backup after the turn and drive forward"), and a starting number ("100ms for 0.5 power"). Gemini had nothing to guess.

With the 6.5 s shoot from 8.2, Shoot Delayed now adds up to 15 + 0.3 + 1.0 + 2.0 + 6.5 + 0.45 + 0.55 + 3.0 + 0.1 = **28.9 s**. Under 29, with the safety timer below as the backstop.

`[SCREENSHOT: the STATE_5C case in BaseAuto.java]`

## 8.4 Add a safety timer

Whatever the total, add a hard stop so the robot can never run past the period:

> **Prompt:**
>
> Add a safety timeout to `BaseAuto`. Use the existing `totalAutoTimer`. If it ever passes 29.5 seconds, regardless of the current state, set all drive motors to zero, stop the flywheel and intake, and go to `STATE_6_ALL_STOP`. Show the total elapsed time on telemetry (it may already be there).

The Driver Station's own 30-second timer will stop the OpMode regardless. This stops the robot *cleanly*, from your own code, half a second before that — every motor at zero in a known state, rather than whatever the DS interrupted.

## 8.5 Reliability runs

Run each auto **five times** from the same starting position. For each run write down:

| Run | OpMode | Balls scored | Ended in zone? | Touching wall? | Total time (s) | Notes |
|---|---|---|---|---|---|---|
| 1 | Delayed | | | | | |
| 2 | Delayed | | | | | |
| 3 | Delayed | | | | | |
| 4 | Delayed | | | | | |
| 5 | Delayed | | | | | |

Five out of five is the target. Four is acceptable if the miss was small. Fewer means something needs tuning.

## 8.6 If it's inconsistent

Time-based autonomous varies because of battery voltage, wheel slip, and where the robot started. Fixes, in order of how often they help:

| Problem | Fix |
|---|---|
| Park distance varies run to run | Lower the power and lengthen the time. "Change STATE_5B to 0.3 power for 4.0 seconds." Slower is more consistent. (Then re-add the total.) |
| Worse when battery is low | Test with a fresh battery and note the voltage. Consider: "Scale all drive powers in the auto by 12.5 divided by the current battery voltage, read from `hardwareMap.voltageSensor`, so the robot moves the same distance on a low battery. Cap at 1.0." |
| Turn angle varies | Same fix as drive: slower turn, longer time. |
| First shot wild | "Increase STATE_1B_SETTLE_1000MS from 1.0 to 1.5 seconds." |
| Starting position varies | Not a code fix. Make a physical jig or tape marks for the starting position. |
| Back-off-wall doesn't clear the wall | "Change STATE_5C_PARK_BACK_OFF_WALL from 100 to 150 ms." |

## 8.7 Match-day checklist

- [ ] Battery charged; note the voltage you tuned at
- [ ] Wheels clean
- [ ] Starting position marked or jigged
- [ ] **Right OpMode selected** — Shoot First or Shoot Delayed, agreed with the alliance partner
- [ ] Balls preloaded the same way every time
- [ ] Field clear of people

## 8.8 Save it — and tag it

Commit: "Auto final — delayed under 30 s, 5/5, LEAVE kept."

In Git, also tag it: **Git → New Tag**, name it `week1-final`. When you start adding odometry or changing the robot later, you can always get this version back.

**Compare with:** [`../example-code/08-auto-tuning/`](../example-code/08-auto-tuning/) — The Hive's final `BaseAuto.java` (minus LEDs), with the back-off-wall state and the 8.3 s delayed shoot.

## Checkpoint 8 test

- [ ] Both autos add up to under 29 s on paper
- [ ] Safety timeout is in
- [ ] LEAVE confirmed: not touching the wall when the timer expires
- [ ] 5 runs logged per auto; at least 4 succeed
- [ ] Reader can explain the safety timeout and every state
- [ ] Saved and tagged

## What's next

You have a working robot with two autonomous routines, and a team that can change any of it by describing what they want. [Checkpoint 9](09-extras.md) covers the extras The Hive added — LED status lights, endgame rumble, and a 4-ball experiment — and what to describe next season.
