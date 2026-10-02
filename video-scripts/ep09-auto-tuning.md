# Episode 9 — Under 30 Seconds and Match-Ready

**Length:** ~4 minutes
**Format:** Robot footage + screen recording, voice-over
**Narrator:** Mechanical team member
**Guide page:** `guide/08-auto-tuning.md`
**Starting state:** Checkpoint 7 committed

---

## Script

**[TITLE CARD: Episode 9 — Under 30 Seconds]**

Last episode. The autos work. Now they have to work every time, and they have to be done before the buzzer.

**[TEXT CARD: the two state-duration tables, each summing to a total — hold 5 s]**

First, add it up. Every state has a time. Shoot First: back up 0.3, settle 1.0, spool 2.0, shoot 10. Thirteen point three. Fine.

Shoot Delayed: 15 seconds of delay, then the same 13.3, then back up 0.45, turn 0.55, drive 3.0. Thirty-two point three.

**[TEXT CARD zoom: "32.3 s — OVER"]**

Over. The Driver Station kills the OpMode at thirty, so this robot gets cut off in the middle of the park drive. Every match. Nothing in testing tells you that. You have to add it up.

**[SCREEN: Gemini prompt]**

Where's the slack? Shooting. Ten seconds to launch three balls is generous. So the delayed auto gets a shorter shoot, and Shoot First keeps its ten:

**[TEXT CARD: "In `BaseAuto`, make the shoot duration depend on the delay: 10.0 seconds when the delay is 0, 8.3 seconds when the delay is greater than 0. Add a `getShootDurationSeconds()` method for it. Don't change anything else." — hold 5 s]**

That's 30.6. Still over. Hold that thought — there's one more thing to add first.

**[ROBOT: end of a Delayed run — robot pressed against the wall in the park zone]**

Look where it ends: pushed up against the wall. That scores the park. But the game gives *more* points for parking without touching the wall. So here's the prompt I typed — word for word:

**[TEXT CARD: "On the delayed autonomous, we end by bumping into the wall to get park points. However, we get points for not touching the wall. Can we add a backup after the turn and drive forward? I'm thinking 100ms for 0.5 power backwards at the end should move us away." — hold 6 s]**

Look at the shape of that. The rule — we get points for not touching the wall. The ask — a backup after the turn and drive. And a starting number — 100 milliseconds at 0.5. Gemini had nothing to guess. It added STATE_5C_PARK_BACK_OFF_WALL: all four motors at minus 0.5 for 100 milliseconds, between the park drive and ALL_STOP.

**[SCREEN: the STATE_5C case in BaseAuto.java]**

**[TEXT CARD: "30.6 + 0.1 = 30.7 s — still over"]**

Which brings the delayed auto to 30.7. Our final code shipped exactly like that — 30.7 on paper — and on the field it finished, off the wall. So the timer has a little slack. Don't count on it. A run that only works because the timer is generous is one low battery away from not working.

The fix is one more number:

**[TEXT CARD: "The delayed auto totals more than 30 seconds and the period is 30. Reduce the delayed shoot duration from 8.3 to 6.5 seconds so the whole run finishes with a full second to spare." — hold 5 s]**

Or shorten the delay itself, if your partner doesn't need the full fifteen. Either way, re-add the column until it's under 29.

**[TEXT CARD: "15 + 0.3 + 1.0 + 2.0 + 6.5 + 0.45 + 0.55 + 3.0 + 0.1 = 28.9 s"]**

Twenty-eight point nine. Under.

**[SCREEN: Gemini prompt]**

Whatever your total, add a safety timer.

**[TEXT CARD: "Add a safety timeout to `BaseAuto`. Use the existing `totalAutoTimer`. If it ever passes 29.5 seconds, regardless of the current state, set all drive motors to zero, stop the flywheel and intake, and go to `STATE_6_ALL_STOP`. Show the total elapsed time on telemetry (it may already be there)." — hold 5 s]**

If the total timer ever passes twenty-nine and a half, whatever state we're in: stop everything, go to ALL_STOP. The Driver Station will cut you off at thirty anyway, but this stops the robot cleanly from your own code, and it covers practice runs where nobody set a timer.

