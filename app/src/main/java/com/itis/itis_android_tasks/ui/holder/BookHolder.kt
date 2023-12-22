package com.itis.itis_android_tasks.ui.holder

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.itis.itis_android_tasks.databinding.BookItemBinding
import com.itis.itis_android_tasks.model.Book

class BookHolder(
    private val viewBinding: BookItemBinding,
    private val onBookClicked: ((Book) -> Unit),
    private val onBookLongClicked: (Book, Int) -> Unit
): ViewHolder(viewBinding.root){

    private var item: Book? = null

    init {
        viewBinding.root.setOnClickListener {
            item?.let(onBookClicked)
        }

        viewBinding.root.setOnLongClickListener {
            item?.let {
                onBookLongClicked(it, adapterPosition)
            }
            true
        }
    }

    fun bindItem (item: Book) {
        this.item = item
        viewBinding.titleTv.text = item.title
        viewBinding.publicationYearTv.text = item.publicationYear.toString()
        viewBinding.ratingTv.text = item.rating.toString()
    }

    //favorites shouldn't be long-clickable because they shouldn't have "delete" or "add to favorites" option-dialog
    //as they're deleted by swiping
    fun bindFavoriteItem(item: Book) {
        bindItem(item)
        viewBinding.root.isLongClickable = false
    }
}
