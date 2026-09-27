# Checkpoint 2 — Mecanum Drive

**At the end of this checkpoint:** A TeleOp OpMode that drives the robot in all directions from the left stick and rotates from the right stick.

**Time:** 15–30 minutes including the test.

---

## 2.1 Ask for the drive, and only the drive

Same Gemini conversation as Checkpoint 1.

> **Prompt:**
>
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

`[SCREENSHOT: Gemini panel with the prompt sent and the start of the generated OpMode visible]`

## 2.2 Read what it wrote

Open `TeamCode/src/main/java/org/firstinspires/ftc/teamcode/HiveTeleOp.java`. You should be able to find these four things. Have the Reader point at each one:

1. **Hardware mapping** — lines like `frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");`. Check the strings in quotes match your config exactly.
2. **Reversing** — `frontLeft.setDirection(DcMotor.Direction.REVERSE);` for the two left motors.
3. **Reading the gamepad** — `gamepad1.left_stick_y`, `gamepad1.left_stick_x`, `gamepad1.right_stick_x`.
4. **Setting power** — `frontLeft.setPower(...)` and three more.

If any of the four is missing, ask: "I don't see where you reverse the left motors. Add it."

**Compare with:** [`../example-code/02-mecanum-drive/HiveTeleOp.java`](../example-code/02-mecanum-drive/HiveTeleOp.java) — a reference version written against the same prompt (simulated until the real one replaces it).

## 2.3 Build and deploy

Click **Run ▶**. Wait for "Install successfully finished."

**If it won't build:**
- Red error in the bottom panel → select the whole error text, paste it into Gemini: *"This compile error came from the code you wrote. Fix it."* Deploy again.
- The most common one is `cannot find symbol` — Gemini used a class or method that doesn't exist in this SDK version. Pasting the error fixes it nearly every time.

## 2.4 Test

Robot on the floor, wheels free to move, nobody's feet nearby.

On the Driver Hub: select `HiveTeleOp` from the TeleOp list → **Init** → **▶**.

| Try | Expected | Write down what actually happened |
|---|---|---|
| Push left stick forward | Robot drives forward | |
| Pull left stick back | Robot drives backward | |
| Push left stick left | Robot strafes left | |
| Push left stick right | Robot strafes right | |
| Push right stick left | Robot rotates counter-clockwise | |
| Push right stick right | Robot rotates clockwise | |
| Release everything | Robot stops | |

`[SCREENSHOT: Driver Hub with HiveTeleOp selected, telemetry showing four motor powers]`

## 2.5 If it's wrong

Write down the symptom in words, then use the matching prompt.

| Symptom | Prompt |
|---|---|
| Forward goes backward | "Forward and backward are swapped. Negate the forward input." |
| Forward works but strafe is mirrored | "Strafe left moves the robot right. Flip the sign on the strafe term." |
| Rotation is mirrored | "Right stick right rotates the robot counter-clockwise. Flip the rotation sign." |
| Robot spins in place on forward | Two motors are reversed wrong. "When I push forward, the robot spins. The left motors are on the left side of the robot. Re-check which motors are reversed." Then physically verify which motor is which by running one at a time. |
| One wheel doesn't move | Probably a config or cable problem, not code. Check the port on the hub and the name in the config. |
| Robot crashes on Init: *Unable to find a hardware device with name "X"* | Name mismatch. Compare the quoted string in the code to the config. Tell Gemini the correct name. |
| Drives, but jerky or too fast | "Cube the joystick inputs so small movements are more precise." or "Cap the drive power at 0.7." |

Every fix is one prompt, one deploy, one test. Don't stack them.

## 2.6 Save it

Once all seven rows in the test table pass: commit in Git (**Git → Commit**, message "Mecanum drive works") or copy `HiveTeleOp.java` to a safe folder.

## Checkpoint 2 test

- [ ] All seven drive tests pass
- [ ] The Reader can point at the four parts of the code (mapping, reversing, gamepad, power) and say what each does
- [ ] Saved

Go to [Checkpoint 3 — Intake](03-intake.md).
