package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AgeSelectionActivity : AppCompatActivity() {
    private lateinit var ageGroupRadioGroup: RadioGroup
    private lateinit var continueButton: Button
    private lateinit var auth: FirebaseAuth
    private val database = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_age_selection)

        ageGroupRadioGroup = findViewById(R.id.age_group_radio_group)
        continueButton = findViewById(R.id.continue_button)
        auth = FirebaseAuth.getInstance()

        continueButton.setOnClickListener {
            val selectedId = ageGroupRadioGroup.checkedRadioButtonId
            if (selectedId != -1) {
                val selectedRadioButton = findViewById<RadioButton>(selectedId)
                val selectedAgeGroup = selectedRadioButton.text.toString()

                saveAgeGroupToFirebase(selectedAgeGroup)
            } else {
                Toast.makeText(this, "Please select an age group", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveAgeGroupToFirebase(ageGroup: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            database.child("users").child(userId).child("ageGroup").setValue(ageGroup)
                .addOnSuccessListener {
                    // Navigate to com.example.quizapp.MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to save age group", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
