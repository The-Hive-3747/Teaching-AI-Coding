# Checkpoint 4 — Shooter

**At the end of this checkpoint:** Holding the right trigger spins up the flywheels, waits until they're at speed, then feeds pieces in with the intake. Releasing stops everything.

**Time:** 20–40 minutes. This is the first checkpoint with timing, so expect two or three trips around the loop.

---

## 4.1 Why timing matters here

A flywheel takes time to reach speed. If you feed a piece in before it's spun up, the piece dribbles out. So the shooter isn't "turn on two motors" — it's a sequence:

```
  ┌──────────────┐
  │     IDLE     │   everything off
  └──────┬───────┘
         │  trigger pressed
         ▼
  ┌──────────────┐   flywheels on; wait until spun up
  │  SPINNING_UP │   (about 1.5 s — you'll tune this)
  └──────┬───────┘
         │  1.5 s passed
         ▼
  ┌──────────────┐   intake pulses: on 0.3 s, off 0.2 s, repeat
  │   FEEDING    │   (flywheels stay on the whole time)
  └──────┬───────┘
         │  trigger released
         ▼
       back to IDLE
```

Describe it as a sequence and Gemini will write it as one. The names in the boxes are the names we'll ask Gemini to use, so the code matches the picture.

## 4.2 Ask for it

> **Prompt:**
>
> Add the shooter to `HiveTeleOp`. Don't change the drive or intake code.
>
> - Map two motors named `flywheelLeft` and `flywheelRight`, one on each side of the launch path. For now leave both un-reversed; I'll tell you which one to reverse after testing.
> - Use an enum with the states IDLE, SPINNING_UP, and FEEDING.
> - While the right trigger is held past 0.5:
>   1. Run both flywheels at 0.8 power.
>   2. Wait 1.5 seconds for them to spin up. During this wait, don't run the intake.
>   3. After that, pulse the intake forward: 0.3 seconds on at full power, 0.2 seconds off, repeating for as long as the trigger is held.
> - When the trigger is released, stop the flywheels and the intake immediately.
> - The bumper intake controls from before should still work when the trigger isn't held.
> - Use `ElapsedTime` for the timing, not `sleep()` — the drive must keep responding while the shooter is spinning up.
> - Show the shooter state (idle / spinning up / feeding) and the flywheel power on telemetry.

`[SCREENSHOT: Gemini panel with the shooter prompt and its reply summarizing the state logic]`

The `ElapsedTime` line matters. If Gemini uses `sleep()`, the whole robot freezes during spin-up and you can't drive. If you don't know what that means, that's fine — just include the line.

## 4.3 Read what it wrote

This one's longer. Find:
1. Mapping for `flywheelLeft` and `flywheelRight`.
2. The `enum` with IDLE, SPINNING_UP, FEEDING. Ask the Reader to find where the state changes from SPINNING_UP to FEEDING. It should involve a timer check like `if (shooterTimer.seconds() > 1.5)`.
3. The pulse — a timer that flips the intake between on and off every 0.3 / 0.2 seconds.
4. The trigger release — something that sets both flywheels and the intake to zero and resets the state.

> **Ben:** link the known-good version → `../example-code/04-TeleOp-with-Shooter.java`

## 4.4 Build, deploy, test

**Safety:** Flywheels launch things. Clear the area in front of the shooter. Test with no pieces first, then with pieces.

| Try | Expected | Actual |
|---|---|---|
| Hold right trigger (no pieces) — **watch the flywheels** | Both wheels push *toward the exit*. If one pulls backward, note which. | |
| Hold right trigger (no pieces) | Flywheels spin up; about 1.5 s later, intake starts pulsing | |
| Release | Everything stops immediately | |
| Hold trigger with pieces loaded | Pieces launch, one per pulse, roughly | |
| Drive while trigger held | Drive still works during spin-up and feeding | |
| Right bumper with trigger released | Intake still works normally | |

## 4.5 If it's wrong

| Symptom | Prompt |
|---|---|
| One flywheel pulls backward / piece doesn't launch | "`flywheelRight` spins the wrong way. Reverse it." (Name whichever one you saw pulling backward. Write down which one you reversed — the autonomous needs the same setting.) |
| Pieces dribble out weakly | Spin-up too short, or power too low. "Increase the spin-up wait to 2.0 seconds." Then, if still weak: "Increase flywheel power to 1.0." One at a time. |
| Feeding starts immediately | "The intake is pulsing before the flywheels have spun up. Don't start pulsing until 1.5 seconds after the trigger is first pressed." |
| Two pieces launch together | Pulse too long. "Shorten the intake pulse from 0.3 to 0.2 seconds on. Keep the off time at 0.2." |
| Nothing feeds | Pulse too short for the intake to move a piece. "Lengthen the pulse to 0.4 seconds on." |
| Can't drive during spin-up | Gemini used `sleep()`. "You used sleep() in the shooter. Replace it with ElapsedTime so the loop keeps running." |
| Flywheels don't stop on release | "When the trigger is released, set both flywheels and the intake to zero power in the same loop." |
| Intake bumpers stopped working | "The bumper intake controls should still work when the trigger is not held. Only the trigger should override them." |

The right numbers for spin-up, power, and pulse timing depend on your robot. You'll find them by changing one number at a time. Write down what you tried:

| Spin-up (s) | Power | Pulse on / off (s) | Result |
|---|---|---|---|
| 1.5 | 0.8 | 0.3 / 0.2 | |
| | | | |

## 4.6 Save it

Commit: "Shooter works — spin-up 1.5 s, power 0.8, pulse 0.3/0.2" (put your real numbers in).

## Checkpoint 4 test

- [ ] All five shooter tests pass
- [ ] Drive and intake still pass their earlier tests
- [ ] Reader can find the state change from spinning-up to feeding and explain what the timer does
- [ ] Your tuning numbers are written down
- [ ] Saved

Go to [Checkpoint 5 — Refine](05-refine.md).
