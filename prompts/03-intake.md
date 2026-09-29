# Checkpoint 3 — intake

> **RECONSTRUCTED** — see the README for what that means.

Guide page: `../guide/03-intake.md`

## Prompt (as used in the guide)

> **Prompt:**
>
> Add the intake to `MecanumTeleOp`. Don't change any of the drive code — it works.
>
> - Put the intake in its own class, `Intake`, so the OpMode stays readable. **An autonomous OpMode will use this class later too**, so put the behavior in plain methods — `setCollect(boolean on)`, `setReject(boolean on)`, `stop()` — and have `update(gamepad, isReversed)` only map buttons to those methods. Nothing inside the class should depend on a gamepad except `update`.
> - It has the motor `intake` and the two CR servos `intake_servo_left` (reversed) and `intake_servo_right`. They always run together at the same power.
> - Pressing A toggles Collect mode: 0.5 power, pulling balls in. Press again to stop. Use edge detection so holding A doesn't flicker it.
> - Pressing left bumper or X toggles Reject mode: −0.5 power. Reject overrides Collect.
> - If A is pressed while in Reject mode, Reject turns off and Collect takes over.
> - Set the intake motor to brake at zero power.
> - Add the intake power and the reject-mode state to telemetry.

## What it should produce

*(Snapshot derived from the team's final code. This reconstructed prompt has not been run; Gemini's output will differ in names and layout.)*

New `Intake.java` (motor + two CR servos, A toggles collect with edge detection, reject at −0.5) and the LB/X reject toggle in `MecanumTeleOp`. Compare `../example-code/03-intake/`.

## Likely first-test failures

| Symptom | Prompt |
|---|---|
| A pushes balls out | "The intake motor direction is backwards. Reverse `intake`." |
| One servo pushes out | "`intake_servo_right` is spinning the wrong way. Reverse it." |
| Collect flickers when A is held | "A is toggling on every loop. Use edge detection: only toggle when A goes from not pressed to pressed." |
