// Builds slides/kickoff.pptx from the content in kickoff-outline.md.
// Run from the repo root:  node slides/build-deck.js
// Then import the .pptx into Google Slides (File → Import slides).
const pptxgen = require("pptxgenjs");
const path = require("path");
const fs = require("fs");

const ROOT = path.resolve(__dirname, "..");
const IMG = (f) => path.join(ROOT, "docs", "images", f);
const OUT = path.join(__dirname, "kickoff.pptx");

// Theme: The Hive's gold on black, dark throughout.
const THEME = {
  name: "The Hive",
  headFontFace: "Arial",
  bodyFontFace: "Calibri",
  colors: {
    dk1: "FFFFFF", // text
    lt1: "000000", // background
    dk2: "C8C8C8", // muted text
    lt2: "161616", // card background
    accent1: "F9BE14", // Hive gold
    accent2: "FFD75E", // light gold
    accent3: "3DDC84", // "green LEDs" — used sparingly for "it is / works"
    accent4: "FF5C5C", // red — "it isn't / broke"
    accent5: "8AB4F8",
    accent6: "9E9E9E",
    hlink: "F9BE14",
    folHlink: "C8C8C8",
  },
};

const pres = new pptxgen();
pres.layout = "LAYOUT_WIDE"; // 13.33 x 7.5
pres.theme = { headFontFace: THEME.headFontFace, bodyFontFace: THEME.bodyFontFace };
pres.title = "Describe your robot. Let AI write the code. Test. Repeat.";
pres.author = "The Hive, FTC 3747";
const C = pres.SchemeColor;
const W = 13.33, H = 7.5;
const GOLD = THEME.colors.accent1;

// ---------- layouts ----------
pres.defineSlideMaster({
  title: "TITLE",
  background: { color: C.background1 },
  objects: [],
});
pres.defineSlideMaster({
  title: "CONTENT",
  background: { color: C.background1 },
  margin: [0.5, 0.5, 0.9, 0.5],
  objects: [
    { image: { path: IMG("hive-mark.png"), x: 0.5, y: 6.85, w: 0.42, h: 0.43 } },
    { text: { text: "The Hive · FTC 3747 · github.com/The-Hive-3747/Teaching-AI-Coding",
              options: { x: 1.0, y: 6.85, w: 9, h: 0.43, fontSize: 10, color: C.text2, margin: 0, valign: "middle" } } },
    { placeholder: { options: { name: "title", type: "title", x: 0.5, y: 0.4, w: 12.33, h: 0.9,
        fontSize: 32, bold: true, color: C.accent1, margin: 0, valign: "middle", align: "left" }, text: "" } },
  ],
  slideNumber: { x: 12.3, y: 6.85, w: 0.6, h: 0.43, fontSize: 10, color: C.text2, align: "right" },
});

let n = 0;
function content(title, notes) {
  const s = pres.addSlide({ masterName: "CONTENT" });
  s.addText(title, { placeholder: "title" });
  if (notes) s.addNotes(notes);
  n++;
  return s;
}
function hex(s, x, y, size, label, opts = {}) {
  s.addShape(pres.ShapeType.hexagon, { x, y, w: size, h: size, line: { color: C.accent1, width: 2 }, fill: { color: opts.fill || C.background1 }, objectName: "hex " + label });
  s.addText(String(label), { x, y, w: size, h: size, align: "center", valign: "middle", fontSize: opts.fontSize || 18, bold: true, color: opts.color || C.accent1, margin: 0, isTextBox: true, objectName: "hex label " + label });
}
function card(s, x, y, w, h, name) {
  s.addShape(pres.ShapeType.roundRect, { x, y, w, h, rectRadius: 0.12, fill: { color: C.background2 }, line: { color: C.background2 }, objectName: name });
}
const body = (extra = {}) => ({ fontSize: 16, color: C.text1, margin: 0, isTextBox: true, valign: "top", ...extra });
const bullets = (items, extra = {}) => items.map((t, i) => ({ text: t, options: { bullet: true, breakLine: i < items.length - 1, paraSpaceAfter: 6, ...extra } }));

