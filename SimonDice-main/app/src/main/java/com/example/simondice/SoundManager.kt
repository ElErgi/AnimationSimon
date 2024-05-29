package com.example.simondice

import android.media.AudioAttributes
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
class SoundManager(activity: AppCompatActivity, private val soundResources: List<Int>) {

    private val soundPool: SoundPool
    private val soundIds = IntArray(soundResources.size)

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(soundResources.size)
            .setAudioAttributes(audioAttributes)
            .build()

        for (i in soundResources.indices) {
            soundIds[i] = soundPool.load(activity, soundResources[i], 1)
        }
    }

    fun playSound(index: Int) {
        soundPool.play(soundIds[index], 1.0f, 1.0f, 1, 0, 1.0f)
    }
}
