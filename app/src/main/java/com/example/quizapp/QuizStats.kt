package com.example.quizapp

data class QuizStats(
    val quizzesAttempted: Int = 0,
    val questionsAttempted: Int = 0,
    val correct: Int = 0,
    val incorrect: Int = 0,
    val correctPercent: Int = 0
)
