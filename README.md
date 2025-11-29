#  Context-Aware Security Vault

An Android application demonstrating **Context-Aware Authentication**.
Created as a final assignment for the "User Interface Security & Privacy" course.

##  Project Overview
The app simulates a secure vault that only unlocks when specific **physical** and **environmental** conditions are met simultaneously. It utilizes device sensors and system states to verify the user's context before allowing access.

##  The 5 Security Conditions
[cite_start]To enable the login button, the user must satisfy **ALL** the following conditions[cite: 10, 20, 27]:

1.  [cite_start]** Orientation:** Phone must be held **Upside Down** (Bat Mode)[cite: 36].
2.  [cite_start]** Connectivity:** **Airplane Mode** must be turned **ON**[cite: 43].
3.  [cite_start]** Battery Status:** Battery percentage must be an **Even Number** (e.g., 50%, 82%)[cite: 28].
4.  [cite_start]** Sound Environment:** Device volume must be set to **0 (Silent Mode)**[cite: 42].
5.  [cite_start]** Display Mode:** Device must be in **Dark Mode**[cite: 31].

> **Visual Feedback:** The app provides real-time feedback (Red ❌ / Green ✅) for each condition to assist in testing and debugging.

##  Dynamic Password Logic
[cite_start]Once all physical conditions are met, the user must enter a dynamic password based on the current battery level[cite: 50].

**The Algorithm:** `[Battery Level] + [Sum of Digits] + [Reversed Level]`

### Examples:
* **If Battery is 84%:**
    * Level: `84`
    * Sum: `8 + 4 = 12`
    * Reversed: `48`
    * **Password:** `841248`
* **If Battery is 50%:**
    * Level: `50`
    * Sum: `5 + 0 = 5`
    * Reversed: `05`
    * **Password:** `50505`

##  Tech Stack
* **Language:** Kotlin
* **Environment:** Android Studio Ladybug/Koala (API 34/35)
* **Sensors Used:** Accelerometer (Gravity detection).
* **System Services:** AudioManager, BatteryManager, Settings.Global, UI Configuration.

##  How to Test (Emulator)
1.  **Rotate** the emulator 180 degrees (Virtual Sensors).
2.  Enable **Airplane Mode** from the quick settings panel.
3.  Set **Battery Level** to an even number via Extended Controls (3 dots > Battery).
4.  Lower **Volume** to zero.
5.  Enable **Dark Theme** in Android Settings > Display.
6.  Enter the calculated password based on the battery level you set.
