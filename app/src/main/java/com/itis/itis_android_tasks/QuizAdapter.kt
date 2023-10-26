package com.itis.itis_android_tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.itis.itis_android_tasks.databinding.ItemQuestionBinding

class QuizAdapter(
    val items: MutableList<OptionData>,
    private val onItemChecked: ((Int, Boolean) -> Unit)? = null,
    val questionNumber: Int,
    val finishButton: MaterialButton
) : RecyclerView.Adapter<OptionHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionHolder {
        return OptionHolder(
            viewBinding = ItemQuestionBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            onItemChecked = onItemChecked,
            questionNumber = questionNumber,
            finishButton = finishButton
        )
    }

    override fun onBindViewHolder(holder: OptionHolder, position: Int) {
        holder.bindItem(item = items[position], itemCount)
    }

    override fun getItemCount(): Int = items.count()

}
