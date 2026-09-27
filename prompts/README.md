# Prompts

The prompts that actually built the robot code, in the order they were used. Each checkpoint has:

- **The prompt** — what was typed into Gemini in Android Studio
- **What it produced** — one line on the result
- **What went wrong** (if anything) — and the follow-up prompt that fixed it

The follow-up prompts are the most important part. Teams will get the first version wrong in the same ways we did (a motor spinning backwards, an intake that never stops, a flywheel that fires before it spins up). Seeing the clarification that fixed it is what makes the method teachable.

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

Each file is a template. The guide has a drafted prompt for every checkpoint; replace it with the real one from the build once recovered, and keep the draft only where the real one wasn't saved.

## TODO

- [ ] Paste in the real prompts from the robot-in-one-week build (Ben)
- [ ] Note which prompts came from the mechanical team vs. the coordinator
