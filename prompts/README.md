# Prompts

The prompts that built The Hive's robot code, mapped to the guide's checkpoints.

## Two kinds of prompt in here

**Verbatim** — prompts the team actually typed, quoted exactly, typos and all. Six were first recovered from Gemini's own summary of the build ([`docs/AI_Development_Transcript_And_Guide.md`](../docs/AI_Development_Transcript_And_Guide.md)); then the plugin's backend log turned up with **all 81** of the week's prompts, timestamped, in [`docs/gemini-log-transcript.md`](../docs/gemini-log-transcript.md). The six are still the ones the guide builds its checkpoints around, and they're the most valuable thing in this repo: they show what "describe the change in plain words" looks like when a non-programmer does it.

**Reconstructed** — the guide teaches in a checkpoint order the team didn't follow (they asked for the whole TeleOp at once on night one). So the checkpoint prompts here are written to produce the code the team ended up with, one subsystem at a time, and marked `RECONSTRUCTED`. Where the real prompt for the same step exists, it's quoted alongside.

**Read the full log.** It has things the six don't: Gemini asking five clarifying questions on the first prompt and getting a one-line answer; a 15-query search spree for an LED datasheet that failed; the firewheels going in at 5:17 PM and coming out at 7:50 PM the same day; the park routine going from "strafe right 3 s" to "turn left and drive" to "It worked!" over ninety minutes; and the delay being set to 1 second for testing and back to 15 afterward.

## The six verbatim prompts

| # | Prompt | Checkpoint | What changed |
|---|---|---|---|
| 3 | *"hey, in the latest push, holding both joysticks left made the robot strafe right, and vice versa. please fix"* | 2 | Strafe signs in the mecanum mix |
| 4 | *"what is the current time of spinning the motors back before starting the flywheel? it is spitting balls out, so i would like to shorten it"* | 5 | Reverse bump 200 → 100 ms |
| 5 | *"On the delayed autonomous, we end by bumping into the wall to get park points. However, we get points for not touching the wall. Can we add a backup after the turn and drive forward? I'm thinking 100ms for 0.5 power backwards at the end should move us away."* | 8 | New `STATE_5C_PARK_BACK_OFF_WALL` |
| 6 | *"I thought gamepad 1 dpad was slow mode, not tuning the flywheel."* | 5 | Flywheel tuning moved to gamepad 2 |
| 7 | *"When I start up the flywheel, I want the process to continue to do the reverse function. But then I don't want the intake to resume. I want the intake to stop (whether it was stopped or running)."* | 5 | `isCollectOn = false` during the bump |
| 8 | *"Let's set the default speed to 0.95"* | 5 | Default flywheel target |

(Numbers are Gemini's "Session Iteration" numbers from the transcript.)

## What they have in common

- **None mention code.** No variable names, no files, no Java.
- **All name the symptom** in the words a driver would use: "spitting balls out," "strafe right," "I don't want the intake to resume."
- **Two ask before changing** ("what is the current time…"; "Can we add…"). Asking first got the team an explanation *and* the change.
- **One explains the rule and gives a starting number** ("we get points for not touching the wall… I'm thinking 100ms for 0.5 power"). Rule, ask, number: nothing left to guess.
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

- [ ] 16 long prompts are clipped in the log (listed at the end of `docs/gemini-log-transcript.md`). Paste them in from the chat panel (Ben)
