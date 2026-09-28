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

**[PIP: robot, close on the flywheel and intake]**

Here's why. A flywheel takes time to get up to speed. And a ball pressed against a stopped flywheel jams it. So the shooter isn't "turn on a motor." It's a sequence.

**[TEXT CARD: the IDLE → REVERSE_BUMP → PAUSE → RUNNING diagram — hold 5 s]**

Press B. Reverse bump: flywheel *and* intake run backward at minus 0.5 for 200 milliseconds, to push a jammed ball off the wheel. Pause: everything stops for 300 milliseconds, so the motor isn't fighting itself. Running: flywheel forward at target power, and it stays there. Now, while it's running, hold right bumper and the intake pulses — 100 on, 200 off — so one ball feeds at a time. B again: back to idle.

If you can draw it like this, you can describe it. And if you describe it as a sequence with those state names, Gemini writes it as one — and the code matches the picture.

**[SCREEN: Gemini panel, prompt]**

**[TEXT CARD: the full Checkpoint 4 prompt — scroll slowly, ~8 s]**

Add the flywheel, don't touch drive or intake. Own class, `Flywheel`, like `Intake`. One motor named `flywheel`, reversed, brake at zero. An enum: IDLE, REVERSE_BUMP, PAUSE, RUNNING. B, edge-detected, from IDLE starts the sequence: bump at minus 0.5 for 200 milliseconds, pause 300, then running at the target, 1.0. B in any other state stops it. D-pad up and down nudge the target by 0.05.

Then the part people miss: the intake needs to know what the flywheel is doing. So `Flywheel` gets `isReversing`, `isPausing`, `isOn`, and `Intake` uses them — reverse with the bump, stop during the pause, and pulse 100 on at 0.5, 200 off while running with right bumper held. Reject mode also runs the flywheel backward. Telemetry.

**[TEXT CARD zoom: "All timing with ElapsedTime, no sleep()"]**

And this line. If you don't know what it means, include it anyway. Without it, Gemini might use sleep, and sleep freezes the whole robot during the startup sequence — you can't drive. ElapsedTime lets everything keep running.

One more thing in that prompt, on purpose: D-pad tuning. That's the same D-pad that does precision driving. It's going to conflict. We're leaving it in, because fixing it is one of the mechanical team's prompts in the next episode.

**[SCREEN: generation, 4×]**

**[SCREEN: editor, Flywheel.java — longer scroll]**

This one's bigger. Four things to find.

**[Highlight]** The enum: `StartPhase` — IDLE, REVERSE_BUMP, PAUSE, RUNNING. Same names as the diagram.

**[Highlight]** The B edge-detect, and the branch: not idle, stop; idle, go to REVERSE_BUMP and reset the timer.

**[Highlight]** The two transitions. Timer past 0.200 — PAUSE. Past 0.300 — RUNNING. Find both.

**[Highlight]** The power decision: minus 0.5 in the bump or reject, zero in the pause, target when running.

**[SCREEN: Intake.java pulse, then MecanumTeleOp update order]**

Then in Intake, the pulse: a timer, mod 0.300, on when it's under 0.100. And in MecanumTeleOp, `flywheel.update` runs *before* `intake.update`, because the intake needs this loop's flywheel state.

**[SCREEN: Run ▶]**

**[TEXT CARD: "Clear the area in front of the shooter"]**

Deploy. And before you test: the flywheel launches balls. Clear the area. Test with no balls first.

**[SPLIT: Driver Hub telemetry showing flywheel phase | robot]**

Press B. Watch the flywheel. A quick backward twitch, a quick stop, then it spins up forward and stays. Telemetry: REVERSE_BUMP, PAUSE, RUNNING.

**[ROBOT close-up: the flywheel spinning the wrong way in RUNNING]**

**[TEXT CARD: "What went wrong: the flywheel spins the wrong way in RUNNING"]**

Ours came up backwards. One sentence:

**[TEXT CARD: "`flywheel` spins the wrong way. Reverse it." — hold 3 s]**

**[SPLIT: telemetry | robot — B pressed again, then RB held]**

Again. Bump, pause, running — the right way. Hold right bumper: the intake ticks. You can hear it. Release — ticking stops, flywheel keeps going. B — stops.

**[ROBOT: with balls loaded]**

Now with balls. B, wait for RUNNING, hold right bumper.

**[ROBOT: first attempt — keep whatever happens]**

**[Example failure, replace with real:]**

**[TEXT CARD: "What went wrong: RB with the flywheel idle ran the intake — ball jammed on the stopped wheel"]**

Somebody held the bumper before pressing B, and the intake pushed a ball into a wheel that wasn't turning. That's a description that was missing:

**[TEXT CARD: "Right bumper should only pulse the intake when the flywheel is RUNNING. Otherwise ignore it." — hold 3 s]**

**[ROBOT: second attempt — balls launch one at a time]**

That's it. A direction, then a rule — and it shoots, one ball per pulse.

> **Ben:** replace the two failures above with what actually happened on The Hive's first shooter tests. The guide doesn't record them. The one real shooter complaint — "it is spitting balls out" — belongs to Episode 6; don't spend it here.

**[SCREEN: the tuning table in the guide being filled in]**

Write the numbers down. Bump, pause, target, pulse on and off, and what happened. Ours: 200, 300, 1.0, 100 over 200. These numbers are your robot's. Next season's robot will need different ones, and you'll want to know where you started.

**[ROBOT: driving while the flywheel starts up]**

Last check: drive during the startup sequence. If the robot can't move while the flywheel comes up, Gemini used sleep, and you say so: "You used sleep() in the flywheel sequence. Replace it with ElapsedTime so loop() keeps running."

**[SCREEN: Git commit with the numbers in the message]**

Commit — and put the numbers in the message. "Shooter works — bump 200 ms, target 1.0, pulse 100/200."

**[TEXT CARD: Checkpoint 4 checklist]**

Checkpoint four: all eight shooter tests pass, drive and intake still pass their earlier tests, someone can find both phase transitions and explain why the intake needs the flywheel's state, the tuning numbers are written down, saved.

**[END CARD: "Next: Episode 6 — Refine: the mechanical team's real prompts" + repo URL]**

---

## Shot list

- [ ] Flywheel and intake close-up
- [ ] Sequence diagram card
- [ ] Prompt scroll
- [ ] Editor with four highlights in Flywheel.java, plus the Intake pulse and update order
- [ ] Safety card
- [ ] Split-screen test, no balls
- [ ] Robot tests with balls, 2–3 attempts
- [ ] Tuning table
- [ ] Drive-during-startup
- [ ] Commit
