package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizapp.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private var totalQuizAttempts = 0 // Initialize with 0
    private var totalCorrectAnswers = 0 // Initialize with 0
    private var totalIncorrectAnswers = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve total quiz attempts and correct answers from the intent
        totalQuizAttempts = intent.getIntExtra("totalQuizAttempts", 0)
        totalIncorrectAnswers = intent.getIntExtra("totalIncorrectAnswers", 0)

        totalCorrectAnswers = QuizActivity.totalLifetimeCorrectAnswers
        totalIncorrectAnswers = QuizActivity.totalLifetimeIncorrectAnswers
        totalQuizAttempts =  QuizActivity.totalLifetimeQuizAttempts

        // Load total quiz attempts and correct answers in the UI
        loadProfileData()

        // Set click listener for Back to Home button
        binding.BackToHome.setOnClickListener {
            finish() // Finish the ProfileActivity
        }
    }

    private fun loadProfileData() {
        binding.apply {
            totalAttemptsTextView.text = "Total Quiz Attempts: $totalQuizAttempts"
            totalCorrectTextView.text = "Total Correct Answers: $totalCorrectAnswers"
            incorrectanswersTextView.text = "Total Incorrect Answers: $totalIncorrectAnswers "
        }
    }
}
