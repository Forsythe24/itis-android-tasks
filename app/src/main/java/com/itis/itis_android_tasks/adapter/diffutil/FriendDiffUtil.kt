package com.itis.itis_android_tasks.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.itis.itis_android_tasks.model.Friend

class FriendDiffUtil(
    private val oldItemsList: List<Any>,
    private val newItemsList: List<Any>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldItemsList.size

    override fun getNewListSize(): Int = newItemsList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItemsList[oldItemPosition]
        val newItem = newItemsList[newItemPosition]
        return if (oldItem is Friend && newItem is Friend) {
            oldItem.id == newItem.id
        } else {
            false
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItemsList[oldItemPosition]
        val newItem = newItemsList[newItemPosition]

        return if (oldItem is Friend && newItem is Friend) {
             (oldItem.name == newItem.name) &&
                     (oldItem.info == newItem.info)
        } else {
            return false
        }
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldItemsList[oldItemPosition]
        val newItem = newItemsList[newItemPosition]

        return if (oldItem is Friend && newItem is Friend && oldItem.isLiked != newItem.isLiked) {
            newItem.isLiked
        } else {
            super.getChangePayload(oldItemPosition, newItemPosition)
        }
    }
}
