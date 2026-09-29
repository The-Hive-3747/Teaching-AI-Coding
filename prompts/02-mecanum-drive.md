# Checkpoint 2 — mecanum drive

> **RECONSTRUCTED prompt, VERBATIM fix** — see the README for what that means.

Guide page: `../guide/02-mecanum-drive.md`

## Prompt (as used in the guide)

> **Prompt:**
>
> Create a TeleOp OpMode in the TeamCode module called `MecanumTeleOp`. For now, only the drive — no intake, no flywheel yet.
>
> - Use the iterative `OpMode` style (`init()` and `loop()`), not `LinearOpMode`.
> - Map the four drive motors by the names I gave you. Reverse `front_left_drive` and `back_left_drive`. Set all four to brake when power is zero.
> - Tank drive: left stick Y = left wheels, right stick Y = right wheels. Remember the stick Y axis is negative when pushed forward.
> - Strafe: average the two sticks' X values. Multiply by 1.1 to make up for mecanum strafe losses.
> - D-pad: up/down drive straight at 0.5, left/right strafe at 0.5, overriding the sticks while held.
> - Combine with standard mecanum mixing, normalize so no motor is asked for more than 1.0, then scale everything by 0.8.
> - Telemetry: show all four motor powers.
>
> Use the `@TeleOp` annotation so it shows up on the Driver Hub.

## What it should produce

*(Snapshot derived from the team's final code. This reconstructed prompt has not been run; Gemini's output will differ in names and layout.)*

`MecanumTeleOp.java`: iterative `OpMode`, four `hardwareMap.get` calls in `init()`, left motors reversed, tank + averaged-strafe mix, D-pad overrides, normalize then × 0.8, telemetry. Compare `../example-code/02-mecanum-drive/`.

## What went wrong — and the real fix

Strafe was mirrored. The prompt the team typed, verbatim (Gemini's transcript, Session Iteration 3):

> **VERBATIM:** *hey, in the latest push, holding both joysticks left made the robot strafe right, and vice versa. please fix*

Gemini flipped the strafe signs in the four mixing lines:

```java
frontLeftPower  = leftY + strafe;
backLeftPower   = leftY - strafe;
frontRightPower = rightY - strafe;
backRightPower  = rightY + strafe;
```

Note what the prompt does *not* do: guess at the cause, name a variable, or suggest code. It says what was pressed and what happened. That was enough.
