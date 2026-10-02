# Kickoff Presentation — Outline and Speaker Notes

**Presenters:** The Hive (FTC 3747) students — Tom, Sophi and Sadiqah. The mentors who ran the experiment are quoted on the slides, not on stage.
**Audience:** FTC teams from across Utah — students and coaches in the same room. Assume some have never opened Android Studio and some have a working TeleOp already.
**Length:** 30 minutes, no Q&A. Timings per section below add to 30 with the demo.
**Goal:** By the end, every team knows what the mentor experiment showed, knows the checkpoint order, and knows where the guide and videos are — and believes they can do this next week.

**The frame, in one sentence:** At Robot in One Week, The Hive coded their robot by hand, the traditional way. The mentors had their own robot for the mentor competition and ran an experiment on it: no hand-written code — everything described to Gemini in Android Studio. The students saw both, and this talk is what they took from it: **hand-coding vs. architecting with AI**, and how any Utah team can use the second one.

Structure: the story first (the whole room cares about that), then the method, then the live demo, then what to take home. Coaches get the "how to run this with a team" slide near the end so students don't tune out early.

**Deck file:** `slides/kickoff.pptx` — built from this outline; import into Google Slides with **File → Import slides**. Logo gold `#F9BE14` on black.

---

## 1. Title (1 slide, 0 min)

**Slide:** "Describe your robot. Let AI write the code. Test. Repeat."
Subtitle: What The Hive learned watching our mentors code a robot in one week with Gemini in Android Studio — and how your team can do it too.
Footer: The Hive · FTC 3747 · Beehive Academy. Logo.

**Notes:** On screen while people sit down. No talking needed.

---

## 2. Who we are and what this is (1 slide, 1 min)

**Slide:** Three lines.
- We're The Hive, FTC 3747. At Robot in One Week we built and coded our robot by hand — the normal way.
- Our mentors had their own robot for the mentor competition. They ran an experiment on it: **no hand-written Java. Everything described to Gemini.**
- We watched both happen. This is what we learned, and what you can use.

**Notes (Tom/Sophi/Sadiqah — whoever opens):** Say the second bullet slowly. The whole talk hangs on it. Make the split clear: our robot was ours, coded by us, typed by us. The mentor robot was the experiment.

---

## 3. The problem every team has (1 slide, 1 min)

**Slide:** Three bullets, big text:
- Most teams have 1 programmer. Sometimes 0.
- Every mechanical change waits on that one person.
- The mechanical people know what the robot should do — they just can't type it in Java.

**Notes:** Ask for a show of hands: "How many teams have exactly one person who can write Java?" Then: "How many of those people are also the coach?" Let it land. This is the pain the method solves. If you want, say which of us is the one programmer on The Hive.

---

## 4. What the mentors did — the week (2 slides, 4 min)

**Slide 4a — timeline.** One line per session, from Gemini's own log of the build. The quotes are verbatim, typos and all.
- Wed Sep 17, 10:26 PM — Ben (coordinator) describes the whole robot: mecanum drive, flywheel with a 3-phase startup, pulsed intake → working TeleOp, split into subsystem classes
- Thu Sep 18, ~9:50 PM — first autonomous state machines (Shoot First / Shoot Delayed), plus a 4-ball experiment
- Fri Sep 19, 8:29 AM — Asim (mechanical mentor): *"hey, in the latest push, holding both joysticks left made the robot strafe right, and vice versa. please fix"*
- 8:36 AM — *"what is the current time of spinning the motors back before starting the flywheel? it is spitting balls out, so i would like to shorten it"* — reverse bump 200 → 100 ms
- 8:40 AM — *"On the delayed autonomous, we end by bumping into the wall to get park points. However, we get points for not touching the wall. Can we add a backup after the turn and drive forward? I'm thinking 100ms for 0.5 power backwards at the end should move us away."*
- 11:35 AM — *"I thought gamepad 1 dpad was slow mode, not tuning the flywheel."*
- 11:37 AM — *"When I start up the flywheel, I want the process to continue to do the reverse function. But then I don't want the intake to resume. I want the intake to stop (whether it was stopped or running)."*
- 2:57 PM — *"Let's set the default speed to 0.95"* — tuned value baked in
- Sat Sep 20 — scrimmage. Autonomous scored; TeleOp drove. (Clips: `docs/video/scrimmage-match3-*.mp4`)

