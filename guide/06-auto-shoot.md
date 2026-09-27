# Checkpoint 6 — Autonomous: Move Off the Wall and Shoot

**At the end of this checkpoint:** An autonomous OpMode that drives forward off the wall, spins up, shoots the preloaded pieces, and stops. No parking yet.

**Time:** 30–45 minutes.

---

## 6.1 Autonomous without sensors

You don't need odometry or encoders to score in autonomous. Time-based driving ("go forward at 0.5 power for 0.9 seconds") is repeatable enough for a first week, as long as the battery is charged and the wheels are clean.

The autonomous is a **state machine**: a list of steps, each with a condition for moving to the next. For this checkpoint:

```
  START
    │
    ▼
┌──────────────┐  all four drive motors forward at 0.5 power
│  LEAVE_WALL  │  for 0.9 s
└──────┬───────┘
       ▼
┌──────────────┐  drive motors stop; flywheels on at 0.8
│   SPIN_UP    │  wait 1.5 s
└──────┬───────┘
       ▼
┌──────────────┐  intake pulses 0.3 s on / 0.2 s off
│    SHOOT     │  for 3 pulses (1.5 s)
└──────┬───────┘
       ▼
┌──────────────┐  everything off
│     DONE     │
└──────────────┘
```

Each box is a state. Each arrow is "when the timer passes N seconds, go to the next state." That's all a state machine is.

## 6.2 Ask for it

Start a **new** Gemini conversation for the autonomous, and paste your robot description from Checkpoint 1 first. Then:

> **Prompt:**
>
> Create a new Autonomous OpMode in TeamCode called `HiveAutoShoot`. Use the `@Autonomous` annotation. It uses the same hardware as `HiveTeleOp` — same motor names and exactly the same motor reversals as `HiveTeleOp` has now (check that file). Use the same flywheel power, spin-up time, and pulse timing that `HiveTeleOp` uses.
>
> Implement it as a state machine using an enum and `ElapsedTime`. The states are:
>
> 1. `LEAVE_WALL` — the robot starts with its back against the wall, facing the field. All four drive motors forward at 0.5 power for 0.9 seconds. Then stop the drive motors.
> 2. `SPIN_UP` — flywheels on. Wait for the spin-up time. Intake off.
> 3. `SHOOT` — keep flywheels on. Pulse the intake with the same on/off timing as TeleOp. Do this 3 times.
> 4. `DONE` — everything off. Stay here.
>
> Reset the timer every time the state changes. Show the current state and the timer on telemetry. Don't use `sleep()` — use the timer so telemetry keeps updating.
>
> Start with these exact numbers; I'll tune them after testing.

"Same as TeleOp" is deliberate: you tuned those numbers in Checkpoints 4–5, and telling Gemini to read them from `HiveTeleOp` means you can't forget to carry one over. The "starts with its back against the wall" line is there because "forward" only means something once Gemini knows which way the robot is facing.

`[SCREENSHOT: Gemini panel showing the generated state machine with the enum and switch visible]`

## 6.3 Read what it wrote

Find:
1. `enum State { LEAVE_WALL, SPIN_UP, SHOOT, DONE }` (or similar).
2. A `switch (state)` inside the loop, with a `case` for each state.
3. In each case, a check like `if (timer.seconds() > 0.9) { state = State.SPIN_UP; timer.reset(); }`.
4. A pulse counter in `SHOOT`.

Ask the Reader: "How does the robot get from LEAVE_WALL to SPIN_UP?" The answer is the timer check. If they can say that, they understand state machines.

**Compare with:** [`../example-code/06-auto-shoot/HiveAutoShoot.java`](../example-code/06-auto-shoot/HiveAutoShoot.java) — a reference version written against the same prompt (simulated until the real one replaces it).

## 6.4 Test

**Setup:** Robot against the wall in its starting position, pieces preloaded, field clear.

On the Driver Hub: `HiveAutoShoot` from the Autonomous list → Init → ▶.

Watch and write down:

| Step | Expected | Actual |
|---|---|---|
| Leaves the wall | Drives forward roughly the right distance, stops | |
| Spins up | Flywheels audibly at speed before feeding starts | |
| Shoots | Pieces launch, one per pulse | |
| Done | Everything stops | |
| Total time | Under 10 seconds | |

Run it **three times**. Time-based auto varies; you want to see the variation.

## 6.5 If it's wrong

| Symptom | Prompt |
|---|---|
| Drives backward off the wall | "LEAVE_WALL drives backward. Negate the drive power (or reverse it) so the robot moves away from the wall." |
| Drives too far / not far enough | "Change LEAVE_WALL duration from 0.9 to 0.7 seconds." (or up) One change per run. |
| Drifts sideways while leaving | Mecanum wheels aren't all equal. "Add a small correction: run the right-side motors at 0.55 and the left at 0.5." Tune. |
| Shoots weak | Same as Checkpoint 4: longer spin-up or more power. |
| Pieces don't feed | "Lengthen the SHOOT pulse to 0.4 s on." |
| Only 2 of 3 pieces launch | "Add a fourth pulse to SHOOT." or "Add a 0.5 s pause before the first pulse." |
| Never reaches DONE / keeps pulsing | Pulse counter bug. "SHOOT never exits. After 3 complete pulses, move to DONE." |
| Doesn't move at all | Check the state actually changes on telemetry. If it's stuck in LEAVE_WALL with the timer counting, the drive motors aren't getting power. "In LEAVE_WALL, I don't see the motors getting power. Show me where setPower is called." |

Keep the tuning table:

| LEAVE_WALL (s) | Power | SPIN_UP (s) | Pulses | Result |
|---|---|---|---|---|
| 0.9 | 0.5 | 1.5 | 3 | |
| | | | | |

## 6.6 Save it

Commit: "Auto shoots from the wall."

## Checkpoint 6 test

- [ ] Three runs in a row: leaves the wall, spins up, launches all preloaded pieces, stops
- [ ] Reader can explain how the state machine moves between states
- [ ] Tuning numbers written down
- [ ] Saved

Go to [Checkpoint 7 — Autonomous: park](07-auto-park.md).
