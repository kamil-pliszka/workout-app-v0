package com.pl.myworkoutapp.ui.effects

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.*
import android.speech.tts.TextToSpeech
import androidx.annotation.RequiresPermission
import com.pl.myworkoutapp.ui.common.KeepScreenController
import java.io.Closeable

class AndroidPlatformEffects(
    private val context: Context,
    private val keepScreenController: KeepScreenController,
    private val activityProvider: () -> Activity?
) : PlatformEffects, Closeable {
    private var ttsReady = false
    private val pendingTexts = mutableListOf<String>()

    override fun keepScreenOn(enabled: Boolean) {
        keepScreenController.enabled.value = enabled
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    override fun vibrate(durationMs: Long) {
        println("vibrate: $durationMs")
        //val vibrator = activity.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
        vibrator.vibrate(
            VibrationEffect.createOneShot(
                durationMs,
                VibrationEffect.DEFAULT_AMPLITUDE
            )
        )
    }

    override fun playSound(type: SoundType) {
        //val activity = activityProvider() ?: return
        //MediaPlayer.create(activity, R.raw.some_sound).start()
    }


    private val tts = TextToSpeech(context) { status ->
        ttsReady = status == TextToSpeech.SUCCESS
        if (ttsReady) {
            pendingTexts.forEach(::speakInternal)
            pendingTexts.clear()
        }
    }

    override fun speak(text: String) {
        if (!ttsReady) {
            pendingTexts += text
            return
        }

        speakInternal(text)
    }

    private fun speakInternal(text: String) {
        tts.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            null
        )
    }

    override fun close() {
        tts.stop()
        tts.shutdown()
    }
}