// ================= 1. Title =================
{
  const s = pres.addSlide({ masterName: "TITLE" });
  s.addImage({ path: IMG("match-closeup-green-leds.jpg"), x: 7.0, y: 0, w: W - 7.0, h: H, sizing: { type: "cover", w: W - 7.0, h: H }, transparency: 15, objectName: "robot photo" });
  s.addImage({ path: IMG("hive-mark.png"), x: 0.7, y: 0.6, w: 1.1, h: 1.13, objectName: "logo mark" });
  s.addText("Describe your robot.\nLet AI write the code.\nTest. Repeat.", { x: 0.7, y: 2.0, w: 6.2, h: 2.9, fontSize: 44, bold: true, color: C.accent1, margin: 0, isTextBox: true, valign: "top", objectName: "title" });
  s.addText("What The Hive learned watching our mentors code a robot in one week with Gemini in Android Studio — and how your team can do it too.", { x: 0.7, y: 5.0, w: 6.0, h: 1.3, fontSize: 16, color: C.text1, margin: 0, isTextBox: true, objectName: "subtitle" });
  s.addText("The Hive · FTC 3747 · Beehive Academy", { x: 0.7, y: 6.5, w: 8, h: 0.4, fontSize: 14, color: C.text2, margin: 0, isTextBox: true, objectName: "footer" });
  s.addNotes("On screen while people sit down. No talking needed.");
}

// ================= 2. Who we are =================
{
  const s = content("Who we are, and what this is", "Say the second line slowly. The whole talk hangs on it. Make the split clear: our robot was ours, coded by us, typed by us. The mentor robot was the experiment.");
  const rows = [
    ["1", "We're The Hive, FTC 3747. At Robot in One Week we built and coded our robot by hand — the normal way."],
    ["2", "Our mentors had their own robot for the mentor competition. They ran an experiment on it: no hand-written Java. Everything described to Gemini."],
    ["3", "We watched both happen. This is what we learned, and what you can use."],
  ];
  rows.forEach(([k, t], i) => {
    const y = 1.7 + i * 1.45;
    hex(s, 0.5, y, 0.8, k);
    s.addText(t, { x: 1.6, y, w: 6.9, h: 1.2, ...body({ fontSize: 18, valign: "middle" }), objectName: "line " + k });
  });
  s.addImage({ path: IMG("match-shooting-at-goal.jpg"), x: 9.1, y: 1.5, w: 2.6, h: 4.8, objectName: "shooting photo" });
  s.addText("The mentor robot in a match", { x: 9.1, y: 6.35, w: 3.2, h: 0.3, fontSize: 10, color: C.text2, margin: 0, isTextBox: true, objectName: "caption" });
}

// ================= 3. The problem =================
{
  const s = content("The problem every team has", 'Ask for a show of hands: "How many teams have exactly one person who can write Java?" Then: "How many of those people are also the coach?" Let it land. This is the pain the method solves. If you want, say which of us is the one programmer on The Hive.');
  const items = [
    ["1", "Most teams have 1 programmer.", "Sometimes 0."],
    ["⏳", "Every mechanical change waits", "on that one person."],
    ["?", "The mechanical people know what the robot should do.", "They just can't type it in Java."],
  ];
  items.forEach(([k, a, b], i) => {
    const x = 0.5 + i * 4.2;
    card(s, x, 1.7, 3.9, 4.6, "card " + i);
    hex(s, x + 0.3, 2.0, 1.0, k, { fontSize: 24 });
    s.addText(a, { x: x + 0.3, y: 3.3, w: 3.3, h: 1.5, ...body({ fontSize: 22, bold: true }), objectName: "head " + i });
    s.addText(b, { x: x + 0.3, y: 4.9, w: 3.3, h: 1.1, ...body({ fontSize: 18, color: C.text2 }), objectName: "sub " + i });
  });
}

