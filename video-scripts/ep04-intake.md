# Episode 4 — Add the Intake

**Length:** ~3 minutes
**Format:** Screen recording + robot footage, voice-over
**Narrator:** A student (not the original programmer, if possible)
**Guide page:** `guide/03-intake.md`
**Starting state:** Checkpoint 2 committed

---

## Script

**[TITLE CARD: Episode 4 — Add the Intake]**

The drive works. Now we add one thing: the intake. This episode is short because that's the point — adding one subsystem to working code is fast when you say what not to touch.

**[SCREEN: Gemini panel, typing]**

**[TEXT CARD: the full Checkpoint 3 prompt — hold 5 s]**

"Add the intake to MecanumTeleOp. Don't change any of the drive code — it works."

**[TEXT CARD zoom on that second sentence]**

That second sentence is the most important line in this episode. Without it, Gemini sometimes rewrites the whole file, and things that were fine stop being fine.

Then: put it in its own class, `Intake`, with init, update, and stop. The motor named `intake` plus the two CR servos, `intake_servo_left` reversed and `intake_servo_right` — always running together. Press A to toggle collect at 0.5, with edge detection so holding A doesn't flicker it. Left bumper or X toggles reject at minus 0.5. Reject overrides collect; A while rejecting switches to collect. Brake at zero. Telemetry.

The "own class" line is a choice. In our build, Gemini did this split on its own from the first prompt — nobody asked. We ask for it here so it happens on purpose.

**[SCREEN: Gemini generating, 4×]**

**[SCREEN: editor, Intake.java, then the intake lines in MecanumTeleOp]**

Two files now. In Intake.java, find: three hardwareMap.get calls — one motor, two servos — with the left servo set to REVERSE. The A-button edge detection: `if gamepad.a and not previousAState`. An if-else that picks minus 0.5, 0.5, or zero. And one method that sets power on all three.

In MecanumTeleOp: an Intake field, `intake.init` in init, `intake.update` in loop, and the left-bumper-or-X toggle.

Quick question for whoever's reading: what happens if you press A twice, fast? That's what `previousAState` is for. If you can explain that, you understand the most reused pattern in FTC code.

**[SCREEN: Run ▶ → "Install successfully finished"]**

**[SPLIT: Driver Hub | robot with a ball in front of the intake]**

Test. Ball in front of the intake. Press A once.

**[ROBOT: intake pulls the ball in — or pushes it away]**

**[If it went the wrong way, keep it:]**

**[TEXT CARD: "What went wrong: A pushed the ball out"]**

Ours ran backwards. Most common intake bug there is. The fix is one sentence:

**[TEXT CARD: "The intake motor direction is backwards. Reverse `intake`." — hold 3 s]**

If it's the motor pulling in but one servo pushing out, same idea: "`intake_servo_right` is spinning the wrong way. Reverse it." Deploy. Test.

> **Ben:** the guide doesn't record what went wrong on The Hive's first intake test. Use the real thing if anyone remembers; otherwise stage this one.

**[ROBOT: A — pulls in and keeps going. A again — stops. LB — everything backward. LB again — stops. LB, then A — reject stops, collect starts.]**

Collect. Stop. Reject. Stop. Reject, then A — straight to collect.

**[ROBOT: driving while collecting]**

And drive while collecting, because in a match you'll do both at once.

**[SCREEN: Git commit "Intake works"]**

Commit.

**[TEXT CARD: Checkpoint 3 checklist]**

Checkpoint three: all six intake tests pass, the drive still passes its Checkpoint 2 tests, someone can explain edge detection and what happens on each button, and it's saved.

**[END CARD: "Next: Episode 5 — Add the shooter" + repo URL]**

---

## Shot list

- [ ] Prompt + generation
- [ ] Editor: Intake.java and the MecanumTeleOp changes
- [ ] Deploy
- [ ] Robot: collect / stop / reject / stop / reject-then-collect, with a ball
- [ ] Robot: drive + collect together
- [ ] Commit

## Notes

- Having a student narrate this one — ideally someone who wasn't the original programmer — is a deliberate choice. It shows the method transfers.
