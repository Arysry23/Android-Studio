package com.example.quizapp


import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.databinding.QuizItemRecyclerRowBinding

class LocalQuizListAdapter(
    private var localQuizModelList: List<QuizModel>,
    private val listener: OnQuizItemClickListener
) : RecyclerView.Adapter<LocalQuizListAdapter.LocalMyViewHolder>() {

    interface OnQuizItemClickListener {
        fun onItemClick(position: Int)
    }

    class LocalMyViewHolder(private val binding: QuizItemRecyclerRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(model: QuizModel, listener: OnQuizItemClickListener) {
            binding.apply {
                quizTitleText.text = model.title
                quizSubtitleText.text = model.subtitle
                quizTimeText.text = model.time + " Mn"

            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocalMyViewHolder {
        val binding = QuizItemRecyclerRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LocalMyViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newLocalQuizModelList: List<QuizModel>) {
        localQuizModelList = newLocalQuizModelList
        notifyDataSetChanged()
    }



    override fun getItemCount(): Int {
        return localQuizModelList.size
    }

    override fun onBindViewHolder(holder: LocalMyViewHolder, position: Int) {
        holder.bind(localQuizModelList[position], listener)
    }

    fun getItem(position: Int): QuizModel {
        return localQuizModelList[position]
    }
}


