package com.example.quizapp

data class LeaderboardEntry(
    val userName: String = "",
    val score: Int = 0,
    val time: Long = 0L,  // Time is now stored as a Long
    val userId: String = ""
)

