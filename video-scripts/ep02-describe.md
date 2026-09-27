# Episode 2 — Describe Your Robot

**Length:** ~4 minutes
**Format:** Screen recording + Driver Hub footage, voice-over
**Narrator:** Ben
**Guide page:** `guide/01-describe-the-robot.md`
**Starting state:** Checkpoint 0 complete, Gemini panel empty

---

## Script

**[TITLE CARD: Episode 2 — Describe Your Robot]**

**[PIP: robot on the table]**

Before Gemini writes a single line, it needs to know what it's writing for. This episode is about getting that description right, because everything after this depends on it.

**[SCREEN: Driver Hub → Configure Robot → config → Edit]**

Start on the Driver Hub. Configure Robot, open your config, and go port by port. Every motor and servo has a name here. Those exact names — same spelling, same capitalization — are what Gemini has to use. If the config says `frontLeft` and Gemini writes `front_left`, the robot crashes on Init.

**[SCREEN: a table being filled in — text editor or the guide's ROBOT.md]**

So write them all down. Name, type, which hub and port, what it does, and which way it spins. If you don't know the direction yet, write "unknown" — you'll find out when you test.

**[TEXT CARD: the hardware table from the guide — hold 4 s]**

Here's ours.

> **Ben:** replace with The Hive's real table on the card.

Notice the direction column. The left drive motors are mounted mirrored, so they need to be reversed. The two flywheels face each other, so they spin opposite ways. Gemini can't see the robot. If you don't say this, it guesses, and it guesses wrong about half the time.

**[SCREEN: controls table]**

Then controls. Decide as a team what every button does, and write that down too. Left stick drives, right stick turns, bumpers for intake, trigger for shooting. Whatever you pick — pick it now, not while you're prompting.

**[SCREEN: Gemini panel, pasting the description prompt]**

Now open Gemini and paste it all in as one message.

**[TEXT CARD: the full Checkpoint 1 prompt — hold 6 s, or scroll slowly]**

Robot type. Every motor by name. What each one does. Which ones are reversed. The controls. And the last line is the important one:

**[TEXT CARD zoom: "Don't write any code yet. Just confirm you understand the robot and tell me if anything is unclear."]**

Don't write code yet. We want Gemini to read it back to us, so we can catch misunderstandings before they turn into code.

**[SCREEN: Gemini's reply, sped up if long]**

Here's the reply. It restates the robot. Read this carefully. If it says something like "both flywheels spin the same direction" — that's wrong, and this is the moment to fix it. Just reply: "No, they face each other and spin opposite directions."

**[SCREEN: saving ROBOT.md into the TeamCode folder]**

Last thing: save the description as a file in the project. Gemini conversations don't last forever, and you'll want to paste this again later. Putting it in the project also means Gemini can find it.

**[TEXT CARD: Checkpoint 1 checklist, 4 items]**

Checkpoint one: every device in the table with its exact name, controls agreed, Gemini has confirmed and been corrected, and the description is saved. No robot test yet — that's next.

**[END CARD: "Next: Episode 3 — Mecanum drive in one prompt" + repo URL]**

---

## Shot list

- [ ] Driver Hub config screen (film or screen-record)
- [ ] Table being filled in
- [ ] Gemini prompt paste
- [ ] Gemini reply
- [ ] Saving ROBOT.md

## Notes

- If Gemini's reply actually gets something wrong on the take, keep it. Correcting it on camera is better than a clean take.
