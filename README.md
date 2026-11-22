# Puja · Mantra Jaap

An Android app that chants a selected mantra aloud in Hindi **108 times**, with a live counter
(`12 / 108`) and the mantra text shown in a scrollable box.

Mantras included:

1. Kamakhya Devi
2. Maha Mrityunjay Jap
3. Vishnu Sahasranama (Shri Rama Rama Rameti)
4. Surya Beej Mantra
5. Rahu Remedy
6. Shani Beej Mantra
7. Hanuman Chalisa (full text)

## Install on your phone

1. Open the [latest release](../../releases/tag/latest) on your Android phone and download **MantraJaap.apk**.
   (Until the first merge to `main`, get it from the **Actions** tab instead: open the latest
   "Build APK" run and download the **MantraJaap-apk** artifact, which is a zip containing the APK.)
2. Open the downloaded file. If Android asks, allow your browser or file manager to
   "install unknown apps".
3. Open **Mantra Jaap**, pick a mantra, and press **Play**.

The app uses the phone's built-in text-to-speech with the **Hindi (India)** voice. If that voice is
missing, the app shows a prompt that takes you to the voice download screen. With Google's speech
engine: *Settings → System → Languages → Text-to-speech output → Speech Services by Google →
Install voice data → Hindi (India)*.

## How it works

- Pick a mantra from the dropdown and press **Play**. It is spoken 108 times in a row and the
  counter shows which repetition is playing.
- **Pause** / **Resume** keeps the count. **Stop** resets it to 0.
- A **Speed** slider sets how fast the voice chants (0.5× to 1.5×).
- The screen stays on while chanting.
- Text is shown in English letters as written; the Hindi voice reads a Devanagari version of the
  same text for correct pronunciation.

## Changing the repeat count

All mantras use `DEFAULT_REPEAT_COUNT = 108` in
[`Mantras.kt`](app/src/main/java/com/puja/mantrajaap/Mantras.kt). Change that one number to change
it everywhere, or give a single mantra its own count with `repeatCount = 21` (the Surya Beej Mantra
has a comment showing where, since it is traditionally chanted 21 times).

## Build it yourself

Requirements: Android Studio (or the Android SDK with platform 35) and JDK 17+.

```bash
./gradlew assembleDebug
# APK: app/build/outputs/apk/debug/app-debug.apk
adb install app/build/outputs/apk/debug/app-debug.apk
```

Or open the folder in Android Studio and press Run.

Every push builds the APK on GitHub Actions (`.github/workflows/build-apk.yml`); pushes to `main`
also publish it as the `latest` release.

## iPhone / web version

A web version of the same app lives in [`web/`](web/) (plain HTML/JS, no build step). Open the
hosted link in **Safari**, tap **Share → Add to Home Screen**, and it appears with the Mantra Jaap
icon and opens full screen. It uses the phone's Hindi voice (iPhone: Settings → Accessibility →
Spoken Content → Voices → Hindi). Keep the screen on while chanting; iOS may pause speech when the
phone locks.
