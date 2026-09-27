# Checkpoint 3 — intake

> **SIMULATED** — the guide's prompt, not yet The Hive's original. Replace when recovered.

Guide page: `../guide/03-intake.md`
Produces: `../example-code/03-intake/HiveTeleOp.java`

## Prompt (as used)

> Add the intake to `HiveTeleOp`. Don't change any of the drive code — it works.
>
> - Map the motor named `intake`.
> - While the right bumper is held, run the intake at full power forward (pulling pieces in).
> - While the left bumper is held, run it at full power in reverse (pushing pieces out).
> - When neither bumper is held, stop it.
> - Add the intake power to telemetry.

**Who typed it:** coordinator (this is the last one the coordinator needs to type)

## What it produced

One new `hardwareMap.get`, one `setDirection(FORWARD)`, an `if / else if / else` on the bumpers setting `intakePower` to `1.0`, `-1.0`, or `0.0`, one `setPower`, one telemetry line. The drive block is untouched.

## What went wrong (most likely on first test)

**Runs backwards.** Right bumper pushes the piece away. Nobody knows which way an intake motor spins until it's on the robot.

## The fix prompt

> The intake direction is backwards. Reverse the `intake` motor. Don't change anything else.

## Also plausible

**Gemini rewrote the drive block** while adding the intake (renamed variables, changed the mix, added a speed limiter nobody asked for). Symptom: the drive feels different or broke.

> You changed the drive code. Restore it exactly as it was and only add the intake section.

If it can't, revert to the Checkpoint 2 commit and ask again with "Don't change any of the drive code" as the **first** line of the prompt.
