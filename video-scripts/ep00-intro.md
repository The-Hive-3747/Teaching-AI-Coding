# Episode 0 — Why We're Doing This

**Length:** ~2 minutes
**Format:** A student on camera, cut with B-roll of the mentor robot
**Narrator:** Tom (or whichever of the three opens the kickoff)

---

## Script

**[ON CAMERA — narrator, the mentor robot on the table behind them]**

Hi. I'm Tom, from The Hive, FTC 3747. At our robot-in-one-week, we students build and code a robot in seven days, the normal way. Our mentors have their own competition the same week. This is their robot — and they ran an experiment on it.

**[B-ROLL: robot driving, intaking, shooting — 5 seconds]**

Not one line of its code was written by hand. Ben, our coordinator, has written Java for twenty-five years, and he didn't type any. The drive, the intake, the shooter, the autonomous — all of it was written by describing what they wanted in plain English to Gemini, inside Android Studio.

And here's the more important thing.

**[B-ROLL: Asim at the robot, Ben at the laptop, Gemini panel visible]**

After the first night, most of the changes weren't Ben's. They came from Asim, the mechanical mentor — the person who built the robot, who doesn't write Java. Sometimes he was at the keyboard himself — tank drive, the strafe fix, a whole experimental autonomous with its tuning. Sometimes he was standing over the robot calling out what to change while Ben typed it. Either way: the timings, the power levels, the park routine, the fixes — those came from the person who built the robot.

**[ON CAMERA]**

That's not a trick, and it's not cheating. He understood the robot better than anyone, because he built it. All he needed was a way to tell the code what the robot should do. That's what this series is. We coded ours by hand. Next time, we'd do it this way — and we think your team can too.

**[TEXT CARD: the loop diagram — DESCRIBE → GENERATE → TEST → REFINE]**

The method is simple. Describe one thing. Let Gemini write it. Test it on the real robot. If it's wrong, describe what was missing. Repeat.

**[TEXT CARD: the checkpoint list, 0–8]**

We'll go through it in nine short videos, one per step. Install. Describe your robot. Drive. Intake. Shooter. Refine. Then the autonomous, in three pieces: shoot, park, and make it reliable under 30 seconds.

Each video matches a page in the written guide, so you can follow along either way.

**[ON CAMERA]**

One rule before you start: do one thing at a time, and test after every change. That's the whole secret. Everything else is details.

**[B-ROLL: robot finishing the autonomous, parking]**

Let's go.

**[END CARD: "Next: Episode 1 — Install Android Studio and FtcRobotController" + repo URL]**

---

## Shot list

- [ ] Narrator on camera, two setups (intro and close)
- [x] Robot driving/intaking/shooting, 10 s total — cut from `docs/video/scrimmage-match3-auto.mp4` and `scrimmage-match3-teleop-start.mp4` (Test Match 3, already captured from the stream); more on the stream from 3:39:22 (https://www.youtube.com/live/E53OUghEmlg?t=13162); stills in `docs/images/`
- [ ] Asim at the robot, Ben at the laptop with Gemini panel, 5 s
- [x] Robot completing autonomous, 5 s — the end of `docs/video/scrimmage-match3-auto.mp4`
- [ ] Loop diagram text card
- [ ] Checkpoint list text card

## Notes

- Keep it under 2 minutes. This is a hook, not a lecture.
- The narrator introduces themself by first name only. Get each student's and parent's okay before publishing outside the team.
