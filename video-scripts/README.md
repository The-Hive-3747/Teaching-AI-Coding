# Video Series Plan

Nine short videos, one per checkpoint, plus a 2-minute intro. Each video follows its guide page exactly, so a viewer can pause the video and read the same step in the guide.

## Format

- **Length:** 3–6 minutes each. Install is the exception (8–10 min, mostly time-lapsed).
- **Style:** Screen recording of Android Studio with a picture-in-picture of the robot when it's being tested. Narrator voice-over. No talking-head sections except the intro.
- **Narrator:** One person for consistency, with Asim narrating the checkpoints he drove (Refine and the autonomous episodes) if he's willing — a mentor who doesn't write Java is the strongest demonstration of the point.
- **On screen:** Every prompt typed is shown in full, and paused on for 3 seconds so viewers can read it. Every test is shown on the real robot.
- **Captions:** Burned-in captions for the prompts. Auto-captions for the narration.

## Episode list

| # | Title | Length | Guide page | Narrator |
|---|---|---|---|---|
| 0 | Why we're doing this | 2 min | — | Ben (on camera) |
| 1 | Install Android Studio and FtcRobotController | 8–10 min | 00-install.md | Ben |
| 2 | Describe your robot | 4 min | 01-describe-the-robot.md | Ben |
| 3 | Mecanum drive in one prompt | 5 min | 02-mecanum-drive.md | Ben or programmer |
| 4 | Add the intake | 3 min | 03-intake.md | Student |
| 5 | Add the shooter | 6 min | 04-shooter.md | Student |
| 6 | Refine: the mechanical mentor's real prompts | 5 min | 05-refine.md | Asim (mechanical mentor), or Ben |
| 7 | Autonomous: back off the wall and shoot | 6 min | 06-auto-shoot.md | Asim (or Episode 6 narrator) |
| 8 | Autonomous: Shoot First, Shoot Delayed, park | 4 min | 07-auto-park.md | Asim (or Episode 6 narrator) |
| 9 | Under 30 seconds and match-ready | 4 min | 08-auto-tuning.md | Asim (or Episode 6 narrator) |

Checkpoint 9 (extras: LEDs, endgame rumble, the 4-ball experiment) has no episode of its own; Episode 9 points to it in its close.

## Recording checklist (every episode)

- [ ] Android Studio at a readable zoom (**View → Appearance → Zoom IDE**). Editor font 16+ (**Settings → Editor → Font**).
- [ ] Gemini panel docked on the right, wide enough that prompts don't wrap awkwardly.
- [ ] Robot on a table or floor, camera on a tripod, good light.
- [ ] Battery charged. Note the voltage.
- [ ] Driver Hub screen recorded or filmed for the test sections.
- [ ] Start every episode from the previous checkpoint's commit (Git → checkout that commit, or keep a branch per checkpoint while recording).
- [ ] Record the Gemini generation in real time; speed up in editing to 4× with a "sped up" label.

## Editing conventions

- Lower-third title at the start: episode number and name.
- When a prompt is typed: cut to a full-screen text card of the prompt, hold 3 s, then back to the recording.
- When a test runs: split screen, Driver Hub telemetry on one side, robot on the other.
- When something goes wrong (and it will — keep those takes): red "What went wrong" card, then the fix prompt as a text card.
- End card: "Next: [episode name]" and the repo URL.

## Series-wide assets

- [ ] Intro/outro bumper with The Hive logo (5 s)
- [ ] Text-card template for prompts
- [ ] "Sped up 4×" label
- [ ] "What went wrong" card
- [ ] End card with repo URL
