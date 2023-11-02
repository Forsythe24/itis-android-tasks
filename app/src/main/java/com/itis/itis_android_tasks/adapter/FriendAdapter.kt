package com.itis.itis_android_tasks.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.itis.itis_android_tasks.adapter.diffutil.FriendDiffUtil
import com.itis.itis_android_tasks.databinding.DateItemBinding
import com.itis.itis_android_tasks.databinding.FriendItemBinding
import com.itis.itis_android_tasks.model.Date
import com.itis.itis_android_tasks.model.Friend
import com.itis.itis_android_tasks.ui.holder.DateViewHolder
import com.itis.itis_android_tasks.ui.holder.FriendsViewHolder
import com.itis.itis_android_tasks.utils.ItemTypes
import java.lang.IllegalArgumentException

class FriendAdapter(
    private val onFriendClicked: ((Friend) -> Unit),
    private val onLikeClicked: ((Int, Friend) -> Unit),
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var itemsList = mutableListOf<Any>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ItemTypes.FRIEND.number -> {
                FriendsViewHolder(
                    viewBinding = FriendItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    onFriendClicked = onFriendClicked,
                    onLikeClicked = onLikeClicked,
                )
            }


            ItemTypes.DATE.number -> {
                DateViewHolder(
                    viewBinding = DateItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }


            else -> throw IllegalArgumentException("Invalid ViewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FriendsViewHolder -> holder.bindFriend(item = itemsList[position] as Friend)

            is DateViewHolder -> holder.bindDate(item = itemsList[position] as Date)

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            (payloads.first() as? Boolean)?.let {
                (holder as? FriendsViewHolder)?.changeLikeBtnStatus(it)
            }
        }
        super.onBindViewHolder(holder, position, payloads)
    }

    override fun getItemCount(): Int = itemsList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<Any>) {
        val diff = FriendDiffUtil(oldItemsList = itemsList, newItemsList = list)
        val diffResult = DiffUtil.calculateDiff(diff)
        itemsList.clear()
        itemsList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateItem(position: Int, item: Friend) {
        this.itemsList[position] = item
        notifyItemChanged(position, item.isLiked)
    }

    override fun getItemViewType(position: Int): Int {
        return when(itemsList[position]) {
            is Friend -> ItemTypes.FRIEND.number
            is Date -> ItemTypes.DATE.number

            else -> throw IllegalArgumentException("Invalid Item")
        }
    }

}
