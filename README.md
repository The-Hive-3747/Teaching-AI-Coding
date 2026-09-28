# Teaching AI Coding — FTC Robot in One Week

Materials for teaching FIRST Tech Challenge teams to write their robot code by describing it in plain language to Gemini in Android Studio, then testing and refining it one subsystem at a time.

This is how The Hive (FTC #3747) built its robot-in-one-week code: a coordinator described the architecture, hardware names, and control scheme once; after that, the mechanical team refined timings, power levels, and the autonomous routine in natural language without writing Java themselves. The code is in this repo verbatim, and so are six of the mechanical team's prompts.

## What's here

| Folder | What it is | Audience |
|---|---|---|
| `slides/` | Kickoff presentation outline and speaker notes | Presenter |
| `guide/` | Step-by-step written guide, one file per checkpoint, with diagrams and screenshot callouts | Teams following along |
| `video-scripts/` | Series plan and a script for each checkpoint video | Video production |
| `prompts/` | The six verbatim prompts from the build, plus reconstructed ones for each checkpoint | Teams |
| `example-code/` | The Hive's final code verbatim (`final/`), and per-checkpoint snapshots derived from it | Teams (for comparison, not copying) |
| `docs/` | Gemini's own transcript and summary of the build | Everyone |

## The checkpoint sequence

Every deliverable follows the same order. Each checkpoint ends with a test on the real robot before moving on.

0. **Install** — Android Studio, the FtcRobotController project, sign in to Gemini
1. **Describe the robot** — hardware names, ports, purposes, directions, controls, sequences
2. **Mecanum drive** — tank + strafe TeleOp, test
3. **Intake** — collect and reject, test
4. **Shooter** — flywheel startup sequence and pulsed feed, test
5. **Refine** — the mechanical team's four real prompts
6. **Autonomous: back off the wall and shoot** — first state machine, test
7. **Autonomous: park** — Shoot First and Shoot Delayed, test
8. **Autonomous: under 30 seconds** — add it up, non-contact park, final test
9. **Extras** — LEDs, endgame rumble, the 4-ball experiment

## Status

- [x] Real code in `example-code/final/`, Gemini's transcript in `docs/`
- [x] Six verbatim prompts in `prompts/` and quoted through the guide; the rest reconstructed
- [x] Per-checkpoint snapshots derived from the real code (compile-checked; not run on the robot in that form)
- [ ] Real hardware config XML (`example-code/hardware-config.xml` is reconstructed)
- [ ] Confirm what happened on the field with the 30.7 s delayed auto (see `example-code/README.md`)
- [ ] Confirm `Firewheel.java` is an abandoned experiment
- [ ] Screenshots captured (see `[SCREENSHOT: ...]` callouts in `guide/`)
- [ ] Kickoff deck built from `slides/kickoff-outline.md`
- [ ] Videos recorded

## Season note

This guide is written for The Hive's 2026–27 (BIOBUZZ) season robot, against FtcRobotController SDK v12.0: mecanum drive, one flywheel, intake with two rollers, goBILDA LEDs, time-based autonomous (no odometry). The prompting method carries over; the hardware details will not.
