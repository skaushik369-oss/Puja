package com.puja.mantrajaap

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Locale

/**
 * Speaks a mantra in Hindi a fixed number of times using Android's built-in text-to-speech.
 *
 * Android's speech engine has no real "pause", so pausing stops the current line and
 * resuming starts that same line again. The count is never lost.
 */
class ChantPlayer(context: Context) : TextToSpeech.OnInitListener {

    enum class Status { Idle, Playing, Paused, Finished }

    enum class VoiceProblem {
        /** No speech engine is installed or it failed to start. */
        NoEngine,
        /** The engine works but has no Hindi voice. */
        NoHindiVoice,
        /** The engine reported an error while speaking (often: voice not downloaded, no internet). */
        SpeakError,
    }

    data class State(
        val engineReady: Boolean = false,
        val status: Status = Status.Idle,
        /** The repetition being spoken now, starting at 1. 0 when nothing has started. */
        val current: Int = 0,
        val total: Int = DEFAULT_REPEAT_COUNT,
        val problem: VoiceProblem? = null,
    )

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()

    private val main = Handler(Looper.getMainLooper())
    private val tts = TextToSpeech(context.applicationContext, this)

    private var hindiAvailable = false
    private var mantra: Mantra? = null
    private var repetition = 0
    private var part = 0
    private var speechRate = 1.0f

    /** Bumped on every start, pause and stop so callbacks from old utterances are ignored. */
    private var generation = 0

    override fun onInit(status: Int) {
        main.post {
            if (status != TextToSpeech.SUCCESS) {
                _state.update { it.copy(problem = VoiceProblem.NoEngine) }
                return@post
            }
            tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {}
                override fun onDone(utteranceId: String?) {
                    main.post { onPartDone(utteranceId) }
                }
                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    main.post { onPartError(utteranceId) }
                }
                override fun onError(utteranceId: String?, errorCode: Int) {
                    main.post { onPartError(utteranceId) }
                }
            })
            _state.update { it.copy(engineReady = true) }
            checkHindiVoice()
        }
    }

    /** Checks (again) whether a Hindi voice is installed. Call after the user returns from settings. */
    fun checkHindiVoice(): Boolean {
        if (!_state.value.engineReady) return false
        if (_state.value.status == Status.Playing) return hindiAvailable
        val result = try {
            tts.setLanguage(HINDI)
        } catch (e: Exception) {
            TextToSpeech.LANG_NOT_SUPPORTED
        }
        hindiAvailable = result != TextToSpeech.LANG_MISSING_DATA &&
            result != TextToSpeech.LANG_NOT_SUPPORTED
        if (hindiAvailable && _state.value.problem == VoiceProblem.NoHindiVoice) {
            _state.update { it.copy(problem = null) }
        }
        return hindiAvailable
    }

    /** Starts the mantra from 1, or resumes it if it is paused. */
    fun play(selected: Mantra) {
        val s = _state.value
        if (!s.engineReady) {
            if (s.problem == null) return // still starting up
            _state.update { it.copy(problem = VoiceProblem.NoEngine) }
            return
        }
        if (!checkHindiVoice()) {
            _state.update { it.copy(problem = VoiceProblem.NoHindiVoice) }
            return
        }
        if (s.status == Status.Paused && mantra == selected) {
            generation++
            speakCurrentPart()
            return
        }
        mantra = selected
        repetition = 1
        part = 0
        generation++
        _state.update { it.copy(total = selected.repeatCount) }
        speakCurrentPart()
    }

    fun pause() {
        if (_state.value.status != Status.Playing) return
        generation++
        tts.stop()
        _state.update { it.copy(status = Status.Paused) }
    }

    fun stop() {
        generation++
        tts.stop()
        mantra = null
        repetition = 0
        part = 0
        _state.update { it.copy(status = Status.Idle, current = 0) }
    }

    /** 1.0 is normal speed. Applies from the next line spoken. */
    fun setSpeechRate(rate: Float) {
        speechRate = rate
    }

    fun clearProblem() {
        _state.update { it.copy(problem = null) }
    }

    fun shutdown() {
        generation++
        tts.stop()
        tts.shutdown()
    }

    private fun speakCurrentPart() {
        val m = mantra ?: return
        tts.setSpeechRate(speechRate)
        val id = "$generation:$repetition:$part"
        tts.speak(m.speechParts[part], TextToSpeech.QUEUE_FLUSH, Bundle(), id)
        _state.update { it.copy(status = Status.Playing, current = repetition) }
    }

    private fun isCurrent(utteranceId: String?): Boolean =
        utteranceId?.substringBefore(':') == generation.toString() &&
            _state.value.status == Status.Playing

    private fun onPartDone(utteranceId: String?) {
        if (!isCurrent(utteranceId)) return
        val m = mantra ?: return
        part++
        if (part >= m.speechParts.size) {
            part = 0
            repetition++
            if (repetition > m.repeatCount) {
                generation++
                mantra = null
                _state.update { it.copy(status = Status.Finished, current = m.repeatCount) }
                return
            }
        }
        speakCurrentPart()
    }

    private fun onPartError(utteranceId: String?) {
        if (!isCurrent(utteranceId)) return
        generation++
        _state.update { it.copy(status = Status.Paused, problem = VoiceProblem.SpeakError) }
    }

    companion object {
        val HINDI: Locale = Locale("hi", "IN")
    }
}
