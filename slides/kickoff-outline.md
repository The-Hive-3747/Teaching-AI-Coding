# Kickoff Presentation — Outline and Speaker Notes

**Audience:** Mixed room of FTC students and coaches. Assume some have never opened Android Studio and some have a working TeleOp already.
**Length:** 45 minutes (30 min talk + demo, 15 min Q&A). Cut sections marked *(optional)* for a 30-minute slot.
**Goal:** By the end, every team believes they can do this next week, knows the checkpoint order, and knows where the guide and videos are.

Structure: big picture first for the whole room, then the concrete method, then a live demo. Coaches get the "how to run this with a team" material near the end so students don't tune out early.

---

## 1. Title (1 slide)

**Slide:** "Describe your robot. Let AI write the code. Test. Repeat."
Subtitle: How The Hive's mentors coded a robot in one week with Gemini in Android Studio — and how your team can too.

**Notes:** Keep this on screen while people sit down. No talking needed.

---

## 2. The problem every team has (1 slide)

**Slide:** Three bullets, big text:
- Most teams have 1 programmer. Sometimes 0.
- Mechanical changes wait on that one person.
- The programmer wants to help other teams too.

**Notes:** Ask for a show of hands: "How many teams have exactly one person who can write Java?" Then: "How many of those people are also the coach?" Let it land. This is the pain the method solves.

---

## 3. What happened in our robot-in-one-week (1–2 slides)

**Set-up, said out loud before 3a:** At our robot-in-one-week the students built and coded their robot the normal way. The mentors had our own competition, and we ran an experiment on our robot: no hand-written code at all — everything described to Gemini. This is what happened.

**Slide 3a:** Timeline graphic, one line per session. These are Gemini's own "session iterations" from its log of the build. *(Ben: put days on them.)*
- Session 1: Coordinator describes the robot — mecanum drive, flywheel with a 3-phase startup, pulsed intake, LEDs → working TeleOp, split into subsystem classes
- Session 2: First autonomous state machines (shoot first / shoot delayed), plus a 4-ball experiment
- Session 3: Asim (mechanical mentor): *"hey, in the latest push, holding both joysticks left made the robot strafe right, and vice versa. please fix"*
- Session 4: *"what is the current time of spinning the motors back before starting the flywheel? it is spitting balls out, so i would like to shorten it"* — reverse bump 200 → 100 ms
- Session 5: *"On the delayed autonomous, we end by bumping into the wall to get park points. However, we get points for not touching the wall. Can we add a backup after the turn and drive forward? I'm thinking 100ms for 0.5 power backwards at the end should move us away."* — back off the wall
- Session 6: *"I thought gamepad 1 dpad was slow mode, not tuning the flywheel."* — controls untangled
- Session 7: *"When I start up the flywheel, I want the process to continue to do the reverse function. But then I don't want the intake to resume. I want the intake to stop (whether it was stopped or running)."* — spool-up jam fixed
- Session 8: *"Let's set the default speed to 0.95"* — tuned value baked in (the same session also created the `ExperimentalParkShootFirst` OpMode)

**Slide 3b:** The punchline, alone on a slide:
> "I've written Java for twenty-five years. I didn't type a line of it. After the first build, the changes came from the mechanical mentors — sometimes typed by them, sometimes called out from over the robot while I typed. They don't write Java."

**Notes:** This is the story. Tell it as a story, not a list. Be precise about who did what, because the audience will ask — and be clear this was the mentors' robot, not the students'. The students coded theirs by hand; the mentors proved the method on theirs. Two things make the experiment honest: the coordinator *can* write Java (say so — it means the choice to describe instead of type was a choice, not a workaround), and the mechanical mentors can't, and made most of the changes anyway. The coordinator described the architecture and got the first TeleOp and the first autonomous state machines. From there it was a pair: Asim, the mechanical mentor, at the robot deciding what to change; the coordinator at the keyboard typing it — that's the Sep 18 autonomous session, the park routine, and most of the Sep 19 fixes. And Asim at the keyboard himself for eight prompts: tank drive, the strafe fix, the spitting-balls pair, and the entire ExperimentalParkShootFirst autonomous with three rounds of tuning. The point for the room: the person who understands the robot drives the prompt, whoever's hands are on the keys. The tool didn't replace the programmer; it freed the programmer. The quotes on 3a are verbatim — typos and all — and that's the point: those are the words of people describing a robot they built, not code they wrote.

`[SCREENSHOT: the Gemini panel in Android Studio showing one of those prompts as typed — the "spitting balls out" one if it's still in the history]`

---

