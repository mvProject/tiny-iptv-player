/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:59
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:58
 *
 */

package com.mvproject.videoapp.data.helpers

import android.content.Context
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.provider.Settings
import io.github.aakira.napier.Napier

class ViewSettingsHelper(private val context: Context) {

    private val audioManager =
        (context.getSystemService(Context.AUDIO_SERVICE) as AudioManager).also {
            it.requestAudioFocus(AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN).build())
        }

    val volume
        get() =
            audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

    private val maxVolume
        get() =
            audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

    fun volumeUp() {
        updateVolume(volume + CHANGE_STEP)
    }

    fun volumeDown() {
        updateVolume(volume - CHANGE_STEP)
    }

    private fun updateVolume(volumeValue: Int) {
        Napier.e("testing currentVolume: $volume, volumeValue: $volumeValue,  maxVolume $maxVolume")
        val newValue = if (volumeValue in VOLUME_MIN..maxVolume) volumeValue else volume
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newValue, VOLUME_FLAG)
        Napier.w("testing volume after update $newValue")
    }

    val currentScreenBrightness: Int
        get() {
            val systemBrightness = Settings.System.getInt(
                context.contentResolver,
                Settings.System.SCREEN_BRIGHTNESS
            )
            val brightnessFloat = systemBrightness.toFloat() * (1f / 255)
            val currentBrightness = (brightnessFloat / (1.0 / 30.0)).toInt()
            Napier.w("testing systemBrightness: $systemBrightness, brightnessFloat: $brightnessFloat, currentScreenBrightness: $currentBrightness")
            return currentBrightness
        }


    private companion object {
        const val CHANGE_STEP = 1
        const val VOLUME_FLAG = 0
        const val VOLUME_MIN = 0
    }
}