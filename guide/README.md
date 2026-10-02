# Guide — Writing Your Robot Code with Gemini in Android Studio

A step-by-step guide for FTC teams. Follow the checkpoints in order. Each one ends with a test on the real robot; don't move on until the test passes.

## How to use this guide

- Each checkpoint is one file. Do them in order.
- Text in a box like this is a prompt to type into the Gemini panel:

  > **Prompt:** Write a TeleOp OpMode that...

- `[SCREENSHOT: ...]` marks where a screenshot goes. Until they're added, the text describes what you'd see.
- **Test** sections tell you exactly what to try on the robot and what "working" looks like.
- **If it's wrong** sections list the most common failures and the prompt that fixes each one.

## Checkpoints

| # | File | What you'll have at the end |
|---|---|---|
| 0 | [00-install.md](00-install.md) | Android Studio open, FtcRobotController project loaded, Gemini signed in |
| 1 | [01-describe-the-robot.md](01-describe-the-robot.md) | A written description of every motor and servo that Gemini has read |
| 2 | [02-mecanum-drive.md](02-mecanum-drive.md) | A TeleOp that drives (tank + strafe, D-pad precision) |
| 3 | [03-intake.md](03-intake.md) | ...and collects / rejects balls |
| 4 | [04-shooter.md](04-shooter.md) | ...and runs the flywheel startup sequence and pulse-feeds |
| 5 | [05-refine.md](05-refine.md) | The four real refinement prompts, in the mechanical team's words — some typed by the mechanical specialist, some dictated to the coordinator (the fifth, the strafe fix, is in Checkpoint 2) |
| 6 | [06-auto-shoot.md](06-auto-shoot.md) | An autonomous that backs off the wall and shoots |
| 7 | [07-auto-park.md](07-auto-park.md) | ...split into Shoot First and Shoot Delayed, which parks |
| 8 | [08-auto-tuning.md](08-auto-tuning.md) | ...inside 30 seconds, ending clear of the wall for the LEAVE points |
| 9 | [09-extras.md](09-extras.md) | LEDs, endgame rumble, the 4-ball experiment, next season |

Also: [the-loop.md](the-loop.md) — the describe → generate → test → refine method, on one page. Read it first if you only read one thing.

The robot in this guide is The Hive's (FTC #3747) robot-in-one-week robot: mecanum drive, one flywheel, an intake with two rollers, goBILDA LEDs. The code it ended up with is in [`example-code/final/`](../example-code/final/), verbatim, and the prompts quoted in Checkpoints 2, 5 and 8 are the ones the mechanical team actually typed.

## Before you start

You need:
- A laptop that runs Android Studio (Windows, Mac, or Linux; 16 GB RAM recommended, 8 GB minimum)
- A Google account signed in to Gemini in Android Studio, confirmed working before the session. The no-cost tier has a daily usage quota per account, so one account per team.
- Your robot's hardware configured on the Driver Hub or Robot Controller app, with a name for every motor and servo
- The robot itself, or at least the Control Hub, charged and nearby

## Rules that make this work

1. **One change per prompt.** Don't ask for the drive and the intake in the same message.
2. **Test after every change.** Every checkpoint has a test. Do it.
3. **Save what works.** After each passing test, commit in Git or copy the file somewhere. You'll want to go back.
4. **When it's wrong, the description was missing something.** Say the missing thing. Don't argue with the code; add to the description.
5. **Read the code.** One person reads the generated OpMode and explains it to the team before you move on. If nobody can explain it, ask Gemini to explain it, then read it again.
