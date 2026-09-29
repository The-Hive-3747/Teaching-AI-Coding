# Prompts

The prompts that built The Hive's robot code, mapped to the guide's checkpoints.

## Two kinds of prompt in here

**Verbatim** — six prompts the mechanical team actually typed, recovered from Gemini's own summary of the build ([`docs/AI_Development_Transcript_And_Guide.md`](../docs/AI_Development_Transcript_And_Guide.md)). These are marked `VERBATIM` and quoted exactly, typos and all. They are the most valuable thing in this repo: they show what "describe the change in plain words" looks like when a non-programmer does it.

**Reconstructed** — the first two sessions (the initial build and the first autonomous) weren't saved as prompts; the transcript only summarizes the developer's intent. For those, and for the checkpoint-by-checkpoint prompts the guide uses, the prompts here are written to produce the code the team ended up with. Marked `RECONSTRUCTED`.

## The six verbatim prompts

| # | Prompt | Checkpoint | What changed |
|---|---|---|---|
| 3 | *"hey, in the latest push, holding both joysticks left made the robot strafe right, and vice versa. please fix"* | 2 | Strafe signs in the mecanum mix |
| 4 | *"what is the current time of spinning the motors back before starting the flywheel? it is spitting balls out, so i would like to shorten it"* | 5 | Reverse bump 200 → 100 ms |
| 5 | *"On the delayed autonomous, we end by bumping into the wall to get park points. However, we get points for not touching the wall. Can we add a backup after the turn and drive forward?"* | 8 | New `STATE_5C_PARK_BACK_OFF_WALL` |
| 6 | *"I thought gamepad 1 dpad was slow mode, not tuning the flywheel."* | 5 | Flywheel tuning moved to gamepad 2 |
| 7 | *"When I start up the flywheel, I want the process to continue to do the reverse function. But then I don't want the intake to resume. I want the intake to stop (whether it was stopped or running)."* | 5 | `isCollectOn = false` during the bump |
| 8 | *"Let's set the default speed to 0.95"* | 5 | Default flywheel target |

(Numbers are Gemini's "Session Iteration" numbers from the transcript.)

## What they have in common

- **None mention code.** No variable names, no files, no Java.
- **All name the symptom** in the words a driver would use: "spitting balls out," "strafe right," "I don't want the intake to resume."
- **Two ask before changing** ("what is the current time…"; "Can we add…"). Asking first got the team an explanation *and* the change.
- **One explains the rule** ("we get points for not touching the wall"). That's why Gemini knew "a backup" meant 100 ms, not a return to the shooting spot.
- **One states an expectation** ("I thought gamepad 1 dpad was slow mode"). Stating what you expected is a complete bug report.

## Files

| File | Checkpoint |
|---|---|
| `01-describe-the-robot.md` | Checkpoint 1 — the one-time description of the robot: hardware, names, purposes, controls, sequences |
| `02-mecanum-drive.md` | First TeleOp; the strafe fix |
| `03-intake.md` | Adding the intake |
| `04-shooter.md` | Adding the flywheel startup sequence and pulse feed |
| `05-refine.md` | The four refinement prompts |
| `06-auto-shoot.md` | Back off the wall and shoot |
| `07-auto-park.md` | Two autos, delayed one parks |
| `08-auto-tuning.md` | Under 30 seconds; the back-off-the-wall prompt |

## TODO

- [ ] If the original Session 1 and Session 2 prompts turn up in Gemini's history, replace the reconstructions (Ben)
