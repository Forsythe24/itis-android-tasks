package com.itis.itis_android_tasks.ui.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.itis.itis_android_tasks.model.Book

class BooksDiffUtil(
    private val oldItemsList: List<Book>,
    private val newItemsList: List<Book>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldItemsList.size

    override fun getNewListSize(): Int = newItemsList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItemsList[oldItemPosition]
        val newItem = newItemsList[newItemPosition]
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItemsList[oldItemPosition]
        val newItem = newItemsList[newItemPosition]

        return (oldItem.title == newItem.title)
                && (oldItem.description == newItem.description)
                && (oldItem.publicationYear == newItem.publicationYear)
                && (oldItem.rating == oldItem.rating)

    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldItemsList[oldItemPosition]
        val newItem = newItemsList[newItemPosition]

        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}
