package com.example.quizapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView

class SplashScreenActivity : Activity() {
    private lateinit var quoteTextView: TextView
    private val handler = Handler(Looper.getMainLooper())

    private val quotes = arrayOf(
        "Knowledge is power.",
        "Learning never exhausts the mind.",
        "The only way to do great work is to love what you do.",
        "Success is not final, failure is not fatal: it is the courage to continue that counts.",
        "The beautiful thing about learning is that no one can take it away from you.",
        "An eye for an eye makes the whole world blind."
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val magnifyingGlass = findViewById<ImageView>(R.id.magnifyingGlass)
        quoteTextView = findViewById(R.id.quoteTextView)

        magnifyingGlass.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate))
        updateQuote()

        handler.postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, SPLASH_TIME_OUT)
    }

    private fun updateQuote() {
        val fadeIn = AlphaAnimation(0.0f, 1.0f).apply { duration = 1000 }

        var index = 0
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (index >= quotes.size) index = 0
                quoteTextView.text = quotes[index++]
                quoteTextView.startAnimation(fadeIn)
                handler.postDelayed(this, 2000)
            }
        }, 2000)
    }

    companion object {
        private const val SPLASH_TIME_OUT = 4000L
    }
}

