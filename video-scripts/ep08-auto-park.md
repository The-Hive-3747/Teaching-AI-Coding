# Episode 8 — Autonomous: Shoot First, Shoot Delayed, Park

**Length:** ~4 minutes
**Format:** Screen recording + robot footage, voice-over
**Narrator:** Mechanical team member
**Guide page:** `guide/07-auto-park.md`
**Starting state:** Checkpoint 6 committed, same Gemini conversation

---

## Script

**[TITLE CARD: Episode 8 — Autonomous: Park]**

The auto shoots. Now it parks — and it splits in two. In a match, our alliance partner might want to shoot from the same spot. So: *Shoot First* shoots right away and stays put. *Shoot Delayed* waits 15 seconds for the partner, shoots, then parks. Same state machine, one number different. That's what a base class is for, and Gemini will do the split cleanly if you ask.

**[FOOTAGE: narrator walking the robot by hand from the shooting spot to the park zone]**

Before touching Gemini, walk it. Where does the robot end up after shooting? Which way does it need to turn? How far does it drive? Rough is fine. Ours: back up a little more so there's room, turn left about ninety degrees, drive forward into the park zone.

**[DIAGRAM CARD: top-down field sketch — wall start, back up to shooting spot, back up more, turn left 90°, drive forward into the park zone — hold 5 s]**

> **Ben:** draw The Hive's actual path.

**[TEXT CARD: the state diagram with DELAY at the front and the three park states after STATE_4_STOP_SHOOTING, new states highlighted — hold 5 s]**

New boxes. A DELAY at the very start — zero seconds for Shoot First, 15 for Delayed. Then after STOP_SHOOTING, Shoot First goes straight to ALL_STOP. Delayed goes: park back-up, minus 0.3 for 0.45 seconds. Park turn left, left side minus 0.5, right side plus 0.5, for 0.55 seconds. Park drive forward, 0.4 for 3.0 seconds. Then ALL_STOP. There's the STATE_5 we left the gap for.

**[SCREEN: Gemini, same conversation]**

**[TEXT CARD: the full Checkpoint 7 prompt — hold 8 s]**

"Refactor `AutoShootFirst` into a base class plus two OpModes. Don't change the behavior of the existing states." Then: an abstract `BaseAuto` with a `getInitialDelaySeconds()` method. A DELAY state first. `AutoShootFirst` returns 0, `AutoShootDelayed` returns 15, named "Auto: Shoot First" and "Auto: Shoot Delayed (15s)" on the Driver Hub. A `shouldParkTurnAndDrive()` that's only true when there's a delay. The three park states — STATE_4B_PARK_BACK_UP, STATE_5_PARK_TURN_LEFT, STATE_5B_PARK_DRIVE_FORWARD — exactly what the motors do, exactly how long. Timer reset. Telemetry.

**[SCREEN: generation, 4×; then editor]**

Check four things. Three files now: BaseAuto.java is big, and AutoShootFirst and AutoShootDelayed are each about twelve lines. DELAY is first in the enum, and its case compares the timer to `getInitialDelaySeconds()`. In STATE_4_STOP_SHOOTING, the `if shouldParkTurnAndDrive()` branch. And the three park cases, with the turn putting opposite signs on the two sides.

Ask whoever's reading: which file would you change to make the delay 12 seconds? AutoShootDelayed.java — one number. Which file to change the turn time? BaseAuto.java.

**[SCREEN: Run ▶]**

**[SPLIT: Driver Hub | robot at wall, park zone marked with tape]**

Tape on the floor for the park zone if you don't have field elements. Run Shoot First once to prove nothing changed. Then Shoot Delayed.

**[ROBOT: full Delayed run — 15 s wait, shoots, backs up, turns, drives, stops]**

Fifteen seconds of nothing — that's correct; telemetry says DELAY. Shoots, same as before. Backs up a bit more. Turns left. Drives. Stops — in the zone.

**[ROBOT: runs 2 and 3, quick cuts, showing where it stopped each time]**

Three runs. Mark where it stops each time.

**[Real failure. Example:]**

**[TEXT CARD: "What went wrong: Shoot First parked too"]**

First try, Shoot First drove off to park as well — and the partner was standing there. It's one sentence:

**[TEXT CARD: "`shouldParkTurnAndDrive()` should return false when the delay is 0." — hold 3 s]**

Deploy. Shoot First stays put.

> **Ben:** replace with real. The guide's table also has the other common ones — turned the wrong way, overshot the zone, turned before the balls were gone — each a one-line prompt.

**[SCREEN: tuning table]**

Back-up time, turn time and power, drive time and power, did it end in the zone. Ours: 0.45, 0.55 at 0.5, 3.0 at 0.4.

Total time? Add it up — that's next episode, and it matters more than you'd think.

**[SCREEN: commit "Two autos: shoot first, shoot delayed + park"]**

Three in the zone. Commit.

**[TEXT CARD: Checkpoint 7 checklist]**

Checkpoint seven: Shoot First still passes the Checkpoint 6 test, Shoot Delayed ends in the park zone three runs in a row, someone can trace the full state sequence and say which file holds the delay, saved.

**[END CARD: "Next: Episode 9 — Under 30 seconds and match-ready" + repo URL]**

---

## Shot list

- [ ] Walking the robot by hand
- [ ] Field path diagram
- [ ] Updated state diagram
- [ ] Prompt card
- [ ] Editor check: three files, DELAY, the park branch, the turn signs
- [ ] Shoot First once, then Delayed × 3 with tape marks
- [ ] Failure + fix
- [ ] Tuning table, commit
