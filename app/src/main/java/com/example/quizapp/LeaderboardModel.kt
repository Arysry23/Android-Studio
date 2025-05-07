package com.example.quizapp

data class LeaderboardModel(
    val score: Int = 0,
    val time: Int = 0,
    val userId: String = "" // Store user ID as unique identifier
)

