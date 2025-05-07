package com.example.quizapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.databinding.ItemLeaderboardBinding

class LeaderboardAdapter(private val leaderboardList: List<LeaderboardModel>) :
    RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder>() {

    class LeaderboardViewHolder(val binding: ItemLeaderboardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder {
        val binding = ItemLeaderboardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LeaderboardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LeaderboardViewHolder, position: Int) {
        val leaderboardEntry = leaderboardList[position]
        holder.binding.apply {
            // Display the userId as the identifier
            userIdText.text = leaderboardEntry.userId
            scoreText.text = "Score: ${leaderboardEntry.score}"
            timeText.text = "Time: ${leaderboardEntry.time}s"
        }
    }

    override fun getItemCount(): Int = leaderboardList.size
}

