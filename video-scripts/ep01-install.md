# Episode 1 — Install Android Studio and FtcRobotController

**Length:** 8–10 minutes (long download waits time-lapsed)
**Format:** Screen recording, voice-over
**Narrator:** Ben
**Guide page:** `guide/00-install.md`
**Starting state:** A clean laptop with nothing installed

---

## Script

**[TITLE CARD: Episode 1 — Install]**

**[SCREEN: browser at developer.android.com/studio]**

Before we can describe anything to Gemini, we need Android Studio, the FTC project, and a signed-in Gemini panel. This episode is all setup. Do it before the workshop if you can, because most of it is waiting on downloads.

Go to developer.android.com/studio and download the installer for your system.

**[SCREEN: installer running, "Standard" selected — time-lapse the rest]**

Run it, accept the defaults, and pick Standard. Then it downloads the Android SDK. This is the slow part — I've sped it up here. On school Wi-Fi, expect ten to twenty minutes.

**[SCREEN: Android Studio welcome screen]**

When you see this welcome screen, Android Studio is installed. Now the FTC project.

**[SCREEN: "Get from VCS" button → dialog]**

Click "Get from VCS." In the URL box, paste the FtcRobotController address.

**[TEXT CARD: `https://github.com/FIRST-Tech-Challenge/FtcRobotController` — hold 3 s]**

Pick a folder you'll remember and click Clone. Using Git this way means you can save a working version after every step, and go back if something breaks. We'll do that a lot.

**[SCREEN: Gradle sync progress bar — time-lapse]**

Now it syncs. Another wait — sped up again. If this fails with a network error, school Wi-Fi is usually the reason. A phone hotspot for this one step gets you past it.

**[SCREEN: Project panel, Android view, two modules visible]**

When the sync finishes, look at the Project panel on the left. Two modules. FtcRobotController is FIRST's code — we never edit it. TeamCode is ours. Every OpMode we write goes in here.

**[SCREEN: expand TeamCode down to the teamcode package]**

That's the folder: TeamCode, src, main, java, org, firstinspires, ftc, teamcode. Long path, but you only need to find it once.

**[SCREEN: switch to Project view, create `.aiexclude` at root]**

One thing before Gemini. FIRST's module has dozens of sample programs. If Gemini reads all of those, it gets confused about which robot it's writing for. So we tell it to ignore them.

Switch to Project view, right-click the top folder, New, File. Name it dot-a-i-exclude — with the dot at the front.

**[TEXT CARD: `.aiexclude` contents — the samples path — hold 3 s]**

Paste in the path to the samples folder and save. Now Gemini only looks at our code.

> **Ben:** if you used "Mark Directory as Excluded" instead, record that method here.

**[SCREEN: View → Tool Windows → Gemini]**

Now Gemini. View, Tool Windows, Gemini. Sign in with the Google account your coach set up.

**[SCREEN: Gemini asks about project context — click yes]**

It'll ask whether it can use your project as context. Say yes. That's what lets it see your hardware names and edit your files directly.

**[SCREEN: the mode selector in the Gemini panel, switching to Agent]**

One more thing: the mode. We use Agent mode, where Gemini creates and edits files in the project itself. If your panel only has a chat mode, it'll show you code and you paste it in — same method, more copying. If you don't see Agent mode, update Android Studio.

**[SCREEN: empty Gemini panel, signed in, Agent mode]**

Signed in, Agent mode, empty panel. Don't type anything yet.

**[SCREEN: Control Hub plugged in, run config dropdown showing TeamCode + Control Hub]**

Last step: prove the whole chain works before we write anything. Plug in the Control Hub with USB-C. The device dropdown at the top should show it. Run configuration says TeamCode. Click Run.

**[SCREEN: build output, "Install successfully finished"]**

"Install successfully finished." That means the laptop can build code and put it on the robot.

**[PIP: Driver Hub reconnecting, then showing the Robot Controller version]**

On the Driver Hub, the robot controller restarts and reconnects. The OpMode list is empty — that's expected. FIRST's sample programs are all disabled, so nothing shows until we write our own. The version number matching what we just built is the proof.

**[TEXT CARD: Checkpoint 0 checklist, 5 items]**

Checkpoint zero: Android Studio open, sync finished, dot-a-i-exclude in place, Gemini signed in, one successful deploy. Five checks. If they're all green, you're ready.

**[END CARD: "Next: Episode 2 — Describe your robot" + repo URL]**

---

## Shot list

- [ ] Browser download page
- [ ] Installer (time-lapse)
- [ ] Welcome screen
- [ ] Get from VCS dialog
- [ ] Gradle sync (time-lapse)
- [ ] Project panel, Android view
- [ ] Project panel, Project view, creating `.aiexclude`
- [ ] Gemini sign-in flow
- [ ] Run config + deploy
- [ ] Driver Hub OpMode list (PIP)

## Common failures to show (optional, 30 s each)

- USB cable that's charge-only → no device in dropdown → swap cable
- "Installation failed" signature mismatch → accept Android Studio's offer to uninstall and reinstall → run again