## 4. What this is and isn't (1 slide)

**Slide:** Two columns.

| It is | It isn't |
|---|---|
| Describing what the robot should do, in English | Copy-pasting code you don't understand |
| Testing after every change | Asking for the whole robot at once |
| Learning what your robot's parts are called and how they interact | Skipping the learning |
| A way for the whole team to change code | A way to avoid having anyone learn code |

**Notes:** Coaches worry about this. Say it plainly: students still have to understand what a motor is, what direction it spins, what a state machine does. They just don't have to type the Java. Be honest that we haven't run this with students yet — the mentors ran it on the mentor robot. That's why there's a Reader role on slide 11: it's how you make sure the understanding is there when students do it.

Gemini in Android Studio needs a Google account signed in — have that done before the session.

---

## 5. The method: describe → generate → test → refine (1 slide)

**Slide:** A loop diagram. Four boxes in a circle:

```
   ┌─────────────┐
   │  DESCRIBE   │  "The intake is a motor named 'intake' plus two
   │             │   CR servos. A toggles collect at 0.5 power..."
   └──────┬──────┘
          ▼
   ┌─────────────┐
   │  GENERATE   │  Gemini writes or edits the OpMode
   └──────┬──────┘
          ▼
   ┌─────────────┐
   │    TEST     │  Deploy to the robot. Try it.
   └──────┬──────┘
          ▼
   ┌─────────────┐
   │   REFINE    │  "it is spitting balls out, so i would
   │             │   like to shorten it"
   └──────┬──────┘
          │
          └──────────► back to GENERATE
```

**Notes:** The loop is the whole workshop. Every checkpoint is one trip around it. The skill students build is the DESCRIBE and REFINE steps: being specific about names, directions, timing, and order.

---

## 6. The checkpoints (1 slide)

**Slide:** Numbered list, one line each. Same list as the README.

0. Install Android Studio + FtcRobotController, sign in to Gemini
1. Describe the robot: names, ports, purposes, directions
2. Mecanum drive → **test**
3. Intake → **test**
4. Shooter: flywheel startup sequence + pulsed feed → **test**
5. Refine: the mechanical mentor's four real prompts
6. Auto v1: back off the wall and shoot → **test**
7. Auto v2: Shoot First / Shoot Delayed, which parks → **test**
8. Auto v3: under 30 seconds, keep the LEAVE points → **test**
9. Extras: LEDs, endgame rumble, the 4-ball experiment

**Notes:** Point out that "test" appears after every step. The single biggest mistake is asking for everything at once. Small steps mean when something breaks, you know exactly which description caused it.

There's a video for each checkpoint and a guide page for each checkpoint. Show the repo URL.

---

## 7. Why small steps matter (1 slide) *(optional)*

**Slide:** Side by side.

**Asked for everything at once:**
> "Write a TeleOp with mecanum drive, intake, and a flywheel shooter, plus an autonomous that shoots and parks."
→ 600 lines across five files. Strafe mirrored. Flywheel spits balls out. Intake jams the spool-up. Where do you start?

**Asked one step at a time:**
> "Write a TeleOp with tank-style mecanum drive. Left stick Y is the left wheels, right stick Y the right wheels, stick X strafes. Motors are named..."
→ 100 lines. Drive works. Next.

**Notes:** Honest version: our session 1 *was* the all-at-once prompt, and it worked — but the three bugs above were all in it, and it took sessions 3, 4 and 7 to find them one at a time. The guide's checkpoints are the one-at-a-time version of the same week. Skip if short on time.

---

## 8. What a good description looks like (1–2 slides)

**Slide 8a:** The five things every description needs:
1. **Names** — exactly as they appear in the Robot Controller config (`front_left_drive`, not "the front left motor")
2. **Purpose** — what the part does for the robot
3. **Direction** — which way is "forward," and which motors are mounted mirrored
4. **Control** — which button or stick; **toggle or hold**; what happens on release
5. **Timing and order** — what has to happen before what, and for how long

**Slide 8b:** A before/after for a *new feature*.

Vague:
> "Add a shooter."

Specific:
> "One motor named `flywheel`, reversed. Pressing B runs the flywheel and intake backward at −0.5 for 200 ms to clear a jammed ball, stops everything for 300 ms, then runs the flywheel forward at full power and keeps it there. Pressing B again stops it. While it's running and right bumper is held, pulse the intake 100 ms on at 0.5, 200 ms off, so balls feed one at a time."

**Notes:** Read the vague one and ask the room what Gemini would have to guess. Then read the specific one. Everything Gemini would have guessed is now stated.

