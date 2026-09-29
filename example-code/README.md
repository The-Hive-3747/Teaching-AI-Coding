# Example Code

The Hive's actual robot-in-one-week code, plus snapshots of what it looked like at each checkpoint.

These are for **comparison, not copying**. The point of the workshop is that your team describes *your* robot and Gemini writes code that matches *your* hardware config. If you paste ours in, it won't match your motor names, directions, or timings, and you'll skip the part where your team learns to describe what they want.

## `final/` — the real code, verbatim

Everything in The Hive's `TeamCode` folder at the end of the week, unchanged, including the `backup/` folder the team had Gemini create before big changes.

| File | What it is |
|---|---|
| `MecanumTeleOp.java` | The TeleOp. Tank drive + strafe, D-pad precision moves, A/B/RB/LB/X subsystem controls, gamepad 2 flywheel tuning, LED status colors, endgame rumble warnings |
| `Flywheel.java` | Flywheel subsystem: B toggles a 3-phase startup (100 ms reverse bump → 300 ms pause → run at 0.95) |
| `Intake.java` | Intake subsystem: motor + two CR servos; collect (0.5), pulsed feed (100 ms on / 200 ms off), reject (−0.5) |
| `LedController.java` | goBILDA LED servos: color = state (init, driving, collecting, spinning up, feeding, warning) |
| `BaseAuto.java` | Shared autonomous state machine: back up, settle, spool, pulse-shoot, then (delayed only) back up, turn left, drive to park, back off the wall |
| `AutoShootFirst.java` / `AutoShootDelayed.java` | The two match autos: 0 s delay (shoot and stop) and 15 s delay (shoot, then park) |
| `BaseAutoExperimental.java` + `*Experimental.java`, `ExperimentalParkShootFirst.java` | The 4-ball experiment: shoot 3, scoop a 4th from in front of the robot, shoot it |
| `Firewheel.java` | Subsystem for the original **transfer mechanism** — two CR servos that carried balls from the intake to the flywheel. The mechanism had to be ripped out mid-week for mechanical problems, and the team recoded quickly so the intake fed the flywheel directly. The class was left behind, unused |
| `backup/` | Snapshots the team had Gemini save before refactors |

## Per-checkpoint snapshots — derived

The team didn't commit after each step, so these were **reconstructed** by stripping the final code down to what each checkpoint would have had. They compile (checked with `javac` against the SDK v12.0 API surface) but haven't been run on the robot in this form.

| Folder | Checkpoint | What it has |
|---|---|---|
| `02-mecanum-drive/` | Mecanum drive | `MecanumTeleOp` — drive only. Tank + strafe, D-pad precision, 0.8 cap |
| `03-intake/` | Intake | + `Intake` class: A toggles collect, LB/X toggles reject. No feed pulsing yet |
| `04-shooter/` | Shooter | + `Flywheel` class with the 3-phase startup; `Intake` gets feed pulsing. Rolled back to a *plausible* pre-Checkpoint-5 state (200 ms bump, 1.0 target, tuning on gamepad 1's D-pad) — not recovered history. The only real intermediate artifact, `final/backup/FlywheelBackup.java`, has tuning on gamepad 2's *triggers* with a gamepad-1 fallback, so the team's actual path had at least one more step than the guide shows |
| `05-refine/` | Refine | Final `Flywheel` and `Intake` verbatim; `MecanumTeleOp` minus LEDs and rumble. All five real refinement prompts applied |
| `06-auto-shoot/` | Autonomous v1 | `AutoShootFirst` as a single OpMode: back up → settle → spool → pulse-shoot 10 s → stop |
| `07-auto-park/` | Autonomous v2 | Split into `BaseAuto` + `AutoShootFirst` (0 s) + `AutoShootDelayed` (15 s, parks). Before the back-off-wall step |
| `08-auto-tuning/` | Autonomous v3 | Final `BaseAuto` minus LEDs: back-off-wall step, 8.3 s shoot for Delayed |
| `hardware-config.xml` | — | Reconstructed config: real device names, servo ports from the source, motor ports are placeholders |

Each autonomous folder also carries the `Flywheel.java` and `Intake.java` it depends on.

**Note on the autos (06–08):** like the real code they're derived from, these drive the subsystems through a *simulated* `Gamepad` (`autoGamepad.right_bumper = true`). The guide's Checkpoint 6 asks for direct method calls instead and explains why the fake-gamepad version shipped: nobody told Gemini an autonomous was coming when it wrote the subsystem classes. Read the snapshots as "what happened," and the guide as "what to ask for."

## The numbers

| Value | Where |
|---|---|
| Drive power cap | 0.8 |
| Precision (D-pad) drive | 0.5 |
| Strafe multiplier | 1.1 (compensates for mecanum strafe loss) |
| Flywheel target | 0.95 (was 1.0) |
| Flywheel reverse bump | −0.5 for 100 ms (was 200 ms) |
| Flywheel pause after bump | 300 ms |
| Intake collect / reject | 0.5 / −0.5 |
| Feed pulse | 100 ms on at 0.5 / 200 ms off |
| Auto: back up to shooting spot | −0.3 for 0.30 s, then settle 1.0 s |
| Auto: spool before shooting | 2.0 s |
| Auto: shoot duration | 10.0 s (Shoot First), 8.3 s (Shoot Delayed) |
| Auto park (Delayed): extra back-up | −0.3 for 0.45 s |
| Auto park: turn left | ±0.5 × 0.8 for 0.55 s |
| Auto park: drive to zone | 0.4 × 0.8 for 3.0 s |
| Auto park: back off wall | −0.5 for 0.10 s |

## A timing note for Checkpoint 8

Add up Shoot Delayed: 15.0 + 0.30 + 1.0 + 2.0 + 8.3 + 0.45 + 0.55 + 3.0 + 0.10 = **30.7 s**, against a 30-second period. On the field it finished, off the wall — so the Driver Station's cutoff has a little slack. The shoot duration had already been cut from 10.0 to 8.3 s. The guide's Checkpoint 8 cuts it further, to 6.5 s, because slack isn't margin: a low battery or a slow loop eats it.

## Known quirks in the final code

Found by reading the code, not by the team on the robot — they didn't see either of the first two in practice. Left in place because `final/` is verbatim. Each is a good Checkpoint-5-style exercise.

- **`Intake.isFeeding()` is true whenever RB is held**, flywheel running or not (`Intake.java`), so the LED goes white and telemetry says feeding is ACTIVE even when nothing feeds. One-line fix: `isFeeding = gamepad.right_bumper && isFlywheelOn;`.
- **The 4-ball experiment's "reverse the intake" step doesn't reverse.** `BaseAutoExperimental` STATE_6 sets `autoGamepad.left_bumper = true`, but `Intake.update()` never reads `left_bumper` — reject is driven by the `isReversed` parameter, which the autos pass as `false`. Collect is still on from STATE_5, so the intake runs *forward* during that state. The experiment can't have worked as described; a `reverse()` method on `Intake`, or passing `autoGamepad.left_bumper` as `isReversed`, fixes it.
- **`Flywheel.startDirect()` hard-codes 0.95** separately from the default `targetPower`. Change one without the other and TeleOp and auto silently diverge. A Checkpoint 5 "change the default speed" prompt should say "in both places."
- **`MecanumTeleOp`'s header says "2Hz flashing"**; the code flashes at 1 Hz. The code is right (the rules flag >2 Hz).

- [ ] Replace `hardware-config.xml` with the real exported config
- [x] `Firewheel.java` explained (removed transfer mechanism)
- [x] Delayed auto: 30.7 s on paper, finished off the wall on the field
