package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var adminButton: Button
    private lateinit var showPasswordCheckBox: CheckBox
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        usernameEditText = findViewById(R.id.email_edit_text)
        passwordEditText = findViewById(R.id.password_edit_text)
        loginButton = findViewById(R.id.login_button)
        registerButton = findViewById(R.id.register_button)
        adminButton = findViewById(R.id.adminbtn)
        showPasswordCheckBox = findViewById(R.id.show_password_checkbox)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            loginUser(username, password)
        }

        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            registerUser(username, password)
        }

        adminButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            loginAsAdmin(username, password)
        }

        showPasswordCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                passwordEditText.transformationMethod = null
            } else {
                passwordEditText.transformationMethod = android.text.method.PasswordTransformationMethod.getInstance()
            }
        }
    }

    private fun loginUser(username: String, password: String) {
        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    navigateToMainActivity()
                } else {
                    Toast.makeText(this@LoginActivity, "Login failed. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun registerUser(username: String, password: String) {
        auth.createUserWithEmailAndPassword(username, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    navigateToMainActivity()
                } else {
                    Toast.makeText(this@LoginActivity, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun loginAsAdmin(username: String, password: String) {
        if (username == "admin123@gmail.com" && password == "admin123") {
            navigateToMainActivity(true)
        } else {
            Toast.makeText(this@LoginActivity, "Admin login failed. Please check credentials.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToMainActivity(isAdmin: Boolean = false) {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.putExtra("isAdmin", isAdmin)
        startActivity(intent)
        finish()
    }
}