**Slide 8c:** A real *refinement* prompt, verbatim, called out by the mechanical mentor from over the robot:

> *"When I start up the flywheel, I want the process to continue to do the reverse function. But then I don't want the intake to resume. I want the intake to stop (whether it was stopped or running)."*

**Notes:** No code words. It describes a sequence — keep this step, then don't do that, regardless of what was happening before. That precision is why it was a one-line fix in `Intake.java`. This is the slide that convinces the mechanical kids in the room.

---

## 9. Live demo (5–8 minutes)

**Setup before the session:** Android Studio open, FtcRobotController project loaded, Gemini panel signed in, a robot (or at least a Control Hub) on the table, connected and configured. Have the "describe the robot" prompt already pasted in the panel and sent so you're not waiting on generation.

**Demo script:**
1. Show the Gemini panel. Show the robot-description prompt already there.
2. Type the mecanum drive prompt live. Send it. While it generates, narrate what you asked for.
3. Show the generated OpMode. Point at the hardware names — they match the config.
4. Build and deploy. Drive the robot.
5. Type a refinement live — use the real one: *"hey, in the latest push, holding both joysticks left made the robot strafe right, and vice versa. please fix"* Deploy. Drive again.
6. Stop there. "That's the whole method. Everything else is more trips around the loop."

**Fallback if the robot won't cooperate:** Have a screen recording of the same demo ready. Play it, narrate over it.