// ================= 4a. Timeline =================
{
  const s = content("What the mentors did — the week", "Tell this as a story, not a list. Be precise about who did what, because coaches will ask: Ben described the architecture on night one and built the TeleOp out over the first two days — the shooter startup sequence, the pulsed feed, the LEDs. The first autonomous was Asim's design, called out at the robot while Ben typed, and from there it was a pair — Asim deciding what to change, Ben typing — plus eight prompts Asim typed himself: tank drive, the strafe fix, the spitting-balls pair, a whole experimental autonomous with three rounds of tuning. The point for the room: the person who understands the robot drives the prompt, whoever's hands are on the keys. Two things make the experiment honest, and say both: Ben CAN write Java, so choosing to describe instead of type was a choice; Asim can't, and made most of the changes anyway. The quotes are verbatim, typos and all. The screenshot is the 'spitting balls out' prompt exactly as typed, with Gemini's answer. If there's time, play docs/video/scrimmage-match3-auto.mp4 from the laptop here.");
  const rows = [
    ["Sep 17, 10:26 PM", "Ben (coordinator) describes the whole robot → working TeleOp that night, subsystem classes"],
    ["Sep 18, ~9:50 PM", "First autonomous state machines, Shoot First / Shoot Delayed — Asim's design, called out at the robot, typed by Ben"],
    ["Sep 19, 8:29 AM", "Asim (mechanical), at the keyboard: “holding both joysticks left made the robot strafe right, and vice versa. please fix”"],
    ["8:36 AM", "“it is spitting balls out, so i would like to shorten it” — reverse bump 200 → 100 ms"],
    ["8:40 AM", "“we get points for not touching the wall. Can we add a backup after the turn and drive forward?”"],
    ["11:35 AM", "“I thought gamepad 1 dpad was slow mode, not tuning the flywheel.”"],
    ["11:37 AM", "“I don't want the intake to resume. I want the intake to stop (whether it was stopped or running).”"],
    ["2:57 PM", "“Let's set the default speed to 0.95” — default changed from 1.0"],
    ["Scrimmage", "Autonomous scored; TeleOp drove."],
  ];
  const y0 = 1.5, dy = 0.58;
  s.addShape(pres.ShapeType.line, { x: 0.78, y: y0 + 0.15, w: 0, h: dy * (rows.length - 1), line: { color: C.accent1, width: 1.5 }, objectName: "timeline" });
  rows.forEach(([t, txt], i) => {
    const y = y0 + i * dy;
    s.addShape(pres.ShapeType.hexagon, { x: 0.65, y: y + 0.02, w: 0.26, h: 0.26, fill: { color: C.accent1 }, line: { color: C.accent1 }, objectName: "dot " + i });
    s.addText(t, { x: 1.05, y, w: 1.45, h: 0.3, ...body({ fontSize: 11, bold: true, color: C.accent1 }), objectName: "time " + i });
    s.addText(txt, { x: 2.5, y, w: 4.6, h: dy, ...body({ fontSize: 11, italic: txt.startsWith("“"), color: txt.startsWith("“") ? C.text1 : C.text2 }), objectName: "event " + i });
  });
  s.addImage({ path: IMG("android-studio-spitting-balls-prompt.png"), x: 7.4, y: 1.5, w: 5.43, h: 3.22, objectName: "spitting balls screenshot" });
  s.addText("Sep 19, 8:36 AM, as typed — and Gemini's reply: it reports the numbers (200 ms at −0.5, 300 ms pause), offers two options, then changes both at once. The next prompt put the power back.", { x: 7.4, y: 4.8, w: 5.43, h: 0.8, ...body({ fontSize: 11, color: C.text2 }), objectName: "screenshot caption" });
  s.addText("67 prompts over three days. 0 lines of Java typed.", { x: 7.4, y: 5.7, w: 5.43, h: 0.8, ...body({ fontSize: 20, bold: true, color: C.accent1 }), objectName: "stat" });
}

// ================= 4b. Quote =================
{
  const s = pres.addSlide({ masterName: "TITLE" });
  s.addShape(pres.ShapeType.hexagon, { x: -3.0, y: 1.2, w: 5.0, h: 5.2, line: { color: C.accent1, width: 2 }, fill: { color: "000000", transparency: 100 }, objectName: "hex decoration" });
  s.addText("“I've written Java for twenty-five years. I didn't type a line of it. After the first build, the changes came from the mechanical mentors — sometimes typed by them, sometimes called out from over the robot while I typed. They don't write Java.”", { x: 2.6, y: 1.4, w: 9.9, h: 4.0, fontSize: 28, color: C.text1, margin: 0, isTextBox: true, valign: "middle", objectName: "quote" });
  s.addText("— Ben, our coordinator", { x: 2.6, y: 5.5, w: 10, h: 0.5, fontSize: 18, color: C.accent1, margin: 0, isTextBox: true, objectName: "attribution" });
  s.addNotes("Put it up and let it sit for a few seconds. Then: the person who understands the robot drives the prompt, whoever's hands are on the keys.");
}

