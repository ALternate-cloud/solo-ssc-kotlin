# Solo Leveling SSC Preparation App (Native Kotlin + Jetpack Compose)

A standalone native Android application built with **Kotlin** and **Jetpack Compose**, implementing the full **Solo Leveling Gamified Exam Preparation System** for SSC CGL / CHSL / MTS / CPO aspirants.

---

## ⚡ Key Features & Architectures

1. **Hunter Status Window & Leveling System**:
   - Dynamic Leveling curve: `Max EXP = 100 * 1.22^(Level - 1)`
   - Hunter Ranks: **E-Rank Aspirant** up to **Shadow Monarch (Supreme Rank)**
   - Core Stats Allocation: **INT** (Mastery & MP), **VIT** (Endurance & HP), **AGI** (Speed), **SEN** (Precision), **STR** (Discipline)
   - Target SSC CGL Posts: Income Tax Inspector, MEA ASO, GST Inspector, CBI Sub-Inspector, ED AEO, CAG AAO.

2. **CBT Mock Exam & Boss Battle Engine**:
   - Authentic SSC CGL CBT marking scheme: **+2.0 Marks for Correct**, **-0.50 Marks for Incorrect**
   - Boss HP vs Player HP live combat bars during tests
   - Question palette with Answered, Flagged for Review, and Unattempted tracking
   - Comprehensive score breakdown with accuracy % and level-up rewards

3. **"ARISE" Shadow Mistake Extraction**:
   - Mistake Notebook automatically records questions answered incorrectly during raids
   - The iconic **"ARISE"** action extracts defeated monsters into loyal Shadow Soldiers
   - Levels up Shadow Commanders: **Igris** (Quant speed boost), **Beru** (Negative mark shield), **Iron** (English EXP), **Tusk** (Insight vision shortcuts)

4. **Daily Quests & Penalty Zone**:
   - Daily syllabus targets for Quant, Reasoning, English, and Focus
   - Daily Reset countdown timer
   - Red Gate Penalty Survival Mode triggered when daily study targets are missed

5. **Procedural Infinite Question Generator**:
   - Algorithmic math & logic generator producing infinite syllabus-accurate questions for the **Demon Castle Infinite Tower**

6. **Sanctum of Focus (Pomodoro) & Hunter Shop**:
   - 25 / 50 minute deep concentration timer with study EXP rewards
   - Item Vault for Elixirs, Streak Shields, Ruler's Authority Relics, and Monarch Titles

---

## 🛠️ How to Build & Run

### Method 1: Android Studio (Recommended)
1. Open **Android Studio** (Koala / Ladybug or newer).
2. Choose **Open** and select the `/android` directory.
3. Allow Gradle to sync dependencies.
4. Click **Run (`Shift + F10`)** on any Android device or emulator running Android 8.0+ (API 26+).

### Method 2: Command Line (Gradle)
```bash
cd android
./gradlew assembleDebug
```
The debug APK will be generated at `android/app/build/outputs/apk/debug/app-debug.apk`.

---

## 📁 Package Architecture (`com.sololeveling.sscprep`)
- `domain.model`: Pure Kotlin data models (`PlayerState`, `HunterRank`, `Question`, `DungeonGate`, `ShadowCommander`, `DailyTask`, `ShopItem`)
- `domain.engine`: Business logic engines (`PlayerEngine`, `DungeonEngine`, `ShadowEngine`, `InfiniteQuestionGenerator`, `DailyQuestEngine`, `ShopEngine`)
- `data`: Offline persistence (`SystemRepository`) and question vaults (`QuestionVaultData`, `PyqPapersData`)
- `audio`: Real-time synthesizer & haptics (`SystemSoundAndHaptics`)
- `ui.theme`: Solo Leveling holographic cyberpunk theme (`Theme`, `Color`, `Type`)
- `ui.components`: Custom composables (`SystemWindowCard`, `StatProgressBar`, `RankBadgeChip`, `SoloGlowingButton`)
- `ui.screens`: Screens for Status, Quests, Vault, Dungeons, CBT Raids, Shadows, Focus, Shop, Syllabus, and Leaderboard
- `ui.viewmodel`: Central reactive StateFlow coordinator (`MainViewModel`)
