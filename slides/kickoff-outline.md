# Kickoff Presentation — Outline and Speaker Notes

**Audience:** Mixed room of FTC students and coaches. Assume some have never opened Android Studio and some have a working TeleOp already.
**Length:** 45 minutes (30 min talk + demo, 15 min Q&A). Cut sections marked *(optional)* for a 30-minute slot.
**Goal:** By the end, every team believes they can do this next week, knows the checkpoint order, and knows where the guide and videos are.

Structure: big picture first for the whole room, then the concrete method, then a live demo. Coaches get the "how to run this with a team" material near the end so students don't tune out early.

---

## 1. Title (1 slide)

**Slide:** "Describe your robot. Let AI write the code. Test. Repeat."
Subtitle: How The Hive built its robot-in-one-week code with Gemini in Android Studio — and how your team can too.

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

**Slide 3a:** Timeline graphic, one line per day. *(Ben: fill in actual days.)*
- Day 1: Coordinator describes architecture, hardware names, controls to Gemini → working mecanum drive
- Day 2: Intake added
- Day 3: Shooter added, intake/flywheel timing fixed
- Day 4–5: Mechanical team takes over — adjusts timings and power in plain English
- Day 6: Mechanical team builds the autonomous: shoot, then park
- Day 7: Under 30 seconds. Done.

**Slide 3b:** The punchline, alone on a slide:
> "After day 3, the mechanical team wrote the autonomous. Nobody on that team writes Java."

**Notes:** This is the story. Tell it as a story, not a list. Emphasize that the coordinator was *helping other teams* while the mechanical team did the autonomous. The tool didn't replace the programmer; it freed the programmer.

`[SCREENSHOT: the Gemini panel in Android Studio showing one of the mechanical team's actual prompts, e.g. the one that adjusted the intake pulse timing]`

---

## 4. What this is and isn't (1 slide)

**Slide:** Two columns.

| It is | It isn't |
|---|---|
| Describing what the robot should do, in English | Copy-pasting code you don't understand |
| Testing after every change | Asking for the whole robot at once |
| Learning what your robot's parts are called and how they interact | Skipping the learning |
| A way for the whole team to change code | A way to avoid having anyone learn code |

**Notes:** Coaches worry about this. Say it plainly: students still have to understand what a motor is, what direction it spins, what a state machine does. They just don't have to type the Java. Several students on our team could read and explain the code by the end, because they'd described every line of it.

Mention up front: check with your coach about your school's or district's AI policy. Gemini in Android Studio needs a Google account signed in.

---

## 5. The method: describe → generate → test → refine (1 slide)

**Slide:** A loop diagram. Four boxes in a circle:

```
   ┌─────────────┐
   │  DESCRIBE   │  "The intake is a motor named 'intake'.
   │             │   Right bumper runs it forward at full power..."
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
   │   REFINE    │  "The intake runs backwards. Reverse it."
   │             │  "The flywheel needs 1.5 s to spin up before feeding."
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
4. Shooter → **test**
5. Refine: fix directions, timing, pulsing
6. Auto v1: move off the wall and shoot → **test**
7. Auto v2: park → **test**
8. Auto v3: under 30 seconds → **test**

**Notes:** Point out that "test" appears after every step. The single biggest mistake is asking for everything at once. Small steps mean when something breaks, you know exactly which description caused it.

There's a video for each checkpoint and a guide page for each checkpoint. Show the repo URL.

---

## 7. Why small steps matter (1 slide) *(optional)*

**Slide:** Side by side.

**Asked for everything at once:**
> "Write a TeleOp with mecanum drive, intake, and a flywheel shooter, plus an autonomous that shoots and parks."
→ 400 lines. Intake backwards. Flywheel fires before spin-up. Auto drives into the wall. Where do you start?

**Asked one step at a time:**
> "Write a TeleOp with mecanum drive. Left stick moves, right stick rotates. Motors are named..."
→ 60 lines. Drive works. Next.

**Notes:** Skip if short on time; the point is already made in slide 6.

---

## 8. What a good description looks like (1–2 slides)

**Slide 8a:** The five things every description needs:
1. **Names** — exactly as they appear in the Robot Controller config (`frontLeft`, not "the front left motor")
2. **Purpose** — what the part does for the robot
3. **Direction** — which way is "forward," and which motors are mounted mirrored
4. **Control** — which button or stick, and what happens on press vs. hold vs. release
5. **Timing and order** — what has to happen before what, and for how long

**Slide 8b:** A real before/after. *(Ben: replace with an actual pair from the build.)*

Vague:
> "Add a shooter."

Specific:
> "Add a flywheel shooter. Two motors named `flywheelLeft` and `flywheelRight`, mounted facing each other so they must spin in opposite directions. When the driver holds the right trigger, spin both flywheels to 0.8 power. After 1.5 seconds, pulse the intake motor forward to feed: 0.3 seconds on, 0.2 seconds off, repeating. Release the trigger to stop everything."

**Notes:** Read the vague one and ask the room what Gemini would have to guess. Then read the specific one. Everything Gemini would have guessed is now stated.

---

## 9. Live demo (5–8 minutes)

**Setup before the session:** Android Studio open, FtcRobotController project loaded, Gemini panel signed in, a robot (or at least a Control Hub) on the table, connected and configured. Have the "describe the robot" prompt already pasted in the panel and sent so you're not waiting on generation.

**Demo script:**
1. Show the Gemini panel. Show the robot-description prompt already there.
2. Type the mecanum drive prompt live. Send it. While it generates, narrate what you asked for.
3. Show the generated OpMode. Point at the hardware names — they match the config.
4. Build and deploy. Drive the robot.
5. Type a refinement live: "Strafing is reversed. Fix it." Deploy. Drive again.
6. Stop there. "That's the whole method. Everything else is more trips around the loop."

**Fallback if the robot won't cooperate:** Have a screen recording of the same demo ready. Play it, narrate over it.

`[SCREENSHOT: Gemini panel with the mecanum drive prompt and the generated code visible side by side — for the fallback slide]`

---

## 10. Where it goes wrong, and the fix (1 slide)

**Slide:** Table.

| Symptom | Cause | Fix |
|---|---|---|
| Code won't compile: "cannot find symbol" | Gemini invented a class or method name | Paste the error back into the panel: "Fix this compile error" |
| Motor runs backwards | Direction wasn't described | "The left motors are mounted mirrored. Reverse `frontLeft` and `backLeft`." |
| Shooter fires before spinning up | Timing wasn't described | "Wait 1.5 seconds after spin-up starts before feeding." |
| Intake never stops | Release behavior wasn't described | "When the bumper is released, stop the intake." |
| Auto drives too far | Time-based; power or duration off | "Reduce the LEAVE_WALL drive from 0.9 s to 0.7 s." |
| Gemini rewrote everything, broke what worked | Asked for a change without saying "only change X" | "Only change the intake section. Leave the drive code as is." |

**Notes:** Every one of these happened to us. The guide's checkpoint 5 is all about this. The pattern: when it's wrong, the description was missing something. Say the missing thing.

---

## 11. For coaches: running this with a team (1–2 slides)

**Slide 11a:** Suggested roles.
- **Describer** — one student types into Gemini. Rotates.
- **Tester** — one student deploys and drives. Says exactly what happened.
- **Reader** — one student reads the generated code aloud and explains it. This is where the learning happens.
- **Coach** — asks "what did you tell it?" when something breaks. Doesn't type.

**Slide 11b:** Ground rules that worked for us.
- One change per prompt.
- Test after every change. No exceptions.
- Commit (or copy the file) after every working test, so you can go back.
- If Gemini rewrites something that already worked, say "only change X."
- The reader has to be able to explain the code before the team moves on.

**Notes:** The Reader role is how you answer the "aren't they just cheating" question. If a student can explain what the state machine does and why the timings are what they are, they learned it. Also mention account setup: coaches should sort out Google accounts for Gemini before the session, and check any age or district restrictions.

---

## 12. Autonomous without odometry (1 slide) *(optional)*

**Slide:** A simple state machine diagram.

```
  START
    │
    ▼
┌───────────────┐  drive forward 0.9 s
│  LEAVE_WALL   │
└───────┬───────┘
        ▼
┌───────────────┐  flywheels on, wait 1.5 s
│   SPIN_UP     │
└───────┬───────┘
        ▼
┌───────────────┐  pulse intake ×3
│    SHOOT      │
└───────┬───────┘
        ▼
┌───────────────┐  flywheels off, rotate 0.6 s
│ TURN_TO_PARK  │
└───────┬───────┘
        ▼
┌───────────────┐  drive forward 2.0 s
│ DRIVE_TO_PARK │
└───────┬───────┘
        ▼
┌───────────────┐
│     DONE      │
└───────────────┘
```

**Notes:** Time-based auto is fine for a first week. You don't need odometry to score. Each box is one state; each arrow is "after N seconds, move to the next state." Students can describe this exact diagram to Gemini and get a working state machine. Checkpoint 6 builds the first four boxes; checkpoint 7 adds the two park boxes; checkpoint 8 makes it reliable.

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

---

## 14. Close (1 slide)

**Slide:**
> Start small. Test every step. When it's wrong, describe what was missing.

Repo URL. Contact for questions.

**Notes:** Q&A. Likely questions and short answers:
- *"Does this work with Blocks / OnBot Java?"* — The method does; the tool doesn't. Gemini is inside Android Studio. Teams on OnBot Java could use the web version of Gemini and paste, but this workshop is built around Android Studio.
- *"What about odometry / Road Runner?"* — Same method, more to describe. Start with time-based; add odometry once you have it wired.
- *"Is this allowed?"* — FIRST has published guidance on generative AI; check the current season's Game Manual and that guidance before the session, and check your school's policy. The safe position regardless: students must understand and be able to explain the code they submit. *(Ben: confirm the current wording and whether disclosure is expected before putting this on a slide.)*
- *"Will it work with ChatGPT / Claude instead?"* — The describe/test/refine method works with any of them. Gemini in Android Studio is convenient because it sees your project and edits files in place.

---

## Assets needed for the deck

- [ ] `[SCREENSHOT]` Gemini panel with a real mechanical-team prompt (slide 3)
- [ ] `[SCREENSHOT]` Gemini panel with the mecanum prompt and generated code (slide 9 fallback)
- [ ] Team photo or robot photo for the title slide
- [ ] Actual day-by-day timeline from the build (slide 3a)
- [ ] A real vague/specific prompt pair from the build (slide 8b)
- [ ] Fallback screen recording of the live demo (slide 9)
- [ ] Repo URL once the repo exists