// ================= 5. Two robots =================
{
  const s = content("Hand-coding vs. architecting with AI", "This is the slide that's ours. The right column is from the mentor transcript (docs/verbatim-transcript.md); the left column is yours to fill from your own week — be concrete and be fair to both. The honest comparison is not 'AI was faster': it's WHERE THE BOTTLENECK WAS. On our robot every change went through whoever could type Java. On the mentor robot the bottleneck moved to describing precisely — and that's a skill the mechanical people already had. That's the thing we want every Utah team to hear.");
  const rows = [
    ["[THE HIVE: how many of us could write the code?]", "2 people wrote prompts — one writes Java, one doesn't"],
    ["[THE HIVE: hours spent coding during the week]", "67 prompts over three days; working TeleOp on night one"],
    ["[THE HIVE: what broke, and who could fix it]", "Mechanical mentor fixed strafe, flywheel bump and controls — in English, no Java"],
    ["[THE HIVE: how a mechanical change reached the code]", "Firewheel transfer servos came out Sep 18 (reported 7:50 PM); code reworked that night by describing the new design"],
    ["[THE HIVE: what we understood about our code]", "Someone still has to read what Gemini wrote — the Reader role"],
  ];
  const colW = 5.9, gap = 0.5, x1 = 0.5, x2 = x1 + colW + gap;
  s.addText("Our robot — hand-coded by us", { x: x1, y: 1.4, w: colW, h: 0.5, ...body({ fontSize: 20, bold: true, color: C.text1 }), objectName: "col1 head" });
  s.addText("Mentor robot — described to Gemini", { x: x2, y: 1.4, w: colW, h: 0.5, ...body({ fontSize: 20, bold: true, color: C.accent1 }), objectName: "col2 head" });
  rows.forEach(([a, b], i) => {
    const y = 2.0 + i * 0.82;
    card(s, x1, y, colW, 0.72, "l" + i); card(s, x2, y, colW, 0.72, "r" + i);
    s.addText(a, { x: x1 + 0.2, y, w: colW - 0.4, h: 0.72, ...body({ fontSize: 13, italic: true, color: C.text2, valign: "middle" }), objectName: "lt" + i });
    s.addText(b, { x: x2 + 0.2, y, w: colW - 0.4, h: 0.72, ...body({ fontSize: 13, valign: "middle" }), objectName: "rt" + i });
  });
  s.addText("The bottleneck moved: from “who can type Java” to “who can describe the robot precisely.”", { x: 0.5, y: 6.25, w: 12.3, h: 0.45, ...body({ fontSize: 15, bold: true, color: C.accent1 }), objectName: "takeaway" });
}

// ================= 6. Is / isn't =================
{
  const s = content("What this is — and isn't", "Coaches worry about this. Say it plainly: you still have to understand what a motor is, which way it spins, what a state machine does. You just don't have to type the Java. Be honest that this hasn't been run with students yet — the mentors ran it on the mentor robot, and we're the first team trying to turn it into something students do. That's why there's a Reader role later. Drop this slide if the demo runs long.");
  const is = ["Describing what the robot should do, in English", "Testing after every change", "Learning what your robot's parts are called and how they interact", "A way for the whole team to change code"];
  const isnt = ["Copy-pasting code you don't understand", "Asking for the whole robot at once", "Skipping the learning", "A way to avoid having anyone learn code"];
  const colW = 5.9, x1 = 0.5, x2 = 6.9;
  card(s, x1, 1.5, colW, 4.9, "is card"); card(s, x2, 1.5, colW, 4.9, "isnt card");
  s.addText("It is", { x: x1 + 0.4, y: 1.7, w: 5, h: 0.6, ...body({ fontSize: 26, bold: true, color: C.accent3 }), objectName: "is head" });
  s.addText("It isn't", { x: x2 + 0.4, y: 1.7, w: 5, h: 0.6, ...body({ fontSize: 26, bold: true, color: C.accent4 }), objectName: "isnt head" });
  s.addText(bullets(is, { paraSpaceAfter: 14 }), { x: x1 + 0.4, y: 2.5, w: colW - 0.8, h: 3.7, ...body({ fontSize: 17 }), objectName: "is list" });
  s.addText(bullets(isnt, { paraSpaceAfter: 14 }), { x: x2 + 0.4, y: 2.5, w: colW - 0.8, h: 3.7, ...body({ fontSize: 17 }), objectName: "isnt list" });
}

