# The Loop

Everything in this guide is one method, repeated:

```
        ┌──────────────────────────────────────────┐
        │                                          │
        ▼                                          │
   ┌──────────┐    ┌──────────┐    ┌──────────┐    │
   │ DESCRIBE │───►│ GENERATE │───►│   TEST   │────┤ works? → next checkpoint
   └──────────┘    └──────────┘    └──────────┘    │
                        ▲                          │ doesn't?
                        │         ┌──────────┐     │
                        └─────────│  REFINE  │◄────┘
                                  └──────────┘
```

## Describe

Tell Gemini what you want in plain English. A good description has five things:

| | What it means | Example |
|---|---|---|
| **Names** | The exact names from your hardware config | `frontLeft`, `intake`, `flywheelLeft` |
| **Purpose** | What the part does for the robot | "the intake pulls game pieces into the robot" |
| **Direction** | Which way is forward; which motors are mounted mirrored | "the left-side motors are mounted backwards" |
| **Control** | Which button or stick; what happens on press, hold, release | "hold right bumper to run intake; release to stop" |
| **Timing and order** | What happens before what, for how long | "spin up for 1.5 s, then pulse the intake 0.3 s on, 0.2 s off" |

If any of these is missing, Gemini will guess. Guesses are usually wrong.

## Generate

Gemini writes or edits the OpMode. Read what it wrote. You don't need to understand every line, but you should be able to find:
- where it maps the hardware names
- where it reads the gamepad
- where it sets motor power

If Gemini asks a clarifying question, answer it. If it makes an assumption out loud ("I'll assume the flywheels spin the same direction"), check it.

## Test

Deploy to the robot. Run the OpMode. Try the specific thing you just added. Write down exactly what happened, in words a teammate would understand:

- "Strafe left goes right."
- "Intake runs but never stops."
- "Flywheel starts, but the feed pulses immediately instead of waiting."

Exact observations become exact refinements.

## Refine

Turn the observation into a description of what was missing:

| You saw | You say |
|---|---|
| Strafe left goes right | "Strafing is mirrored. Flip the sign on the strafe term." |
| Intake never stops | "When the right bumper is released, stop the intake motor." |
| Feed pulses too early | "Wait 1.5 seconds after the flywheel starts before the first intake pulse." |
| Gemini rewrote the drive code and broke it | "Only change the intake section. Leave the drive code exactly as it is." |

Then back to Generate.

## The one rule

**One change per trip around the loop.** If you change three things and something breaks, you don't know which change did it. If you change one thing, you do.
