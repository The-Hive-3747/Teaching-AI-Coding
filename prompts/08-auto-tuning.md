# Checkpoint 8 — under 30 seconds; keeping the LEAVE points

> **RECONSTRUCTED prompts, VERBATIM park prompt** — see the README for what that means.

Guide page: `../guide/08-auto-tuning.md`

## Prompts (as used in the guide)

> **Prompt:**
>
> In `BaseAuto`, make the shoot duration depend on the delay: 10.0 seconds when the delay is 0, 8.3 seconds when the delay is greater than 0. Add a `getShootDurationSeconds()` method for it. Don't change anything else.

> **Prompt:**
>
> The delayed auto totals more than 30 seconds and the period is 30. Reduce the delayed shoot duration from 8.3 to 6.5 seconds so the whole run finishes with a full second to spare.

> **Prompt:**
>
> Add a safety timeout to `BaseAuto`. Use the existing `totalAutoTimer`. If it ever passes 29.5 seconds, regardless of the current state, set all drive motors to zero, stop the flywheel and intake, and go to `STATE_6_ALL_STOP`. Show the total elapsed time on telemetry (it may already be there).

## The real prompt: back off the wall

> **VERBATIM:** *On the delayed autonomous, we end by bumping into the wall to get park points. However, we get points for not touching the wall. Can we add a backup after the turn and drive forward?*

**Changed:** new `STATE_5C_PARK_BACK_OFF_WALL` — all four motors at −0.5 for 100 ms — between the park drive and ALL_STOP (Gemini's transcript, Session Iteration 5).
**Style:** explains the rule. Because the prompt says *why* ("we get points for not touching the wall"), Gemini knew "a backup" meant a nudge, not a retreat.

## The arithmetic

Shoot Delayed as it left Checkpoint 7 totals 32.3 s. The 8.3 s shoot duration in the final code brings it to 30.6, and the back-off step makes it 30.7 on paper. On the field it finished, off the wall — so the timer has some slack. The guide's Checkpoint 8 still walks through cutting it to 28.9, because slack isn't margin.

## Compare

`../example-code/08-auto-tuning/` — final `BaseAuto.java` minus LEDs.