// ================= 7. The loop =================
{
  const s = content("The method: describe → generate → test → refine", "The loop is the whole workshop. Every checkpoint in the guide is one trip around it. The skill you build is DESCRIBE and REFINE: being specific about names, directions, timing and order.");
  const steps = [
    ["DESCRIBE", "“The intake is a motor named ‘intake’ plus two CR servos. A toggles collect at 0.5 power…”"],
    ["GENERATE", "Gemini writes or edits the OpMode"],
    ["TEST", "Deploy to the robot. Try it."],
    ["REFINE", "“it is spitting balls out, so i would like to shorten it”"],
  ];
  const bw = 2.7, bh = 1.0, gx = 0.45, y = 2.3, x0 = 0.5;
  steps.forEach(([h, t], i) => {
    const x = x0 + i * (bw + gx);
    s.addShape(pres.ShapeType.hexagon, { x, y, w: bw, h: bh, fill: { color: i === 0 || i === 3 ? C.accent1 : C.background2 }, line: { color: C.accent1, width: 2 }, objectName: "step " + h });
    s.addText(h, { x, y, w: bw, h: bh, align: "center", valign: "middle", fontSize: 20, bold: true, color: i === 0 || i === 3 ? C.background1 : C.accent1, margin: 0, isTextBox: true, objectName: "step label " + h });
    if (i < 3) s.addShape(pres.ShapeType.rightArrow, { x: x + bw + 0.05, y: y + 0.3, w: gx - 0.1, h: 0.4, fill: { color: C.accent1 }, line: { color: C.accent1 }, objectName: "arrow " + i });
    s.addText(t, { x, y: y + bh + 0.25, w: bw, h: 1.6, ...body({ fontSize: 13, color: C.text2, italic: t.startsWith("“") }), objectName: "step text " + h });
  });
  // return arrow: refine → generate
  const xr = x0 + 3 * (bw + gx) + bw / 2, xg = x0 + 1 * (bw + gx) + bw / 2;
  s.addShape(pres.ShapeType.line, { x: xg, y: 5.4, w: xr - xg, h: 0, line: { color: C.accent1, width: 1.5, beginArrowType: "triangle" }, objectName: "return line" });
  s.addText("…and back to GENERATE", { x: xg + 0.2, y: 5.5, w: 6, h: 0.4, ...body({ fontSize: 12, color: C.text2 }), objectName: "return label" });
  s.addText("The skill you build is DESCRIBE and REFINE: names, directions, timing, order.", { x: 0.5, y: 6.2, w: 12.3, h: 0.5, ...body({ fontSize: 15, bold: true, color: C.accent1 }), objectName: "takeaway" });
}

// ================= 8. Checkpoints =================
{
  const s = content("The checkpoints — one trip around the loop each", "Point out that 'test' comes after every step. The single biggest mistake is asking for everything at once. Small steps mean when something breaks, you know which description caused it. There's a guide page and a video per checkpoint — show the repo URL.");
  const cps = [
    ["0", "Install Android Studio + FtcRobotController, sign in to Gemini", false],
    ["1", "Describe the robot: names, ports, purposes, directions", false],
    ["2", "Mecanum drive", true],
    ["3", "Intake", true],
    ["4", "Shooter: flywheel startup sequence + pulsed feed", true],
    ["5", "Refine: the mechanical mentor's four real prompts", false],
    ["6", "Auto v1: back off the wall and shoot", true],
    ["7", "Auto v2: Shoot First / Shoot Delayed, which parks", true],
    ["8", "Auto v3: under 30 seconds, keep the LEAVE points", true],
    ["9", "Extras: LEDs, endgame rumble, the 4-ball experiment", false],
  ];
  cps.forEach(([k, t, test], i) => {
    const col = i < 5 ? 0 : 1, row = i % 5;
    const x = 0.5 + col * 6.3, y = 1.55 + row * 0.98;
    hex(s, x, y, 0.7, k, { fontSize: 18 });
    s.addText([{ text: t, options: {} }, ...(test ? [{ text: "  → test", options: { bold: true, color: C.accent3 } }] : [])], { x: x + 0.9, y, w: 5.1, h: 0.7, ...body({ fontSize: 14, valign: "middle" }), objectName: "cp " + k });
  });
  s.addText("github.com/The-Hive-3747/Teaching-AI-Coding — a guide page and a video per checkpoint", { x: 0.5, y: 6.35, w: 12.3, h: 0.4, ...body({ fontSize: 13, color: C.accent1 }), objectName: "repo" });
}

// ================= 9a. Five things =================
{
  const s = content("What a good description has", "Read these out. Names: exactly as in the Robot Controller config — 'front_left_drive', not 'the front left motor'. If Gemini has to guess any of the five, it will, and the guess is where the bug comes from.");
  const items = [
    ["Names", "exactly as in the Robot Controller config: front_left_drive, not “the front left motor”"],
    ["Purpose", "what the part does for the robot"],
    ["Direction", "which way is “forward”; which motors are mounted mirrored"],
    ["Control", "which button or stick; toggle or hold; what happens on release"],
    ["Timing and order", "what has to happen before what, and for how long"],
  ];
  items.forEach(([h, t], i) => {
    const y = 1.5 + i * 1.0;
    hex(s, 0.5, y, 0.75, i + 1, { fontSize: 18 });
    s.addText(h, { x: 1.5, y, w: 2.9, h: 0.75, ...body({ fontSize: 20, bold: true, valign: "middle" }), objectName: "k" + i });
    s.addText(t, { x: 4.4, y, w: 8.4, h: 0.75, ...body({ fontSize: 15, color: C.text2, valign: "middle" }), objectName: "v" + i });
  });
  s.addText("Anything you leave out, Gemini guesses.", { x: 0.5, y: 6.35, w: 12.3, h: 0.4, ...body({ fontSize: 15, bold: true, color: C.accent1 }), objectName: "takeaway" });
}

