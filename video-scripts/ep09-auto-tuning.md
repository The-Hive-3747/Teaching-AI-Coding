# Episode 9 — Under 30 Seconds and Match-Ready

**Length:** ~4 minutes
**Format:** Robot footage + screen recording, voice-over
**Narrator:** Mechanical team member
**Guide page:** `guide/08-auto-tuning.md`
**Starting state:** Checkpoint 7 committed

---

## Script

**[TITLE CARD: Episode 9 — Under 30 Seconds]**

Last episode. The auto works. Now it has to work every time, and it has to be done before the buzzer.

**[TEXT CARD: the state duration table, summing to a total — hold 5 s]**

First, add it up. Every state has a time. Leave wall, spin up, three pulses, turn, drive. Ours totals about seven seconds. The period is thirty. We're fine on time, so this episode is mostly about *reliability*.

If you're closer to thirty, the guide has a section on where to save time. The biggest one: start the flywheels while you're still driving off the wall, so spin-up overlaps.

**[SCREEN: Gemini prompt]**

Either way, add a safety timer. Even if you're nowhere near thirty.

**[TEXT CARD: the safety-timeout prompt — hold 5 s]**

A second ElapsedTime that starts at the beginning. If it ever passes 28 seconds, whatever state we're in: stop everything, go to DONE. The Driver Station will cut you off at thirty anyway, but this stops the robot cleanly from your own code, and it covers practice runs where nobody set a timer.

**[SCREEN: editor, the timeout check at the top of the loop]**

There it is. One check, before the switch.

**[SPLIT: Driver Hub | robot — five full runs, quick cuts, with a scorecard building up on screen]**

Now, five runs from the same starting spot. Every run: how many pieces scored, did it park, how long.

**[SCORECARD: 5 rows filling in]**

Run one: three pieces, parked. Run two: three, parked. Run three: two pieces, parked. Run four: three, parked. Run five: three, parked.

Four out of five clean. The miss was a piece. Good enough to compete; but let's see if we can get it to five.

**[TEXT CARD: "What went wrong: run 3, first shot was weak"]**

**[TEXT CARD: "Increase SPIN_UP from 1.5 to 2.0 seconds." — hold 3 s]**

**[ROBOT: five more runs, quick cuts, scorecard: 5/5]**

Five for five.

> **Ben:** replace with The Hive's real reliability numbers and whatever actually needed tuning. If it was drive distance varying, show the "lower power, longer time" fix — that's the most useful one for other teams.

**[TEXT CARD: "Slower is more consistent" — with the DRIVE_TO_PARK 0.5 / 2.0 → 0.35 / 2.8 example]**

The general rule for time-based auto: if a distance varies run to run, lower the power and lengthen the time. Slower is more consistent. Every time.

**[FOOTAGE: battery voltage on the Driver Hub]**

And note the battery voltage you tuned at. Low battery, shorter distances. If it's a problem, the guide has a prompt for scaling power by voltage.

**[TEXT CARD: match-day checklist — hold 5 s]**

Match day: battery charged, wheels clean, starting position marked, right OpMode selected — HiveAutoShoot, not the TeleOp — pieces loaded the same way every time, field clear.

**[SCREEN: Git commit, then Git → New Tag "week1-final"]**

Commit. And this time, tag it. "week1-final." When you add odometry next month and everything's different, this tag is how you get back to a robot that worked.

**[TEXT CARD: Checkpoint 8 checklist]**

Checkpoint eight: safety timeout in, five runs logged with at least four good, well under thirty, someone can explain the timeout and every state, saved and tagged.

**[ON CAMERA or VO over the robot completing a full run]**

That's the series.

I build intakes. A week ago I'd never opened Android Studio. The autonomous you just watched — I wrote the prompts for it. Not because I learned Java, but because I knew what the robot needed to do and I learned how to say it precisely.

Your mechanical team knows your robot better than anyone. Give them this, and see what they build.

Start with Episode 1.

**[END CARD: repo URL, "Start at Episode 1"]**

---

## Shot list

- [ ] Duration table card
- [ ] Safety timeout prompt + editor
- [ ] 5 runs with scorecard overlay
- [ ] Failure + fix + 5 more runs
- [ ] "Slower is more consistent" card
- [ ] Battery voltage
- [ ] Match-day checklist card
- [ ] Commit + tag
- [ ] Closing: full run, narrator close

## Notes

- The scorecard overlay is worth the editing effort — it makes "reliability" concrete.
- If the narrator did Episodes 6–9, consider a two-line on-camera close from them instead of Ben. It lands harder.
