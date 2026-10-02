# Checkpoint 4 — Shooter

**At the end of this checkpoint:** B starts the flywheel through its three-phase startup and leaves it running; holding right bumper pulse-feeds balls into it; B again stops it.

**Time:** 30–45 minutes. This is the first checkpoint with timing, so expect two or three trips around the loop.

---

## 4.1 Why timing matters here

A flywheel takes time to reach speed, and a ball pressed against a stopped flywheel jams it. So the shooter isn't "turn on a motor" — it's a sequence:

```
  ┌──────────────┐
  │     IDLE     │   flywheel off
  └──────┬───────┘
         │  B pressed
         ▼
  ┌──────────────┐   flywheel AND intake backward at -0.5
  │ REVERSE_BUMP │   for 200 ms — pushes a jammed ball off the wheel
  └──────┬───────┘
         │  200 ms passed
         ▼
  ┌──────────────┐   everything stopped
  │    PAUSE     │   for 300 ms — so the motor isn't fighting itself
  └──────┬───────┘
         │  300 ms passed
         ▼
  ┌──────────────┐   flywheel forward at target power, stays on
  │   RUNNING    │   RB held → intake pulses 100 ms on / 200 ms off
  └──────┬───────┘
         │  B pressed again
         ▼
       back to IDLE
```

Describe it as a sequence with those state names, and Gemini will write it as one — and the code will match the picture.

This sequence is The Hive's, and it arrived in three prompts on the evening of Sep 18, all verbatim. First the process, with a request for a design rather than code:

> *We removed the firewheels. They interfered with the flywheel. We now only use the intake and the flywheels.*
>
> *The process is that we keep the flywheel off. We turn on the intake and intake the balls. We then stop the intake and need to have the intake reverse for 200ms. Then we need to turn on the flywheel and have it get up to speed. Then, while the flywheel is on, we need to activate the intake again at a slower speed, which allows the balls to be sent to the flywheel slowly.*
>
> *I do not want this process fully automated, I still want the button presses. Propose to me a control scheme for this.*

Then a correction to Gemini's proposal (*"For the A button, let's not execute the 200ms reverse bump. Let's only do that with the flywheel on/off…"*), and an hour later, after driving it, the three phases as they shipped:

> *When we start the flywheel, let's have it go back at .3 power with the intake to reject any stuck balls, stop for 300ms and then go forward at power.*

"Propose to me a control scheme" is worth stealing: describe the physical process, ask for the design, then correct the design. Code comes after.

## 4.2 Ask for it

> **Prompt:**
>
> Add the flywheel to `MecanumTeleOp`. Don't change the drive or intake code.
>
> - Put it in its own class, `Flywheel`, like `Intake`, and like `Intake` the autonomous will use it later: plain methods `startSequence()` (the 3-phase startup), `stop()`, and later `startDirect()`; `update(gamepad, isReversed)` only maps B and the D-pad to them.
> - One motor named `flywheel`, reversed, brake at zero power.
> - Use an enum with the states IDLE, REVERSE_BUMP, PAUSE, RUNNING.
> - Pressing B (edge-detected) when IDLE starts the sequence: REVERSE_BUMP runs the flywheel at −0.5 for 200 ms; PAUSE stops it for 300 ms; RUNNING sets it to the target power, 1.0, and stays there. Pressing B in any other state stops the flywheel and returns to IDLE.
> - D-pad up/down adjusts the target power by ±0.05, capped at 0 and 1.
> - The intake needs to know the flywheel's state. Give `Flywheel` methods `isReversing()`, `isPausing()`, `isOn()`, and change `Intake.update` to take those three booleans:
>   - While the flywheel is in REVERSE_BUMP, the intake also runs at −0.5.
>   - While it's in PAUSE, the intake stops.
>   - Give `Intake` a method `setFeed(boolean on)`. While `setFeed(true)` and the flywheel is RUNNING, pulse the intake: 100 ms on at 0.5, 200 ms off, repeating. Reset the pulse timer when feeding first turns on. `update()` calls `setFeed(gamepad.right_bumper)`.
>   - Otherwise the intake behaves as before (collect / reject / off).
> - Reject mode (LB/X) also runs the flywheel at −0.5.
> - All timing with `ElapsedTime`, no `sleep()` — the drive must keep responding through the whole sequence.
> - Telemetry: flywheel phase, target power, intake power, whether feeding is active.

