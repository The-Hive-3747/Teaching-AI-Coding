# Episode 2 — Describe Your Robot

**Length:** ~4 minutes
**Format:** Screen recording + Driver Hub footage, voice-over
**Narrator:** Student (Tom)
**Guide page:** `guide/01-describe-the-robot.md`
**Starting state:** Checkpoint 0 complete, Gemini panel empty

---

## Script

**[TITLE CARD: Episode 2 — Describe Your Robot]**

**[PIP: robot on the table]**

Before Gemini writes a single line, it needs to know what it's writing for. This episode is about getting that description right, because everything after this depends on it.

**[SCREEN: Driver Hub → Configure Robot → config → Edit]**

Start on the Driver Hub. Configure Robot, open your config, and go port by port. Every motor and servo has a name here. Those exact names — same spelling, same capitalization — are what Gemini has to use. If the config says `front_left_drive` and you tell Gemini "the front left motor," it invents a name, the code compiles, and the robot crashes on Init with "Unable to find a hardware device."

**[SCREEN: a table being filled in — text editor or the guide's AGENTS.md]**

So write them all down. Name, type, which hub and port, what it does, and which way it spins. If you don't know the direction yet, write "unknown — test." You'll find out when you test.

**[TEXT CARD: the hardware table from the guide — hold 4 s]**

Here's ours. Four drive motors: `front_left_drive`, `front_right_drive`, `back_left_drive`, `back_right_drive`. One `flywheel`. One `intake` motor, plus two continuous-rotation servos, `intake_servo_left` and `intake_servo_right`, that roll balls in. And two LED lights we'll get to at the very end.

> **Ben:** the guide's motor port numbers are placeholders — confirm them against the real config before this card goes on screen.

Notice the direction column. The left drive motors are mounted mirrored, so they're reversed. The flywheel is mounted so it needs reversing too. And the left intake servo is mirrored, so it's reversed so both rollers pull in. Gemini can't see the robot. If you don't say this, it guesses, and it guesses wrong about half the time.

**[SCREEN: controls table]**

Then controls. Decide as a team what every button does, and write that down too. Ours: tank drive — left stick drives the left wheels, right stick drives the right, push either stick sideways to strafe. D-pad for slow precision moves. A toggles the intake to collect. B toggles the flywheel. Hold right bumper to feed. Left bumper or X toggles reject, which runs everything backward.

Two words matter there: *toggle* and *hold*. Collect, flywheel, reject — those stay on until you press again. Feeding stops the instant you let go. Say which is which, or Gemini will guess.

**[SCREEN: the flywheel startup sequence written as numbered steps]**

And anything with timing, write out as numbered steps. Ours: press B. Flywheel and intake run backward for a moment to push out a jammed ball. Everything stops for a moment. Flywheel spins forward and stays there. Now holding right bumper pulses the intake so one ball feeds at a time. The numbers on those "moments" — 200 milliseconds, 300 milliseconds — you can change later. Missing steps you can't.

**[SCREEN: Gemini panel, pasting the description prompt]**

Now open Gemini and paste it all in as one message.

**[TEXT CARD: the full Checkpoint 1 prompt — hold 6 s, or scroll slowly]**

Every motor and servo by name. What each one does. Which ones are reversed. The controls — toggle or hold. The startup sequence with its numbers. Non-blocking, no sleep. And the last line is the important one:

**[TEXT CARD zoom: "What questions do you have?"]**

What questions do you have. That's how our real first prompt ended. Gemini came back with five numbered questions — iterative or linear, how big a tuning step, that kind of thing — and the whole answer was:

**[TEXT CARD: "1. iterative 2. 0.05 per step 3. Let's have them run at fixed speeds of 0.3 4. Yes.  5. Yes." — hold 4 s]**

And then the code appeared. Let it ask. Answer the questions. That's cheaper than fixing guesses.

**[SCREEN: Gemini's reply, sped up if long]**

Here's the reply. It restates the robot. Read this carefully. If it restates the startup as "spin the flywheel backward for 200 milliseconds" and leaves the intake out — that's wrong, and this is the moment to fix it. Just reply: "No — the intake reverses with the flywheel during the bump."

**[SCREEN: saving AGENTS.md in the project root]**

Last thing: save the description as a file in the project. Save it as AGENTS.md in the project root. Agent mode reads that file as standing instructions, so the description rides along with every prompt from now on.

**[TEXT CARD: Checkpoint 1 checklist, 5 items]**

Checkpoint one: every device in the table with its exact name, the team agrees on the controls and which ones toggle versus hold, every timed sequence written as numbered steps, Gemini has confirmed and been corrected, and the description is saved. No robot test yet — that's next.

**[END CARD: "Next: Episode 3 — Mecanum drive in one prompt" + repo URL]**

---

## Shot list

- [ ] Driver Hub config screen (film or screen-record)
- [ ] Table being filled in
- [ ] Startup sequence as numbered steps
- [ ] Gemini prompt paste
- [ ] Gemini reply
- [ ] Saving AGENTS.md

## Notes

- If Gemini's reply actually gets something wrong on the take, keep it. Correcting it on camera is better than a clean take.
