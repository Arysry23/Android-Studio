package com.example.quizapp

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.databinding.QuizItemRecyclerRowBinding

class QuizListAdapter(private var quizModelList: List<QuizModel>) :
    RecyclerView.Adapter<QuizListAdapter.MyViewHolder>() {

    class MyViewHolder(private val binding: QuizItemRecyclerRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: QuizModel) {
            binding.apply {
                quizTitleText.text = model.title
                quizSubtitleText.text = model.subtitle
                quizTimeText.text = model.time + " Min"
                root.setOnClickListener {
                    val intent = Intent(root.context, QuizActivity::class.java)
                    // Convert the map values to a list
                    QuizActivity.questionModelList = model.questionList.values.toList()
                    QuizActivity.time = model.time
                    root.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = QuizItemRecyclerRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newQuizModelList: List<QuizModel>) {
        quizModelList = newQuizModelList
        notifyDataSetChanged()
    }

    fun addQuiz(quiz: QuizModel) {
        val updatedList = quizModelList.toMutableList()
        updatedList.add(quiz)
        setData(updatedList)
    }

    override fun getItemCount(): Int {
        return quizModelList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(quizModelList[position])
    }
}

