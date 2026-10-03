# Kickoff Presentation — Outline and Speaker Notes

**Presenters:** The Hive (FTC 3747) students — Tom, Sophi and Sadiqah. The mentors who ran the experiment are quoted on the slides, not on stage.
**Audience:** FTC teams from across Utah — students and coaches in the same room. Assume some have never opened Android Studio and some have a working TeleOp already.
**Length:** about 38 minutes, no Q&A (30 before the "why this matters" section was added; Ben's call: don't hold to time, get them the information).
**Goal:** By the end, every team knows what the mentor experiment showed, knows the checkpoint order, and knows where the guide and videos are — and believes they can do this next week.

**The frame, in one sentence:** At Robot in One Week, The Hive coded their robot by hand, the traditional way. The mentors had their own robot for the mentor competition and ran an experiment on it: no hand-written code — everything described to Gemini in Android Studio. The students saw both, and this talk is what they took from it: **hand-coding vs. architecting with AI**, and how any Utah team can use the second one.

Structure (reordered Oct 3 for a skeptical room): who we are and the plain facts → the problem → the programmer's new job → the objection, stated by us first ("AI can shrink your skills") → our rule → the seventy-year pattern → *then* the mentors' experiment → the method → the demo → what's at stake (Lehi, the door) → how to run it → training sessions and close. The stakes come after the demo on purpose: before trust, job statistics read as pressure; after they've watched it work, they read as opportunity.

Sourcing for slides 5–7 and 15–16: every quote and number is sourced and flagged in [`docs/research-ai-skills-case.md`](../docs/research-ai-skills-case.md); read its last two sections (hostile questions; "must not repeat") before presenting. Where a line is our inference rather than a finding, the slide says "we think" — keep it that way.

**Deck file:** `slides/kickoff.pptx` — built from this outline; import into Google Slides with **File → Import slides**. Logo gold `#F9BE14` on black. A designed version of the same deck also exists as a Claude Slides artifact (Ben has the link); it adds one slide — the "spitting balls out" prompt as typed — and plays the Test Match 3 auto clip on the timeline slide.

---

## 1. Title (1 slide, 0 min)

**Slide:** "Describe your robot. Let AI write the code. Test. Repeat."
Subtitle: What The Hive learned watching our mentors code a robot in one week with Gemini in Android Studio — and how your team can do it too.
Footer: The Hive · FTC 3747 · Beehive Academy. Logo.

**Notes:** On screen while people sit down. No talking needed.

---

## 2. Who we are and what this is (1 slide, 1 min)

**Slide:** Three lines.
- We're The Hive, FTC 3747. This year our students built and coded our robot by hand. No AI.
- Our mentors entered the mentor competition with a separate robot, and ran an experiment on it: **no hand-written Java. Everything described to Gemini.**
- We watched both. This is what the experiment showed, what we'd do with it, and how you can use it.

**Notes (Tom/Sophi/Sadiqah — whoever opens):** Say every line plainly; this room suspects mentors do our work. Line 1: our students hand-coded our robot this year — no AI. Line 2: the AI experiment was the mentors', on a separate robot. Line 3: we're handing over what it showed. Don't argue the rumor; state the facts and move on.

---

## 3. The problem every team has (1 slide, 1 min)

**Slide:** Three bullets, big text:
- Most teams have 1 programmer. Sometimes 0.
- Every mechanical change waits on that one person.
- The mechanical people know what the robot should do — they just can't type it in Java.

**Notes:** Ask for a show of hands: "How many teams have exactly one person who can write Java?" Then: "How many of those people are also the coach?" Let it land. This is the pain the method solves. If you want, say which of us is the one programmer on The Hive.

---

## 4. The programmer doesn't go away. The job changes. (1 slide, 2 min)

**Slide:** Two cards.

*Before anyone else touches it* — the programmer:
- **Describes** the robot — every motor and servo, name, port, direction
- **Architects** it — subsystems, state machines, what the autonomous will need later
- **Sets the process** — one change per prompt, test, commit
- That groundwork is what lets a mechanical teammate make a change in English and have it land in the right place

*After every change* — the programmer **reviews** what the AI did and catches drift. On the mentor robot: the autonomous that drove the intake through a fake gamepad; the D-pad that ended up doing two things — slow mode *and* flywheel tuning — instead of one. Both worked. Both were wrong. Only a programmer reading the code saw it.

Takeaway: *The programmer gets the big picture back: "What do I want vision to do?" instead of "How do I parse the Limelight result?"*

