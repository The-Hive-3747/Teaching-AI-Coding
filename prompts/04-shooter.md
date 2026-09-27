# Checkpoint 4 — shooter and intake → flywheel timing

> **SIMULATED** — the guide's prompt, not yet The Hive's original. Replace when recovered.

Guide page: `../guide/04-shooter.md`
Produces: `../example-code/04-shooter/HiveTeleOp.java`

## Prompt (as used)

> Add the shooter to `HiveTeleOp`. Don't change the drive or intake code.
>
> - Map two motors named `flywheelLeft` and `flywheelRight`, one on each side of the launch path. For now leave both un-reversed; I'll tell you which one to reverse after testing.
> - Use an enum with the states IDLE, SPINNING_UP, and FEEDING.
> - While the right trigger is held past 0.5:
>   1. Run both flywheels at 0.8 power.
>   2. Wait 1.5 seconds for them to spin up. During this wait, don't run the intake.
>   3. After that, pulse the intake forward: 0.3 seconds on at full power, 0.2 seconds off, repeating for as long as the trigger is held.
> - When the trigger is released — at any point, even during spin-up — stop the flywheels and the intake immediately and go back to IDLE.
> - The bumper intake controls from before should still work when the shooter is IDLE. While it's spinning up or feeding, ignore the bumpers.
> - Let the flywheels coast to a stop (float) rather than brake.
> - Use `ElapsedTime` for the timing, not `sleep()` — the drive must keep responding while the shooter is spinning up.
> - Show the shooter state (idle / spinning up / feeding) and the flywheel power on telemetry.

**Who typed it:** mechanical team, with the coordinator watching

## What it produced

A `ShooterState` enum, two `ElapsedTime` timers (one for time-in-state, one for the pulse phase), a `switch` on the state inside the loop, and a `pulseOn` boolean that flips when its timer passes 0.3 / 0.2 s. The bumper block is wrapped in `if (shooterState == IDLE)`. ~170 lines total.

## What went wrong (first test, no pieces)

**`flywheelRight` pulling backward.** Visible immediately when the trigger is held: one wheel pushes toward the exit, the other pulls away.

## The fix prompt

> `flywheelRight` spins the wrong way. Reverse it. Don't change anything else.

## What went wrong (second test, with pieces)

**First shot weak, later ones fine.** 1.5 s isn't enough for these wheels from a standstill.

> Increase the spin-up wait from 1.5 to 2.0 seconds.

## Other things this prompt prevents (that the first draft didn't)

- Without "released at any point, even during spin-up," some generated versions only checked the trigger in FEEDING — a quick tap left the flywheels running.
- Without "ignore the bumpers while spinning up or feeding," a bumper press mid-shot overrode the pulse and jammed a piece.
- Without "use ElapsedTime, not sleep()," a `sleep(1500)` in the spin-up froze the drive for a second and a half every shot.
