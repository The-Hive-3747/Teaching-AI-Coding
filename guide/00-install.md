# Checkpoint 0 — Install

**At the end of this checkpoint:** Android Studio is open with the FtcRobotController project loaded, the build works, the Gemini panel is signed in, and you've deployed a stock sample OpMode to your robot once.

**Time:** 30–60 minutes, most of it waiting on downloads. Do this before the workshop if you can.

---

## 0.1 Install Android Studio

1. Go to https://developer.android.com/studio and download the installer for your OS.
2. Run it. Accept the defaults. When it asks about install type, choose **Standard**.
3. First launch will download the Android SDK. Let it finish. This is the slow part.

`[SCREENSHOT: Android Studio setup wizard, "Standard" install type selected]`

**If it's wrong:**
- *"Not enough disk space"* — Android Studio plus SDK needs about 10 GB free. Clear space and retry.
- *Mac: "Android Studio can't be opened"* — Right-click the app → Open, then confirm.

## 0.2 Get the FtcRobotController project

You have two options. Git is better because you can save working versions as you go (see Rule 3 in the [README](README.md)).

**Option A — clone with Git (recommended):**
1. In Android Studio's welcome screen, choose **Get from VCS** (or **File → New → Project from Version Control**).
2. URL: `https://github.com/FIRST-Tech-Challenge/FtcRobotController`
3. Pick a folder. Click **Clone**.

`[SCREENSHOT: "Get from Version Control" dialog with the FtcRobotController URL filled in]`

**Option B — download a ZIP:**
1. Go to https://github.com/FIRST-Tech-Challenge/FtcRobotController
2. Green **Code** button → **Download ZIP**. Unzip it somewhere you'll remember.
3. Android Studio → **Open** → select the unzipped folder.

Either way, Android Studio will now run a **Gradle sync**. Wait for the progress bar at the bottom to finish. First sync can take 5–10 minutes.

`[SCREENSHOT: Gradle sync progress bar at the bottom of the window]`

### Three pop-ups you'll see — and what to click

Android Studio will show a few notifications during or right after the first sync. Two you accept, one you decline. Getting these wrong is the most common reason a fresh install won't build.

| Pop-up | What it says (roughly) | Click |
|---|---|---|
| **Gradle Daemon JVM toolchain** | "Migrate to Gradle Daemon JVM criteria" / "Gradle JDK configuration has changed" | **Accept / Migrate.** This tells Gradle which Java to use and is harmless. Declining leaves you with JDK-mismatch errors later. |
| **Gradle / Android Gradle Plugin upgrade** | "A newer version of Gradle / AGP is available. Upgrade?" or the **Upgrade Assistant** | **Decline / Don't ask again.** The FTC project pins the versions it's tested with. Upgrading breaks the build, and the fix is re-cloning. |
| **Windows Defender** (Windows only) | "Windows Defender might be impacting your build performance" | **Accept the automatic exclusion.** Defender scanning every file Gradle touches makes builds 2–5× slower. |

`[SCREENSHOT: the Daemon JVM toolchain migration prompt, with the accept button highlighted]`
`[SCREENSHOT: the Gradle upgrade prompt, with the decline button highlighted]`
`[SCREENSHOT: the Windows Defender notification with "Automatically exclude" highlighted]`

If you missed the Defender notification, add the exclusions by hand: **Windows Security → Virus & threat protection → Manage settings → Exclusions → Add**. Add these folders:
- your project folder (where you cloned FtcRobotController)
- `C:\Users\<you>\.gradle`
- `C:\Users\<you>\AppData\Local\Android\Sdk`
- the Android Studio install folder (usually `C:\Program Files\Android\Android Studio`)

Mac users can skip the Defender step.

> **Ben:** confirm the exact wording of the toolchain prompt from the version you record with, and whether Android Studio's automatic exclusion covered all four folders or you had to add some by hand.

**If it's wrong:**
- *Sync fails with a network error* — check the laptop is online; school Wi-Fi sometimes blocks Gradle downloads. Try a phone hotspot once to get the initial download done.
- *Missing SDK component* — click the blue link in the error message; Android Studio offers to install it.
- *You clicked "Upgrade" by accident* — the simplest recovery is to delete the project folder and clone again (Option A above). It's faster than untangling it.
- *JDK / "invalid Java home" / toolchain errors* — you probably declined the Daemon JVM toolchain migration. **File → Settings → Build, Execution, Deployment → Build Tools → Gradle**, set **Gradle JDK** to the embedded JDK (usually labeled "jbr"), and sync again.
- *Builds are very slow on Windows* — Defender exclusions aren't in place. See above.

