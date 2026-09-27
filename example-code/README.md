# Example Code

Known-good OpModes from The Hive's robot-in-one-week build, one snapshot per checkpoint.

These are for **comparison, not copying**. The point of the workshop is that your team describes *your* robot and Gemini writes code that matches *your* hardware config. If you paste ours in, it won't match your motor names, directions, or timings, and you'll skip the part where your team learns to describe what they want.

Use these to answer "does mine look roughly right?" after each checkpoint.

The filenames are numbered for reading order. Java requires the filename to match the class name, so these won't compile if dropped into `TeamCode` as-is — another reason they're for reading, not copying.

## Files

| File | Checkpoint | What it should do |
|---|---|---|
| `02-MecanumTeleOp.java` | Mecanum drive | Drives in all directions from one joystick, rotates from the other |
| `03-TeleOp-with-Intake.java` | Intake | Same, plus intake in/out on buttons |
| `04-TeleOp-with-Shooter.java` | Shooter | Same, plus flywheel spin-up and feed sequence |
| `05-TeleOp-refined.java` | Refine | Directions, timing, and pulsing corrected |
| `06-Auto-Shoot.java` | Autonomous v1 | Move off the wall, spin up, shoot |
| `07-Auto-Shoot-Park.java` | Autonomous v2 | Same, then drive to park |
| `08-Auto-Final.java` | Autonomous v3 | Tuned to finish under 30 seconds |
| `hardware-config.xml` | — | The Robot Controller config these OpModes expect |

## TODO

- [ ] Add the real OpModes from the build (Ben)
- [ ] Add the hardware config XML
- [ ] Note the FtcRobotController SDK version they were built against
