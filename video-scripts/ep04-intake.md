# Episode 4 — Add the Intake

**Length:** ~3 minutes
**Format:** Screen recording + robot footage, voice-over
**Narrator:** A student (not the original programmer, if possible)
**Guide page:** `guide/03-intake.md`
**Starting state:** Checkpoint 2 committed

---

## Script

**[TITLE CARD: Episode 4 — Add the Intake]**

The drive works. Now we add one thing: the intake. This episode is short because that's the point — adding one subsystem to working code is fast when you say what not to touch.

**[SCREEN: Gemini panel, typing]**

**[TEXT CARD: the full Checkpoint 3 prompt — hold 5 s]**

"Add the intake to HiveTeleOp. Don't change any of the drive code — it works."

**[TEXT CARD zoom on that second sentence]**

That second sentence is the most important line in this episode. Without it, Gemini sometimes rewrites the whole file, and things that were fine stop being fine.

Then: map the motor named intake. Right bumper held, run it in. Left bumper held, run it out. Neither, stop. Telemetry.

**[SCREEN: Gemini generating, 4×]**

**[SCREEN: editor, scroll to the intake section]**

Two things to find. The mapping line for intake. And an if-else on the bumpers that sets power to one, minus one, or zero.

Quick question for whoever's reading: what happens if you hold both bumpers? Look at which if comes first. That's your answer. Doesn't matter which, just know it.

**[SCREEN: Run ▶ → "Install successfully finished"]**

**[SPLIT: Driver Hub | robot with a game piece in front of the intake]**

Test. Piece in front of the intake. Hold right bumper.

**[ROBOT: intake pulls piece in — or pushes it away]**

**[If it went the wrong way, keep it:]**

**[TEXT CARD: "What went wrong: Right bumper pushed the piece out"]**

Ours ran backwards. Most common intake bug there is. The fix is one sentence:

**[TEXT CARD: "The intake direction is backwards. Reverse the intake motor." — hold 3 s]**

Deploy. Test.

**[ROBOT: pulls in. Release — stops. Left bumper — pushes out. Release — stops.]**

In. Stop. Out. Stop.

**[ROBOT: driving while holding right bumper]**

And drive while intaking, because in a match you'll do both at once.

**[SCREEN: Git commit "Intake works"]**

Commit.

**[TEXT CARD: Checkpoint 3 checklist]**

Checkpoint three: five intake tests pass, drive still works, someone can explain what each bumper does, and it's saved.

**[END CARD: "Next: Episode 5 — Add the shooter" + repo URL]**

---

## Shot list

- [ ] Prompt + generation
- [ ] Editor intake section
- [ ] Deploy
- [ ] Robot: intake in/out/stop, with a piece
- [ ] Robot: drive + intake together
- [ ] Commit

## Notes

- Having a student narrate this one — ideally someone who wasn't the original programmer — is a deliberate choice. It shows the method transfers.
