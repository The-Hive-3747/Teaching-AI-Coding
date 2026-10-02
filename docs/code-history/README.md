# Code history — the TeamCode folder after every prompt that changed it

Replayed from the Gemini agent session's 191 file writes and edits, and validated: the last state here is byte-for-byte the code in `example-code/final/`. Each folder is the full `TeamCode/.../teamcode/` directory as it stood after that prompt; `CHANGED` lists which files that prompt touched. Prompt numbers match `docs/verbatim-transcript.md`.

| Step | Prompt | Changed |
|---|---|---|
| [01](01-after-prompt-02/) | **2** 1. iterative 2. 0.05 per step 3. Let's have them run at fixed speeds of 0.3 4. Yes.  5. Ye… | Flywheel.java, Intake.java, MecanumTeleOp.java |
| [02](02-after-prompt-03/) | **3** This looks good, but we want variables declared in class variables, not redeclared each lo… | Flywheel.java, Intake.java, MecanumTeleOp.java |
| [03](03-after-prompt-05/) | **5** Let's turn the driving power max down to 0.8 | Flywheel.java, MecanumTeleOp.java |
| [04](04-after-prompt-08/) | **8** From gemini search:   Color & PWM Mapping ChartTarget ColorServo Position (0.0 to 1.0)Raw … | Intake.java, LedController.java, MecanumTeleOp.java |
| [05](05-after-prompt-10/) | **10** Let's make the endgame warnings flash for 4 seconds, but only according to the FTC rules. … | Firewheel.java, Intake.java, LedController.java, MecanumTeleOp.java |
| [06](06-after-prompt-12/) | **12** The firewheel left starts up when the the start button is hit. | Firewheel.java |
| [07](07-after-prompt-13/) | **13** The left intake servo is the wrong direction | Intake.java, MecanumTeleOp.java |
| [08](08-after-prompt-14/) | **14** When we start up, the left was green and the right led was purple. | LedController.java |
| [09](09-after-prompt-17/) | **17** the flywheel is going the wrong way. | Flywheel.java |
| [10](10-after-prompt-18/) | **18** hey, i prefer tank drive. can you set left joystick to controlling the left motors, right … | MecanumTeleOp.java |
| [11](11-after-prompt-20/) | **20** For the A button, let's not execute the 200ms reverse bump. Let's only do that with the fl… | Flywheel.java, Intake.java, MecanumTeleOp.java |
| [12](12-after-prompt-22/) | **22** flywheel is going the wrong direction | Flywheel.java |
| [13](13-after-prompt-23/) | **23** When the intake is shooting, let's use a 0.5 power. | Intake.java, MecanumTeleOp.java |
| [14](14-after-prompt-24/) | **24** When we start the flywheel, let's have it go back at .3 power with the intake to reject an… | Flywheel.java, Intake.java, MecanumTeleOp.java |
| [15](15-after-prompt-25/) | **25** When we are shooting, I'd like to pulse the intake at 0.5 power at 200ms on, 200ms off. | Intake.java |
| [16](16-after-prompt-26/) | **26** During the phase 3 you mentioned above. | Intake.java, MecanumTeleOp.java |
| [17](17-after-prompt-29/) | **29** Let | Intake.java |
| [18](18-after-prompt-30/) | **30** Let's set the flywheel limit to 1.0 and let's set the flywheel default to 1.0 speed when s… | Flywheel.java |
| [19](19-after-prompt-31/) | **31** The end flashing is pusling too fast. And it looks like white. How are you doing the flash… | MecanumTeleOp.java |
| [20](20-after-prompt-33/) | **33** Yes. Please do so. | AutoShootDelayed.java, AutoShootFirst.java, BaseAuto.java |
| [21](21-after-prompt-34/) | **34** I'd like to do two more experimental autonomous modes. The team has suggested that we can … | AutoShootDelayedExperimental.java, AutoShootFirstExperimental.java, BaseAutoExperimental.java |
| [22](22-after-prompt-36/) | **36** After testing, we need to move the robot 6 inches from the wall to shoot. We estimate that… | BaseAuto.java, BaseAutoExperimental.java |
| [23](23-after-prompt-37/) | **37** Let's move the up/down on the dpad for tuning the flywheel to gamepad2. Let's make up dpad… | Flywheel.java, Intake.java, MecanumTeleOp.java |
| [24](24-after-prompt-38/) | **38** On the delayed side, I want to strafe to the right for 3 seconds at 0.3 power. If that exc… | BaseAuto.java, BaseAutoExperimental.java |
| [25](25-after-prompt-39/) | **39** The robot spit out the balls in experimental. Let's not reverse the intake before we shoot… | BaseAuto.java, BaseAutoExperimental.java, Flywheel.java |
| [26](26-after-prompt-40/) | **40** Let's only go forward 1/4 of the ime that we currently go back (and forward). It is too fa… | BaseAuto.java, BaseAutoExperimental.java |
| [27](27-after-prompt-41/) | **41** This is better. We need a full second of settling time.  We also need to turn off the flyw… | BaseAuto.java, BaseAutoExperimental.java |
| [28](28-after-prompt-42/) | **42** Let's make sure that we don't reverse twice in a row - once after intake and once for the … | BaseAutoExperimental.java |
| [29](29-after-prompt-43/) | **43** The flywheel is not stopped in time for the fourth ball intake. The intake moves it to a s… | BaseAuto.java, BaseAutoExperimental.java, Flywheel.java |
| [30](30-after-prompt-44/) | **44** The intake seems to start up when the robot drives backwards in the beginning. When the fl… | BaseAutoExperimental.java |
| [31](31-after-prompt-45/) | **45** The strafing is the opposite direction. And let's increase power to 0.5. | BaseAuto.java, BaseAutoExperimental.java, MecanumTeleOp.java |
| [32](32-after-prompt-46/) | **46** Before we strafe, do the same backup for the same time again. | BaseAuto.java, BaseAutoExperimental.java |
| [33](33-after-prompt-48/) | **48** Let's make the backup 300ms for all the backups. We need to be a bit closer to the goal. | BaseAuto.java, BaseAutoExperimental.java |
| [34](34-after-prompt-49/) | **49** Let's change the 15 second delay to a 1 second delay for our testing convenience. We are g… | AutoShootDelayed.java, AutoShootDelayedExperimental.java, BaseAuto.java, BaseAutoExperimental.java |
| [35](35-after-prompt-50/) | **50** Instead of backing up for 500ms, let's back up for 750ms before our turn and drive. We are… | BaseAuto.java, BaseAutoExperimental.java |
| [36](36-after-prompt-51/) | **51** We are not changing the shooting backup, correct? Just the turning.  And I forgot about th… | BaseAuto.java, BaseAutoExperimental.java |
| [37](37-after-prompt-52/) | **52** It worked! Let's reduce the turn from 0.5 seconds to 0.45 seconds. And let's set the delay… | AutoShootDelayed.java, AutoShootDelayedExperimental.java, BaseAuto.java, BaseAutoExperimental.java |
| [38](38-after-prompt-54/) | **54** hey, in the latest push, holding both joysticks left made the robot strafe right, and vice… | MecanumTeleOp.java |
| [39](39-after-prompt-55/) | **55** what is the current time of spinning the motors back before starting the flywheel? it is s… | BaseAutoExperimental.java, Flywheel.java, Intake.java |
| [40](40-after-prompt-56/) | **56** don't bump down the power, keep it the same. let's just try the shortened time. | Flywheel.java, Intake.java |
| [41](41-after-prompt-57/) | **57** On the delayed autonomous, we end by bumping into the wall to get park points. However, we… | BaseAuto.java, BaseAutoExperimental.java |
| [42](42-after-prompt-58/) | **58** The turn should also be for 0.55 seconds not for 0.45 seconds. | BaseAuto.java, BaseAutoExperimental.java |
| [43](43-after-prompt-59/) | **59** create backup copies of the existing operation modes. we are now going to create a separat… | ExperimentalParkShootFirst.java, Flywheel.java, AutoShootDelayedBackup.java, AutoShootDelayedExperimentalBackup.java, AutoShootFirstBackup.java, AutoShootFirstExperimentalBackup.java, BaseAutoBackup.java, BaseAutoExperimentalBackup.java, FlywheelBackup.java, IntakeBackup.java, LedControllerBackup.java, MecanumTeleOpBackup.java |
| [44](44-after-prompt-60/) | **60** two changes: first, make sure that it goes back to pick up the 4th ball. the experimental … | ExperimentalParkShootFirst.java |
| [45](45-after-prompt-61/) | **61** a few changes: have the first drive backwards after the turn left be 1.65 seconds instead … | ExperimentalParkShootFirst.java |
| [46](46-after-prompt-62/) | **62** okay, lets up the first backwards drive to 1.75 seconds, and drop the second backwards dri… | ExperimentalParkShootFirst.java |
| [47](47-after-prompt-63/) | **63** Do not change the functionality of gamepad 1. I want gamepad 2 to also change the speed of… | Flywheel.java |
| [48](48-after-prompt-64/) | **64** I thought gamepad 1 dpad was slow mode, not tuning the flywheel. | Flywheel.java |
| [49](49-after-prompt-65/) | **65** When I start up the flywheel, I want the process to continue to do the reverse function. B… | Intake.java |
| [50](50-after-prompt-67/) | **67** Let's set the default speed to 0.95 | Flywheel.java |