**Notes:** This is the answer to "AI replaces the programmer" — it doesn't; it moves them up. Both drift examples are verbatim in `docs/verbatim-transcript.md` (the simulated gamepad in `BaseAuto`; prompt 64, "I thought gamepad 1 dpad was slow mode, not tuning the flywheel"). What the programmer gets back is the big picture: on our robot, adding one autonomous state was 10–20 minutes of copying boilerplate.

---

## 5. AI can shrink your skills. Or multiply them. (1 slide, 2 min)

**Slide:** Three stat cards, red:
- **−17% on the exam** — PNAS 2025, about 1,000 high-school students (math), randomized; plain ChatGPT: practice +48%, unassisted exam −17%; a hints-only tutor "largely mitigated" the harm
- **+19% longer, not shorter** — METR 2025, 16 expert maintainers on their own code, 246 tasks; believed they'd been 20% faster
- **50 vs 67 comprehension score** — Anthropic, Jan 2026, their own tool, their own study (not peer-reviewed); 52 mostly-junior engineers; delegating hurt, asking questions didn't
Takeaway: *The difference is how you use it. Delegate and auto-accept: you lose understanding. Ask, read the diff, explain it: you don't. That's our rule — next slide.*

**Notes:** This is the slide that earns the right to be listened to: the objection, stated by us, with better evidence than the room has. Say it before they do. If MIT "Your Brain on ChatGPT" comes up: essays, 18 people, criticized stats — don't use it either way.

## 6. Our rule: no AI until you can debug without it (1 slide, 2 min)

**Slide:** The checklist quote in full:
> *"Do not use AI to help you on these assignments. We will probably use AI during the season. However, if you do not understand the basics of programming taught here, you will not be able to debug the code during the season. And, it becomes painfully obvious if you use AI because you cannot answer how your program works."* — The Hive, 2026 Programming Training Checklist

The gate: **14** chapters of *Learn Java for FTC* by hand (14 of 15; analog sensors skipped) · **40** FTCSIM exercises · **0** AI until the mentor has checked every box, in order · **then** AI as a season tool, and you still explain the code. Same rule as AP CS Principles ("students must be prepared to explain their code in detail" — College Board) and FTC judging (students answer; adults may only observe).
Takeaway: *Fundamentals first, then AI. We think that's how you get the speed without losing the understanding.*

**Notes:** "We think" is deliberate — no study has tested the sequence; it's our reading of the mode findings (5c) plus two studies where prior skill helped (Prather 2024; Kazemitabaar 2023).

## 7. Seventy years of "that's not real programming" (1 slide, 1 min)

**Slide:** Timeline, one quote per row:
- 1954, FORTRAN — *"the strength of the skepticism about 'automatic programming'… as it existed in 1954"* (John Backus, who led the FORTRAN team, writing in 1978)
- 1983, Pascal — *"Real Programmers use FORTRAN. Quiche Eaters use PASCAL."* (Ed Post)
- 2005, IDE autocomplete — *"And I think it's making us dumber."* (Charles Petzold, on IntelliSense)
- 2017, Stack Overflow — 15.4% of 1.3 M Android apps contained security-related code copied from Stack Overflow; 97.9% of those had an insecure snippet (Fischer et al., IEEE S&P)
- 2026, AI coding — Gemini drove our mentors' autonomous through a fake gamepad. It worked. A human reading the code caught it.
Takeaway: *Every layer took the typing. None took the architecture, the testing, or the reading.* Brooks 1987: "the hard part of building software [is] the specification, design, and testing of this conceptual construct, not the labor of representing it."

**Notes:** Say "most layers," not "every" — there's no named 1954 skeptic, only Backus remembering them. Fowler's limit (2025): this layer is non-deterministic, so reading and testing matter more, not less. Tell the fake-gamepad story here.

## 8. What the mentors did — the week (2 slides, 4 min)

**Slide 4a — timeline.** One line per session, from Gemini's own log of the build. The quotes are verbatim, typos and all.
- Sep 17, 10:26 PM — Ben (coordinator) describes the whole robot → working TeleOp that night, split into subsystem classes. Ben keeps building it out through Sep 18 (shooter startup sequence, pulsed feed, LEDs)
- Sep 18, ~9:50 PM — first autonomous state machines (Shoot First / Shoot Delayed): Asim's design, called out at the robot, typed by Ben
- Sep 19, 8:29 AM — Asim (mechanical mentor), at the keyboard: *"hey, in the latest push, holding both joysticks left made the robot strafe right, and vice versa. please fix"*
- 8:36 AM — *"what is the current time of spinning the motors back before starting the flywheel? it is spitting balls out, so i would like to shorten it"* — reverse bump 200 → 100 ms
- 8:40 AM — *"On the delayed autonomous, we end by bumping into the wall to get park points. However, we get points for not touching the wall. Can we add a backup after the turn and drive forward? I'm thinking 100ms for 0.5 power backwards at the end should move us away."*
- 11:35 AM — *"I thought gamepad 1 dpad was slow mode, not tuning the flywheel."*
- 11:37 AM — *"When I start up the flywheel, I want the process to continue to do the reverse function. But then I don't want the intake to resume. I want the intake to stop (whether it was stopped or running)."*
- 2:57 PM — *"Let's set the default speed to 0.95"* — default changed from 1.0
- Scrimmage day — autonomous scored; TeleOp drove. (Clips: `docs/video/scrimmage-match3-*.mp4`)

