package com.sololeveling.sscprep.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.sin

class SystemSoundAndHaptics(private val context: Context) {

    private val prefs = context.getSharedPreferences("solo_audio_prefs", Context.MODE_PRIVATE)

    var isSoundEnabled: Boolean
        get() = prefs.getBoolean("sound_enabled", true)
        set(value) = prefs.edit().putBoolean("sound_enabled", value).apply()

    var isHapticsEnabled: Boolean
        get() = prefs.getBoolean("haptics_enabled", true)
        set(value) = prefs.edit().putBoolean("haptics_enabled", value).apply()

    private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vibratorManager?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    private val audioScope = CoroutineScope(Dispatchers.Default)

    fun triggerHaptic(type: String = "light") {
        if (!isHapticsEnabled) return
        try {
            if (vibrator == null || !vibrator.hasVibrator()) return

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val effect = when (type) {
                    "light" -> VibrationEffect.createOneShot(15, VibrationEffect.DEFAULT_AMPLITUDE)
                    "medium" -> VibrationEffect.createOneShot(35, VibrationEffect.DEFAULT_AMPLITUDE)
                    "heavy" -> VibrationEffect.createOneShot(70, VibrationEffect.DEFAULT_AMPLITUDE)
                    "arise" -> VibrationEffect.createWaveform(longArrayOf(0, 40, 60, 100, 80, 150), -1)
                    "levelup" -> VibrationEffect.createWaveform(longArrayOf(0, 30, 40, 50, 40, 80), -1)
                    else -> VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE)
                }
                vibrator.vibrate(effect)
            } else {
                @Suppress("DEPRECATION")
                when (type) {
                    "light" -> vibrator.vibrate(15)
                    "medium" -> vibrator.vibrate(35)
                    "heavy" -> vibrator.vibrate(70)
                    "arise" -> vibrator.vibrate(longArrayOf(0, 40, 60, 100, 80, 150), -1)
                    "levelup" -> vibrator.vibrate(longArrayOf(0, 30, 40, 50, 40, 80), -1)
                    else -> vibrator.vibrate(20)
                }
            }
        } catch (e: Exception) {
            // Ignored on devices without haptic engines
        }
    }

    fun playClick() {
        triggerHaptic("light")
        playTone(frequency = 880.0, durationMs = 35, volume = 0.25f)
    }

    fun playAlert() {
        triggerHaptic("medium")
        audioScope.launch {
            playTone(frequency = 587.33, durationMs = 100, volume = 0.4f)
            playTone(frequency = 880.0, durationMs = 180, volume = 0.5f)
        }
    }

    fun playLevelUp() {
        triggerHaptic("levelup")
        audioScope.launch {
            val notes = listOf(523.25, 659.25, 783.99, 1046.50) // C5, E5, G5, C6
            for (f in notes) {
                playTone(frequency = f, durationMs = 80, volume = 0.4f)
            }
        }
    }

    fun playAriseSound() {
        triggerHaptic("arise")
        audioScope.launch {
            // Deep resonant monarch chord
            val notes = listOf(130.81, 164.81, 196.00, 261.63, 392.00) // C3, E3, G3, C4, G4
            for (f in notes) {
                playTone(frequency = f, durationMs = 150, volume = 0.5f)
            }
        }
    }

    fun playBossRoar() {
        triggerHaptic("heavy")
        audioScope.launch {
            // Deep intimidating boss roar chord
            val notes = listOf(110.0, 98.0, 87.3, 73.4) // A2, G2, F2, D2
            for (f in notes) {
                playTone(frequency = f, durationMs = 120, volume = 0.6f)
            }
        }
    }

    private fun playTone(frequency: Double, durationMs: Int, volume: Float) {
        if (!isSoundEnabled) return
        try {
            val sampleRate = 44100
            val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
            val generatedSnd = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                val time = i.toDouble() / sampleRate
                val angle = 2.0 * Math.PI * time * frequency
                // Apply simple envelope to avoid clicks
                val envelope = when {
                    i < 200 -> i / 200.0
                    i > numSamples - 300 -> (numSamples - i) / 300.0
                    else -> 1.0
                }
                val sampleVal = (sin(angle) * 32767.0 * volume * envelope).toInt().toShort()
                generatedSnd[i] = sampleVal
            }

            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(generatedSnd.size * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            audioTrack.write(generatedSnd, 0, generatedSnd.size)
            audioTrack.play()
        } catch (e: Exception) {
            // Safe fallback
        }
    }
}