Image on 4a: `docs/images/android-studio-spitting-balls-prompt.png` — the "spitting balls out" prompt exactly as it was typed, with Gemini's answer.

**Slide 4b — the quote, alone on the slide:**
> "I've written Java for twenty-five years. I didn't type a line of it. After the first build, the changes came from the mechanical mentors — sometimes typed by them, sometimes called out from over the robot while I typed. They don't write Java."
> — Ben, our coordinator

**Notes:** Tell 4a as a story, not a list. Be precise about who did what, because coaches will ask: Ben described the architecture on night one and got the first TeleOp and the first autonomous. From then on it was a pair — Asim at the robot deciding what to change, Ben at the keyboard typing it — and for eight prompts Asim at the keyboard himself: tank drive, the strafe fix, the spitting-balls pair, a whole experimental autonomous with three rounds of tuning. The point for the room: **the person who understands the robot drives the prompt, whoever's hands are on the keys.** Two things make the experiment honest, and say both: Ben *can* write Java, so choosing to describe instead of type was a choice; Asim can't, and made most of the changes anyway. Then put 4b up and let it sit for a few seconds.

---

## 5. Two robots, one week: hand-coding vs. architecting with AI (1 slide, 3 min)

**Slide:** Two columns. This is the slide that's ours.

| Our robot — hand-coded | Mentor robot — described to Gemini |
|---|---|
| `[THE HIVE: how many of us could write the code?]` | 2 people wrote prompts; 1 of them writes Java, 1 doesn't |
| `[THE HIVE: hours spent coding during the week]` | 74 prompts over three days; first working TeleOp on night one |
| `[THE HIVE: what broke, and who could fix it]` | Mechanical mentor fixed strafe, flywheel bump, controls — in English, from over the robot |
| `[THE HIVE: how a mechanical change reached the code]` | Transfer servos pulled out Sep 18 at 7:50 PM; code reworked that night by describing the new design |
| `[THE HIVE: what we understood about our code]` | Someone still has to read what Gemini wrote — see Reader role, slide 11 |

**Notes:** The right column is from the mentor transcript (`docs/verbatim-transcript.md`); the left column is yours to fill from your own week — be concrete and be fair to both. The honest comparison is not "AI was faster": it's **where the bottleneck was.** On our robot every change went through whoever could type Java. On the mentor robot the bottleneck moved to *describing precisely* — and that's a skill the mechanical people already had. That's the thing we want every Utah team to hear.

---

## 6. What this is and isn't (1 slide, 2 min)

**Slide:** Two columns.

| It is | It isn't |
|---|---|
| Describing what the robot should do, in English | Copy-pasting code you don't understand |
| Testing after every change | Asking for the whole robot at once |
| Learning what your robot's parts are called and how they interact | Skipping the learning |
| A way for the whole team to change code | A way to avoid having anyone learn code |

**Notes:** Coaches worry about this. Say it plainly: you still have to understand what a motor is, which way it spins, what a state machine does. You just don't have to type the Java. Be honest that this hasn't been run with students yet — the mentors ran it on the mentor robot, and we're the first team trying to turn it into something students do. That's why there's a Reader role on slide 11.

---

## 7. The method: describe → generate → test → refine (1 slide, 2 min)

**Slide:** The loop, four boxes:

```
   DESCRIBE  →  "The intake is a motor named 'intake' plus two
                 CR servos. A toggles collect at 0.5 power..."
   GENERATE  →  Gemini writes or edits the OpMode
   TEST      →  Deploy to the robot. Try it.
   REFINE    →  "it is spitting balls out, so i would like to shorten it"
                 … back to GENERATE
```

**Notes:** The loop is the whole workshop. Every checkpoint in the guide is one trip around it. The skill you build is DESCRIBE and REFINE: being specific about names, directions, timing and order.

