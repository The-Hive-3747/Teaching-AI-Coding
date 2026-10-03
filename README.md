# Teaching AI Coding — FTC Robot in One Week

<img src="docs/images/hive-logo.svg" alt="The Hive 3747" width="320">

Materials for teaching FIRST Tech Challenge teams to write their robot code by describing it in plain language to Gemini in Android Studio, then testing and refining it one subsystem at a time.

At The Hive's robot-in-one-week, the students built and coded their robot the traditional way. The mentors entered the mentor competition with a robot of their own — and coded it entirely by describing it to Gemini in Android Studio. Ben, the coordinator, is a Java developer; he described the architecture on night one and typed no Java. After that the changes came from Asim and the mechanical mentors, who don't write Java: sometimes Asim at the keyboard himself, sometimes over the robot calling out the change while Ben typed. Timings, power levels, the park routine, a whole experimental autonomous: described in plain English. The code is in this repo verbatim, and so is every prompt from the week, with its author.

The experiment was the mentors'. The workshop is so that students can do the same. The mentor competition at the Robot in 7 Days Scrimmage is on the FIRST Robotics Utah stream, starting at 3:39:22: [youtube.com/live/E53OUghEmlg?t=13162](https://www.youtube.com/live/E53OUghEmlg?t=13162).

## What's here

| Folder | What it is | Audience |
|---|---|---|
| `slides/` | Kickoff presentation outline and speaker notes | Presenter |
| `guide/` | Step-by-step written guide, one file per checkpoint, with diagrams and screenshot callouts | Teams following along |
| `video-scripts/` | Series plan and a script for each checkpoint video | Video production |
| `prompts/` | The real prompts from the build mapped to checkpoints, plus reconstructed ones where the team didn't follow the checkpoint order | Teams |
| `example-code/` | the mentor robot's final code verbatim (`final/`), and per-checkpoint snapshots derived from it | Teams (for comparison, not copying) |
| `docs/` | `verbatim-transcript.md` (every prompt, tagged by author, with Gemini's full replies), `code-history/` (the code after each of the 50 prompts that changed it), and Gemini's own summary | Everyone |

## The checkpoint sequence

Every deliverable follows the same order. Each checkpoint ends with a test on the real robot before moving on.

0. **Install** — Android Studio, the FtcRobotController project, sign in to Gemini
1. **Describe the robot** — hardware names, ports, purposes, directions, controls, sequences
2. **Mecanum drive** — tank + strafe TeleOp, test
3. **Intake** — collect and reject, test
4. **Shooter** — flywheel startup sequence and pulsed feed, test
5. **Refine** — the mechanical mentor's four real prompts
6. **Autonomous: back off the wall and shoot** — first state machine, test
7. **Autonomous: park** — Shoot First and Shoot Delayed, test
8. **Autonomous: under 30 seconds** — add it up, keep the LEAVE points, final test
9. **Extras** — LEDs, endgame rumble, the 4-ball experiment

## Status

- [x] Real code in `example-code/final/`, Gemini's transcript in `docs/`
- [x] Every prompt from the week recovered verbatim (`docs/verbatim-transcript.md`); the code after each one replayed and validated (`docs/code-history/`)
- [x] Per-checkpoint snapshots derived from the real code (compile-checked; not run on the robot in that form)
- [ ] Real hardware config XML (`example-code/hardware-config.xml` is reconstructed)
- [x] Delayed auto: 30.7 s on paper, finished off the wall on the field
- [x] `Firewheel.java` explained: transfer mechanism, in and out on Sep 18
- [x] Every prompt attributed (Ben / Asim at the keyboard / Asim's request typed by Ben)
- [x] Robot and match photos in `docs/images/`; drive-test video and two scrimmage clips (Test Match 3 auto and TeleOp) in `docs/video/`
- [x] Android Studio screenshots of two real prompts (tank drive; "spitting balls out") in `docs/images/`
- [ ] Remaining screenshots (install pop-ups, Driver Hub, generated code) — see `[SCREENSHOT: ...]` callouts in `guide/`
- [x] Kickoff deck: `slides/kickoff.pptx` (import into Google Slides), built by `slides/build-deck.js` from `slides/kickoff-outline.md` — The Hive's slide 5 column still to fill in
- [x] "Why this matters" section for skeptical coaches (5 slides) — every quote and number sourced in `docs/research-ai-skills-case.md`; two refuter passes
- [x] `guide/receipts.md` — how a team proves its students wrote the code (prompt-per-commit, session export, author tags)
***REMOVED***
- [ ] Training sessions: dates, place, sign-up (`[THE HIVE: …]` on the "What you need" slide)
- [ ] Videos recorded

## Credits

- **The Hive's students** — hand-coded the team's own robot this season, and present this workshop: Tom, Sophi and Sadiqah on stage and narrating the videos.
- **Ben** — coordinator; described the mentor robot's architecture to Gemini on night one and typed no Java.
- **Annie** — head coach.
- **Asim** — mechanical mentor; built the mentor robot and authored most of the later changes in plain English, at the keyboard for eight of them.
- **Royd** — `[Ben: Royd's role]`
- **Claude** (Anthropic) — drafted the guide, deck, scripts and research from the real transcript; every claim was then refuter-checked against sources.
- **Gemini in Android Studio** (Google) — wrote every line of the mentor robot's code from the mentors' descriptions, and its own summary of the week.

This workshop was itself made the way it teaches: described, generated, tested, refined — with the prompts kept.

## Season note

This guide is written for the mentor robot from The Hive's 2026–27 (BIOBUZZ) robot-in-one-week, against FtcRobotController SDK v12.0: mecanum drive, one flywheel, intake with two rollers, goBILDA LEDs, time-based autonomous (no odometry). The prompting method carries over; the hardware details will not.
