# Checkpoint 9 — Extras

Everything else The Hive described into existence during the week. None of it is required to compete; all of it is worth seeing, because each one shows the same method applied to something new.

---

## 9.1 LED status lights

The robot has two goBILDA LED modules on servo ports (`led_left`, `led_right`). They're driven like servos: the "position" picks the color. The Hive had Gemini build a `LedController` class and then described what color meant what:

> **Prompt (reconstructed):**
>
> Add goBILDA LED indicators on servo ports `led_left` (Control Hub 3) and `led_right` (Expansion Hub 3). They're driven as servos with an expanded 500–2500 µs PWM range; color is the servo position: red 0.00, orange 0.17, yellow 0.33, green 0.50, blue 0.67, purple 0.83, white 1.00. Make a `LedController` class with `init(hardwareMap)` and `setColor(position)`.
>
> In TeleOp, set the color every loop by priority:
> - white while feeding (RB held with flywheel running)
> - red during reject mode or the flywheel's reverse bump
> - orange during the flywheel pause, or when the flywheel is running and collect is on
> - purple when the flywheel is running
> - green when collect is on
> - blue otherwise (driving)
> Yellow during init, green when init completes.

Why bother: the driver can see at a glance, from across the field, whether the flywheel is up to speed (purple) before pulling the feed trigger. That's a real match-time advantage from a "describe what you want" prompt.

**Code:** [`example-code/final/LedController.java`](../example-code/final/LedController.java) and the LED block in [`MecanumTeleOp.java`](../example-code/final/MecanumTeleOp.java). One thing to check on your robot: that "feeding" really means feeding (`isFeeding` in the shipped code is just "RB held", so the white shows whenever RB is down — the team never noticed because nobody holds RB with the flywheel off).

## 9.2 Endgame warnings

> **Prompt (reconstructed):**
>
> Add a match timer that starts when START is pressed. At 90 seconds (30 s left in a 120 s TeleOp) rumble gamepad 1 twice and flash the LEDs orange at 1 Hz for 4 seconds. At 110 seconds (10 s left) rumble for one second and flash red at 1 Hz for 4 seconds. Show time remaining on telemetry.

The 1 Hz flash rate is deliberate — the FTC robot rules (R203 in the 2025–26 manual) say lights flashing faster than 2 Hz invite extra scrutiny and may have to be disabled. If you add flashing lights, say the rate in the prompt, and check the current season's manual.

**Code:** sections 4 and 5 of `loop()` in [`MecanumTeleOp.java`](../example-code/final/MecanumTeleOp.java).

## 9.3 Gamepad 2

The Checkpoint 5 fix ("I thought gamepad 1 dpad was slow mode") moved flywheel tuning to gamepad 2. Once a second gamepad exists, more can move there. Common asks:

- "Move A, B, RB, LB and X to gamepad 2. Gamepad 1 only drives."
- "Gamepad 2 Y sets the flywheel target to 0.8 (close shot), gamepad 2 A sets 0.95 (far shot)."

## 9.4 The 4-ball experiment

The Hive's `BaseAutoExperimental` tries something clever: shoot the three preloaded balls, then scoop a fourth that was dropped in front of the robot at the start, back it away from the flywheel, spool, and shoot it. Seventeen states, counting the delay. Gemini's log describes it step by step (Session Iteration 2).

Two things it teaches:
- **A state machine can get long and stay readable** as long as every state does one thing. Seventeen one-thing states beat five do-three-things states.
- **Experiments get their own files.** `BaseAutoExperimental` and its subclasses live next to the working autos, so trying the trick never risked the match code. Ask for that: "Make a copy called `BaseAutoExperimental` and change only the copy."
- **And they can be wrong.** The step that's supposed to back the 4th ball away by reversing the intake sets a gamepad button the `Intake` class never reads, so the intake runs forward instead (details in [`example-code/README.md`](../example-code/README.md)). It's a clean example of a description Gemini implemented literally — "press left bumper" — when the code path needed something else. The fix prompt: "In STATE_6 the intake should run in reverse. `Intake.update` doesn't read the left bumper; pass a reverse flag instead."

**Code:** [`example-code/final/BaseAutoExperimental.java`](../example-code/final/BaseAutoExperimental.java) and its two subclasses.

## 9.5 Backups by asking

Gemini's own summary of the week lists "Maintain Field Backups: always ask the AI to save backup snapshots before major architectural refactors" as a top-four principle. The `backup/` folder in `example-code/final/` is the result. The prompt is just:

> "Before you change anything, save a copy of every file in TeamCode to a `backup` subfolder with `Backup` added to each class name."

Git does this better (see Rule 3 in the [README](README.md)), but if your team isn't using Git yet, this works today.

## 9.6 Let Gemini write the log

The tuning log in Checkpoint 5 doesn't have to be kept by hand. At the end of a session:

> "Write a markdown file `docs/BUILD_LOG.md` summarizing every change you made today: the prompt I gave, what you changed, and which file. Add to it, don't overwrite."

That's how [`docs/AI_Development_Transcript_And_Guide.md`](../docs/AI_Development_Transcript_And_Guide.md) came to exist — Gemini wrote it, and it's the source for the real prompts quoted in this guide.

## 9.7 Two things that happened that aren't in the transcript

**The robot changed under the code.** On Sep 18 the transfer mechanism — two CR servos (`firewheel_left`, `firewheel_right`) that carried balls from the intake to the flywheel — went in at 5:17 PM and came out at 7:50 PM: *"We removed the firewheels. They interfered with the flywheel."* The team recoded quickly: the intake now feeds the flywheel directly, which is the design in this guide. `Firewheel.java` in [`example-code/final/`](../example-code/final/) is the class that was left behind. The prompt for that kind of change is a Checkpoint 1 update plus a Checkpoint 5 fix: "We removed the transfer servos. The intake now feeds the flywheel directly. Remove `Firewheel` from the OpModes and make the right bumper pulse the intake instead."

**The AI took the expedient route.** The autonomous drives the subsystems through a simulated gamepad (Checkpoint 6 explains). It works, but it ties the auto to the TeleOp button mapping, and it happened because nobody told Gemini an autonomous was coming when it wrote `Intake` and `Flywheel`. Gemini will make it work; it won't always make it work the way you'd choose. Tell it what the code will be used for, not just what it does now.

## 9.8 Next season

The prompting method carries over; the hardware doesn't. When the new game is announced:

1. Redo Checkpoint 1 for the new robot. Everything else follows.
2. Reuse the subsystem pattern: `Flywheel`, `Intake` become whatever the new mechanisms are.
3. If odometry or encoders are wired up, describe them in Checkpoint 1 like any other device, and replace the time-based states one at a time: "Use the drive encoders instead of time so STATE_1_BACK_UP moves exactly 8 inches."
4. Keep the loop: one change per prompt, test after every change, save what works.