---

## 8. The checkpoints (1 slide, 2 min)

**Slide:** Numbered list, one line each — same as the README.

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

Repo URL on the slide.

**Notes:** Point out that "test" comes after every step. The single biggest mistake is asking for everything at once. Small steps mean when something breaks, you know which description caused it. There's a guide page and a video per checkpoint.

---

## 9. What a good description looks like (2 slides, 3 min)

**Slide 9a:** The five things every description needs:
1. **Names** — exactly as in the Robot Controller config (`front_left_drive`, not "the front left motor")
2. **Purpose** — what the part does for the robot
3. **Direction** — which way is "forward"; which motors are mounted mirrored
4. **Control** — which button or stick; **toggle or hold**; what happens on release
5. **Timing and order** — what has to happen before what, and for how long

**Slide 9b:** Vague vs. specific.

Vague: *"Add a shooter."*

Specific: *"One motor named `flywheel`, reversed. Pressing B runs the flywheel and intake backward at −0.5 for 200 ms to clear a jammed ball, stops everything for 300 ms, then runs the flywheel forward at full power and keeps it there. Pressing B again stops it. While it's running and right bumper is held, pulse the intake 100 ms on at 0.5, 200 ms off, so balls feed one at a time."*

Then the real refinement, verbatim, called out from over the robot:
> *"When I start up the flywheel, I want the process to continue to do the reverse function. But then I don't want the intake to resume. I want the intake to stop (whether it was stopped or running)."*

**Notes:** Read the vague one and ask the room what Gemini would have to guess. Then the specific one: everything it would have guessed is now stated. The verbatim one has no code words at all — it describes a sequence — and that's why it was a one-line fix in `Intake.java`. This is the slide for the mechanical kids in the room.

---

## 10. Live demo (1 slide + the demo, 7 min)

**Slide:** One line: "Now watch it happen." Under it, the tank-drive prompt as the mentors typed it, with the code it produced — `docs/images/android-studio-tank-drive-prompt.png`. Leave this up while switching to Android Studio.

**Setup before the session:** Android Studio open, FtcRobotController project loaded, Gemini panel signed in, the robot on the table, connected and configured, battery charged. The robot-description prompt already pasted and sent so you're not waiting on generation. **Dry-run the whole demo the day before on the same laptop and the same Wi-Fi-free setup.**

**Demo script (one of you drives the laptop, one narrates, one handles the robot):**
1. Show the Gemini panel with the robot description already there.
2. Type the mecanum drive prompt live. Send it. While it generates, narrate what you asked for.
3. Show the generated OpMode. Point at the hardware names — they match the config.
4. Build and deploy. Drive the robot.
5. Type a refinement live — the real one: *"hey, in the latest push, holding both joysticks left made the robot strafe right, and vice versa. please fix"* Deploy. Drive again.
6. Stop there. "That's the whole method. Everything else is more trips around the loop."

**If it breaks:** that's the method too. Say what you see, paste the error into the panel, and let the room watch the refine step. Don't apologize — a live fix is a better demo than a clean run.

---

## 11. Where it goes wrong, and the fix (1 slide, 2 min)

**Slide:** Table. The italic ones are the actual prompts.

| Symptom | Cause | Fix |
|---|---|---|
| Won't compile: "cannot find symbol" | Gemini invented a name | Paste the error back: "Fix this compile error" |
| Strafe is mirrored | Mecanum mixing signs | *"holding both joysticks left made the robot strafe right, and vice versa. please fix"* |
| Flywheel startup spits balls out | Reverse bump too long | *"what is the current time of spinning the motors back…? it is spitting balls out, so i would like to shorten it"* |
| Two controls on one button | Control scheme drifted | *"I thought gamepad 1 dpad was slow mode, not tuning the flywheel."* |
| Auto drives the intake through a *fake gamepad* | Nobody told Gemini an autonomous was coming | Say it up front: "An autonomous will use this class later — put the behavior in plain methods." Gemini takes the expedient route unless you say what's coming. |
| Auto runs over 30 s | Gemini was told the limit; nobody saw the sum | "Add up every state's duration and tell me the worst-case run time." Then cut one number. |
| Gemini rewrote everything, broke what worked | Didn't say "only change X" | "Only change the intake section. Leave the drive code as is." |

