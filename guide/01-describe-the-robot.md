# Checkpoint 1 — Describe the Robot

**At the end of this checkpoint:** You have a written description of every motor and servo — name, port, purpose, direction — plus the control scheme, and Gemini has read it. Every later prompt builds on this.

**Time:** 20–30 minutes. Most of it is the team agreeing on what things are called.

---

## 1.1 Why this comes first

Gemini can only use the names you give it. If your hardware config calls the motor `front_left_drive` and you tell Gemini "the front left motor," it will invent a name, the code will compile, and the robot will crash on startup with *"Unable to find a hardware device with name..."*.

So before writing any code: get the names right, write them down, and give the whole list to Gemini once.

## 1.2 Get the names from the hardware config

On the Driver Hub: **⋮ menu → Configure Robot → (your config) → Edit**. Go through every port on the Control Hub and Expansion Hub and write down what's there.

`[SCREENSHOT: Driver Hub Configure Robot screen showing motor ports with names]`

Fill in a table like this. **Use the exact names, exact capitalization.** This is the mentor robot:

| Name in config | Type | Hub / port | What it does | Direction notes |
|---|---|---|---|---|
| `front_left_drive` | DC motor | Control Hub, motor 0 | Front-left mecanum wheel | Left side mounted mirrored — reverse |
| `front_right_drive` | DC motor | Control Hub, motor 1 | Front-right mecanum wheel | |
| `back_left_drive` | DC motor | Control Hub, motor 2 | Back-left mecanum wheel | Left side mounted mirrored — reverse |
| `back_right_drive` | DC motor | Control Hub, motor 3 | Back-right mecanum wheel | |
| `flywheel` | DC motor | Expansion Hub, motor 0 | Shooter wheel; launches balls | Reversed |
| `intake` | DC motor | Expansion Hub, motor 1 | Pulls balls in; also feeds them into the flywheel | Positive = in |
| `intake_servo_left` | CR servo | Control Hub, servo 1 | Left intake roller | Reversed so both pull in |
| `intake_servo_right` | CR servo | Expansion Hub, servo 5 | Right intake roller | |
| `led_left`, `led_right` | Servo | CH servo 3 / EH servo 3 | goBILDA status lights | (Checkpoint 9) |

> **Ben:** motor ports above are placeholders — the source doesn't say. Servo ports are from the code comments.

Two rules:
- If you don't know which way a motor spins yet, write "unknown — test." You'll find out at the next checkpoint.
- If whoever's building changes a motor's port, this table changes, the config changes, and Gemini needs to be told. Keep it current.

## 1.3 Describe the controls

Decide, as a team, what every button does. Write it down before asking for code. The mentor robot's scheme:

| Control | Action |
|---|---|
| **Gamepad 1** left stick Y | Left-side wheels forward/back (tank drive) |
| Right stick Y | Right-side wheels forward/back |
| Either stick X | Strafe (the two X values are averaged) |
| D-pad | Precision moves at 0.5 power: up/down = drive, left/right = strafe |
| A (press) | Toggle intake Collect |
| B (press) | Toggle flywheel: reverse bump → pause → run. Press again to stop |
| Right bumper (hold) | Pulse-feed balls into the running flywheel |
| Left bumper or X (press) | Toggle Reject mode: everything runs backward at −0.5 |
| **Gamepad 2** D-pad up/down | Flywheel target speed ±0.05 |

