package com.example.quizapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class AddQuestionActivity : AppCompatActivity() {
    private lateinit var etQuestion: EditText
    private lateinit var etOption1: EditText
    private lateinit var etOption2: EditText
    private lateinit var etOption3: EditText
    private lateinit var etOption4: EditText
    private lateinit var etCorrectOption: EditText
    private lateinit var btnSaveQuestion: Button

    private var quizId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_question)

        etQuestion = findViewById(R.id.etQuestion)
        etOption1 = findViewById(R.id.etOption1)
        etOption2 = findViewById(R.id.etOption2)
        etOption3 = findViewById(R.id.etOption3)
        etOption4 = findViewById(R.id.etOption4)
        etCorrectOption = findViewById(R.id.etCorrectOption)
        btnSaveQuestion = findViewById(R.id.btnSaveQuestion)

        // Parse the quiz ID from the intent extras
        quizId = intent.getStringExtra("QUIZ_ID")

        btnSaveQuestion.setOnClickListener {
            saveQuestionToDatabase()
        }
    }

    private fun saveQuestionToDatabase() {
        // Retrieve question details from EditText fields
        val question = etQuestion.text.toString().trim()
        val option1 = etOption1.text.toString().trim()
        val option2 = etOption2.text.toString().trim()
        val option3 = etOption3.text.toString().trim()
        val option4 = etOption4.text.toString().trim()
        val correctOption = etCorrectOption.text.toString().trim()

        // Validate question details
        if (question.isEmpty() || option1.isEmpty() || option2.isEmpty() ||
            option3.isEmpty() || option4.isEmpty() || correctOption.isEmpty() || quizId.isNullOrEmpty()
        ) {
            // Display error message or toast and return
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a HashMap for options
        val optionsMap = hashMapOf(
            "option1" to option1,
            "option2" to option2,
            "option3" to option3,
            "option4" to option4
        )

        // Create a QuestionModel object
        val questionModel = QuestionModel(
            question = question,
            options = optionsMap, // Use the HashMap for options
            correct = correctOption
        )

        // Save the question to the Firebase database under the quiz ID
        val database = FirebaseDatabase.getInstance()
        val questionRef =
            database.getReference("quizzes/${quizId}/questionList").push() // Updated reference
        questionModel.id = questionRef.key ?: ""
        questionRef.setValue(questionModel)
            .addOnSuccessListener {
                // Question saved successfully
                // Display success message or toast
                Toast.makeText(this, "Question saved successfully", Toast.LENGTH_SHORT).show()
                finish() // Finish activity after saving question
            }
            .addOnFailureListener {
                // Error saving question
                // Display error message or toast
                Toast.makeText(this, "Failed to save question", Toast.LENGTH_SHORT).show()
            }
    }
}