// ================= 9b. Vague vs specific =================
{
  const s = content("Vague vs. specific", "Read the vague one and ask the room what Gemini would have to guess. Then the specific one: everything it would have guessed is now stated. The third one is verbatim from the Sep 19 session — no code words at all, it describes a sequence — and that's why the fix was a few lines in Intake.java. This is the slide for the mechanical kids in the room.");
  card(s, 0.5, 1.45, 3.4, 4.9, "vague card");
  s.addText("Vague", { x: 0.75, y: 1.65, w: 3, h: 0.45, ...body({ fontSize: 18, bold: true, color: C.accent4 }), objectName: "vague head" });
  s.addText("“Add a shooter.”", { x: 0.75, y: 2.3, w: 2.9, h: 1.0, ...body({ fontSize: 22, italic: true }), objectName: "vague text" });
  s.addText("Which motor? Which way? Which button? Toggle or hold? What happens first?", { x: 0.75, y: 3.6, w: 2.9, h: 2.4, ...body({ fontSize: 13, color: C.text2 }), objectName: "vague guesses" });

  card(s, 4.2, 1.45, 8.63, 2.55, "specific card");
  s.addText("Specific", { x: 4.45, y: 1.6, w: 3, h: 0.4, ...body({ fontSize: 18, bold: true, color: C.accent3 }), objectName: "spec head" });
  s.addText("“One motor named flywheel, reversed. Pressing B runs the flywheel and intake backward at −0.5 for 200 ms to clear a jammed ball, stops everything for 300 ms, then runs the flywheel forward at full power and keeps it there. Pressing B again stops it. While it's running and right bumper is held, pulse the intake 100 ms on at 0.5, 200 ms off, so balls feed one at a time.”", { x: 4.45, y: 2.05, w: 8.15, h: 1.9, ...body({ fontSize: 12.5, italic: true }), objectName: "spec text" });

  card(s, 4.2, 4.2, 8.63, 2.15, "real card");
  s.addText("Real — Sep 19 session, verbatim", { x: 4.45, y: 4.33, w: 8, h: 0.4, ...body({ fontSize: 18, bold: true, color: C.accent1 }), objectName: "real head" });
  s.addText("“When I start up the flywheel, I want the process to continue to do the reverse function. But then I don't want the intake to resume. I want the intake to stop (whether it was stopped or running). Does that make sense?”", { x: 4.45, y: 4.78, w: 8.15, h: 1.1, ...body({ fontSize: 13.5, italic: true }), objectName: "real text" });
  s.addText("No code words. A sequence. A few lines changed in Intake.java.", { x: 4.45, y: 5.9, w: 8, h: 0.35, ...body({ fontSize: 12, color: C.text2 }), objectName: "real note" });
}

// ================= 10. Live demo =================
{
  const s = content("Now watch it happen", "Switch to Android Studio. One of you drives the laptop, one narrates, one handles the robot. 1) Show the Gemini panel with the robot description already there. 2) Type the mecanum drive prompt live and send it; narrate what you asked for while it generates. 3) Show the generated OpMode — point at the hardware names, they match the config. 4) Build, deploy, drive. 5) Type the real refinement: \"hey, in the latest push, holding both joysticks left made the robot strafe right, and vice versa. please fix\" — deploy, drive again. 6) Stop there: \"That's the whole method. Everything else is more trips around the loop.\" If it breaks, that's the method too: say what you see, paste the error into the panel, let the room watch the refine step. Don't apologize — a live fix is a better demo than a clean run.");
  s.addImage({ path: IMG("android-studio-tank-drive-prompt.png"), x: 0.5, y: 1.45, w: 8.4, h: 4.99, objectName: "tank drive screenshot" });
  s.addText("The mentors' tank-drive prompt, as typed, and the code it produced. You're about to see the same thing live.", { x: 9.2, y: 1.45, w: 3.63, h: 1.6, ...body({ fontSize: 15 }), objectName: "demo caption" });
  s.addText(bullets(["Description already sent", "Drive prompt, live", "Read the code: names match", "Build, deploy, drive", "Refine: the real strafe fix", "Deploy, drive again"], { paraSpaceAfter: 6 }), { x: 9.2, y: 3.2, w: 3.63, h: 3.0, ...body({ fontSize: 14, color: C.text2 }), objectName: "demo steps" });
}

