# Checkpoint 7 — Autonomous: Park

**At the end of this checkpoint:** Two autonomous OpModes sharing one state machine: **Shoot First** (shoot immediately, stop) and **Shoot Delayed** (wait 15 s for the alliance partner, shoot, then back up, turn left, and drive into the park zone).

**Time:** 30–45 minutes.

---

## 7.1 Why two autos, and why a base class

In a match your alliance partner may also want to shoot from the same spot. The mentors' answer was a second OpMode that waits 15 seconds before doing anything, then shoots and parks — while the Shoot First one shoots right away and stays put so the partner has room.

Two OpModes that share 90% of their logic is exactly what a **base class** is for: `BaseAuto` holds the state machine, and `AutoShootFirst` / `AutoShootDelayed` are each a few lines that say "my delay is 0" or "my delay is 15." Gemini will do this split cleanly if you ask for it.

## 7.2 Work out the park path before prompting

Walk the robot through it by hand from the shooting spot. The mentors' path took three tries in ninety minutes, all verbatim: first *"On the delayed side, I want to strafe to the right for 3 seconds at 0.3 power. If that exceeds our 30 second time for autonomous, let's cut down shooting…"*; then *"The strafing is the opposite direction. And let's increase power to 0.5."*; then the design that stuck:

> *Let's change the 15 second delay to a 1 second delay for our testing convenience. We are going to move it back to 15 seconds after we are done testing… Instead of strafing, let's turn left for .5 seconds at 0.5 power. And then go forward at 0.4 power for 3 seconds.*

Two tuning prompts later: *"It worked! Let's reduce the turn from 0.5 seconds to 0.45 seconds. And let's set the delay back to 15 seconds."* (And the next morning: *"The turn should also be for 0.55 seconds not for 0.45 seconds."*) Setting the delay to 1 s for testing and back to 15 s afterward is a trick worth copying — nobody wants to wait 15 seconds per test run.

The path as it shipped:

1. Back up a little more (0.45 s at −0.3), so there's room to turn.
2. Turn left in place, about 90° (0.55 s at 0.5).
3. Drive forward 3.0 s at 0.4 into the park zone.

`[DIAGRAM: top-down field sketch — start on the wall, back up to shooting spot, back up more, turn left 90°, drive forward into the park zone]`

Rough numbers are fine; you'll tune. Write it as a list like this before you prompt.

```
  ... STATE_4_STOP_SHOOTING
           │
     Shoot First? ──yes──► STATE_6_ALL_STOP
           │ no (Delayed)
           ▼
┌──────────────────────┐  -0.3 for 0.45 s
│ STATE_4B_PARK_BACK_UP│
└──────────┬───────────┘
           ▼
┌──────────────────────┐  left side -0.5, right side +0.5 (×0.8), 0.55 s
│ STATE_5_PARK_TURN_LEFT│
└──────────┬───────────┘
           ▼
┌──────────────────────┐  all four at 0.4 (×0.8), 3.0 s
│STATE_5B_PARK_DRIVE_FORWARD│
└──────────┬───────────┘
           ▼
┌──────────────────────┐
│   STATE_6_ALL_STOP   │
└──────────────────────┘
```

## 7.3 Ask for it

Same conversation as Checkpoint 6.

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

`[SCREENSHOT: the new BaseAuto enum and the two tiny subclass files]`

## 7.4 Read what it wrote

Find:
1. Three files: `BaseAuto.java` (big), `AutoShootFirst.java` and `AutoShootDelayed.java` (each about 15 lines).
2. `DELAY` first in the enum; its case compares `stateTime` to `getInitialDelaySeconds()`.
3. In `STATE_4_STOP_SHOOTING`: the `if (shouldParkTurnAndDrive())` branch.
4. The three park cases, with the turn setting opposite signs on the two sides.

Ask the Reader: "Which file would you change to make the delay 12 seconds?" (`AutoShootDelayed.java`, one number.) "Which file to change the turn time?" (`BaseAuto.java`, and it changes for both — though only Delayed parks.)

**Compare with:** [`../example-code/07-auto-park/`](../example-code/07-auto-park/) — the mentor robot's `BaseAuto` rolled back to before the Checkpoint 8 changes.

## 7.5 Test

Test **Shoot First** once to confirm nothing changed. Then **Shoot Delayed**: robot at the wall, balls loaded, park zone marked with tape if you don't have field elements. Expect to wait 15 seconds doing nothing — that's correct.

| Step | Expected | Actual |
|---|---|---|
| Delay | 15 s of nothing, telemetry says DELAY | |
| Shoots (unchanged) | Same as Checkpoint 6 | |
| Backs up more | Short backward move | |
| Turns | About 90° left | |
| Drives | Ends in the park zone | |
| Done | Stops, stays | |
| Total time | **The Driver Station will cut this run off at 30 s, mid-drive.** That's expected right now — Checkpoint 8 fixes it | |

Three runs. Note where the robot ends up each time — use tape marks.

## 7.6 If it's wrong

| Symptom | Prompt |
|---|---|
| Turns the wrong way | "STATE_5_PARK_TURN_LEFT turns right. Swap the signs: left motors +0.5, right motors −0.5." |
| Turns too far / not enough | "Change STATE_5_PARK_TURN_LEFT from 0.55 to 0.45 seconds." |
| Overshoots the park zone | "Change STATE_5B_PARK_DRIVE_FORWARD from 3.0 to 2.5 seconds." |
| Turns before the balls are gone | Shoot time too short for this many balls. "Increase STATE_3 shoot duration for the delayed auto." |
| Robot drifts during the drive | "In STATE_5B, run the right motors slightly faster: right 0.45, left 0.4." |
| Ends up in the right spot only sometimes | Time-based variance. Lower the power and lengthen the time. "Change STATE_5B to 0.3 power for 4.0 seconds." |
| Shoot First now parks too | "`shouldParkTurnAndDrive()` should return false when the delay is 0." |
| Cut off by the Driver Station before it finishes | Expected — the run is over 30 s. Go to Checkpoint 8. |
| Shooting broke | Gemini touched earlier states. "Restore STATE_1 through STATE_4 exactly as they were." Or revert to the saved Checkpoint 6 files. |

Tuning table:

| BACK_UP 2 (s) | TURN (s) | TURN power | DRIVE (s) | DRIVE power | Ended in zone? |
|---|---|---|---|---|---|
| 0.45 | 0.55 | 0.5 | 3.0 | 0.4 | |
| | | | | | |

## 7.7 Save it

Commit: "Two autos: shoot first, shoot delayed + park."

## Checkpoint 7 test

- [ ] Shoot First still passes the Checkpoint 6 test
- [ ] Shoot Delayed: three runs in a row are heading into the park zone when the DS timer cuts them off (finishing inside 30 s is Checkpoint 8)
- [ ] Reader can trace the full state sequence and say which file holds the delay
- [ ] Saved

Go to [Checkpoint 8 — Under 30 seconds](08-auto-tuning.md).
