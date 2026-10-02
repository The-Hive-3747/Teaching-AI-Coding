# docs

`verbatim-transcript.md` is **the** source: the complete Gemini agent session (`steps.jsonl`), every prompt exactly as typed and every reply in full, with tool calls summarized. `code-history/` is the TeamCode folder replayed after each of the 50 prompts that changed code, validated against `example-code/final/`.

`gemini-log-transcript.md` was the first recovery, extracted from the Gemini plugin's `gemini-backend.log`; it's superseded by the session transcript for this thread but still covers the other two projects (Koalaified, CubedMentors) and carries timestamps — 81 user prompts across five sessions with timestamps, Gemini's reply openings, and which project was open. The primary source for everything quoted as verbatim in this repo. Long prompts are clipped by the log (first 150 / last 149 characters); the 16 affected are listed at the end.

`AI_Development_Transcript_And_Guide.md` is Gemini's own summary of The Hive's build, written by Gemini at the team's request at the end of the week. It is kept **verbatim** — it's a primary source, and the six mechanical-team prompts quoted throughout this repo come from it.

Two places where it disagrees with the code it describes (the code wins):

- It says the back-off-wall step drives "FORWARD for 100 ms at 0.5 power" and calls the state `STATE_PARK_BACK_OFF_WALL`. The code (`example-code/final/BaseAuto.java`) drives backward at −0.5 and names it `STATE_5C_PARK_BACK_OFF_WALL`.
- The TeleOp's own header comment says "2Hz flashing"; the implementation, and the transcript's hardware section, are 1 Hz.

Gemini summaries are useful and worth asking for (see Checkpoint 9.6) — and this is the reminder to read them against the code.