## 0.3 Find your way around the project

In the **Project** panel on the left, switch the dropdown at the top to **Android** view if it isn't already. You'll see two modules:

- **FtcRobotController** — FIRST's code. You don't edit this. It contains the sample OpModes.
- **TeamCode** — your code. Every OpMode you write goes here:
  `TeamCode/src/main/java/org/firstinspires/ftc/teamcode/`

`[SCREENSHOT: Project panel in Android view showing FtcRobotController and TeamCode modules, TeamCode expanded to the teamcode package]`

## 0.4 Keep Gemini focused on your code

The FtcRobotController module contains dozens of sample OpModes. If Gemini reads all of them as context, it gets confused about which robot it's writing for and may copy sample hardware names instead of yours.

Tell Gemini to ignore the samples:

1. In the Project panel, switch to **Project** view (the dropdown at the top).
2. Right-click the project root folder → **New → File**. Name it `.aiexclude` (with the leading dot).
3. Put this in it:

   ```
   FtcRobotController/src/main/java/org/firstinspires/ftc/robotcontroller/external/samples/
   ```

4. Save.

`[SCREENSHOT: the .aiexclude file open in the editor with the samples path]`

> **Note for Ben:** confirm this matches what you did. If you used *Mark Directory as → Excluded* on the samples folder instead, document that here. Both approaches can be listed.

## 0.5 Sign in to Gemini

1. **View → Tool Windows → Gemini** (or click the Gemini icon on the right-hand toolbar).
2. Click **Sign in** and use the Google account your coach set up.
3. Gemini will ask whether it can use your project's code as context. Say **yes** — this is what lets it see your hardware names and edit your files.
4. Check which **mode** the panel is in. This guide assumes **Agent mode**, where Gemini creates and edits files in your project directly. In plain chat ("Ask") mode, Gemini shows code in the panel and you insert it yourself — the method still works, you just do more copying. Agent mode needs a recent Android Studio; if you don't see it, update.

`[SCREENSHOT: Gemini panel after sign-in, mode selector showing Agent, empty prompt box visible]`

> **Ben:** note the Android Studio version and Gemini mode you recorded with, so teams can match it.

**If it's wrong:**
- *Sign-in blocked* — the Google account may not be allowed to use Gemini (age or organization restrictions). Coaches: use a coach-owned or school-provisioned account, and check your district's policy.
- *Gemini panel isn't listed under Tool Windows* — update Android Studio (**Help → Check for Updates**). Gemini ships with current versions.

## 0.6 Deploy once, before you write anything

Prove the whole chain works with FIRST's code before you add your own.

1. Plug the Control Hub into the laptop with a USB-C cable (or connect over Wi-Fi if your team already does that).
2. At the top of Android Studio, make sure the run configuration says **TeamCode** and the device dropdown shows your Control Hub (it'll appear as something like "REV Robotics Control Hub").
3. Click the green **Run** ▶ button.
4. Wait for "Install successfully finished" in the bottom panel.
5. The Robot Controller app on the Control Hub restarts. On the Driver Hub, wait for it to reconnect, then check the Robot Controller version shown on the Driver Hub matches the SDK version you just built (it's in the FtcRobotController project's release notes). The OpMode list will be **empty** — the sample OpModes are all disabled by default, so nothing shows until you write your own. That's expected.

`[SCREENSHOT: run configuration dropdown showing TeamCode and the Control Hub as the target device]`

**If it's wrong:**
- *No device in the dropdown* — try a different USB cable (many are charge-only), and check the Control Hub is powered on.
- *"Installation failed" / signature mismatch* — the Control Hub has a Robot Controller app signed differently from your build. Android Studio usually offers to uninstall and reinstall; accept. If it doesn't, run `adb uninstall com.qualcomm.ftcrobotcontroller` from the terminal in Android Studio, then Run again.
- *Driver Hub can't see the Control Hub* — a Wi-Fi connection problem, not a code problem. The Driver Hub joins the Control Hub's Wi-Fi network; reconnect it per the FTC docs.

## Checkpoint 0 test

- [ ] Android Studio is open with no red errors in the bottom panel
- [ ] Gradle sync finished
- [ ] Toolchain migration accepted, Gradle upgrade declined, Defender exclusions in place (Windows)
- [ ] `.aiexclude` exists and lists the samples folder
- [ ] Gemini panel is signed in
- [ ] Run ▶ deploys to the Control Hub with "Install successfully finished"

All five? Go to [Checkpoint 1 — Describe the robot](01-describe-the-robot.md).