// ================= 11. Where it goes wrong =================
{
  const s = content("Where it goes wrong — and the fix", "The italic rows and the fake-gamepad row happened on the mentor robot. The compile-error and over-30-seconds rows are the ones you'll hit first that the mentors got lucky on — their delayed auto adds up to 30.7 s on paper and the field timer let it finish. The pattern: when it's wrong, the description was missing something. Say the missing thing. The fake-gamepad row is the one Ben cares most about: the auto worked, but it was tied to the TeleOp button map because nobody said an auto was coming.");
  const hdr = (t) => ({ text: t, options: { bold: true, color: GOLD, fill: { color: THEME.colors.lt2 }, fontSize: 15 } });
  const c = (t, it) => ({ text: t, options: { fontSize: 13, color: "FFFFFF", italic: !!it } });
  const rows = [
    [hdr("Symptom"), hdr("Cause"), hdr("Fix")],
    [c("Won't compile: “cannot find symbol”"), c("Gemini invented a name"), c("Paste the error back: “Fix this compile error”")],
    [c("Strafe is mirrored"), c("Mecanum mixing signs"), c("“holding both joysticks left made the robot strafe right, and vice versa. please fix”", 1)],
    [c("Flywheel startup spits balls out"), c("Reverse bump too long"), c("“what is the current time of spinning the motors back…? it is spitting balls out, so i would like to shorten it”", 1)],
    [c("Two controls on one button"), c("Control scheme drifted"), c("“I thought gamepad 1 dpad was slow mode, not tuning the flywheel.”", 1)],
    [c("Auto drives the intake through a fake gamepad"), c("Nobody told Gemini an autonomous was coming"), c("Say it up front: “An autonomous will use this class later — put the behavior in plain methods.” Gemini takes the expedient route unless you say what's coming.")],
    [c("Auto runs over 30 s"), c("Gemini was told the limit; nobody saw the sum"), c("“Add up every state's duration and tell me the worst-case run time.” Then cut one number.")],
    [c("Gemini rewrote everything, broke what worked"), c("Didn't say “only change X”"), c("“Only change the intake section. Leave the drive code as is.”")],
  ];
  s.addTable(rows, { x: 0.5, y: 1.45, w: 12.33, colW: [3.1, 3.0, 6.23], border: { type: "solid", color: "333333", pt: 0.75 }, fill: { color: "000000" }, margin: 0.08, valign: "middle", rowH: 0.55, objectName: "fix table" });
  s.addText("When it's wrong, the description was missing something. Say the missing thing.", { x: 0.5, y: 6.35, w: 12.3, h: 0.4, ...body({ fontSize: 14, bold: true, color: C.accent1 }), objectName: "takeaway" });
}

// ================= 12. Roles & rules =================
{
  const s = content("Running this with your team", "The Reader role is the answer to 'aren't they just cheating.' If a student can explain what the state machine does and why the timings are what they are, they learned it. Tell the room which of these roles each of you would take on The Hive. The Describer on the mentor robot was Asim, the mechanical mentor — he can't write Java, and he made most of the changes.");
  const roles = [
    ["Describer", "Whoever knows the robot best says what to change. They can type it or someone else can — what matters is whose words they are."],
    ["Tester", "Deploys and drives. Says exactly what happened."],
    ["Reader", "Reads the generated code aloud and explains it. This is where the learning happens."],
    ["Coach", "Asks “what did you tell it?” when something breaks. Doesn't type."],
  ];
  roles.forEach(([h, t], i) => {
    const x = 0.5 + i * 3.1;
    card(s, x, 1.45, 2.9, 3.0, "role " + h);
    s.addText(h, { x: x + 0.2, y: 1.6, w: 2.5, h: 0.5, ...body({ fontSize: 20, bold: true, color: C.accent1 }), objectName: "role head " + h });
    s.addText(t, { x: x + 0.2, y: 2.15, w: 2.5, h: 2.2, ...body({ fontSize: 13 }), objectName: "role text " + h });
  });
  s.addText("Ground rules", { x: 0.5, y: 4.7, w: 4, h: 0.4, ...body({ fontSize: 18, bold: true }), objectName: "rules head" });
  s.addText(bullets(["One change per prompt. Test after every change.", "Commit (or copy the file) after every working test.", "If Gemini rewrites something that worked, say “only change X.”", "The Reader has to be able to explain the code before the team moves on."], { paraSpaceAfter: 4 }), { x: 0.5, y: 5.15, w: 12.3, h: 1.5, ...body({ fontSize: 14 }), objectName: "rules" });
}

