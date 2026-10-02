# Checkpoint 3 — Intake

**At the end of this checkpoint:** The same TeleOp, now with an intake: A toggles collecting, LB or X toggles reject mode, and everything stops when told.

**Time:** 15–25 minutes.

---

## 3.1 Ask for the intake, and say what not to touch

> **Prompt:**
>
> Add the intake to `MecanumTeleOp`. Don't change any of the drive code — it works.
>
> - Put the intake in its own class, `Intake`, so the OpMode stays readable. **An autonomous OpMode will use this class later too**, so put the behavior in plain methods — `setCollect(boolean on)`, `setReject(boolean on)`, `stop()` — and have `update(gamepad, isReversed)` only map buttons to those methods. Nothing inside the class should depend on a gamepad except `update`.
> - It has the motor `intake` and the two CR servos `intake_servo_left` (reversed) and `intake_servo_right`. They always run together at the same power.
> - Pressing A toggles Collect mode: 0.5 power, pulling balls in. Press again to stop. Use edge detection so holding A doesn't flicker it.
> - Pressing left bumper or X toggles Reject mode: −0.5 power. Reject overrides Collect.
> - If A is pressed while in Reject mode, Reject turns off and Collect takes over.
> - Set the intake motor to brake at zero power.
> - Add the intake power and the reject-mode state to telemetry.

The line "Don't change any of the drive code — it works" is the important one. Without it, Gemini sometimes rewrites the whole file and changes things that were fine.

The "own class" line is a choice. the mentors' code came out of the first session already split into subsystem classes (whether the coordinator asked for that or Gemini offered it isn't recorded), and it paid off: the autonomous later reused `Intake` and `Flywheel` unchanged. The guide asks for it explicitly so it happens on purpose. If your team would rather keep everything in one file for now, leave that line out.

`[SCREENSHOT: Gemini panel showing the new Intake.java and the changes to MecanumTeleOp]`

## 3.2 Read what it wrote

Two files now. In `Intake.java`, find:
1. Three `hardwareMap.get` calls — one `DcMotor`, two `CRServo`. The left servo gets `setDirection(REVERSE)`.
2. The A-button edge detection: something like `if (gamepad.a && !previousAState)`, then `previousAState = gamepad.a`.
3. An if/else that picks −0.5 (reject), 0.5 (collect), or 0.
4. A method that sets power on all three devices.

In `MecanumTeleOp.java`: an `Intake intake` field, `intake.init(hardwareMap)` in `init()`, `intake.update(...)` in `loop()`, and the LB/X toggle.

Ask the Reader: "What happens if I press A twice fast?" The edge detection is the answer. If they can explain why `previousAState` exists, they've understood the most reused pattern in FTC code.

Also check that `update()` is thin — it reads buttons and calls `setCollect` / `setReject` — and the power decision lives in a method that takes no gamepad. That's the line the autonomous will call in Checkpoint 6. The "autonomous will use this later" sentence in the prompt is what makes Gemini build it this way; leave it out and Gemini takes the shortest path to a working TeleOp, which is fine until Checkpoint 6.

**Compare with:** [`../example-code/03-intake/`](../example-code/03-intake/) — derived from the mentor robot's final `Intake.java` with feed pulsing removed.

## 3.3 Build, deploy, test

**Run ▶**. Then on the Driver Hub: `Mecanum TeleOp` → Init → ▶.

| Try | Expected | Actual |
|---|---|---|
| Press A once | Intake motor and both servos pull in, and keep going | |
| Press A again | All stop | |
| Press LB | Everything runs backward | |
| Press LB again | Stops | |
| Press LB, then A | Reject stops, collect starts | |
| Drive while collecting | Both work at once | |

Put a ball in front of the intake for the first test. "Pulls in" means the ball goes into the robot.

## 3.4 If it's wrong

| Symptom | Prompt |
|---|---|
| A pushes balls out | "The intake motor direction is backwards. Reverse `intake`." |
| Motor pulls in, one servo pushes out | "`intake_servo_right` is spinning the wrong way. Reverse it." (or left) |
| Collect flickers when holding A | "A is toggling on every loop. Use edge detection: only toggle when A goes from not pressed to pressed." |
| Intake never stops | "Pressing A a second time should turn Collect off." |
| Too weak to pull balls | Usually mechanical. Try: "Raise collect power from 0.5 to 0.7." |
| Drive stopped working | Gemini touched the drive code. "You changed the drive code. Restore it exactly as it was and only add the intake." If that fails, go back to your saved Checkpoint 2 file and re-do this checkpoint. |

## 3.5 Save it

Commit: "Intake works."

## Checkpoint 3 test

- [ ] All six intake tests pass
- [ ] Drive still passes the Checkpoint 2 tests
- [ ] Reader can explain edge detection and what happens on each button
- [ ] Saved

Go to [Checkpoint 4 — Shooter](04-shooter.md).