**Match footage for slide 3 or the open:** the mentor competition on the FIRST Robotics Utah scrimmage stream, from 3:39:22 — [https://www.youtube.com/live/E53OUghEmlg?t=13162](https://www.youtube.com/live/E53OUghEmlg?t=13162). Download the clip beforehand; don't rely on the venue's Wi-Fi.

`[SCREENSHOT: Gemini panel with the mecanum drive prompt and the generated code visible side by side — for the fallback slide]`

---

## 10. Where it goes wrong, and the fix (1 slide)

**Slide:** Table.

| Symptom | Cause | Fix |
|---|---|---|
| Code won't compile: "cannot find symbol" | Gemini invented a class or method name | Paste the error back into the panel: "Fix this compile error" |
| Strafe is mirrored | Mecanum mixing signs | *"hey, in the latest push, holding both joysticks left made the robot strafe right, and vice versa. please fix"* |
| Flywheel startup spits balls out | Reverse bump too long | *"what is the current time of spinning the motors back before starting the flywheel? it is spitting balls out, so i would like to shorten it"* |
| Two controls on one button | Control scheme drifted | *"I thought gamepad 1 dpad was slow mode, not tuning the flywheel."* |
| Auto works, but drives the intake through a *fake gamepad* | Nobody told Gemini an autonomous was coming | Say it up front: "An autonomous OpMode will use this class later too — put the behavior in plain methods." Gemini takes the expedient route unless you say what's coming. |
| Auto runs over 30 s | Gemini was told the limit, but nobody saw the sum | "Add up the total duration of all states and tell me the worst-case run time." Then cut one number. |
| Gemini rewrote everything, broke what worked | Asked for a change without saying "only change X" | "Only change the intake section. Leave the drive code as is." |

**Notes:** Every one of these happened to us; the italic ones are the actual prompts. The guide's checkpoint 5 is all about this. The pattern: when it's wrong, the description was missing something. Say the missing thing.

---

## 11. For coaches: running this with a team (1–2 slides)

**Slide 11a:** Suggested roles.
- **Describer** — the person who knows the robot best says what to change. They can type it, or someone else can; what matters is whose words they are. On the mentor robot this was Asim, the mechanical mentor — sometimes at the keyboard, sometimes over the robot with the coordinator typing.
- **Tester** — one student deploys and drives. Says exactly what happened.
- **Reader** — one student reads the generated code aloud and explains it. This is where the learning happens.
- **Coach** — asks "what did you tell it?" when something breaks. Doesn't type.

**Slide 11b:** Ground rules that worked for us.
- One change per prompt.
- Test after every change. No exceptions.
- Commit (or copy the file) after every working test, so you can go back.
- If Gemini rewrites something that already worked, say "only change X."
- The reader has to be able to explain the code before the team moves on.

**Notes:** The Reader role is our answer to the "aren't they just cheating" question. If a student can explain what the state machine does and why the timings are what they are, they learned it.

---

## 12. Autonomous without odometry (1 slide) *(optional)*

**Slide:** The Shoot Delayed autonomous as a state machine — the mentor robot's states, with the guide's Checkpoint 8 shoot time (6.5 s; the team's shipped code has 8.3 s).

```
  START
    │
    ▼
┌────────────────────┐  wait 15 s for the alliance partner (0 s in Shoot First)
│       DELAY        │
└─────────┬──────────┘
          ▼
┌────────────────────┐  -0.3 for 0.30 s: back away from the wall to shooting distance
│  STATE_1_BACK_UP   │
└─────────┬──────────┘
          ▼
┌────────────────────┐  stop for 1.0 s so the robot isn't rocking
│STATE_1B_SETTLE_1000MS│
└─────────┬──────────┘
          ▼
┌────────────────────┐  flywheel straight to speed, 2.0 s (no reverse bump — balls preloaded)
│STATE_2_START_FLYWHEEL│
└─────────┬──────────┘
          ▼
┌────────────────────┐  intake pulses 100 ms on / 200 ms off, 6.5 s
│STATE_3_PULSE_SHOOTING│
└─────────┬──────────┘
          ▼
┌────────────────────┐  -0.3 for 0.45 s
│STATE_4B_PARK_BACK_UP│
└─────────┬──────────┘
          ▼
┌────────────────────┐  0.55 s
│STATE_5_PARK_TURN_LEFT│
└─────────┬──────────┘
          ▼
┌────────────────────┐  0.4 for 3.0 s, into the zone
│STATE_5B_PARK_DRIVE_FORWARD│
└─────────┬──────────┘
          ▼
┌────────────────────┐  -0.5 for 0.1 s: "we get points for not touching the wall"
│STATE_5C_PARK_BACK_OFF_WALL│
└─────────┬──────────┘
          ▼
┌────────────────────┐
│  STATE_6_ALL_STOP  │
└────────────────────┘
```

**Notes:** Time-based auto is fine for a first week. You don't need odometry to score. Each box is one state; each arrow is "after N seconds, move to the next state." Students can describe this exact diagram to Gemini and get a working state machine. Checkpoint 6 builds the shooting boxes; checkpoint 7 adds the delay and the park boxes; checkpoint 8 adds the last one — from a real prompt — and makes the total fit in 30 seconds. Point out that the mechanical mentor drove the park routine and asked for the back-off step. Then add up the column out loud: 30.7 s on paper. It finished on the field — the timer has a little slack — but that's the Checkpoint 8 lesson: add it up and leave margin, because slack isn't margin.

---

## 13. What you need to start (1 slide)

**Slide:** Checklist.
- [ ] A laptop that can run Android Studio (Windows, Mac, or Linux; 8 GB RAM minimum, 16 GB better)
- [ ] A Google account signed in to Gemini in Android Studio
- [ ] The FtcRobotController project cloned
- [ ] Your robot's hardware config done on the Driver Hub / Robot Controller (names and ports)
- [ ] A written list of every motor and servo: name, port, purpose, direction
- [ ] The guide and videos: `[REPO URL]`

**Notes:** The hardware list is the one thing to do *before* opening Gemini. Everything else follows from it.

Worth saying out loud for anyone installing tonight: during the first Gradle sync, Android Studio pops up three things. Accept the Gradle Daemon JVM toolchain migration. **Decline** the Gradle/AGP upgrade — that one breaks the FTC project. On Windows, accept the Defender exclusion or builds crawl. Checkpoint 0 in the guide has screenshots of all three.

---

## 14. Close (1 slide)

**Slide:**
> Start small. Test every step. When it's wrong, describe what was missing.

Repo URL. Contact for questions.

**Notes:** Q&A. Likely questions and short answers:
- *"Does this work with Blocks / OnBot Java?"* — The method does; the tool doesn't. Gemini is inside Android Studio. Teams on OnBot Java could use the web version of Gemini and paste, but this workshop is built around Android Studio.
- *"What about odometry / Road Runner?"* — Same method, more to describe. Start with time-based; add odometry once you have it wired.
- *"Will it work with ChatGPT / Claude instead?"* — The describe/test/refine method works with any of them. Gemini in Android Studio is convenient because it sees your project and edits files in place.

---

## Assets needed for the deck

- [ ] `[SCREENSHOT]` Gemini panel with a real mechanical-team prompt, ideally as typed (slide 3)
- [ ] `[SCREENSHOT]` Gemini panel with the mecanum prompt and generated code (slide 9 fallback)
- [x] Robot photos in `docs/images/` — `match-closeup-green-leds.jpg` for the title, the two wide scrimmage frames for slide 3
- [ ] Days for the eight sessions (slide 3a)
- [ ] Fallback screen recording of the live demo (slide 9)
- [ ] Repo URL once the repo exists
