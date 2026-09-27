# Prompts

The prompts that build the robot code, in the order they're used. Each checkpoint has:

- **The prompt** — what was typed into Gemini in Android Studio
- **What it produced** — one line on the result
- **What went wrong** — and the follow-up prompt that fixed it

The follow-up prompts are the most important part. Teams will get the first version wrong in the same ways we did (a motor spinning backwards, an intake that never stops, a flywheel that fires before it spins up). Seeing the clarification that fixed it is what makes the method teachable.

## Status: simulated

The Hive's original Gemini prompts are saved on a local machine and haven't been recovered yet. Until they are, these files hold a **simulated run**: the guide's prompts, worked through in order against FtcRobotController SDK v12.0, with the code that each one produces in `../example-code/` and the failures that are most likely on a first test. Every file says `SIMULATED` at the top. When the real prompts arrive, they replace the simulated ones and the marker comes off.

## Files

| File | Checkpoint |
|---|---|
| `01-describe-the-robot.md` | Checkpoint 1 — the one-time description of the robot: hardware, names, purposes, controls |
| `02-mecanum-drive.md` | First TeleOp |
| `03-intake.md` | Adding the intake |
| `04-shooter.md` | Adding the flywheel and the intake → flywheel timing |
| `05-refine.md` | Direction, timing, pulsing fixes |
| `06-auto-shoot.md` | Move off the wall and shoot |
| `07-auto-park.md` | Add the park |
| `08-auto-tuning.md` | Getting under 30 seconds |

## What the simulation taught us about the prompts

Working through them as Gemini would, these were the places the original drafts left something to guess. Each has been added to the guide's prompt:

| Checkpoint | Gap | Line added |
|---|---|---|
| 2 | Brake vs. coast when sticks are released | "Set the drive motors to brake when their power is zero." |
| 4 | What happens if the trigger is released *during* spin-up | "When the trigger is released — at any point, even during spin-up — ... go back to IDLE." |
| 4 | Whether bumpers work mid-shot | "While it's spinning up or feeding, ignore the bumpers." |
| 4 | Flywheel stop behavior | "Let the flywheels coast to a stop (float) rather than brake." |
| 6 | Which numbers to use after Checkpoint 5 tuning | "Use the same flywheel power, spin-up time, and pulse timing that `HiveTeleOp` uses." |
| 6 | Which motors are reversed after Checkpoint 4/5 fixes | "Exactly the same motor reversals as `HiveTeleOp` has now (check that file)." |

The pattern: every gap was a **what-happens-when** question (a release, an overlap, a carry-over from earlier work). Those are the ones to think through before prompting.

## TODO

- [ ] Replace simulated content with the real prompts from the build (Ben)
- [ ] Note which prompts came from the mechanical team vs. the coordinator
