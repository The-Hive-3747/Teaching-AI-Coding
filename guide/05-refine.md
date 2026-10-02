# Checkpoint 5 — Refine

**At the end of this checkpoint:** The TeleOp does exactly what the drivers want, and the team can turn any complaint from a driver into a one-sentence prompt.

**Time:** As long as you want. This is the checkpoint the mechanical team owns.

---

## 5.1 What this checkpoint is

Checkpoints 2–4 got the robot working. Now it needs to work *well*. That means driving practice, noticing what's annoying, and fixing it — and this is where the people who don't write code take over.

The pattern is always the same:

1. A driver says what's wrong, in plain words.
2. Someone types that into Gemini — naming the part, the symptom, and (if they know it) the change.
3. Deploy, test, keep or undo.

## 5.2 The Hive's real refinements

These are the actual prompts the mechanical team typed, in order, from Gemini's own log of the build. Read them for the *style*: short, specific about what was observed, no code words.

### "It is spitting balls out"

> *what is the current time of spinning the motors back before starting the flywheel? it is spitting balls out, so i would like to shorten it*

Gemini shortened `REVERSE_DURATION_SEC` from 200 ms to 100 ms — and, on its own initiative, also proposed lowering the reverse power. The next prompt, two minutes later, verbatim:

> *don't bump down the power, keep it the same. let's just try the shortened time.*

Three things to notice: the prompt **asks first** ("what is the current time") so the team learns what the number is before changing it; it names the symptom ("spitting balls out") rather than guessing a fix; and when Gemini changed two things, the team put one back. One change, one test.

### "I thought gamepad 1 dpad was slow mode"

> *I thought gamepad 1 dpad was slow mode, not tuning the flywheel.*

The first build put flywheel tuning on the D-pad — the same D-pad used for precision driving. Both worked at once, which is worse than either. The team had already asked to move it the night before (Sep 18, 10:10 PM: *"Let's move the up/down on the dpad for tuning the flywheel to gamepad2…"*), but the next morning the conflict was still there. This one sentence, stated as an expectation, got it fixed for good: tuning on **gamepad 2's** D-pad, gamepad 1's D-pad to driving.

The lesson isn't the sentence; it's the re-test. A change you asked for isn't a change you have until the robot shows it.

### "I don't want the intake to resume"

> *When I start up the flywheel, I want the process to continue to do the reverse function. But then I don't want the intake to resume. I want the intake to stop (whether it was stopped or running).*

Before this, if Collect was on when you pressed B, the intake reversed for the bump and then went right back to collecting — pushing the next ball into a flywheel that was still spooling. The fix was one line in `Intake.java` (`isCollectOn = false` during the bump). Notice how precisely the prompt describes the *sequence*: keep the reverse, then stop, regardless of what it was doing before.

### "Let's set the default speed to 0.95"

> *Let's set the default speed to 0.95*

The drivers had a gamepad 2 D-pad to try flywheel speeds in practice; this makes their pick the default so nobody has to tune it at the start of every match. That's the tuning loop closing: D-pad to experiment, then bake the answer into the code.

### Also from the log: the strafe fix

> *hey, in the latest push, holding both joysticks left made the robot strafe right, and vice versa. please fix*

That one's in [Checkpoint 2](02-mecanum-drive.md); it happened during the refine phase but belongs to the drive.

## 5.3 What makes these prompts work

| Prompt | Names the part | Names the symptom | Names the change |
|---|---|---|---|
| spitting balls out | "spinning the motors back before starting the flywheel" | "spitting balls out" | "shorten it" |
| dpad was slow mode | "gamepad 1 dpad" | "tuning the flywheel" (unexpected) | implied: make it slow mode only |
| intake to resume | "the intake" after "start up the flywheel" | "I don't want the intake to resume" | "stop, whether it was stopped or running" |
| default speed | "default speed" | — | "0.95" |
| strafe | "holding both joysticks left" | "strafe right, and vice versa" | "please fix" |

