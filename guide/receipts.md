# Receipts — How to Prove Your Students Wrote It

When a mentor types Java, nothing records who decided what. When a student describes a change to Gemini, every decision is a timestamped sentence in that student's own words. That makes AI-assisted code *more* provable than hand-written code — if you keep the log.

This page is how to keep it. It's what The Hive does, and it's how this repo's [`docs/verbatim-transcript.md`](../docs/verbatim-transcript.md) came to exist: every prompt from the mentors' build, exactly as typed, tagged with who said it.

---

## 1. One prompt, one commit, the student's name on it

The simplest receipt needs no export at all. After every change that works:

1. Commit it.
2. Put the prompt in the commit message, and the name of the student who wrote the prompt.

```
Strafe fix — prompted by Sophi:
"hey, in the latest push, holding both joysticks left made the robot strafe right, and vice versa. please fix"
```

Git already records the date and time. Over a season that's a log anyone can read in the GitHub history — a judge, a coach, a parent — and it shows the robot was changed one described step at a time, by named students.

Set the Git author to the student at the keyboard (Android Studio: **Git → Settings → Git → Author** per commit, or `git config user.name` on each student's login). If one laptop is shared, the prompt line in the message does the job.

## 2. Export the actual conversation

Android Studio keeps the full agent session on disk — every prompt, every reply, every file Gemini wrote. There's no export button, but the file is plain text (JSON lines).

**Where it is (Windows):**

```
%LOCALAPPDATA%\Google\AndroidStudio<version>\projects\<your-project>.<hash>\
```

Search that folder (and below) for `steps.jsonl`. Each agent conversation has its own folder named with a long id; the thread title is inside the file. The mentors' session was at `…\projects\ftcrobotcontroller.66bb2583\…\<thread-id>\steps.jsonl`.

**Mac:** the same `projects` folder lives under `~/Library/Application Support/Google/AndroidStudio<version>/` (or `~/Library/Caches/Google/AndroidStudio<version>/` on some versions). Spotlight for `steps.jsonl`.

> **Ben:** confirm the Mac path, and the Windows sub-folder under `projects\…` where `steps.jsonl` sits — the repo only has the Windows path from your machine.

**Lighter alternative:** `gemini-backend.log` in Android Studio's log folder (**Help → Show Log in Explorer / Finder**) records every prompt with a timestamp. It clips long prompts to the first 150 and last 149 characters, so it's a timeline, not a transcript. [`docs/gemini-log-transcript.md`](../docs/gemini-log-transcript.md) is what that looks like.

**What not to bother with:** `app-internal-state.db` in the `AppData\Roaming` folder is not the chat history (it's an H2 database of IDE state).

## 3. Turn the export into a transcript

Copy `steps.jsonl` into your repo under `docs/`, then ask Gemini (or any AI) to render it:

> "Read `docs/steps.jsonl`. Write `docs/transcript.md` with every user prompt exactly as typed, in order, numbered, with its timestamp, followed by Gemini's reply in full. Summarize tool calls to one line each. Do not clean up spelling or casing."

Then the step that makes it a receipt: **tag each prompt with its author.** The student who wrote it adds a line under it:

```
> Author: Sadiqah (at the keyboard)
```

or, for a change called out while someone else typed:

```
> Author: Tom's request, typed by Sophi
```

Do this the same week; memory fades. In this repo the author tags were worked out from writing style and then confirmed by the people who were there — it worked, but it was harder than writing them down at the time.

## 4. Credit it where FIRST and the College Board already ask you to

- **FTC portfolio:** the Competition Manual's rule A201 lets teams use AI for the portfolio "provided they respect intellectual property rights and include a footnote or endnote credit." Do the same for code even though the manual is silent on it: FIRST's own blog says teams can use AI for "writing robot code, etc." with credit, and gives the form *"Essay created by Team XXXX and ChatGPT, or similar."*
- **In the code:** a one-line comment at the top of each AI-written file, the way the College Board requires for AP CS Principles: `// This code was generated using Gemini in Android Studio, from prompts by <names>.`
- **In the repo README:** link the transcript. That's the receipt.

## 5. The test that matters

None of this replaces the one that already exists: at judging, students answer and adults may only observe. The Control Award interview is where "the kids didn't do this" gets settled, and a student who can walk a judge through the state machine *and* point to the prompt log that built it has an answer no hand-coded team can give.

Rule for the season, in one line: **no AI-generated code gets committed until a student other than the one who prompted it can explain it out loud.** Then commit it with the prompt and the name.
