# Checkpoint 2 — mecanum drive

> **SIMULATED** — the guide's prompt, not yet The Hive's original. Replace when recovered.

Guide page: `../guide/02-mecanum-drive.md`
Produces: `../example-code/02-mecanum-drive/HiveTeleOp.java`

## Prompt (as used)

> Create a new TeleOp OpMode in the TeamCode module called `HiveTeleOp`. For now, only implement the mecanum drive — no intake, no shooter yet.
>
> - Map the four drive motors by the names I gave you.
> - Reverse `frontLeft` and `backLeft`.
> - Left stick Y is forward/back (remember the gamepad Y axis is negative when pushed forward). Left stick X is strafe. Right stick X is rotation.
> - Use standard mecanum mixing and scale the powers so no motor is asked for more than 1.0.
> - Set the drive motors to brake when their power is zero, so the robot stops instead of coasting.
> - Show each motor's power on telemetry so I can see what's happening.
>
> Use the `@TeleOp` annotation so it shows up on the Driver Hub.

**Who typed it:** coordinator

## What it produced

A `LinearOpMode` with a `while (opModeIsActive())` loop: four `hardwareMap.get` calls, two `setDirection(REVERSE)`, the standard `forward ± strafe ± rotate` mix, normalization, four `setPower` calls, telemetry. About 80 lines. Same shape as FIRST's `BasicOmniOpMode_Linear` sample, which is what Gemini has as context.

## What went wrong (most likely on first test)

**Strafe mirrored.** Left stick left → robot goes right. The mecanum mix in the sample assumes an X roller pattern viewed from above; if the wheels are mounted the other way, strafe flips.

## The fix prompt

> Strafe left moves the robot right. Flip the sign on the strafe term. Don't change anything else.

## Second most likely

**Robot spins on forward.** Two motors reversed wrong — usually the config has left/right swapped, not the code. Check by running one motor at a time:

> Add a test mode: while I hold X, run only `frontLeft` forward at 0.3 power; A = `backLeft`, Y = `frontRight`, B = `backRight`. I'll use it to check which motor is which.

(FIRST's sample has this exact test commented out. Once the config is right, ask Gemini to remove it.)
