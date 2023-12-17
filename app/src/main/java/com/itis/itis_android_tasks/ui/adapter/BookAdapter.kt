package com.itis.itis_android_tasks.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.itis.itis_android_tasks.databinding.BookItemBinding
import com.itis.itis_android_tasks.model.Book
import com.itis.itis_android_tasks.ui.adapter.diffutil.BooksDiffUtil
import com.itis.itis_android_tasks.ui.holder.BookHolder

class BookAdapter(
    private val onBookClicked: (Book) -> Unit,
    private val onBookLongClicked: (Book, Int) -> Unit
) : RecyclerView.Adapter<BookHolder>() {

    private var itemsList = mutableListOf<Book>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookHolder {
        return BookHolder(
            BookItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false),
            onBookClicked,
            onBookLongClicked
        )
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    override fun onBindViewHolder(holder: BookHolder, position: Int) {
        holder.bindItem(itemsList[position])
    }

    override fun onBindViewHolder(
        holder: BookHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (itemsList[position].isFavorite) {
            holder.bindFavoriteItem(itemsList[position])
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<Book>) {
        val diff = BooksDiffUtil(oldItemsList = itemsList, newItemsList = list)
        val diffResult = DiffUtil.calculateDiff(diff)
        itemsList.clear()
        itemsList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

}
