# Example Code

Reference OpModes, one folder per checkpoint. Each folder holds the file as it should look **at the end of that checkpoint**, so you can compare yours after each test.

These are for **comparison, not copying**. The point of the workshop is that your team describes *your* robot and Gemini writes code that matches *your* hardware config. If you paste ours in, it won't match your motor names, directions, or timings, and you'll skip the part where your team learns to describe what they want.

## Folders

| Folder | Checkpoint | What it should do |
|---|---|---|
| `02-mecanum-drive/HiveTeleOp.java` | Mecanum drive | Drives in all directions from the left stick, rotates from the right |
| `03-intake/HiveTeleOp.java` | Intake | ...plus intake in/out on the bumpers |
| `04-shooter/HiveTeleOp.java` | Shooter | ...plus flywheel spin-up and feed on the right trigger (IDLE → SPINNING_UP → FEEDING) |
| `05-refine/HiveTeleOp.java` | Refine | `flywheelRight` reversed, spin-up 2.0 s, cubed inputs, rotation × 0.7, slow mode on left trigger, deadzone |
| `06-auto-shoot/HiveAutoShoot.java` | Autonomous v1 | LEAVE_WALL → SPIN_UP → SHOOT → DONE |
| `07-auto-park/HiveAutoShoot.java` | Autonomous v2 | ...→ TURN_TO_PARK → DRIVE_TO_PARK → DONE |
| `08-auto-tuning/HiveAutoShoot.java` | Autonomous v3 | ...plus a 28-second safety timeout |
| `hardware-config.xml` | — | Example Robot Controller config these OpModes expect (names are what matter) |

## Where these came from

**Status: simulated, not yet from the real robot.**

These were written by working through the guide's prompts in order, as Gemini would, against the FtcRobotController SDK v12.0 (2026–27 season) and the conventions in FIRST's own sample OpModes (`BasicOmniOpMode_Linear` and friends). Each file was compiled with `javac` against the SDK's API surface to catch syntax and API mistakes. They have **not** been built with Gradle or run on a robot yet.

Motor names, the park path (turn right ~90°, drive ~4 ft), and every number are placeholders that match the guide. When The Hive's real OpModes are recovered they replace these.

- [ ] Replace with the real OpModes from the build (Ben)
- [ ] Replace `hardware-config.xml` with the real config
- [ ] Build once in Android Studio and deploy to confirm

## Tuning values used

| Value | TeleOp (05) | Auto (08) |
|---|---|---|
| Flywheel power | 0.8 | 0.8 |
| Spin-up | 2.0 s | 2.0 s |
| Intake pulse on / off | 0.3 / 0.2 s | 0.3 / 0.2 s |
| Shots | as long as trigger held | 3 pulses |
| LEAVE_WALL | — | 0.5 power, 0.9 s |
| TURN_TO_PARK | — | ±0.5 power, 0.6 s (clockwise) |
| DRIVE_TO_PARK | — | 0.5 power, 2.0 s |
| Safety timeout | — | 28 s |
