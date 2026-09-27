# Checkpoint 3 — Intake

**At the end of this checkpoint:** The same TeleOp, now with an intake that runs in on one bumper, out on the other, and stops when released.

**Time:** 10–20 minutes.

---

## 3.1 Ask for the intake, and say what not to touch

> **Prompt:**
>
> Add the intake to `HiveTeleOp`. Don't change any of the drive code — it works.
>
> - Map the motor named `intake`.
> - While the right bumper is held, run the intake at full power forward (pulling pieces in).
> - While the left bumper is held, run it at full power in reverse (pushing pieces out).
> - When neither bumper is held, stop it.
> - Add the intake power to telemetry.

The line "Don't change any of the drive code — it works" is the important one. Without it, Gemini sometimes rewrites the whole file and changes things that were fine.

`[SCREENSHOT: Gemini panel showing the diff or the new intake block in the code]`

## 3.2 Read what it wrote

Find:
1. `intake = hardwareMap.get(DcMotor.class, "intake");`
2. An if/else that checks `gamepad1.right_bumper` and `gamepad1.left_bumper` and sets power to `1.0`, `-1.0`, or `0`.

Ask the Reader: "What happens if I hold both bumpers?" The answer depends on which `if` comes first. That's fine — just know the answer.

> **Ben:** link the known-good version → `../example-code/03-TeleOp-with-Intake.java`

## 3.3 Build, deploy, test

**Run ▶**. Then on the Driver Hub: `HiveTeleOp` → Init → ▶.

| Try | Expected | Actual |
|---|---|---|
| Hold right bumper | Intake pulls in | |
| Release | Intake stops | |
| Hold left bumper | Intake pushes out | |
| Release | Intake stops | |
| Drive while holding right bumper | Both work at once | |

Put a game piece in front of the intake for the first test. "Pulls in" means the piece goes into the robot.

## 3.4 If it's wrong

| Symptom | Prompt |
|---|---|
| Right bumper pushes pieces out | "The intake direction is backwards. Reverse the `intake` motor." |
| Intake runs but never stops | "When neither bumper is held, set the intake power to zero." |
| Intake is too weak to pull pieces | Usually mechanical, but try: "Run the intake at full power, 1.0." If it's already at 1.0, the problem is the mechanism or the motor choice. |
| Intake too aggressive, pieces jam | "Run the intake at 0.7 power instead of 1.0." |
| Drive stopped working | Gemini touched the drive code. "You changed the drive code. Restore it exactly as it was and only add the intake." If that fails, go back to your saved Checkpoint 2 file and re-do this checkpoint. |

## 3.5 Save it

Commit: "Intake works."

## Checkpoint 3 test

- [ ] All five intake tests pass
- [ ] Drive still passes the Checkpoint 2 tests
- [ ] Reader can explain what happens on each bumper and on release
- [ ] Saved

Go to [Checkpoint 4 — Shooter](04-shooter.md).
