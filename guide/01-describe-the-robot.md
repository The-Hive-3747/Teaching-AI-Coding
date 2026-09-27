# Checkpoint 1 — Describe the Robot

**At the end of this checkpoint:** You have a written description of every motor and servo — name, port, purpose, direction — and Gemini has read it. Every later prompt builds on this.

**Time:** 20–30 minutes. Most of it is the team agreeing on what things are called.

---

## 1.1 Why this comes first

Gemini can only use the names you give it. If your hardware config calls the motor `frontLeft` and you tell Gemini "the front left motor," it will invent a name, the code will compile, and the robot will crash on startup with *"Unable to find a hardware device with name..."*.

So before writing any code: get the names right, write them down, and give the whole list to Gemini once.

## 1.2 Get the names from the hardware config

On the Driver Hub: **⋮ menu → Configure Robot → (your config) → Edit**. Go through every port on the Control Hub and Expansion Hub and write down what's there.

`[SCREENSHOT: Driver Hub Configure Robot screen showing motor ports with names]`

Fill in a table like this. **Use the exact names, exact capitalization.**

| Name in config | Type | Hub / port | What it does | Direction notes |
|---|---|---|---|---|
| `frontLeft` | DC motor | Control Hub, motor 0 | Front-left drive wheel | Mounted mirrored — must be reversed |
| `frontRight` | DC motor | Control Hub, motor 1 | Front-right drive wheel | |
| `backLeft` | DC motor | Control Hub, motor 2 | Back-left drive wheel | Mounted mirrored — must be reversed |
| `backRight` | DC motor | Control Hub, motor 3 | Back-right drive wheel | |
| `intake` | DC motor | Expansion Hub, motor 0 | Pulls game pieces in | Positive power = in |
| `flywheelLeft` | DC motor | Expansion Hub, motor 1 | Left shooter wheel | |
| `flywheelRight` | DC motor | Expansion Hub, motor 2 | Right shooter wheel | Unknown — test; one of the two will likely need reversing |

> **Ben:** replace this with The Hive's real config. The examples above are illustrative.

Two rules:
- If you don't know which way a motor spins yet, write "unknown — test." You'll find out at the next checkpoint.
- If the mechanical team changes a motor's port, this table changes, the config changes, and Gemini needs to be told. Keep it current.

## 1.3 Describe the controls

Decide, as a team, what every button does. Write it down before asking for code.

| Control | Action |
|---|---|
| Left stick | Drive: forward/back and strafe left/right |
| Right stick X | Rotate |
| Right bumper (hold) | Intake in |
| Left bumper (hold) | Intake out (reverse) |
| Right trigger (hold) | Spin up flywheels, then feed |
| Gamepad 2 | (unused for now) |

> **Ben:** replace with the actual control scheme.

## 1.4 Give it all to Gemini

Open the Gemini panel. Paste in a single message that has everything above. This becomes the first message in the conversation, and every later prompt refers back to it.

> **Prompt:**
>
> I'm writing code for a FIRST Tech Challenge robot using the FtcRobotController SDK. All my code goes in the TeamCode module. Here's the robot.
>
> **Drive:** Mecanum drive, four motors. Names in the hardware config: `frontLeft`, `frontRight`, `backLeft`, `backRight`. The left-side motors are mounted mirrored, so `frontLeft` and `backLeft` need to be reversed for the robot to drive forward when all four get positive power.
>
> **Intake:** One motor named `intake`. Positive power pulls game pieces into the robot.
>
> **Shooter:** Two flywheel motors named `flywheelLeft` and `flywheelRight`, one on each side of the launch path, so the game piece is pushed out between them. One of them will probably need to be reversed; I'll tell you which after testing. The intake feeds pieces into the flywheels.
>
> **Controls (gamepad 1):** Left stick moves the robot (Y = forward/back, X = strafe). Right stick X rotates. Right bumper held = intake in. Left bumper held = intake out. Right trigger held = spin up the flywheels, then feed.
>
> Don't write any code yet. Just confirm you understand the robot and tell me if anything is unclear.

`[SCREENSHOT: Gemini panel with the robot description sent and Gemini's confirmation reply]`

The last line matters. You want Gemini to read and confirm, not immediately generate 300 lines. If it asks a question, answer it. If it restates something wrong ("so the flywheels spin the same direction"), correct it now.

## 1.5 Save the description

Put the same text in a file so you can re-paste it later (Gemini's memory of a conversation doesn't last forever, and you'll want to start fresh conversations sometimes).

Save it as `TeamCode/ROBOT.md` — or anywhere in the project. Having it in the project also means Gemini can find it as context.

## Checkpoint 1 test

There's no robot test yet. The test is:

- [ ] Every motor and servo in the hardware config is in your table, with its exact name
- [ ] The team agrees on the control scheme
- [ ] Gemini has confirmed the description and you've corrected anything it got wrong
- [ ] The description is saved in a file

All four? Go to [Checkpoint 2 — Mecanum drive](02-mecanum-drive.md).
