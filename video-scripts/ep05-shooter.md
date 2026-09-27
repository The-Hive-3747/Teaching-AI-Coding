# Episode 5 — Add the Shooter

**Length:** ~6 minutes
**Format:** Screen recording + robot footage, voice-over
**Narrator:** Student
**Guide page:** `guide/04-shooter.md`
**Starting state:** Checkpoint 3 committed

---

## Script

**[TITLE CARD: Episode 5 — Add the Shooter]**

The shooter is the first thing with timing in it, so this episode is a little longer. Expect to go around the loop two or three times before it's right. That's normal.

**[PIP: robot, close on the flywheels and intake]**

Here's why. A flywheel takes time to get up to speed. If you feed a piece in too early, it just dribbles out. So the shooter isn't "turn on two motors." It's a sequence.

**[TEXT CARD: the SPIN UP → FEED → STOP diagram — hold 5 s]**

Trigger pressed: spin up, wait about a second and a half. Then feed — pulse the intake, on, off, on, off. Trigger released: everything stops.

If you can draw it like this, you can describe it. And if you describe it as a sequence, Gemini writes it as one.

**[SCREEN: Gemini panel, prompt]**

**[TEXT CARD: the full Checkpoint 4 prompt — scroll slowly, ~8 s]**

Add the shooter, don't touch drive or intake. Two flywheels, one on each side of the launch path; leave both un-reversed for now. Three states: IDLE, SPINNING_UP, FEEDING. While the trigger is held: flywheels to 0.8. Wait 1.5 seconds, no intake during the wait. Then pulse the intake, 0.3 on, 0.2 off, as long as the trigger is held. Release, stop everything. Bumpers still work when the trigger isn't held.

**[TEXT CARD zoom: "Use ElapsedTime for the timing, not sleep()"]**

And this line. If you don't know what it means, include it anyway. Without it, Gemini might use sleep, and sleep freezes the whole robot during spin-up — you can't drive. ElapsedTime lets everything keep running.

**[SCREEN: generation, 4×]**

**[SCREEN: editor, the shooter section — longer scroll]**

This one's bigger. Four things to find.

**[Highlight]** Flywheel mapping.

**[Highlight]** The enum: IDLE, SPINNING_UP, FEEDING — same names as the diagram. Find where it goes from SPINNING_UP to FEEDING — there. A timer check: if the timer's past 1.5 seconds.

**[Highlight]** The pulse. A timer that flips the intake on and off.

**[Highlight]** Trigger release. Both flywheels and the intake to zero, state back to IDLE.

**[SCREEN: Run ▶]**

**[TEXT CARD: "Clear the area in front of the shooter"]**

Deploy. And before you test: flywheels launch things. Clear the area. Test with no pieces first.

**[SPLIT: Driver Hub telemetry showing shooter state | robot]**

Hold the trigger. Watch the flywheels first — both should push toward the exit.

**[ROBOT close-up: one flywheel spinning the wrong way]**

**[TEXT CARD: "What went wrong: flywheelRight is pulling backward"]**

Ours: the right one's pulling the wrong way. We told Gemini we'd say which one after testing. Now we know.

**[TEXT CARD: "flywheelRight spins the wrong way. Reverse it." — hold 3 s]**

Write down which one you reversed. The autonomous will need the same setting.

**[SPLIT: telemetry | robot — trigger held]**

Again. Telemetry: SPINNING_UP. Listen — flywheels coming up. About a second and a half... FEEDING, and the intake starts pulsing. Release. Everything stops.

**[ROBOT: with pieces loaded]**

Now with pieces.

**[ROBOT: first attempt — keep whatever happens]**

**[Example failure, replace with real:]**

**[TEXT CARD: "What went wrong: pieces launched weakly"]**

They dribbled. Spin-up wasn't long enough for our wheels. One change:

**[TEXT CARD: "Increase the spin-up wait to 2.0 seconds." — hold 3 s]**

**[ROBOT: second attempt — clean shots]**

That's it. Three trips around the loop — a direction, then a number — and it shoots.

> **Ben:** replace the failures above with what actually happened on The Hive's first shooter tests. If it took more than three tries, show two and say "a few more like that."

**[SCREEN: the tuning table in the guide being filled in]**

Write the numbers down. Spin-up, power, pulse on, pulse off, and what happened. These numbers are your robot's. Next season's robot will need different ones, and you'll want to know where you started.

**[ROBOT: driving while shooting]**

Last check: drive while the trigger's held. If the robot can't move during spin-up, Gemini used sleep, and you say so: "You used sleep() in the shooter. Replace it with ElapsedTime."

**[SCREEN: Git commit with the numbers in the message]**

Commit — and put the numbers in the message.

**[TEXT CARD: Checkpoint 4 checklist]**

Checkpoint four: shooter tests pass, drive and intake still work, someone can find the spin-up-to-feed transition, numbers written down, saved.

**[END CARD: "Next: Episode 6 — Refine" + repo URL]**

---

## Shot list

- [ ] Flywheel close-up
- [ ] Sequence diagram card
- [ ] Prompt scroll
- [ ] Editor with four highlights
- [ ] Safety card
- [ ] Split-screen test, no pieces
- [ ] Robot tests with pieces, 2–3 attempts
- [ ] Tuning table
- [ ] Drive-while-shooting
- [ ] Commit
