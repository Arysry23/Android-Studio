package com.example.quizapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.databinding.ItemWeeklyTriviaLeaderboardBinding

class WeeklyTriviaLeaderboardAdapter(
    private val leaderboardList: List<LeaderboardEntry>
) : RecyclerView.Adapter<WeeklyTriviaLeaderboardAdapter.LeaderboardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder {
        val binding = ItemWeeklyTriviaLeaderboardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LeaderboardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LeaderboardViewHolder, position: Int) {
        val entry = leaderboardList[position]
        holder.bind(entry, position + 1)
    }

    override fun getItemCount(): Int = leaderboardList.size

    inner class LeaderboardViewHolder(
        private val binding: ItemWeeklyTriviaLeaderboardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(entry: LeaderboardEntry, position: Int) {
            binding.apply {
                // Position
                this.position.text = position.toString()

                // User info
                userName.text = entry.userId
                userTime.text = formatTime(entry.time)

                // Score
                userScore.text = entry.score.toString()

                // Position color based on rank
                when (position) {
                    1 -> this.position.backgroundTintList = ContextCompat.getColorStateList(root.context, R.color.gold)
                    2 -> this.position.backgroundTintList = ContextCompat.getColorStateList(root.context, R.color.silver)
                    3 -> this.position.backgroundTintList = ContextCompat.getColorStateList(root.context, R.color.bronze)
                    else -> this.position.backgroundTintList = ContextCompat.getColorStateList(root.context, R.color.light_gray)
                }
            }
        }

        private fun formatTime(seconds: Long): String {
            val minutes = seconds / 60
            val remainingSeconds = seconds % 60
            return String.format("Time: %02d:%02d", minutes, remainingSeconds)
        }
    }
}
