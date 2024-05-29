package com.example.simondice

import android.graphics.Color
import android.os.Handler
import android.widget.Button

class ButtonClickListener(
    private val activity: MainActivity,
    private val soundManager: SoundManager,
    private val buttons: List<Button>
) {

    private var isSequenceRunning = false
    private var sequence = mutableListOf<Int>()
    private lateinit var buttonPlay: Button
    private var userSequence = mutableListOf<Button>()

    init {
        generateSequence()
    }

    fun setButtonClickListeners() {
        buttonPlay = activity.findViewById(R.id.buttonRepeat)
        buttonPlay.text = "Play"
        buttonPlay.isEnabled = true

        buttonPlay.setOnClickListener {
            if (!isSequenceRunning) {
                isSequenceRunning = true
                it.isEnabled = false
                userSequence.clear()
                activity.hideWinMessage()
                activity.hideLoseMessage()
                playSequence()
            }
        }

        for (button in buttons) {
            button.setOnClickListener {
                if (!isSequenceRunning && userSequence.size < sequence.size) {
                    val color = button.backgroundTintList?.defaultColor ?: Color.WHITE
                    button.setBackgroundColor(Color.parseColor("#CCCCCC"))
                    Handler().postDelayed({
                        button.setBackgroundColor(color)
                    }, 200)

                    userSequence.add(button)
                    soundManager.playSound(buttons.indexOf(button))

                    if (userSequence.size == sequence.size) {
                        if (checkUserSequence()) {
                            // Si el usuario adivina correctamente, añadir un botón aleatorio a la secuencia
                            addNewButtonToSequence()
                            activity.runOnUiThread { activity.showWinMessage() }
                        } else {
                            // Si el usuario adivina incorrectamente, reiniciar la secuencia
                            sequence.clear()
                            generateSequence()
                            activity.runOnUiThread { activity.showLoseMessage() }
                        }
                    }
                }
            }
        }
    }

    private fun playSequence() {
        var index = 0
        val handler = Handler()
        val runnable = object : Runnable {
            override fun run() {
                if (index < sequence.size) {
                    val button = buttons[sequence[index]]
                    val color = button.backgroundTintList?.defaultColor ?: Color.WHITE
                    button.setBackgroundColor(Color.parseColor("#CCCCCC"))
                    soundManager.playSound(sequence[index])
                    handler.postDelayed({
                        button.setBackgroundColor(color)
                        index++
                        handler.postDelayed(this, 1000)
                    }, 1000)
                } else {
                    isSequenceRunning = false
                    buttonPlay.isEnabled = true
                }
            }
        }
        handler.post(runnable)
    }

    private fun checkUserSequence(): Boolean {
        if (userSequence.size != sequence.size) return false
        for (i in sequence.indices) {
            if (buttons.indexOf(userSequence[i]) != sequence[i]) return false
        }
        return true
    }

    private fun generateSequence() {
        sequence.clear()
        sequence.add((0 until buttons.size).random())
    }

    private fun addNewButtonToSequence() {
        sequence.add((0 until buttons.size).random())
    }
}
