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
| **Names** | The exact names from your hardware config | `front_left_drive`, `intake`, `flywheel` |
| **Purpose** | What the part does for the robot | "the intake pulls balls in and also feeds them into the flywheel" |
| **Direction** | Which way is forward; which motors are mounted mirrored | "the left-side motors are mounted backwards" |
| **Control** | Which button or stick; toggle or hold; what happens on release | "A toggles collect; hold right bumper to feed, release to stop" |
| **Timing and order** | What happens before what, for how long | "reverse 200 ms, stop 300 ms, then run; feed pulses 100 ms on / 200 ms off" |

If any of these is missing, Gemini will guess. Guesses are usually wrong.

## Generate

Gemini writes or edits the OpMode. Read what it wrote. You don't need to understand every line, but you should be able to find:
- where it maps the hardware names
- where it reads the gamepad
- where it sets motor power

If Gemini asks a clarifying question, answer it. If it makes an assumption out loud ("I'll assume the flywheel spins forward"), check it.

## Test

Deploy to the robot. Run the OpMode. Try the specific thing you just added. Write down exactly what happened, in words a teammate would understand:

- "hey, in the latest push, holding both joysticks left made the robot strafe right, and vice versa. please fix"
- "what is the current time of spinning the motors back before starting the flywheel? it is spitting balls out, so i would like to shorten it"
- "I thought gamepad 1 dpad was slow mode, not tuning the flywheel."

Those are three of The Hive's actual prompts, exactly as typed. Exact observations become exact refinements.

## Refine

Turn the observation into a description of what was missing:

| You saw | You say |
|---|---|
| Strafe is mirrored | "holding both joysticks left made the robot strafe right, and vice versa. please fix" |
| Ball pops out when the flywheel starts | "what is the current time of spinning the motors back before starting the flywheel? it is spitting balls out, so i would like to shorten it" |
| Intake resumes after the bump and jams | "…I don't want the intake to resume. I want the intake to stop (whether it was stopped or running)." |
| Gemini rewrote the drive code and broke it | "Only change the intake section. Leave the drive code exactly as it is." |

Then back to Generate.

## The one rule

**One change per trip around the loop.** If you change three things and something breaks, you don't know which change did it. If you change one thing, you do.
