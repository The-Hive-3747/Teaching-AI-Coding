# Teaching AI Coding — FTC Robot in One Week

Materials for teaching FIRST Tech Challenge teams to write their robot code by describing it in plain language to Gemini in Android Studio, then testing and refining it one subsystem at a time.

This is how The Hive (FTC #3747) built its robot-in-one-week code: a coordinator described the architecture, hardware names, and control scheme once; after that, the mechanical team refined timings, power levels, and the autonomous routine in natural language without writing Java themselves.

## What's here

| Folder | What it is | Audience |
|---|---|---|
| `slides/` | Kickoff presentation outline and speaker notes | Presenter |
| `guide/` | Step-by-step written guide, one file per checkpoint, with diagrams and screenshot callouts | Teams following along |
| `video-scripts/` | Series plan and a script for each checkpoint video | Video production |
| `prompts/` | The actual prompts that worked, including the clarifications that fixed mistakes | Teams |
| `example-code/` | Known-good TeleOp and autonomous OpModes at each checkpoint | Teams (for comparison, not copying) |

## The checkpoint sequence

Every deliverable follows the same order. Each checkpoint ends with a test on the real robot before moving on.

0. **Install** — Android Studio, the FtcRobotController project, sign in to Gemini
1. **Describe the robot** — hardware names, ports, purposes, motor directions
2. **Mecanum drive** — first TeleOp, test
3. **Intake** — add, test
4. **Shooter** — flywheel, timing between intake and flywheel, test
5. **Refine** — fix direction, timing, pulsing by giving better descriptions
6. **Autonomous: move off the wall and shoot** — first state machine, test
7. **Autonomous: park** — extend the state machine, test
8. **Autonomous: under 30 seconds** — tune timings, final test

## Status

- [ ] Real prompts added to `prompts/`
- [ ] Known-good code added to `example-code/`
- [ ] Screenshots captured (see `[SCREENSHOT: ...]` callouts in `guide/`)
- [ ] Kickoff deck built from `slides/kickoff-outline.md`
- [ ] Videos recorded

## Season note

This guide is written for the 2026–27 season robot: mecanum drive, intake, flywheel shooter, time-based autonomous (no odometry). The prompting method carries over; the hardware details will not.
