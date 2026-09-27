# Episode 3 — Mecanum Drive in One Prompt

**Length:** ~5 minutes
**Format:** Screen recording + robot footage, voice-over
**Narrator:** Ben or team programmer
**Guide page:** `guide/02-mecanum-drive.md`
**Starting state:** Checkpoint 1 complete, robot description sent and confirmed

---

## Script

**[TITLE CARD: Episode 3 — Mecanum Drive]**

This is the first code. And the first rule: ask for the drive, and only the drive. No intake, no shooter. One thing.

**[SCREEN: Gemini panel, typing the drive prompt]**

**[TEXT CARD: the full Checkpoint 2 prompt — hold 6 s]**

Here's the prompt. Read it with me. New TeleOp called HiveTeleOp. Only the drive. Map the four motors by the names we gave it. Reverse the left two. Left stick moves, right stick rotates. And the parenthetical about the Y axis being negative — that's a gamepad quirk that trips up every first program, so we tell it up front.

Scale the powers so nothing exceeds one. Telemetry so we can see the numbers. TeleOp annotation so it shows up on the Driver Hub.

**[SCREEN: Gemini generating — 4× speed with label]**

Send it.

**[SCREEN: the generated HiveTeleOp.java open in the editor]**

Here's what came back. You don't have to understand every line, but find four things.

**[SCREEN: highlight the hardwareMap.get lines]**

One: hardware mapping. Four lines, four names in quotes. Check them against your config. These match.

**[SCREEN: highlight setDirection REVERSE lines]**

Two: reversing. frontLeft and backLeft set to REVERSE. That's what we asked for.

**[SCREEN: highlight gamepad1 reads]**

Three: reading the gamepad. Left stick Y, left stick X, right stick X.

**[SCREEN: highlight setPower calls]**

Four: setting power. Four motors, four setPower calls, with the mecanum math above them.

If any of those four are missing, tell Gemini which one. "I don't see where you reverse the left motors. Add it."

**[SCREEN: Run ▶, build output]**

Now build and deploy. Run.

**[Optional: SCREEN: a compile error appears]**

If you get a red compile error — and you will, sometimes — don't try to fix it yourself. Copy the whole error, paste it into Gemini, and say "This compile error came from the code you wrote. Fix it." Run again.

**[SCREEN: "Install successfully finished"]**

Installed.

**[SPLIT: Driver Hub selecting HiveTeleOp → Init → Play | robot on the floor]**

On the Driver Hub, HiveTeleOp is in the TeleOp list. Init. Play.

**[ROBOT: each test from the table, with a caption for each]**

Forward. Back. Strafe left. Strafe right. Rotate left. Rotate right. Release — stops.

Say out loud what actually happened, in words. Not "it's wrong." "Strafe left goes right."

**[Keep the take if something's wrong. If everything works, use this section from a staged take or the Ben-fill-in below.]**

**[TEXT CARD: "What went wrong: Strafe left moved the robot right"]**

On our first run, strafe was mirrored. Here's the fix:

**[TEXT CARD: "Strafe left moves the robot right. Flip the sign on the strafe term." — hold 3 s]**

One sentence. Name the symptom, name the change. Deploy. Test again.

**[ROBOT: strafe now correct]**

Fixed.

> **Ben:** substitute whatever actually went wrong on The Hive's first drive test. If it was direction, use the direction fix. If nothing went wrong, keep this section anyway with the most common failure — viewers need to see the refine step once.

**[SCREEN: Git → Commit, message "Mecanum drive works"]**

It works, so save it. Git, Commit, "Mecanum drive works." Every time a test passes, you commit. When something breaks later — and it will — this is how you get back.

**[TEXT CARD: Checkpoint 2 checklist]**

Checkpoint two: all seven drive tests pass, someone on the team can point at the four parts of the code, and it's saved.

**[END CARD: "Next: Episode 4 — Add the intake" + repo URL]**

---

## Shot list

- [ ] Gemini prompt + generation (4×)
- [ ] Editor with four highlighted sections
- [ ] Build/deploy
- [ ] Driver Hub + robot split for all 7 tests
- [ ] The failure and fix (real or staged)
- [ ] Git commit dialog
