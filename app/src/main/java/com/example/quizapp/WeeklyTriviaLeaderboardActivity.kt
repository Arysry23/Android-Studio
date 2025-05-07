package com.example.quizapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizapp.databinding.ActivityLeaderboardBinding
import com.google.firebase.database.*

class WeeklyTriviaLeaderboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLeaderboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeaderboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup RecyclerView
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@WeeklyTriviaLeaderboardActivity)
            addItemDecoration(DividerItemDecoration(this@WeeklyTriviaLeaderboardActivity, DividerItemDecoration.VERTICAL))
        }

        // Fetch data with improved query
        fetchLeaderboardData()

        // Back button
        binding.backButton.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    private fun fetchLeaderboardData() {
        val databaseRef = FirebaseDatabase.getInstance().reference
            .child("weeklyTrivia")
            .child("leaderboard")

        databaseRef.orderByChild("score").limitToLast(50) // Get more entries for local sorting
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val tempList = mutableListOf<LeaderboardEntry>()
                    for (data in snapshot.children) {
                        data.getValue(LeaderboardEntry::class.java)?.let { tempList.add(it) }
                    }

                    // Sort and take top 10
                    val sortedList = tempList.sortedByDescending { it.score }.take(10)
                    binding.recyclerView.adapter = WeeklyTriviaLeaderboardAdapter(sortedList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@WeeklyTriviaLeaderboardActivity,
                        "Failed to load leaderboard: ${error.message}",
                        Toast.LENGTH_SHORT).show()
                }
            })
    }
}

