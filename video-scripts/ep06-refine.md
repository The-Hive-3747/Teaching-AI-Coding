# Episode 6 — Refine: Fixing Directions, Timing, and Pulsing

**Length:** ~5 minutes
**Format:** Mostly robot and team footage, some screen recording, voice-over
**Narrator:** A mechanical team member. This is the episode that proves the point — the narrator should be someone who doesn't write Java.
**Guide page:** `guide/05-refine.md`
**Starting state:** Checkpoint 4 committed

---

## Script

**[TITLE CARD: Episode 6 — Refine]**

**[ON CAMERA or VO over footage of narrator at the laptop]**

I'm on the mechanical team. I built the intake. I don't write code. This is the episode where that stops mattering.

**[FOOTAGE: driver practicing, narrator watching]**

The robot works. Now it has to work well, and the only way to find out what "well" means is to drive it and complain. Every complaint becomes one sentence to Gemini.

**[TEXT CARD: "Driver: 'It's too twitchy.'"]**

Driver says it's too twitchy. That's not a prompt yet. What's twitchy? Small stick movements are too sensitive.

**[TEXT CARD: "The drive is too sensitive at small stick movements. Cube the joystick inputs so the first 30% of stick travel gives finer control." — hold 4 s]**

That's a prompt. Names the part — drive. Names the symptom — too sensitive at small movements. Names the change — cube the inputs. Send it, deploy, drive.

**[FOOTAGE: driver tries it, nods]**

Better. Commit.

**[TEXT CARD: "Driver: 'I can't line up on the goal, it's too fast.'"]**

Can't line up. Full speed is fine for crossing the field, but not for the last foot.

**[TEXT CARD: "Add a slow mode: while the left trigger is held, scale all drive powers by 0.4." — hold 4 s]**

Part, symptom, change, with the number. Deploy, test.

**[FOOTAGE: driver creeping into position with the trigger held]**

Lines up first try. Commit.

**[TEXT CARD: "Driver: 'I can't turn while I'm shooting.'"]**

**[TEXT CARD: "During the shooter's spin-up, the drive doesn't respond. Make sure the drive code runs every loop regardless of shooter state." — hold 4 s]**

**[FOOTAGE: driving while shooting]**

> **Ben:** replace these three examples with real refinements the mechanical team made. Pull them from the tuning log if there is one. Three is the right number for the video; put the rest in the guide.

**[SCREEN: the before/after table from guide 05-refine.md — hold 6 s]**

The pattern, every time. Weak prompt on the left, strong prompt on the right. The strong one names the part, names exactly what you saw, names the change, and changes one thing.

**[SCREEN: Gemini has rewritten a big chunk — the drive section is gone or different]**

Now the thing that will happen to you. I asked for a shooter change and Gemini rewrote the drive code too. Drive stopped working.

**[TEXT CARD: "You changed code outside the part I asked about. Restore the drive section exactly as it was, and only make the change I asked for." — hold 4 s]**

First try this. Usually works.

**[SCREEN: Git → revert to last commit]**

If it doesn't, go back to the last commit and ask again with "only change X" at the top. This is why we commit after every test. It took me one time losing an hour of work to believe that.

**[SCREEN: asking Gemini to explain]**

One more trick. When you're not sure what the code does, don't guess — ask.

**[TEXT CARD: "Before you change anything: explain in plain English what happens when the right trigger is pressed, step by step." — hold 4 s]**

If the explanation doesn't match what the robot does, you found the bug. If it doesn't match what you want, you found what you forgot to describe.

**[SCREEN: the tuning log]**

And keep a log. Date, who, what you asked, what happened, did you keep it. Ours has — however many — entries. When a judge asks how we developed the code, this is what we show them.

**[TEXT CARD: Checkpoint 5 checklist]**

Checkpoint five: the drivers are happy enough to practice, there's a log, and someone who didn't write the first prompts has made a change. That last one's me.

**[END CARD: "Next: Episode 7 — Autonomous: leave the wall and shoot" + repo URL]**

---

## Shot list

- [ ] Narrator at laptop (establishing)
- [ ] Driver practice footage
- [ ] 3 × (complaint card → prompt card → test footage)
- [ ] Before/after table
- [ ] Gemini overwrite + restore prompt
- [ ] Git revert
- [ ] Explain-first prompt
- [ ] Tuning log

## Notes

- The narrator choice is the message. If the mechanical team member is comfortable on camera, do the open and close on camera.
- Get the student's and parent's okay before publishing outside the team.