**Notes:** Every one of these happened on the mentor robot. The pattern: when it's wrong, the description was missing something. Say the missing thing. The fake-gamepad row is the one Ben cares most about: the auto worked, but it was tied to the TeleOp button map because nobody said an auto was coming.

---

## 12. Running this with your team (1 slide, 2 min)

**Slide:** Roles, then ground rules.

Roles:
- **Describer** — whoever knows the robot best says what to change. They can type it, or someone else can; what matters is whose words they are. On the mentor robot that was Asim.
- **Tester** — deploys and drives. Says exactly what happened.
- **Reader** — reads the generated code aloud and explains it. **This is where the learning happens.**
- **Coach** — asks "what did you tell it?" when something breaks. Doesn't type.

Ground rules:
- One change per prompt. Test after every change.
- Commit (or copy the file) after every working test.
- If Gemini rewrites something that worked, say "only change X."
- The Reader has to be able to explain the code before the team moves on.

**Notes:** The Reader role is the answer to "aren't they just cheating." If a student can explain what the state machine does and why the timings are what they are, they learned it. Tell the room which of these roles each of you would take on The Hive.

---

## 13. What you need to start (1 slide, 1 min)

**Slide:** Checklist.
- A laptop that runs Android Studio (Windows, Mac or Linux; 8 GB RAM minimum, 16 better)
- A Google account signed in to Gemini in Android Studio
- The FtcRobotController project cloned
- Your robot's hardware config done on the Driver Hub (names and ports)
- A written list of every motor and servo: name, port, purpose, direction
- The guide and videos: `github.com/The-Hive-3747/Teaching-AI-Coding`

**Notes:** The hardware list is the one thing to do *before* opening Gemini. For anyone installing tonight: during the first Gradle sync, Android Studio pops up three things. Accept the Daemon JVM toolchain migration. **Decline** the Gradle/AGP upgrade — it breaks the FTC project. On Windows, accept the Defender exclusion or builds crawl. Checkpoint 0 has all three.

---

## 14. Close (1 slide, 0 min)

**Slide:**
> Start small. Test every step. When it's wrong, describe what was missing.

Repo URL. "Find us at the pits — The Hive 3747." Logo.

**Notes:** No Q&A slot, so say where people can find you afterward.

---

## Timing

| Section | Min |
|---|---|
| 1–2 Title, who we are | 1 |
| 3 The problem | 1 |
| 4 The mentors' week | 4 |
| 5 Hand-coding vs. AI | 3 |
| 6 Is / isn't | 2 |
| 7 The loop | 2 |
| 8 Checkpoints | 2 |
| 9 Good descriptions | 3 |
| 10 Live demo | 7 |
| 11 Where it goes wrong | 2 |
| 12 Running it with a team | 2 |
| 13 What you need | 1 |
| **Total** | **30** |

If the demo runs long, drop slide 6.

---

## Assets

- [x] The Hive logo — `docs/images/hive-logo.svg` / `.png`. Title and close slides; palette gold **#F9BE14** on black.
- [x] `docs/images/android-studio-spitting-balls-prompt.png` — slide 4a
- [x] `docs/images/android-studio-tank-drive-prompt.png` — slide 10
- [x] Robot photos — `match-closeup-green-leds.jpg` (title), `match-shooting-at-goal.jpg` (slide 2), `scrimmage-field-wide-start.jpg` (slide 4a)
- [x] Scrimmage clips — `docs/video/scrimmage-match3-auto.mp4`, `scrimmage-match3-teleop-start.mp4` (play from the laptop during slide 4a if there's time; not embedded in the deck)
- [x] Repo URL: https://github.com/The-Hive-3747/Teaching-AI-Coding
- [ ] Slide 5 left column — The Hive's own numbers (`[THE HIVE: …]` placeholders)
- No fallback recording for the demo — it's live.
