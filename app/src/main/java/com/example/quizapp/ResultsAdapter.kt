package com.example.quizapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.databinding.ItemResultBinding

class ResultsAdapter(
    private val questionList: List<QuestionModel>,
    private val userAnswers: List<String>,
    private val correctAnswers: List<String>
) : RecyclerView.Adapter<ResultsAdapter.ResultViewHolder>() {

    class ResultViewHolder(val binding: ItemResultBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val binding = ItemResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val question = questionList.getOrNull(position)
        val userAnswer = userAnswers.getOrNull(position) ?: "No Answer"
        val correctAnswer = correctAnswers.getOrNull(position) ?: "Unknown"

        holder.binding.apply {
            questionText.text = question?.question ?: "Unknown Question"
            userAnswerText.text = "Your Answer: $userAnswer"
            correctAnswerText.text = "Correct Answer: $correctAnswer"

            userAnswerText.setTextColor(if (userAnswer == correctAnswer) Color.GREEN else Color.RED)
        }
    }

    override fun getItemCount(): Int = questionList.size
}

