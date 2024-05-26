package com.example.quizapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.quizapp.databinding.ActivityQuizBinding
import com.example.quizapp.databinding.ScoreDialogueBinding

class QuizActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        var questionModelList: List<QuestionModel> = listOf()
        var time: String = ""
        var totalLifetimeQuizAttempts = 0
        var totalLifetimeCorrectAnswers = 0
        var totalLifetimeIncorrectAnswers = 0
        private val userAnswers = mutableListOf<String>()
        private val correctAnswers = mutableListOf<String>()
    }

    private lateinit var binding: ActivityQuizBinding
    private var currentQuestionIndex = 0
    private var selectedAnswer = ""
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btn0.setOnClickListener(this@QuizActivity)
            btn1.setOnClickListener(this@QuizActivity)
            btn2.setOnClickListener(this@QuizActivity)
            btn3.setOnClickListener(this@QuizActivity)
            nextBtn.setOnClickListener(this@QuizActivity)
        }

        loadQuestion()
        startTimer()

        totalLifetimeQuizAttempts++
    }

    private fun startTimer() {
        val totalTimeInMillis = time.toInt() * 60 * 1000L
        object : CountDownTimer(totalTimeInMillis, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                val minutes = seconds / 60
                val remainingSeconds = seconds % 60
                binding.timerIndicatorTextview.text =
                    String.format("%02d:%02d", minutes, remainingSeconds)
            }

            override fun onFinish() {
                finishQuiz()
            }
        }.start()
    }

    @SuppressLint("SetTextI18n")
    private fun loadQuestion() {
        selectedAnswer = ""

        if (currentQuestionIndex == questionModelList.size) {
            finishQuiz()
            return
        }

        val currentQuestion = questionModelList.getOrNull(currentQuestionIndex)

        if (currentQuestion != null) {
            binding.apply {
                questionIndicatorTextview.text =
                    "Question ${currentQuestionIndex + 1}/ ${questionModelList.size}"
                questionProcessIndicator.progress =
                    (currentQuestionIndex.toFloat() / questionModelList.size.toFloat() * 100).toInt()
                questionTextView.text = currentQuestion.question
                btn0.text = currentQuestion.options["option0"]
                btn1.text = currentQuestion.options["option1"]
                btn2.text = currentQuestion.options["option2"]
                btn3.text = currentQuestion.options["option3"]
            }
        } else {
            Toast.makeText(this, "Error: No question found", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onClick(view: View?) {
        binding.apply {
            btn0.setBackgroundColor(getColor(R.color.white))
            btn1.setBackgroundColor(getColor(R.color.white))
            btn2.setBackgroundColor(getColor(R.color.white))
            btn3.setBackgroundColor(getColor(R.color.white))
        }

        val clickedButton = view as Button
        if (clickedButton.id == R.id.next_btn) {
            if (selectedAnswer.isEmpty()) {
                userAnswers.add(selectedAnswer)
                Toast.makeText(
                    applicationContext,
                    "Please Select an Answer to continue :)",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            if (selectedAnswer == questionModelList[currentQuestionIndex].correct) {
                score++
                totalLifetimeCorrectAnswers++
            } else {
                totalLifetimeIncorrectAnswers++
            }

            currentQuestionIndex++
            loadQuestion()

            if (currentQuestionIndex == questionModelList.size) {
                finishQuiz()
            }
        } else {
            clickedButton.setBackgroundColor(getColor(R.color.aqua))
            selectedAnswer = clickedButton.text.toString()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun finishQuiz() {
        val totalQuestions = questionModelList.size
        val percentage = ((score.toFloat() / totalQuestions.toFloat()) * 100).toInt()

        val dialogueBinding = ScoreDialogueBinding.inflate(layoutInflater)
        dialogueBinding.apply {
            scoreProgressIndicator.progress = percentage
            scoreProgressText.text = "$percentage %"
            if (percentage > 60) {
                scoreTitle.text = "Congrats you have Passed!"
                scoreTitle.setTextColor(Color.BLUE)
            } else {
                scoreTitle.text = "You need to work hard!!"
                scoreTitle.setTextColor(Color.RED)
            }
            scoreSubtitle.text = "$score out of $totalQuestions are correct"
            finishBtn.setOnClickListener {
                val intent = Intent(this@QuizActivity, MainActivity::class.java)
                intent.putExtra("totalQuizAttempts", totalLifetimeQuizAttempts)
                intent.putExtra("totalIncorrectAnswers", totalLifetimeIncorrectAnswers)
                startActivity(intent)
                finish()
            }
            showResults.setOnClickListener {
                val intent = Intent(this@QuizActivity, ResultsActivity::class.java)
                intent.putStringArrayListExtra("userAnswers", ArrayList(userAnswers))
                intent.putStringArrayListExtra("correctAnswers", ArrayList(correctAnswers))
                startActivity(intent)
            }
        }

        AlertDialog.Builder(this)
            .setView(dialogueBinding.root)
            .setCancelable(false)
            .show()
    }
}

