package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.quizapp.databinding.ActivityWeeklyTriviaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class WeeklyTriviaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeeklyTriviaBinding
    private var questionList: MutableList<WeeklyTriviaQuestionModel> = mutableListOf()
    private var userAnswers: MutableList<String> = mutableListOf()
    private var score = 0
    private var currentQuestionIndex = 0
    private var countDownTimer: CountDownTimer? = null
    private var timeLeftInMillis: Long = 0
    private val questionTimeLimit: Long = 30000  // Each question gets 30 seconds
    private var startTime: Long = 0  // Store the start time of the quiz

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeeklyTriviaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Record the start time when the activity is created
        startTime = System.currentTimeMillis()

        fetchRandomTriviaQuestions()

        binding.nextBtn.setOnClickListener {
            if (userAnswers.size == currentQuestionIndex) {
                Toast.makeText(this, "Please select an answer!", Toast.LENGTH_SHORT).show()
            } else {
                currentQuestionIndex++
                loadQuestion()
            }
        }
    }

    private fun fetchRandomTriviaQuestions() {
        FirebaseDatabase.getInstance().reference
            .child("weeklyTrivia")
            .child("questions")
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    questionList.clear()
                    for (data in snapshot.children) {
                        val question = data.getValue(WeeklyTriviaQuestionModel::class.java)
                        question?.let { questionList.add(it) }
                    }
                    loadQuestion()
                } else {
                    Toast.makeText(this, "No trivia questions found in Firebase!", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to fetch trivia questions", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadQuestion() {
        if (currentQuestionIndex < 10 && currentQuestionIndex < questionList.size) {
            val question = questionList[currentQuestionIndex]
            binding.questionTextView.text = question.question
            binding.option1.text = question.options["option1"]
            binding.option2.text = question.options["option2"]
            binding.option3.text = question.options["option3"]
            binding.option4.text = question.options["option4"]

            // Load Image Dynamically using Glide
            val imageName = when (currentQuestionIndex) {
                0 -> "france"
                1 -> "theory"
                2 -> "solar"
                3 -> "ocean"
                4 -> "romeo"
                5 -> "river"
                6 -> "monalisa"
                7 -> "minerals"
                8 -> "jungle"
                9 -> "japan"
                else -> "hitler"
            }

            if (imageName.isNotEmpty()) {
                val resourceId = resources.getIdentifier(imageName, "drawable", packageName)
                if (resourceId != 0) {
                    binding.spaceImageView.setImageResource(resourceId)
                    binding.spaceImageView.visibility = View.VISIBLE
                } else {
                    binding.spaceImageView.visibility = View.GONE
                }
            } else {
                binding.spaceImageView.visibility = View.GONE
            }

            // Start a new timer for the current question
            startQuestionTimer()

            // Set onClick listeners to record user answers
            setOptionClickListeners(question)
        } else {
            finishTrivia()
        }
    }

    private fun startQuestionTimer() {
        timeLeftInMillis = questionTimeLimit
        binding.timerTextView.text = "30s"

        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                val seconds = (millisUntilFinished / 1000).toInt()
                binding.timerTextView.text = "$seconds s"
            }

            override fun onFinish() {
                loadNextQuestion()
            }
        }.start()
    }

    private fun setOptionClickListeners(question: WeeklyTriviaQuestionModel) {
        val options = listOf(binding.option1, binding.option2, binding.option3, binding.option4)
        val animation = AnimationUtils.loadAnimation(this, R.anim.button_press)

        options.forEachIndexed { index, button ->
            button.setOnClickListener { view ->
                // Record answer first
                val selectedOption = "option${index + 1}"
                userAnswers.add(selectedOption)

                // Apply animation
                view.startAnimation(animation)

                // Check answer after animation ends
                animation.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationEnd(animation: Animation?) {
                        checkAnswer(question, selectedOption)
                    }

                    override fun onAnimationStart(animation: Animation?) {}
                    override fun onAnimationRepeat(animation: Animation?) {}
                })
            }
        }
    }

    private fun checkAnswer(question: WeeklyTriviaQuestionModel, selectedOption: String) {
        val correctAnswer = question.correctAnswer.trim()
        val selectedAnswer = question.options[selectedOption]?.trim() ?: ""

        if (selectedAnswer == correctAnswer) {
            score++
        }
        loadNextQuestion()
    }

    private fun loadNextQuestion() {
        countDownTimer?.cancel()  // Stop timer for the current question
        if (currentQuestionIndex < questionList.size - 1) {
            currentQuestionIndex++
            loadQuestion()
        } else {
            finishTrivia()
        }
    }

    private fun finishTrivia() {
        // Calculate the time spent on the quiz
        val endTime = System.currentTimeMillis()
        val timeSpentInMillis = endTime - startTime  // Time spent in milliseconds
        val timeSpentInSeconds = timeSpentInMillis / 1000

        val intent = Intent(this, ResultsActivity::class.java)
        intent.putExtra("userAnswers", ArrayList(userAnswers))
        intent.putExtra("correctAnswers", ArrayList(questionList.map { it.correctAnswer }))
        intent.putExtra("score", score)
        intent.putExtra("timeSpent", timeSpentInSeconds)  // Pass the time spent in seconds
        startActivity(intent)
        finish()
        saveTriviaToLeaderboard(timeSpentInSeconds)
    }

    private fun saveTriviaToLeaderboard(timeSpentInSeconds: Long) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val userId = firebaseUser?.uid ?: return
        val userName = firebaseUser.displayName ?: "Unknown User"

        val databaseRef = FirebaseDatabase.getInstance().reference
            .child("weeklyTrivia")
            .child("leaderboard")

        val newLeaderboardEntry = databaseRef.push()
        val resultMap = mapOf(
            "userName" to userName,
            "score" to score,
            "time" to timeSpentInSeconds,
            "userId" to userId
        )

        newLeaderboardEntry.setValue(resultMap)
            .addOnSuccessListener {
                Toast.makeText(this, "Data saved successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save result", Toast.LENGTH_SHORT).show()
            }
    }
}



