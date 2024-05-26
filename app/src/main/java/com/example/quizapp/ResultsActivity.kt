package com.example.quizapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ResultsActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        val resultTitle = findViewById<TextView>(R.id.resultTitle)
        val correctAnswersText = findViewById<TextView>(R.id.correctAnswersText)
        val incorrectAnswersText = findViewById<TextView>(R.id.incorrectAnswersText)
        val backToQuizButton = findViewById<Button>(R.id.backToQuizButton)

        val userAnswers = intent.getStringArrayListExtra("userAnswers")
        val correctAnswers = intent.getStringArrayListExtra("correctAnswers")

        val totalQuestions = correctAnswers?.size ?: 0
        var correctCount = 0
        var incorrectCount = 0

        // Fetch quiz data from Firebase
        val quizId = intent.getStringExtra("quizId") // Get the quiz ID passed from QuizActivity
        if (quizId != null) {
            val databaseReference = FirebaseDatabase.getInstance().reference.child("quizzes").child(quizId)
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val questions = dataSnapshot.child("questionList").children.map { it.getValue(QuestionModel::class.java) }

                    // Iterate over each question to check user's answers
                    questions.forEachIndexed { index, question ->
                        val userAnswer = userAnswers?.getOrNull(index)
                        val correctAnswer = correctAnswers?.getOrNull(index)

                        if (userAnswer == correctAnswer) {
                            correctCount++
                        } else {
                            incorrectCount++
                        }

                        // Display question, user's answer, and correct answer
                        // You can append this information to a TextView or ListView
                        // For simplicity, let's just print it to Logcat
                        val resultString = "Question: ${question}\n" +
                                "Your Answer: $userAnswer\n" +
                                "Correct Answer: $correctAnswer"
                        println(resultString)
                    }

                    // Display total correct and incorrect answers
                    resultTitle.text = "Quiz Results"
                    correctAnswersText.text = "Correct Answers: $correctCount"
                    incorrectAnswersText.text = "Incorrect Answers: $incorrectCount"
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle errors
                }
            })
        }

        backToQuizButton.setOnClickListener {
            finish()
        }
    }
}