(The gamepad 2 row is where the scheme *ended up*. The first version put flywheel tuning on gamepad 1's D-pad, which collided with precision driving; Checkpoint 5 has the one-sentence prompt that moved it.)

Two things here are worth copying regardless of your robot: **toggles for things that stay on** (collect, flywheel, reject) and **hold for things that should stop the moment you let go** (feeding). Say which is which in the prompt, or Gemini will guess.

## 1.4 Describe the sequences

Anything with timing or ordering, write out as steps before prompting. The mentor robot's flywheel startup:

1. Press B.
2. Flywheel **and intake** run backward at −0.5 for a moment, to push out any ball jammed against the wheel.
3. Everything stops briefly so the flywheel isn't fighting itself.
4. Flywheel spins forward at target power and stays there.
5. Holding right bumper now pulses the intake — briefly on, longer off — so one ball feeds at a time.

You'll put numbers on those "moments" in the prompt (The mentors started with 200 ms and 300 ms; the bump was later shortened to 100 ms). Numbers you can change. Missing steps you can't.

## 1.5 Give it all to Gemini

Open the Gemini panel. Paste in a single message that has everything above. This becomes the first message in the conversation, and every later prompt refers back to it.

> **Prompt:**
>
> I'm writing code for a FIRST Tech Challenge robot using the FtcRobotController SDK (this project). All my code goes in the TeamCode module. Here's the robot.
>
> **Drive:** Mecanum, four motors named `front_left_drive`, `front_right_drive`, `back_left_drive`, `back_right_drive`. The left-side motors are mounted mirrored and need to be reversed. I want tank-style driving: left stick Y drives the left wheels, right stick Y drives the right wheels, and the X of either stick strafes. Cap all drive power at 0.8. The D-pad should do slow precision moves at 0.5 power.
>
> **Flywheel:** One motor named `flywheel`, mounted so it needs to be reversed. It launches balls.
>
> **Intake:** One motor named `intake` plus two continuous-rotation servos, `intake_servo_left` and `intake_servo_right`, that run together with it. The left servo is mounted mirrored and needs reversing. Positive power pulls balls in. The intake is also what feeds balls into the flywheel.
>
> **Controls (gamepad 1):** A toggles the intake on and off for collecting. B toggles the flywheel. Right bumper, held, feeds balls into the flywheel. Left bumper or X toggles a reject mode that runs the intake and flywheel backward.
>
> **Flywheel startup sequence, when B is pressed:** run the flywheel and intake backward at −0.5 for 200 ms to clear a jammed ball, stop everything for 300 ms, then run the flywheel forward at full power and keep it there. Pressing B again stops it.
>
> **Feeding, when right bumper is held and the flywheel is running:** pulse the intake, 100 ms on at 0.5 power, 200 ms off, repeating, so balls feed one at a time.
>
> Everything must be non-blocking: use `ElapsedTime` and state machines, never `sleep()`, so driving stays responsive during the flywheel sequence.
>
> What questions do you have?

`[SCREENSHOT: Gemini panel with the robot description sent and Gemini's confirmation reply]`

The last line matters. It's what the mentors' real first prompt ended with, and it worked: instead of generating 500 lines, Gemini came back with five numbered questions (iterative or linear OpMode? how big a tuning step? …). The coordinator's entire next message was:

> *1. iterative 2. 0.05 per step 3. Let's have them run at fixed speeds of 0.3 4. Yes.  5. Yes.*

and the TeleOp, `Flywheel.java`, and `Intake.java` appeared. Answer the questions. If Gemini restates something wrong, correct it now.

> **Note:** this is a reconstruction in the guide's checkpoint order. the mentors' real first prompt (Sep 17, 10:26 PM) is in [`docs/gemini-log-transcript.md`](../docs/gemini-log-transcript.md), row 1 — it asked for the drive, flywheel and intake in one go, with a flywheel reverse-bump and "What questions do you have?" at the end. The log clips its middle; the first and last lines are intact.

## 1.6 Save the description

Put the same text in a file so you can re-paste it later (Gemini's memory of a conversation doesn't last forever, and you'll want to start fresh conversations sometimes).

Save it as `AGENTS.md` in the project root. Android Studio's Agent mode reads a root `AGENTS.md` as standing instructions, so the description then rides along with every prompt without re-pasting. (Any file works for re-pasting by hand; `AGENTS.md` is the one Gemini picks up on its own.)

## Checkpoint 1 test

There's no robot test yet. The test is:

- [ ] Every motor and servo in the hardware config is in your table, with its exact name
- [ ] The team agrees on the control scheme, and which controls toggle vs. hold
- [ ] Every timed sequence is written out as numbered steps
- [ ] Gemini has confirmed the description and you've corrected anything it got wrong
- [ ] The description is saved in a file

All five? Go to [Checkpoint 2 — Mecanum drive](02-mecanum-drive.md).
