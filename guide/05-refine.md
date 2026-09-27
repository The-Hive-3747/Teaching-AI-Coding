# Checkpoint 5 — Refine

**At the end of this checkpoint:** The TeleOp does exactly what the drivers want, and the team can turn any complaint from a driver into a one-sentence prompt.

**Time:** As long as you want. This is the checkpoint the mechanical team owns.

---

## 5.1 What this checkpoint is

Checkpoints 2–4 got the robot working. Now it needs to work *well*. That means driving practice, noticing what's annoying, and fixing it — and this is where the people who don't write code take over.

The pattern is always the same:

1. A driver says what's wrong, in plain words.
2. Someone turns that into a prompt that names the part, the symptom, and the change.
3. Deploy, test, keep or undo.

## 5.2 Turning complaints into prompts

The skill is being specific. Compare:

| Driver says | Weak prompt | Strong prompt |
|---|---|---|
| "It's too twitchy" | "Make it less twitchy" | "The drive is too sensitive at small stick movements. Cube the joystick inputs so the first 30% of stick travel gives finer control." |
| "The intake keeps spitting pieces back out" | "Fix the intake" | "When the intake is pulling in, pieces bounce back out. Reduce intake power to 0.7 and see if that helps." |
| "I can't drive while it's shooting" | "Let me drive while shooting" | "During the shooter's spin-up, the drive doesn't respond. Make sure the drive code runs every loop regardless of shooter state." |
| "It shoots two at once" | "Only shoot one" | "Two pieces launch on one pulse. Shorten the intake pulse from 0.3 to 0.2 seconds." |
| "Turning is too fast" | "Slow the turning" | "Scale the rotation input by 0.7 so turning is slower than driving." |
| "Sometimes it shoots weak" | "Make it shoot harder" | "The first shot after spin-up is weak but later ones are fine. Increase the spin-up wait from 1.5 to 2.0 seconds." |

What makes the strong prompts strong:
- **Names the part** (drive, intake, rotation)
- **Names the symptom** exactly as observed
- **Names the change**, with a number when there is one
- **Changes one thing**

## 5.3 A menu of common refinements

Pick what your drivers ask for. Each is one prompt, one test.

**Drive feel**
- "Add a slow mode: while the left trigger is held, scale all drive powers by 0.4."
- "Cube the stick inputs for finer control at low speed."
- "Scale rotation by 0.7 so it's slower than straight-line driving."
- "Add a deadzone of 0.05 on all sticks so the robot doesn't creep when the sticks are released."

**Intake**
- "Reverse the intake direction." (if it's still wrong)
- "Change intake power to 0.7."
- "Make the intake toggle on with one press of the right bumper and off with another press, instead of hold."

**Shooter**
- "Change the spin-up wait to X seconds."
- "Change flywheel power to X."
- "Change the pulse to X on / Y off."
- "Add a second shooter speed: right trigger = far shot at 1.0 power, A button = close shot at 0.6 power. Same spin-up and feed logic for both."
- "Keep the flywheels spinning at 0.3 power all the time so spin-up is faster."

**Directions**
- "Reverse motor `X`." — the simplest and most common fix.

**Telemetry**
- "Show the current shooter state and the time since the trigger was pressed on telemetry."

**Gamepad 2**
- "Move the intake and shooter controls to gamepad 2. Gamepad 1 only drives."

## 5.4 When Gemini breaks something that worked

It will happen. Signs: the drive stops working after you asked about the shooter; the file got much shorter; a whole section vanished.

First try:
> "You changed code outside the part I asked about. Restore the [drive/intake/shooter] section exactly as it was, and only make the change I asked for."

If that doesn't fix it, go back to your last saved version (Git → revert, or copy the file back), and ask again with "Only change X" at the top of the prompt.

This is why Rule 3 says save after every passing test.

## 5.5 When Gemini doesn't understand

Sometimes a prompt gets a confused answer, or Gemini changes the wrong thing. Usually it's because the prompt used a word that means something different in code than on the team. "Feed," "shoot," "fire," "launch," "pulse" — decide what your team calls things and use the same words every time. If you called it "feed" in Checkpoint 4, don't call it "fire" now.

You can also ask Gemini to explain before changing:
> "Before you change anything: explain in plain English what happens when the right trigger is pressed, step by step."

If the explanation doesn't match what the robot does, you've found the bug. If it doesn't match what you *want*, you've found the missing description.

## 5.6 Keep a log

Mechanical team: every time you change something, write one line:

| Date | Who | Prompt (short) | Result | Kept? |
|---|---|---|---|---|
| | | "Add slow mode on left trigger, 0.4×" | Drivers like it for lining up | Yes |
| | | "Intake power 1.0 → 0.7" | Still bounces out | No, reverted |
| | | "Rotation scaled by 0.7" | Turning controllable now | Yes |

This is the team's tuning history. It's also what you'll show a judge who asks how you developed the code.

## 5.7 Save it

Commit after every kept change. Message = the prompt, roughly.

**Compare with:** [`../example-code/05-refine/HiveTeleOp.java`](../example-code/05-refine/HiveTeleOp.java) — a reference version written against the same prompt (simulated until the real one replaces it).

## Checkpoint 5 test

- [ ] Drivers are happy enough to practice with it
- [ ] The tuning log has at least a few entries
- [ ] Someone who didn't write the original prompts has successfully made a change
- [ ] Saved

Go to [Checkpoint 6 — Autonomous: shoot](06-auto-shoot.md).
