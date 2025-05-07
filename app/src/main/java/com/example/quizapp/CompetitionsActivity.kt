package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.quizapp.databinding.ActivityCompetitionsBinding

class CompetitionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCompetitionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompetitionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupCardAnimations()
    }

    private fun setupCardAnimations() {
        val enterAnim = AnimationUtils.loadAnimation(this, R.anim.card_enter)
        val clickAnim = AnimationUtils.loadAnimation(this, R.anim.card_click)

        // Staggered entrance animation
        Handler(Looper.getMainLooper()).postDelayed({
            binding.startTriviaCard.startAnimation(enterAnim)
        }, 200)

        Handler(Looper.getMainLooper()).postDelayed({
            binding.leaderboardCard.startAnimation(enterAnim)
        }, 400)

        Handler(Looper.getMainLooper()).postDelayed({
            binding.prizeCard.startAnimation(enterAnim)
        }, 600)

        // Click listeners with animations
        binding.startTriviaCard.setOnClickListener {
            it.startAnimation(clickAnim)
            clickAnim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationRepeat(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {
                    startActivity(Intent(this@CompetitionsActivity, WeeklyTriviaActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
            })
        }

        binding.leaderboardCard.setOnClickListener {
            it.startAnimation(clickAnim)
            clickAnim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationRepeat(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {
                    startActivity(Intent(this@CompetitionsActivity, WeeklyTriviaLeaderboardActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
            })
        }

        binding.prizeCard.setOnClickListener {
            it.startAnimation(clickAnim)
            clickAnim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationRepeat(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {
                    showPrizeInfo()
                }
            })
        }
    }

    private fun showPrizeInfo() {
        // Implement your prize info display here
        AlertDialog.Builder(this)
            .setTitle("Prize Information")
            .setMessage("Win amazing prizes every week!\nTop 3 players get:\n1. \$100 gift card\n2. \$50 gift card\n3. \$25 gift card")
            .setPositiveButton("Got it") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