None of them mention a variable, a file, or a line of Java. All of them were enough.

## 5.4 A menu of common refinements

Pick what your drivers ask for. Each is one prompt, one test.

**Drive feel**
- "Lower the drive cap from 0.8 to 0.7." / "Raise it to 1.0."
- "Change the D-pad precision power from 0.5 to 0.35."
- "Cube the stick inputs for finer control at low speed."
- "Add a deadzone of 0.05 on all sticks so the robot doesn't creep when the sticks are released."

**Intake**
- "Change collect power from 0.5 to 0.7."
- "Make reject mode a hold instead of a toggle."

**Flywheel**
- "Change the reverse bump from 100 ms to 150 ms." / "…the pause from 300 ms to 200 ms."
- "Change the feed pulse to 80 ms on / 250 ms off."
- "Add a second target: gamepad 2 Y sets 0.8 for close shots, gamepad 2 A sets 0.95 for far shots."
- "Show the flywheel target on telemetry in big text so the driver can see it from the field."

**Directions**
- "Reverse motor `X`." — the simplest and most common fix.

**Gamepad 2**
- "Move A, B, RB, LB and X to gamepad 2. Gamepad 1 only drives."

## 5.5 When Gemini breaks something that worked

It will happen. Signs: the drive stops working after you asked about the flywheel; the file got much shorter; a whole section vanished.

First try:
> "You changed code outside the part I asked about. Restore the [drive/intake/flywheel] section exactly as it was, and only make the change I asked for."

If that doesn't fix it, go back to your last saved version (Git → revert, or copy the file back), and ask again with "Only change X" at the top of the prompt.

The Hive did this a different way: they asked Gemini to **make backup copies** before big changes. That's the `backup/` folder in [`example-code/final/`](../example-code/final/backup/). It works, and it's what Gemini's own summary recommends. Git does the same job with less clutter; either is fine as long as you do one of them.

## 5.6 When Gemini doesn't understand

Sometimes a prompt gets a confused answer, or Gemini changes the wrong thing. Usually it's because the prompt used a word that means something different in code than on the team. "Feed," "shoot," "fire," "launch," "pulse" — decide what your team calls things and use the same words every time. If you called it "feed" in Checkpoint 4, don't call it "fire" now.

You can also ask Gemini to explain before changing — the "spitting balls out" prompt did exactly this:
> "Before you change anything: what is the current reverse bump time, and what happens step by step when I press B?"

If the explanation doesn't match what the robot does, you've found the bug. If it doesn't match what you *want*, you've found the missing description.

## 5.7 Keep a log

Mechanical team: every time you change something, write one line:

| Date | Who | Prompt (short) | Result | Kept? |
|---|---|---|---|---|
| | | "Reverse bump 200 → 100 ms" | Stopped spitting balls | Yes |
| | | "Flywheel tuning → gamepad 2 dpad" | No more conflict with precision drive | Yes |
| | | "Collect stops after bump" | No more jams on spool-up | Yes |
| | | "Default 0.95" | Drivers' pick from practice | Yes |

This is the team's tuning history. It's also what you'll show a judge who asks how you developed the code. (Gemini can write this log for you — see [Checkpoint 9](09-extras.md).)

## 5.8 Save it

Commit after every kept change. Message = the prompt, roughly.

**Compare with:** [`../example-code/05-refine/`](../example-code/05-refine/) — The Hive's final `Flywheel.java` and `Intake.java` verbatim, and `MecanumTeleOp.java` without the LED and rumble extras.

## Checkpoint 5 test

- [ ] Drivers are happy enough to practice with it
- [ ] The tuning log has at least a few entries
- [ ] Someone who didn't write the original prompts has successfully made a change
- [ ] Saved

Go to [Checkpoint 6 — Autonomous: shoot](06-auto-shoot.md).