Image on 4a: `docs/images/android-studio-spitting-balls-prompt.png` — the "spitting balls out" prompt exactly as it was typed, with Gemini's answer. (Gemini reported the numbers and offered two options — then changed both numbers in the same turn; the next prompt put the power back. Say so if you show it: one change, one test, even when Gemini does two.)

**Slide 4b — the quote, alone on the slide:**
> "I've written Java for twenty-five years. I didn't type a line of it. After the first build, the changes came from the mechanical mentors — sometimes typed by them, sometimes called out from over the robot while I typed. They don't write Java."
> — Ben, our coordinator

(This line is Ben's summary written for the deck, not a transcript quote. Ben approved the wording on Oct 1, 2026.)

**Notes:** Tell 4a as a story, not a list. Be precise about who did what, because coaches will ask: Ben described the architecture on night one and built the TeleOp out over the first two days — the shooter startup sequence, the pulsed feed, the LEDs. The first autonomous was Asim's design, called out at the robot while Ben typed, and from there it was a pair — Asim deciding what to change, Ben typing — plus eight prompts Asim typed himself: tank drive, the strafe fix, the spitting-balls pair, a whole experimental autonomous with three rounds of tuning. The point for the room: **the person who understands the robot drives the prompt, whoever's hands are on the keys.** Two things make the experiment honest, and say both: Ben *can* write Java, so choosing to describe instead of type was a choice; Asim can't, and made most of the changes anyway. Then put 4b up and let it sit for a few seconds.

---

## 9. Two robots, one week: hand-coding vs. architecting with AI (1 slide, 3 min)

**Slide:** Two columns. This is the slide that's ours.

| Our robot — hand-coded | Mentor robot — described to Gemini |
|---|---|
| 3 of us could code it: Tom, Sophi, Sadiqah. Siloed — Tom wrote TeleOp, Sophi wrote autonomous | 2 people wrote prompts; 1 of them writes Java, 1 doesn't |
| 4 days of coding | 67 prompts over two days and an evening; working TeleOp the first night |
| A borrowed sample autonomous only powered two wheels — the other two were braking. Tom saw the robot crawl in morning testing, then had to read Sophi's code cold to find why | Mechanical mentor fixed strafe, flywheel bump and controls — in English, no Java |
| Programmer codes while the mechanical teammate waits; often one programmer does it all and kludges around mechanical problems. One new autonomous state: 10–20 minutes of boilerplate | Firewheel transfer servos came out Sep 18 (reported 7:50 PM); code reworked that night by describing the new design |
| Each of us understood our own file. Nobody could summarize the other's | Someone still has to read what Gemini wrote — the Reader role |

**Notes:** This is the slide that's ours, and the left column is our real week. Four days of coding, three of us who could code the robot, and we were siloed: Tom wrote TeleOp, Sophi wrote autonomous. The bug: an autonomous borrowed from the FtcRobotController samples only powered two wheels, and the other two were set to brake at zero power — so the robot crawled. Tom saw it in testing the morning of the competition, then had to read Sophi's code cold, without knowing what she had done, to find why. Say the thing we'd do differently: AI could have given Tom a summary of the autonomous and he could have questioned it to find the bug. The grunt work: adding one state to the autonomous meant copying the same surrounding code again — 10 to 20 minutes for a new programmer, every time. Our process: the programmer works while a mechanical teammate waits, then sets up the robot and both watch the test; mechanical problems get fixed if quick or go on the to-do list; often it's one programmer doing all of it and kludging around mechanical problems in code. The honest comparison is not 'AI was faster': it's where the bottleneck was. On our robot every change went through whoever could type Java. On the mentor robot the bottleneck moved to describing precisely — and that's a skill the mechanical people already had. The right column is from the mentor transcript: the Sep 17 session was 25 minutes and 5 prompts, then the 18th and the 19th.

---

## 10. The method: describe → generate → test → refine (1 slide, 2 min)

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

## 11. The checkpoints (1 slide, 2 min)

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

## 12. What a good description looks like (2 slides, 3 min)

**Slide 9a:** The five things every description needs:
1. **Names** — exactly as in the Robot Controller config (`front_left_drive`, not "the front left motor")
2. **Purpose** — what the part does for the robot
3. **Direction** — which way is "forward"; which motors are mounted mirrored
4. **Control** — which button or stick; **toggle or hold**; what happens on release
5. **Timing and order** — what has to happen before what, and for how long

**Slide 9b:** Vague vs. specific.

Vague: *"Add a shooter."*

Specific: *"One motor named `flywheel`, reversed. Pressing B runs the flywheel and intake backward at −0.5 for 200 ms to clear a jammed ball, stops everything for 300 ms, then runs the flywheel forward at full power and keeps it there. Pressing B again stops it. While it's running and right bumper is held, pulse the intake 100 ms on at 0.5, 200 ms off, so balls feed one at a time."*

Then a real refinement from the Sep 19 session, verbatim:
> *"When I start up the flywheel, I want the process to continue to do the reverse function. But then I don't want the intake to resume. I want the intake to stop (whether it was stopped or running). Does that make sense?"*

**Notes:** Read the vague one and ask the room what Gemini would have to guess. Then the specific one: everything it would have guessed is now stated. The verbatim one has no code words at all — it describes a sequence — and that's why the fix was a few lines in `Intake.java`. This is the slide for the mechanical kids in the room.

---

## 13. Live demo (1 slide + the demo, 7 min)

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

## 14. Where it goes wrong, and the fix (1 slide, 2 min)

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

**Notes:** The italic rows and the fake-gamepad row happened on the mentor robot; the compile-error and over-30-seconds rows are the ones you'll hit first that the mentors got lucky on (their delayed auto adds up to 30.7 s on paper and the field timer let it finish). The pattern: when it's wrong, the description was missing something. Say the missing thing. The fake-gamepad row is the one Ben cares most about: the auto worked, but it was tied to the TeleOp button map because nobody said an auto was coming.

---

## 15. Lehi, 2026: it's in the job posting (1 slide, 1 min)

**Slide:** Four postings (verified live Oct 3, 2026 — re-open the links the morning of):
- Podium, Lehi, Software Engineering Intern: *"Familiarity with AI-assisted development tools such as Cursor, Claude Code, Codex, GitHub Copilot, or similar."* — alongside "Strong programming fundamentals…"
- Podium, Lehi, Senior SWE: use AI tooling *"as a genuine force multiplier, not a novelty"*; *"A hands-on, daily relationship with AI coding tools"*
- Affirm (US fintech; remote role, Poland): *"every Affirm engineer builds with tools like Claude Code and most PRs are co-authored with AI… excelling in the reviewer's seat… is a core expectation"*
- 1Password: *"Effective at using AI tooling to accelerate development, testing, debugging…"* — and the interview is run without AI
Stats: 2.5% of US postings asked for AI skills in 2025, +55% in a year (Stanford AI Index 2026 / Lightcast) · one Sep 2026 scan of 4,820 SWE postings: 10% required AI coding tools, 20% mentioned them, interns highest at 27% (match.dev) · *"Reflexive AI usage is now a baseline expectation at Shopify."* (Lütke, Apr 2025, as quoted in press). Footer: job ads with AI skills advertise higher pay — PwC 62% within occupation (global, 2025), Lightcast 28% (US, 2024); vendor data, correlation.

**Notes:** Never say "Lightcast 56%" (no source). Don't use Microsoft's "66% won't hire without AI skills" (2024, vendor, self-reported).

## 16. The door is narrower. The tool is expected. (1 slide, 1 min)

**Slide:**
- **−20%** software developers aged 22–25, employment down nearly 20%, late 2022 → Sep 2025, payroll data; for young workers in AI-exposed jobs the gap widened to 19% by Aug 2026 (Stanford "Canaries")
- **16.5%** of entry-level job descriptions emphasize AI skills, up from 10.5% in fall 2025; employers say over a third of entry-level jobs require them (NACE spring 2026, 185 employers)
- **50.5%** of graduating seniors aren't building AI skills at all (NACE student survey 2026)
Quote: *"The value of 90% of my skills just dropped to $0. The leverage for the remaining 10% went up 1000x."* — Kent Beck, creator of Extreme Programming, April 2023
Takeaway: *We think: the jobs disappearing are the ones AI does instead of a person. The ones being posted are the reviewer's seat. The checklist teaches the 10%.*

**Notes:** Concede the nearly-20% number completely; it shows exposed young workers lost ground, not that AI skills protect an individual — that half is ours. NACE is small and self-reported; lead with 16.5%.

---

## 17. Running this with your team (1 slide, 2 min)

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

## 18. What you need to start (1 slide, 1 min)

**Slide:** Checklist.
- A laptop that runs Android Studio (Windows, Mac or Linux)
- A Google account signed in to Gemini in Android Studio
- The FtcRobotController project cloned
- Your robot's hardware config done on the Driver Hub (names and ports)
- A written list of every motor and servo: name, port, purpose, direction
- The guide and videos: `github.com/The-Hive-3747/Teaching-AI-Coding`

Footer line: *Or bring your robot to a Hive training session. We'll teach your students to prompt. You leave with a driving robot — maybe more.*

**Notes:** The training-session offer is the real call to action; hands-on use is the only thing the attitude data associate with changed minds, and it's the answer to "it's a Hive thing." Dates/format: `[THE HIVE: when, where, how to sign up]`. The hardware list is the one thing to do *before* opening Gemini. For anyone installing tonight: during the first Gradle sync, Android Studio pops up three things. Accept the Daemon JVM toolchain migration. **Decline** the Gradle/AGP upgrade — it breaks the FTC project. On Windows, accept the Defender exclusion or builds crawl. Checkpoint 0 has all three.

---

## 19. Who made this (1 slide, 1 min)

**Slide:** Names, two columns.
- **The Hive's students** — hand-coded the team's own robot this season; Tom, Sophi and Sadiqah present this workshop and narrate the videos
- **Ben — coordinator** — described the mentor robot's architecture to Gemini on night one; typed no Java
- **Annie — head coach**
- **Asim — mechanical mentor** — built the mentor robot; authored most of the later changes in plain English, eight at the keyboard
- **Royd — coach and mechanical engineer** — second coach on The Hive
- **Claude (Anthropic)** — drafted the guide, deck, scripts and research from the real transcript; every claim refuter-checked against sources
- **Gemini in Android Studio (Google)** — wrote every line of the mentor robot's code from the mentors' descriptions
Takeaway: *This workshop was made the way it teaches: described, generated, tested, refined — with the prompts kept.*

**Notes:** Read the names. The people are named and the AI is named — the same way we're asking every team to credit its code.

---

## 20. Close (1 slide, 0 min)

**Slide:**
> Start small. Test every step. When it's wrong, describe what was missing.

Repo URL. "Training sessions: bring your robot, leave with it driving. Find us at the pits — The Hive 3747." Logo.

**Notes:** No Q&A slot, so say where people can find you afterward.

---

## Cut: "What this is and isn't"

Dropped from the running order on Oct 3 — slides 4 and 5 now make the same point with evidence. The slide's content is kept in git history (`build-deck.js`, block "6. Is / isn't", commit 719fc59) if it's wanted back.

## Timing

| Section | Min |
|---|---|
| 1–2 Title, who we are | 1 |
| 3 The problem | 1 |
| 4 The programmer's new job | 2 |
| 5 Shrink or multiply | 2 |
| 6 Our rule | 2 |
| 7 Seventy years | 1 |
| 8 The mentors' week | 4 |
| 9 Hand-coding vs. AI | 3 |
| 10 The loop | 2 |
| 11 Checkpoints | 2 |
| 12 Good descriptions | 3 |
| 13 Live demo | 7 |
| 14 Where it goes wrong | 2 |
| 15–16 Lehi, the door | 2 |
| 17 Running it with a team | 2 |
| 18 What you need, training sessions | 1 |
| 19 Who made this | 1 |
| **Total** | **38** |

If the demo runs long, slide 7 (seventy years) can go into the notes of slide 8.

---


## Assets

- [x] The Hive logo — `docs/images/hive-logo.svg` / `.png`. Title and close slides; palette gold **#F9BE14** on black.
- [x] `docs/images/android-studio-spitting-balls-prompt.png` — slide 4a
- [x] `docs/images/android-studio-tank-drive-prompt.png` — slide 10
- [x] Robot photos — `match-closeup-green-leds.jpg` (title), `match-shooting-at-goal.jpg` (slide 2), `scrimmage-field-wide-start.jpg` (slide 4a)
- [x] Scrimmage clips — `docs/video/scrimmage-match3-auto.mp4`, `scrimmage-match3-teleop-start.mp4` (play from the laptop during slide 4a if there's time; not embedded in the deck)
- [x] Repo URL: https://github.com/The-Hive-3747/Teaching-AI-Coding
- [x] Slide 8 left column — The Hive's own week (from Ben, Oct 3)
- No fallback recording for the demo — it's live.
