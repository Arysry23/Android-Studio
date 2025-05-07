package com.example.quizapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizapp.databinding.ActivityLeaderboardBinding

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class LeaderboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLeaderboardBinding
    private val leaderboardList: MutableList<LeaderboardModel> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeaderboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // Get quizId from Intent
        val quizId = intent.getStringExtra("quizId")
        if (quizId != null) {
            // Fetch leaderboard data from Firebase
            fetchLeaderboardData(quizId)
        } else {
            // Handle missing quizId error
            Toast.makeText(this, "Quiz ID is missing!", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Set up back button to exit com.example.quizapp.LeaderboardActivity
        binding.backButton.setOnClickListener {
            finish()  // Handle back arrow clicks
        }
    }

    private fun fetchLeaderboardData(quizId: String) {
        binding.progressBar.visibility = View.VISIBLE

        // Firebase reference to get leaderboard for the specified quizId
        FirebaseDatabase.getInstance().reference
            .child("quizzes")  // Root node for quizzes
            .child(quizId)  // Access the specific quiz by its ID
            .child("leaderboard")  // Access leaderboard data under each quiz
            .orderByChild("score")  // Optional: You can order the leaderboard by score if needed
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    leaderboardList.clear()  // Clear the existing list before adding new data

                    if (dataSnapshot.exists()) {
                        for (snapshot in dataSnapshot.children) {
                            val leaderboardModel = snapshot.getValue(LeaderboardModel::class.java)
                            leaderboardModel?.let {
                                leaderboardList.add(it)  // Add each leaderboard entry to the list
                            }
                        }

                        // Sort the leaderboard list first by score (descending) and then by time (ascending)
                        leaderboardList.sortWith(compareByDescending<LeaderboardModel> { it.score }
                            .thenBy { it.time })

                    }

                    // Hide the progress bar after data is fetched
                    binding.progressBar.visibility = View.GONE

                    // Setup RecyclerView to display leaderboard data
                    setupLeaderboardRecyclerView()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle Firebase read failure
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this@LeaderboardActivity, "Failed to load leaderboard", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun setupLeaderboardRecyclerView() {
        // Set up RecyclerView Adapter to display the leaderboard
        val adapter = LeaderboardAdapter(leaderboardList)
        binding.recyclerView.adapter = adapter
    }
}





