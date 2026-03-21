package com.pl.myworkoutapp.ui.effects

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.speech.tts.TextToSpeech
import android.view.WindowManager
import androidx.annotation.RequiresPermission

class AndroidPlatformEffects(private val activityProvider: () -> Activity?) : PlatformEffects {

    override fun keepScreenOn(enabled: Boolean) {
        val activity = activityProvider() ?: return
        if (enabled) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    override fun vibrate(durationMs: Long) {
        val activity = activityProvider() ?: return
        //val vibrator = activity.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                activity.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            activity.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
        vibrator.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
    }

    override fun playSound(type: SoundType) {
        val activity = activityProvider() ?: return
        //MediaPlayer.create(activity, R.raw.some_sound).start()
    }

    override fun speak(text: String) {
        val activity = activityProvider() ?: return
        val tts = TextToSpeech(activity) {}
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }
}