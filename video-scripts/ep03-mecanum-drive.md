# Episode 3 — Mecanum Drive in One Prompt

**Length:** ~5 minutes
**Format:** Screen recording + robot footage, voice-over
**Narrator:** Ben or team programmer
**Guide page:** `guide/02-mecanum-drive.md`
**Starting state:** Checkpoint 1 complete, robot description sent and confirmed

---

## Script

**[TITLE CARD: Episode 3 — Mecanum Drive]**

This is the first code. And the first rule: ask for the drive, and only the drive. No intake, no flywheel. One thing.

**[SCREEN: Gemini panel, typing the drive prompt]**

**[TEXT CARD: the full Checkpoint 2 prompt — hold 6 s]**

Here's the prompt. Read it with me. New TeleOp called MecanumTeleOp. Iterative OpMode style — init and loop — not LinearOpMode. Only the drive. Map the four motors by the names we gave it. Reverse the two left ones, brake at zero. Tank drive: left stick Y drives the left wheels, right stick Y drives the right wheels. Strafe is the two sticks' X, averaged, times 1.1 to make up for mecanum losses. D-pad: precision moves at 0.5. And the note about the Y axis being negative when you push forward — that's a gamepad quirk that trips up every first program, so we tell it up front.

Normalize so no motor is asked for more than one, then scale everything by 0.8. Telemetry so we can see the four powers. TeleOp annotation so it shows up on the Driver Hub.

**[SCREEN: Gemini generating — 4× speed with label]**

Send it.

**[SCREEN: the generated MecanumTeleOp.java open in the editor]**

Here's what came back. You don't have to understand every line, but find four things.

**[SCREEN: highlight the hardwareMap.get lines]**

One: hardware mapping. In init. Four lines, four names in quotes — `front_left_drive`, `front_right_drive`, `back_left_drive`, `back_right_drive`. Check them against your config. These match.

**[SCREEN: highlight setDirection REVERSE lines]**

Two: reversing. `front_left_drive` and `back_left_drive` set to REVERSE. That's what we asked for.

**[SCREEN: highlight gamepad1 reads]**

Three: reading the gamepad. In loop. Left stick Y, right stick Y, both stick X's, and the four D-pad checks.

**[SCREEN: highlight setPower calls]**

Four: setting power. Four setPower calls, after the mixing and the times 0.8.

If any of those four are missing, tell Gemini which one. "I don't see where you reverse the left motors. Add it."

**[SCREEN: Run ▶, build output]**

Now build and deploy. Run.

**[Optional: SCREEN: a compile error appears]**

If you get a red compile error — and you will, sometimes — don't try to fix it yourself. Copy the whole error, paste it into Gemini, and say "This compile error came from the code you wrote. Fix it." Run again.

**[SCREEN: "Install successfully finished"]**

Installed.

**[SPLIT: Driver Hub selecting Mecanum TeleOp → Init → Play | robot on the floor]**

On the Driver Hub, Mecanum TeleOp is in the TeleOp list. Init. Play.

**[ROBOT: each test from the table, with a caption for each]**

Both sticks forward. Both back. Left stick only — it curves right. Both sticks pushed left — strafe left. Both pushed right — strafe right. D-pad up — creeps forward. D-pad left — creeps left. Release — stops.

Say out loud what actually happened, in words. Not "it's wrong." "Both sticks left made it go right."

**[Keep the take if something's wrong. If everything works, use this section from a staged take.]**

**[TEXT CARD: "What went wrong: holding both joysticks left made the robot strafe right"]**

This is what happened to The Hive. Strafe was mirrored. And here's the fix — this is the prompt the team actually typed, word for word:

**[TEXT CARD: "hey, in the latest push, holding both joysticks left made the robot strafe right, and vice versa. please fix" — hold 3 s]**

That's it. Lowercase, no code words. It names the symptom exactly — "holding both joysticks left made the robot strafe right" — says what's wanted — "please fix" — and doesn't guess at the cause. Gemini flipped the strafe signs in the four mixing lines. Deploy. Test again.

**[ROBOT: strafe now correct]**

Fixed.

> **Ben:** the guide notes this fix actually happened during the refine phase ("in the latest push"), not on the very first drive test. Staging it here is fine; say "a few days later, actually" if you'd rather be literal.

**[SCREEN: Git → Commit, message "Mecanum drive works"]**

It works, so save it. Git, Commit, "Mecanum drive works." Every time a test passes, you commit. When something breaks later — and it will — this is how you get back.

**[TEXT CARD: Checkpoint 2 checklist]**

Checkpoint two: all eight drive tests pass, someone on the team can point at the four parts of the code — mapping, reversing, gamepad, power — and say what each does, and it's saved.

**[END CARD: "Next: Episode 4 — Add the intake" + repo URL]**

---

## Shot list

- [ ] Gemini prompt + generation (4×)
- [ ] Editor with four highlighted sections
- [ ] Build/deploy
- [ ] Driver Hub + robot split for all 8 tests
- [ ] The strafe failure and the real fix prompt (real or staged)
- [ ] Git commit dialog
