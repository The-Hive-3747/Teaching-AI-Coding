# Episode 8 — Autonomous: Park

**Length:** ~4 minutes
**Format:** Screen recording + robot footage, voice-over
**Narrator:** Mechanical team member
**Guide page:** `guide/07-auto-park.md`
**Starting state:** Checkpoint 6 committed, same Gemini conversation

---

## Script

**[TITLE CARD: Episode 8 — Autonomous: Park]**

The auto shoots. Now it parks. This is the easiest kind of change there is: adding boxes to a state machine that already works.

**[FOOTAGE: narrator walking the robot by hand from the shooting spot to the park zone]**

Before touching Gemini, walk it. Where does the robot end up after shooting? Which way does it need to turn? How far does it drive? Rough is fine. Ours: turn right about ninety degrees, drive forward about four feet, stop.

**[DIAGRAM CARD: top-down field sketch — wall start, shooting spot, arrow to park zone — hold 5 s]**

> **Ben:** draw The Hive's actual path.

**[TEXT CARD: the 6-state diagram, new states highlighted — hold 5 s]**

Two new boxes between SHOOT and DONE. Turn to park: flywheels off, rotate in place for 0.6 seconds. Drive to park: forward for 2 seconds. Then done, same as before.

**[SCREEN: Gemini, same conversation]**

**[TEXT CARD: the full Checkpoint 7 prompt — hold 8 s]**

"Add parking to HiveAutoShoot. Don't change LEAVE_WALL, SPIN_UP, or SHOOT — they work." Then the two new states, exactly where they go, exactly what the motors do, exactly how long.

**[SCREEN: generation, 4×; then editor]**

Check four things. Two new names in the enum. SHOOT now goes to TURN_TO_PARK instead of DONE. Flywheels get turned off at the start of TURN_TO_PARK. DRIVE_TO_PARK goes to DONE.

**[SCREEN: Run ▶]**

**[SPLIT: Driver Hub | robot at wall, park zone marked with tape]**

Tape on the floor for the park zone if you don't have field elements. Run it.

**[ROBOT: full run — shoots, turns, drives, stops]**

Shoots. Turns. Drives. Stops — in the zone.

**[ROBOT: runs 2 and 3, quick cuts, showing where it stopped each time]**

Three runs. Mark where it stops each time.

**[Real failure. Example:]**

**[TEXT CARD: "What went wrong: turned left instead of right"]**

First try, it turned the wrong way. Which is fine. It's one sentence:

**[TEXT CARD: "TURN_TO_PARK rotates the wrong way. Swap the signs: left motors −0.5, right motors +0.5." — hold 3 s]**

**[TEXT CARD: "What went wrong: overshot the zone on run 3"]**

And it overshot once.

**[TEXT CARD: "Change DRIVE_TO_PARK from 2.0 to 1.7 seconds." — hold 3 s]**

> **Ben:** replace with real. If The Hive's park was a strafe rather than turn-and-drive, the whole episode's states change — update the guide page to match first, then this script.

**[SCREEN: tuning table]**

Turn time, turn power, drive time, drive power, did it end in the zone.

**[SCREEN: commit "Auto shoots and parks"]**

Three in the zone. Commit.

**[TEXT CARD: Checkpoint 7 checklist]**

Checkpoint seven: three runs end in the zone, shooting still works, someone can trace start to done, saved.

**[END CARD: "Next: Episode 9 — Under 30 seconds" + repo URL]**

---

## Shot list

- [ ] Walking the robot by hand
- [ ] Field path diagram
- [ ] Updated state diagram
- [ ] Prompt card
- [ ] Editor check
- [ ] Full run × 3 with tape marks
- [ ] Failures + fixes
- [ ] Tuning table, commit
