package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizapp.databinding.ActivityMainBinding
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var quizModelList: MutableList<QuizModel>
    private lateinit var adapter: QuizListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        quizModelList = mutableListOf()
        getDataFromFirebase()

        binding.addQuizButton.setOnClickListener {
            val intent = Intent(this@MainActivity, AddQuizActivity::class.java)
            startActivity(intent)
        }

        val isAdmin = intent.getBooleanExtra("isAdmin", false)
        if (isAdmin) {
            binding.addQuizButton.visibility = View.VISIBLE
        } else {
            binding.addQuizButton.visibility = View.GONE
        }

        // Set click listener for the My Profile button
        binding.myProfileButton.setOnClickListener {
            // Navigate to the com.example.quizapp.ProfileActivity
            val intent = Intent(this@MainActivity, ProfileActivity::class.java)
            startActivity(intent)
        }

    }

    private fun setupRecyclerView() {
        binding.progressBar.visibility = View.GONE
        adapter = QuizListAdapter(quizModelList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun getDataFromFirebase() {
        binding.progressBar.visibility = View.VISIBLE
        FirebaseDatabase.getInstance().reference.child("quizzes")
            .get()
            .addOnSuccessListener { dataSnapshot ->
                if (dataSnapshot.exists()) {
                    for (snapshot in dataSnapshot.children) {
                        val quizModel = snapshot.getValue(QuizModel::class.java)
                        quizModel?.let {
                            quizModelList.add(it)
                        }
                    }
                }
                setupRecyclerView()
            }

    }
}



