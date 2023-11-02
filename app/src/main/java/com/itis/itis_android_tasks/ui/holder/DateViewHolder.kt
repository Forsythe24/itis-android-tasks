package com.itis.itis_android_tasks.ui.holder

import androidx.recyclerview.widget.RecyclerView
import com.itis.itis_android_tasks.databinding.DateItemBinding
import com.itis.itis_android_tasks.model.Date

class DateViewHolder(
    private val viewBinding: DateItemBinding
) : RecyclerView.ViewHolder(viewBinding.root) {

    fun bindDate(item: Date) {
        viewBinding.tvDate.text = item.date
    }
}