// ================= 13. What you need =================
{
  const s = content("What you need to start", "The hardware list is the one thing to do BEFORE opening Gemini. For anyone installing tonight: during the first Gradle sync, Android Studio pops up three things. Accept the Daemon JVM toolchain migration. DECLINE the Gradle/AGP upgrade — it breaks the FTC project. On Windows, accept the Defender exclusion or builds crawl. Checkpoint 0 in the guide has all three.");
  const items = [
    "A laptop that runs Android Studio (Windows, Mac or Linux)",
    "A Google account signed in to Gemini in Android Studio",
    "The FtcRobotController project cloned",
    "Your robot's hardware config done on the Driver Hub — names and ports",
    "A written list of every motor and servo: name, port, purpose, direction",
    "The guide and videos: github.com/The-Hive-3747/Teaching-AI-Coding",
  ];
  items.forEach((t, i) => {
    const y = 1.5 + i * 0.78;
    s.addShape(pres.ShapeType.hexagon, { x: 0.5, y: y + 0.12, w: 0.42, h: 0.42, line: { color: C.accent1, width: 1.5 }, fill: { color: C.background1 }, objectName: "check " + i });
    s.addText(t, { x: 1.15, y, w: 7.6, h: 0.66, ...body({ fontSize: 16, valign: "middle" }), objectName: "need " + i });
  });
  card(s, 9.2, 1.5, 3.63, 4.6, "popups card");
  s.addText("First Gradle sync — three pop-ups", { x: 9.4, y: 1.65, w: 3.3, h: 0.7, ...body({ fontSize: 15, bold: true, color: C.accent1 }), objectName: "popups head" });
  s.addText([
    { text: "Accept", options: { bold: true, color: C.accent3 } }, { text: " the Daemon JVM toolchain migration", options: { breakLine: true, paraSpaceAfter: 10 } },
    { text: "Decline", options: { bold: true, color: C.accent4 } }, { text: " the Gradle / AGP upgrade — it breaks the FTC project", options: { breakLine: true, paraSpaceAfter: 10 } },
    { text: "Accept", options: { bold: true, color: C.accent3 } }, { text: " the Windows Defender exclusion, or builds crawl", options: {} },
  ], { x: 9.4, y: 2.4, w: 3.3, h: 3.5, ...body({ fontSize: 14 }), objectName: "popups" });
  s.addText("Checkpoint 0 has all three.", { x: 9.4, y: 5.6, w: 3.3, h: 0.4, ...body({ fontSize: 12, color: C.text2 }), objectName: "popups note" });
}

// ================= 14. Close =================
{
  const s = pres.addSlide({ masterName: "TITLE" });
  s.addImage({ path: IMG("hive-logo.png"), x: 8.2, y: 4.3, w: 4.8, h: 2.7, objectName: "logo" });
  s.addText("Start small.\nTest every step.\nWhen it's wrong, describe what was missing.", { x: 0.7, y: 1.2, w: 11.5, h: 3.6, fontSize: 40, bold: true, color: C.accent1, margin: 0, isTextBox: true, valign: "middle", objectName: "close" });
  s.addText("github.com/The-Hive-3747/Teaching-AI-Coding", { x: 0.7, y: 5.1, w: 7.5, h: 0.5, fontSize: 20, color: C.text1, margin: 0, isTextBox: true, objectName: "repo" });
  s.addText("Guide, videos, every real prompt, and the code — all of it.\nFind us at the pits: The Hive, FTC 3747.", { x: 0.7, y: 5.7, w: 7.5, h: 1.0, fontSize: 16, color: C.text2, margin: 0, isTextBox: true, objectName: "find us" });
  s.addNotes("No Q&A slot, so say where people can find you afterward.");
}

(async () => {
  await pres.writeFile({ fileName: OUT });
  const { applyTheme } = require(process.env.PPTX_SKILL ? path.join(process.env.PPTX_SKILL, "scripts/apply_theme.js") : "/mnt/skills/public/pptx/scripts/apply_theme.js");
  await applyTheme(OUT, THEME);
  console.log("wrote", OUT);
})();
