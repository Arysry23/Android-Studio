package com.example.quizapp


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizapp.databinding.ActivityAddQuizBinding
import com.google.firebase.database.FirebaseDatabase

class AddQuizActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddQuizBinding
    private lateinit var btnSaveQuiz: Button
    private lateinit var btnAddQuestion: Button
    private var quizId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnSaveQuiz = findViewById(R.id.btnSaveQuiz)
        btnSaveQuiz.setOnClickListener {
            saveQuizToDatabase()
        }

        btnAddQuestion = findViewById(R.id.btnAddQuestion)
        btnAddQuestion.setOnClickListener {
            val intent = Intent(this@AddQuizActivity, AddQuestionActivity::class.java)
            intent.putExtra("QUIZ_ID", quizId) // Pass the unique quiz ID
            startActivity(intent)
        }
    }

    private fun saveQuizToDatabase() {
        val title = binding.etQuizTitle.text.toString().trim()
        val subtitle = binding.etQuizSubtitle.text.toString().trim()
        val time = binding.etQuizTime.text.toString().trim()
        val id = binding.etQuizId.text.toString().trim() // New field for unique quiz ID

        if (title.isEmpty() || subtitle.isEmpty() || time.isEmpty() || id.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val quiz = QuizModel(
            id = id,
            title = title,
            subtitle = subtitle,
            time = time,
            questionList = emptyMap() // Initially empty, questions will be added later
        )

        val database = FirebaseDatabase.getInstance()
        val quizRef = database.getReference("quizzes").child(id)
        quizRef.setValue(quiz)
            .addOnSuccessListener {
                Toast.makeText(this, "Quiz saved successfully", Toast.LENGTH_SHORT).show()
                quizId = id
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save quiz", Toast.LENGTH_SHORT).show()
            }
    }
}








