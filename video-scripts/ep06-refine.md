# Episode 6 — Refine: The Mechanical Mentor's Real Prompts

**Length:** ~5 minutes
**Format:** Mostly robot and team footage, some screen recording, voice-over
**Narrator:** Asim, the mechanical mentor, if he's willing — this is the episode that proves the point. Otherwise Ben.
**Guide page:** `guide/05-refine.md`
**Starting state:** Checkpoint 4 committed

---

## Script

**[TITLE CARD: Episode 6 — Refine]**

**[ON CAMERA or VO over footage of narrator at the laptop]**

I'm the mechanical mentor. I built this robot. I don't write code. This is the episode where that stops mattering. Some of these prompts I typed myself. Some I called out from over the robot while Ben typed. Same thing — the words were mine.

**[FOOTAGE: driver practicing, narrator watching]**

The robot works. Now it has to work well, and the only way to find out what "well" means is to drive it and complain. Every complaint becomes one sentence to Gemini. The four you're about to see are the ones we actually typed, word for word, from Gemini's own log of the week.

**[TEXT CARD: "Driver: 'It's spitting balls out when the flywheel starts.'"]**

First one. Press B, and a ball pops out the front before the flywheel's even running. That's the reverse bump doing its job too well. Here's what I typed:

**[TEXT CARD: "what is the current time of spinning the motors back before starting the flywheel? it is spitting balls out, so i would like to shorten it" — hold 4 s]**

Lowercase, no code words. Two things to notice. It *asks first* — "what is the current time" — so I learned the number before I changed it. And it names the symptom — "spitting balls out" — instead of guessing a fix. Gemini said 200 milliseconds, and cut it to 100. Deploy, test.

**[FOOTAGE: B pressed, no ball ejected]**

No more spitting. Commit.

**[TEXT CARD: "Driver: 'The D-pad's changing the flywheel speed.'"]**

Second. The driver used the D-pad to creep forward and the flywheel speed changed. Both things were on gamepad 1's D-pad — precision drive from Episode 3, tuning from Episode 5. Here's the whole prompt:

**[TEXT CARD: "I thought gamepad 1 dpad was slow mode, not tuning the flywheel." — hold 4 s]**

One sentence, stated as an expectation. Gemini moved tuning to gamepad 2's D-pad and left gamepad 1's D-pad to driving.

**[FOOTAGE: driver creeping with gamepad 1 D-pad; second person tuning on gamepad 2]**

Commit.

**[TEXT CARD: "Driver: 'It jams when I start the flywheel with collect on.'"]**

Third, and this one's precise. If collect was on when you pressed B, the intake reversed for the bump — good — and then went right back to collecting, shoving the next ball into a flywheel that was still spooling up. Jam.

**[TEXT CARD: "When I start up the flywheel, I want the process to continue to do the reverse function. But then I don't want the intake to resume. I want the intake to stop (whether it was stopped or running)." — hold 5 s]**

Look at how carefully that describes the *sequence*. Keep the reverse. Then stop. Regardless of what it was doing before. The fix was one line in Intake.java.

**[FOOTAGE: collect on, B pressed, intake reverses then stays stopped]**

Commit.

**[TEXT CARD: "Driver: '0.95 shoots better than 1.0.'"]**

Fourth. After a few days of tuning on the gamepad 2 D-pad, the drivers found 0.95 shot cleaner than full power. So:

**[TEXT CARD: "Let's set the default speed to 0.95" — hold 3 s]**

That's the tuning loop closing. D-pad to experiment, then bake the answer into the code so nobody has to tune it at the start of every match.

> **Ben:** the strafe fix — "hey, in the latest push, holding both joysticks left made the robot strafe right, and vice versa. please fix" — also came from this phase; it's shown in Episode 3. Mention it here as "the fifth one you already saw" or leave it out. The four above are in the order Gemini's log has them.

**[SCREEN: the "What makes these prompts work" table from guide 05-refine.md — hold 6 s]**

The pattern, every time. Names the part — "spinning the motors back before starting the flywheel." Names what you saw — "spitting balls out." Names the change — "shorten it." None of them mention a variable, a file, or a line of Java. All of them were enough.

**[SCREEN: Gemini has rewritten a big chunk — the drive section is gone or different]**

Now the thing that will happen to you. I asked for a flywheel change and Gemini rewrote the drive code too. Drive stopped working.

**[TEXT CARD: "You changed code outside the part I asked about. Restore the drive section exactly as it was, and only make the change I asked for." — hold 4 s]**

First try this. Usually works.

**[SCREEN: Git → revert to last commit]**

If it doesn't, go back to the last commit and ask again with "only change X" at the top. This is why we commit after every test. We also asked Gemini to save backup copies before big changes — that's the `backup` folder in our code. Git or backups; do one of them.

**[SCREEN: asking Gemini to explain]**

One more trick. When you're not sure what the code does, don't guess — ask. The spitting-balls prompt did exactly this.

**[TEXT CARD: "Before you change anything: what is the current reverse bump time, and what happens step by step when I press B?" — hold 4 s]**

If the explanation doesn't match what the robot does, you found the bug. If it doesn't match what you want, you found what you forgot to describe.

**[SCREEN: the tuning log]**

And keep a log. Date, who, what you asked, what happened, did you keep it. Ours: reverse bump 200 to 100 — stopped spitting balls. Tuning to gamepad 2 — no more conflict with precision drive. Collect stops after the bump — no more jams. Default 0.95 — cleaner shots. When a judge asks how we developed the code, this is what we show them.

**[TEXT CARD: Checkpoint 5 checklist]**

Checkpoint five: the drivers are happy enough to practice with it, the tuning log has at least a few entries, someone who didn't write the original prompts has made a change, and it's saved. That third one's me.

**[END CARD: "Next: Episode 7 — Autonomous: back off the wall and shoot" + repo URL]**

---

## Shot list

- [ ] Narrator at laptop (establishing)
- [ ] Driver practice footage
- [ ] 4 × (complaint card → verbatim prompt card → test footage)
- [ ] "What makes these prompts work" table
- [ ] Gemini overwrite + restore prompt
- [ ] Git revert
- [ ] Explain-first prompt
- [ ] Tuning log

## Notes

- The narrator choice is the message. If Asim is comfortable on camera, do the open and close on camera.
- The four prompt cards are verbatim from the guide, typos and casing included. Don't clean them up — the point is that they worked as typed.
- Get the student's and parent's okay before publishing outside the team.
