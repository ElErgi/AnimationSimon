package com.example.simondice

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var buttonClickListener: ButtonClickListener
    private lateinit var soundManager: SoundManager
    private lateinit var buttonRepeat: Button
    private lateinit var buttonBlue: Button
    private lateinit var buttonRed: Button
    private lateinit var buttonYellow: Button
    private lateinit var buttonGreen: Button
    private lateinit var textWin: TextView
    private lateinit var textLose: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonRepeat = findViewById(R.id.buttonRepeat)
        buttonBlue = findViewById(R.id.buttonBlue)
        buttonRed = findViewById(R.id.buttonRed)
        buttonYellow = findViewById(R.id.buttonYellow)
        buttonGreen = findViewById(R.id.buttonGreen)
        textWin = findViewById(R.id.textWin)
        textLose = findViewById(R.id.textLose)

        // Inicializar el SoundManager con los recursos de sonido asociados a cada botón
        soundManager = SoundManager(this, listOf(R.raw.fa, R.raw.gb4, R.raw.la, R.raw.mi))

        // Inicializar el ButtonClickListener y configurar los listeners
        buttonClickListener = ButtonClickListener(this, soundManager, listOf(buttonBlue, buttonRed, buttonYellow, buttonGreen))
        buttonClickListener.setButtonClickListeners()
    }

    fun showWinMessage() {
        textWin.visibility = View.VISIBLE
    }

    fun hideWinMessage() {
        textWin.visibility = View.INVISIBLE
    }

    fun showLoseMessage() {
        animateLoseMessage()
    }

    fun hideLoseMessage() {
        textLose.visibility = View.INVISIBLE
    }

    private fun animateLoseMessage() {
        textLose.visibility = View.VISIBLE

        val fadeIn = ObjectAnimator.ofFloat(textLose, "alpha", 0f, 1f)
        fadeIn.duration = 500

        val scaleXUp = ObjectAnimator.ofFloat(textLose, "scaleX", 1f, 1.5f)
        val scaleYUp = ObjectAnimator.ofFloat(textLose, "scaleY", 1f, 1.5f)
        scaleXUp.repeatCount = 3
        scaleYUp.repeatCount = 3
        scaleXUp.repeatMode = ValueAnimator.REVERSE
        scaleYUp.repeatMode = ValueAnimator.REVERSE
        scaleXUp.duration = 500
        scaleYUp.duration = 500

        val fadeOut = ObjectAnimator.ofFloat(textLose, "alpha", 1f, 0f)
        fadeOut.duration = 500
        fadeOut.startDelay = 2000 // Mantener el texto visible durante 2 segundos

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(fadeIn, AnimatorSet().apply {
            playTogether(scaleXUp, scaleYUp)
        }, fadeOut)
        animatorSet.start()
    }
}
