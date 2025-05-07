package com.example.quizapp

data class WeeklyTriviaQuestionModel(
    var id: String = "",
    val question: String = "",
    val options: Map<String, String> = emptyMap(),
    val correctAnswer: String = "",
    val imageUrl: String = ""
)
