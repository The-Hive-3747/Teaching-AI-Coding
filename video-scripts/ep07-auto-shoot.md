# Episode 7 — Autonomous: Leave the Wall and Shoot

**Length:** ~6 minutes
**Format:** Screen recording + robot footage, voice-over
**Narrator:** Mechanical team member (same as Episode 6 if possible)
**Guide page:** `guide/06-auto-shoot.md`
**Starting state:** Checkpoint 5 committed. Fresh Gemini conversation.

---

## Script

**[TITLE CARD: Episode 7 — Autonomous: Shoot]**

Autonomous. Thirty seconds where the robot drives itself. This sounds like the hard part. It isn't, if you build it the same way we built everything else: one piece at a time. This episode is just: leave the wall, shoot, stop. No parking yet.

**[PIP: robot against the wall in starting position]**

We don't have odometry on this robot. No sensors telling it where it is. So we drive by time: "forward at half power for point-nine seconds." That's repeatable enough to score, as long as the battery's charged.

**[TEXT CARD: the 4-state diagram — LEAVE_WALL → SPIN_UP → SHOOT → DONE — hold 6 s]**

Here's the plan as a picture. Each box is a state. Each arrow says "when the timer passes this many seconds, go to the next box." That's called a state machine, and it's the only concept in this episode.

Leave the wall: drive forward 0.9 seconds. Spin up: flywheels on, wait 1.5. Shoot: pulse the intake three times. Done: everything off.

**[SCREEN: new Gemini conversation, pasting the robot description from Checkpoint 1]**

Start a fresh Gemini conversation for the autonomous, and paste the robot description in first — that's why we saved it as a file.

**[SCREEN: typing the autonomous prompt]**

**[TEXT CARD: the full Checkpoint 6 prompt — scroll slowly, ~10 s]**

Then the prompt. It's the diagram, written out. New Autonomous OpMode called HiveAutoShoot. Same hardware as the TeleOp. A state machine with an enum and ElapsedTime. Four states — and for each one, what the motors do and how long. Reset the timer on each state change. Telemetry. No sleep.

And the last line: "Start with these exact numbers; I'll tune them after testing." Because we will.

**[SCREEN: generation, 4×]**

**[SCREEN: editor, the state machine]**

What came back. Find:

**[Highlight]** The enum. Four states, same names we used.

**[Highlight]** A switch on the state, inside the loop. One case per state.

**[Highlight]** In each case, the timer check. "If timer past 0.9, state becomes SPIN_UP, reset the timer." That's the arrow from the diagram.

**[Highlight]** In SHOOT, a pulse counter.

Ask whoever's reading: how does it get from LEAVE_WALL to SPIN_UP? The timer check. If they can say that, they understand state machines. That's it, that's the whole concept.

**[SCREEN: Run ▶]**

**[SPLIT: Driver Hub, Autonomous list → HiveAutoShoot → Init | robot at the wall, pieces loaded]**

Robot against the wall, pieces loaded, field clear. Autonomous list, HiveAutoShoot, Init, Play.

**[ROBOT: full run, with telemetry state visible on the Driver Hub side]**

LEAVE_WALL — it moves. SPIN_UP — flywheels. SHOOT — one, two, three. DONE.

**[ROBOT: run 2, run 3 — quick cuts]**

Run it three times. Time-based auto varies, and you want to see how much.

**[Keep real failures. Example:]**

**[TEXT CARD: "What went wrong: run 2 only launched 2 pieces"]**

Run two, only two pieces. The third pulse came too fast after the second.

**[TEXT CARD: "Add a fourth pulse to SHOOT." — hold 3 s]**

Or, if it's the first shot: "Add a 0.5 s pause before the first pulse." One change, three more runs.

> **Ben:** replace with what actually happened. If LEAVE_WALL went backwards on the first try — common — show that; it's the clearest example of a direction description that was missing.

**[SCREEN: tuning table]**

Numbers in the table. Leave-wall time, power, spin-up, pulses, result.

**[SCREEN: Git commit "Auto shoots from the wall"]**

Three clean runs. Commit.

**[TEXT CARD: Checkpoint 6 checklist]**

Checkpoint six: three runs in a row that leave the wall, spin up, shoot everything, stop. Someone can explain the state machine. Numbers logged. Saved.

**[END CARD: "Next: Episode 8 — Autonomous: park" + repo URL]**

---

## Shot list

- [ ] Robot in starting position
- [ ] State diagram card
- [ ] Fresh conversation + description paste
- [ ] Prompt scroll
- [ ] Editor with four highlights
- [ ] Split-screen full run × 3
- [ ] Failure + fix
- [ ] Tuning table
- [ ] Commit