`[SCREENSHOT: Gemini panel with the shooter prompt and its reply summarizing the state logic]`

The `ElapsedTime` line matters. If Gemini uses `sleep()`, the whole robot freezes during the startup sequence and you can't drive. If you don't know what that means, that's fine — just include the line. (Gemini's own summary of The Hive's build lists this as the number one lesson.)

The D-pad tuning line reproduces a conflict from The Hive's build (the transcript's Session 6: "I thought gamepad 1 dpad was slow mode, not tuning the flywheel"). It collides with the D-pad precision drive from Checkpoint 2 — hold D-pad up to creep forward and you also nudge the flywheel target. Leave it in: fixing it is a Checkpoint 5 exercise, with the real prompt.

## 4.3 Read what it wrote

In `Flywheel.java`, find:
1. `enum StartPhase { IDLE, REVERSE_BUMP, PAUSE, RUNNING }`.
2. The B edge-detect, and the branch: if not IDLE → `stop()`, else → set phase to REVERSE_BUMP and reset the timer.
3. The phase transitions: `if (phaseTimer.seconds() >= 0.200)` → PAUSE; `>= 0.300` → RUNNING. Ask the Reader to find both.
4. The power decision: −0.5 during REVERSE_BUMP or reject, 0 during PAUSE, target during RUNNING.

In `Intake.java`: the new parameters on `update(...)`, and the pulse — a timer, `% 0.300`, and `< 0.100` picking on vs. off.

In `MecanumTeleOp.java`: `flywheel.update(...)` runs *before* `intake.update(...)`, because the intake needs the flywheel's state from this loop.

**Compare with:** [`../example-code/04-shooter/`](../example-code/04-shooter/) — The Hive's `Flywheel` and `Intake`, rolled back to before the Checkpoint 5 fixes.

## 4.4 Build, deploy, test

**Safety:** the flywheel launches balls. Clear the area in front of the shooter. Test with no balls first, then with balls.

| Try | Expected | Actual |
|---|---|---|
| Press B (no balls) — **watch the flywheel** | Brief backward twitch, brief stop, then spins up forward and stays | |
| Telemetry during that | Phase shows REVERSE_BUMP → PAUSE → RUNNING | |
| Hold RB while RUNNING | Intake pulses — you can hear it tick | |
| Release RB | Pulsing stops; flywheel keeps running | |
| Press B again | Flywheel stops | |
| Load balls, B, wait for RUNNING, hold RB | Balls launch one at a time | |
| Drive during all of the above | Drive keeps working | |
| A (collect) while flywheel is RUNNING, no RB | Collect works normally | |

## 4.5 If it's wrong

| Symptom | Prompt |
|---|---|
| Flywheel spins the wrong way in RUNNING | "`flywheel` spins the wrong way. Reverse it." (or un-reverse) |
| Balls dribble out | Target too low or not spun up. "Raise the flywheel target to 1.0." If already 1.0: mechanical, or add spool time before feeding in auto. |
| Two balls launch together | Pulse on-time too long. "Shorten the feed pulse from 100 ms on to 80 ms on. Keep 200 ms off." |
| Nothing feeds | Pulse too short for the intake to move a ball. "Lengthen the feed pulse to 150 ms on." |
| Flywheel startup spits a ball out the front | Reverse bump too long. This is the real Checkpoint 5 prompt — see there. |
| Can't drive during startup | Gemini used `sleep()`. "You used sleep() in the flywheel sequence. Replace it with ElapsedTime so loop() keeps running." |
| B doesn't stop it | "Pressing B while the flywheel is in any phase other than IDLE should stop it and return to IDLE." |
| Holding RB with the flywheel IDLE runs the intake | "Right bumper should only pulse the intake when the flywheel is RUNNING. Otherwise ignore it." |

Write down what you tried:

| Bump (ms) | Pause (ms) | Target | Pulse on / off (ms) | Result |
|---|---|---|---|---|
| 200 | 300 | 1.0 | 100 / 200 | |
| | | | | |

## 4.6 Save it

Commit: "Shooter works — bump 200 ms, target 1.0, pulse 100/200" (put your real numbers in).

## Checkpoint 4 test

- [ ] All eight shooter tests pass
- [ ] Drive and intake still pass their earlier tests
- [ ] Reader can find both phase transitions and explain why the intake needs the flywheel's state
- [ ] Your tuning numbers are written down
- [ ] Saved

Go to [Checkpoint 5 — Refine](05-refine.md).
