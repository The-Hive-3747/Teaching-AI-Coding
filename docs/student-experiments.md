# Student experiments — October 2026

Two trials by The Hive's own students, after the mentor experiment, recorded here so the result is on hand whenever it's useful. Reported by Ben, October 8, 2026. These are observations from one team's practice sessions, not a study.

## 1. A new programmer describes the parts, not the architecture

**Who:** The Hive's newest programmer, past the training checklist, first time coding a robot with AI.

**What she gave Gemini:** the robot's parts, what has to turn on when, and which buttons run which phases. No architecture — nothing like the mentor experiment's night-one description of subsystem classes and a state machine.

**What came back:** a single OpMode with everything in it. The motor control and the button handling were mixed together.

**Did it work?** Yes, out of the gate. The intake, firewheel and flywheel each needed their direction reversed — the usual first-test fix — and then it drove.

**What it cost:** no structure. Every function was tangled with the button press that triggered it, and it would have bloated with each addition. Readable now, unmaintainable in a month.

## 2. An experienced programmer describes the structure and the plan

**Who:** Tom, who hand-coded last season's TeleOp.

**What he gave Gemini:** last year's robot and code, with specific instructions — comment out the turret; simplify the control scheme to one controller; we'll demo on a quarter field, so we need a reset point in the corner.

**What came back:** readable code with good comments. The agent followed the structure it was handed and documented what it changed and why.

## What the two together show

The AI gives you back the level of thinking you put in. Parts and buttons in → one big OpMode out. Structure and intent in → structured, commented code out. The new programmer's prompt was a complete *description*; it was not a *design*, and Gemini didn't invent one for her — exactly as the mentor experiment's fake-gamepad autonomous showed it won't invent what it isn't told is coming.

This is the practical content of the kickoff's slide 4 ("The programmer doesn't go away. The job changes."): the thing a programmer still has to bring is the architecture — what the classes are, what the states are, what will be added later — and that's the thing a mechanical teammate can't supply. It's also why Checkpoint 1 of the guide asks for a *description* and the first prompts of Checkpoints 2–4 ask for *subsystem classes* by name.

**Practical rule from this:** before the first prompt, the programmer writes down the classes (or at least the subsystems) and the states, even in three lines. "A `Drive` class, an `Intake` class, a `Flywheel` class, each with `init` and `update`; the OpMode only reads the gamepad and calls them." That one sentence is the difference between experiment 1 and experiment 2. A description without it still works — it just doesn't last.

**A fix for experiment 1, after the fact:** "Refactor this OpMode into `Drive`, `Intake` and `Flywheel` classes, each with `init(hardwareMap)` and `update(gamepad)`. Keep the behavior identical. The OpMode should only read the gamepad and call them." Gemini will do this; it's the kind of mechanical restructuring it's good at — and a human still reads the result.
