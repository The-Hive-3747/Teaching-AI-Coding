# Episode 7 — Autonomous: Back Off the Wall and Shoot

**Length:** ~6 minutes
**Format:** Screen recording + robot footage, voice-over
**Narrator:** Student (Sadiqah)
**Guide page:** `guide/06-auto-shoot.md`
**Starting state:** Checkpoint 5 committed. Fresh Gemini conversation.

---

## Script

**[TITLE CARD: Episode 7 — Autonomous: Shoot]**

Autonomous. Thirty seconds where the robot drives itself. This sounds like the hard part. It isn't, if you build it the same way we built everything else: one piece at a time. This episode is just: back off the wall, shoot, stop. No parking yet.

**[PIP: robot against the wall in starting position, shooter facing the goal]**

We don't have odometry on this robot. No sensors telling it where it is. So we drive by time: "backward at 0.3 power for 0.3 seconds." That's repeatable enough to score, as long as the battery's charged and the wheels are clean.

**[TEXT CARD: the 6-state diagram — STATE_1_BACK_UP → STATE_1B_SETTLE_1000MS → STATE_2_START_FLYWHEEL → STATE_3_PULSE_SHOOTING → STATE_4_STOP_SHOOTING → STATE_6_ALL_STOP — hold 6 s]**

Here's the plan as a picture. Each box is a state. Each arrow says "when the timer passes this many seconds, go to the next box." That's called a state machine, and it's the only concept in this episode.

Back up: all four motors at minus 0.3 for 0.30 seconds, to shooting distance. Settle: motors off for a full second. Start flywheel: straight to target, wait 2.0 seconds. Pulse shooting: feed for 10 seconds. Stop shooting. All stop.

Two of those boxes are worth copying. The settle — a robot rocks after it brakes, and shooting while it rocks scatters shots. And no reverse bump in auto: the balls are preloaded, and reversing the intake ejects them. That one's in Gemini's own log of our build as a lesson learned.

And yes, there's no STATE_5. That's the park. It goes in next episode, and leaving the gap now means the names don't shift later.

**[SCREEN: Flywheel.java and Intake.java in the project tree]**

One more idea before the prompt. The auto doesn't get its own shooter code. It calls the same `Flywheel` and `Intake` classes TeleOp uses — `flywheel.startDirect()`, `intake.setFeed(true)`. Everything we tuned last episode comes along for free.

**[SCREEN: BaseAuto.java from the real build, the `autoGamepad` lines highlighted]**

Here's a confession. That's not what our code does. Ours makes a *fake gamepad* — a Gamepad object nobody's holding — and sets its right bumper to true when it wants to feed. The subsystems can't tell the difference. It works. It shipped.

It's also a shortcut. Our auto depends on which button feeds. Move feeding off the right bumper and the auto quietly stops working. We never got to fix that. And it happened because when Gemini wrote `Intake`, nobody told it an autonomous was coming. Gemini takes the fastest route to what you asked for. If you don't say what's coming, the fastest route is a shortcut.

**[TEXT CARD: "An autonomous OpMode will use this class later too." — hold 3 s]**

That's why episode 4's prompt had this line in it. Say what the code will be used for, not just what it does now.

**[SCREEN: new Gemini conversation, pasting the robot description from Checkpoint 1]**

Start a fresh Gemini conversation for the autonomous, and paste the robot description in first — that's why we saved it as a file.

**[SCREEN: typing the autonomous prompt]**

**[TEXT CARD: the full Checkpoint 6 prompt — scroll slowly, ~10 s]**

Then the prompt. It's the diagram, written out. New Autonomous OpMode called `AutoShootFirst`, iterative like MecanumTeleOp. Same drive motors, same reversals, same brake settings — read them from that file. Reuse `Flywheel` and `Intake`, don't modify them. The robot starts with the shooter facing the goal and its back against the wall, balls preloaded.

Then the six states, each with what the motors do and for how long. A new `startDirect()` on Flywheel that skips the bump. `intake.setFeed(true)` during STATE_3 — direct calls, no fake gamepad. Reset the timer on every transition. Tick both subsystems every loop, after the switch. Telemetry. No sleep.

**[SCREEN: generation, 4×]**

**[SCREEN: editor, the state machine]**

What came back. Find:

**[Highlight]** The enum. Six states, same names we used.

**[Highlight]** A switch on the state, inside loop. One case per state.

**[Highlight]** In each case, the timer check. "If state time past 0.30, go to STATE_1B_SETTLE_1000MS, reset the timer." That's the arrow from the diagram.

**[Highlight]** In STATE_3, `intake.setFeed(true)` — and no Gamepad object anywhere in the file. If there is one, that's the shortcut; ask for the direct call.

**[Highlight]** In Flywheel.java, the new `startDirect()`.

Ask whoever's reading: how does it get from BACK_UP to SETTLE? The timer check. If they can say that, they understand state machines. That's it, that's the whole concept.

**[SCREEN: Run ▶]**

**[SPLIT: Driver Hub, Autonomous list → Auto: Shoot First → Init | robot at the wall, balls loaded]**

Robot against the wall, balls loaded, field clear. Autonomous list, Auto: Shoot First, Init, Play.

**[ROBOT: full run, with telemetry state visible on the Driver Hub side]**

BACK_UP — a short move away from the wall. SETTLE — a full second of nothing. START_FLYWHEEL — you can hear it come up; telemetry says RUNNING. PULSE_SHOOTING — one, two, three, and they're gone well before the ten seconds are up. STOP_SHOOTING. ALL_STOP. About 13.3 seconds.

**[ROBOT: run 2, run 3 — quick cuts]**

Run it three times. Time-based auto varies, and you want to see how much.

**[Keep real failures. Example:]**

**[TEXT CARD: "What went wrong: run 1 drove into the wall"]**

First try, it drove *toward* the wall. A direction that wasn't described.

**[TEXT CARD: "STATE_1_BACK_UP is driving the wrong way. The robot starts with its back to the wall and should move away from it — negate the drive power." — hold 3 s]**

**[TEXT CARD: "What went wrong: a ball popped out the front when the flywheel started"]**

And then a ball popped out the front. That's the reverse bump running when it shouldn't.

**[TEXT CARD: "STATE_2 must call `startDirect()`, not the normal startup — it's running the reverse bump." — hold 3 s]**

One change, three more runs.

> **Ben:** replace with what actually happened. The guide says the settle state and the no-bump rule both came out of testing — if there's footage of the wild first shot or the ejected ball, that's the one to use.

**[SCREEN: tuning table]**

Numbers in the table. Back-up time and power, settle, spool, shoot, result. Ours: 0.30 at minus 0.3, 1.0, 2.0, 10.0.

**[SCREEN: Git commit "Auto shoots from the wall"]**

Three clean runs. Commit.

**[TEXT CARD: Checkpoint 6 checklist]**

Checkpoint six: three runs in a row that back off, settle, spool, launch all the preloaded balls, and stop. Someone can explain how the state machine moves between states, and why the auto calls `setFeed` instead of pretending to press a button. Numbers written down. Saved.

**[END CARD: "Next: Episode 8 — Autonomous: Shoot First, Shoot Delayed, park" + repo URL]**

---

## Shot list

- [ ] Robot in starting position
- [ ] State diagram card
- [ ] Simulated-gamepad explanation over the two subsystem files
- [ ] Fresh conversation + description paste
- [ ] Prompt scroll
- [ ] Editor with five highlights
- [ ] Split-screen full run × 3
- [ ] Failure + fix
- [ ] Tuning table
- [ ] Commit
