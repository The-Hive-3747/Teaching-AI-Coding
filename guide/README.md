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
| 2 | [02-mecanum-drive.md](02-mecanum-drive.md) | A TeleOp that drives |
| 3 | [03-intake.md](03-intake.md) | ...and runs the intake |
| 4 | [04-shooter.md](04-shooter.md) | ...and shoots |
| 5 | [05-refine.md](05-refine.md) | Everything spinning the right way at the right time |
| 6 | [06-auto-shoot.md](06-auto-shoot.md) | An autonomous that leaves the wall and shoots |
| 7 | [07-auto-park.md](07-auto-park.md) | ...and parks |
| 8 | [08-auto-tuning.md](08-auto-tuning.md) | ...in under 30 seconds |

Also: [the-loop.md](the-loop.md) — the describe → generate → test → refine method, on one page. Read it first if you only read one thing.

## Before you start

You need:
- A laptop that runs Android Studio (Windows, Mac, or Linux; 16 GB RAM recommended, 8 GB minimum)
- A Google account for Gemini in Android Studio. **Coaches:** sort this out before the session. Personal Google accounts may have an age requirement for Gemini, and most FTC students are minors, so plan on a coach-owned or school-provisioned account that the team uses on the workshop laptop. Check your school or district's policy on students using AI tools.
- Your robot's hardware configured on the Driver Hub or Robot Controller app, with a name for every motor and servo
- The robot itself, or at least the Control Hub, charged and nearby

## Rules that make this work

1. **One change per prompt.** Don't ask for the drive and the intake in the same message.
2. **Test after every change.** Every checkpoint has a test. Do it.
3. **Save what works.** After each passing test, commit in Git or copy the file somewhere. You'll want to go back.
4. **When it's wrong, the description was missing something.** Say the missing thing. Don't argue with the code; add to the description.
5. **Read the code.** One person reads the generated OpMode and explains it to the team before you move on. If nobody can explain it, ask Gemini to explain it, then read it again.