**[SCREEN: editor, the timeout check at the top of the loop]**

There it is. One check, before the switch.

**[SPLIT: Driver Hub | robot — five full Delayed runs, quick cuts, with a scorecard building up on screen]**

Now, five runs of each auto from the same starting spot. Every run: balls scored, ended in the zone, touching the wall, total time.

**[SCORECARD: 5 rows filling in]**

Run one: three balls, in the zone, clear of the wall. Run two: three, in, clear. Run three: three, in — touching. Run four: three, in, clear. Run five: three, in, clear.

Four out of five. The miss was the wall. Good enough to compete; let's see if we can get it to five.

**[TEXT CARD: "What went wrong: run 3 ended touching the wall"]**

**[TEXT CARD: "Change STATE_5C_PARK_BACK_OFF_WALL from 100 to 150 ms." — hold 3 s]**

**[ROBOT: five more runs, quick cuts, scorecard: 5/5]**

Five for five.

> **Ben:** replace with The Hive's real reliability numbers and whatever actually needed tuning. If it was park distance varying, show the "lower power, longer time" fix — that's the most useful one for other teams.

**[TEXT CARD: "Slower is more consistent" — with the STATE_5B 0.4 / 3.0 → 0.3 / 4.0 example]**

The general rule for time-based auto: if a distance varies run to run, lower the power and lengthen the time. Slower is more consistent — in our experience, nearly every time. Then re-add the total.

**[FOOTAGE: battery voltage on the Driver Hub]**

And note the battery voltage you tuned at. Low battery, shorter distances. If it's a problem, the guide has a prompt for scaling power by voltage.

**[TEXT CARD: match-day checklist — hold 5 s]**

Match day: battery charged and the voltage noted, wheels clean, starting position marked or jigged, the *right* OpMode selected — Shoot First or Shoot Delayed, agreed with your alliance partner — balls preloaded the same way every time, field clear.

**[SCREEN: Git commit, then Git → New Tag "week1-final"]**

Commit: "Auto final — delayed under 30 s, 5/5, non-contact park." And this time, tag it. "week1-final." When you add odometry next month and everything's different, this tag is how you get back to a robot that worked.

**[TEXT CARD: Checkpoint 8 checklist]**

Checkpoint eight: both autos add up to under 29 on paper, safety timeout in, non-contact park confirmed, five runs logged per auto with at least four good, someone can explain the timeout and every state, saved and tagged.

**[ON CAMERA or VO over the robot completing a full run]**

That's the series.

I build intakes. A week ago I'd never opened Android Studio. The prompts you just read — "it is spitting balls out," "can we add a backup after the turn" — those are ours. Some I typed, some I said out loud while someone else typed. Not because we learned Java, but because we knew what the robot needed to do and we learned how to say it precisely.

There's one more page in the guide, Checkpoint 9, with the extras we described into existence after this: LED lights so the driver can see from across the field that the flywheel's up to speed, an endgame rumble, and a four-ball autonomous experiment. Same method. Nothing new to learn.

Your mechanical team knows your robot better than anyone. Give them this, and see what they build.

Start with Episode 1.

**[END CARD: repo URL, "Start at Episode 1"]**

---

## Shot list

- [ ] Duration table cards (both autos)
- [ ] Shoot-duration prompt
- [ ] Robot against the wall at the end of a park + the verbatim non-contact prompt card
- [ ] STATE_5C in the editor
- [ ] 6.5 s prompt and the 28.9 s sum card
- [ ] Safety timeout prompt + editor
- [ ] 5 runs with scorecard overlay
- [ ] Failure + fix + 5 more runs
- [ ] "Slower is more consistent" card
- [ ] Battery voltage
- [ ] Match-day checklist card
- [ ] Commit + tag
- [ ] Closing: full run, narrator close, Checkpoint 9 mention

## Notes

- The scorecard overlay is worth the editing effort — it makes "reliability" concrete.
- If the narrator did Episodes 6–9, consider a two-line on-camera close from them instead of Ben. It lands harder.
- The non-contact park prompt is verbatim from the guide. Don't clean it up.
