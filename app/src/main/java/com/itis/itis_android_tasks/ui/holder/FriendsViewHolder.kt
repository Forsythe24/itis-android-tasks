package com.itis.itis_android_tasks.ui.holder

import androidx.recyclerview.widget.RecyclerView
import com.itis.itis_android_tasks.R
import com.itis.itis_android_tasks.databinding.FriendItemBinding
import com.itis.itis_android_tasks.model.Friend

class FriendsViewHolder(
    private val viewBinding: FriendItemBinding,
    private val onFriendClicked: ((Friend) -> Unit),
    private val onLikeClicked: ((Int, Friend) -> Unit),
) : RecyclerView.ViewHolder(viewBinding.root) {

    private var item: Friend? = null

    init {
        viewBinding.root.setOnClickListener {
            this.item?.let(onFriendClicked)

        }
        viewBinding.btnIvLike.setOnClickListener {
            this.item?.let {
                val data = it.copy(isLiked = !it.isLiked)
                onLikeClicked(adapterPosition, data)
            }
        }
    }


    fun bindFriend(item: Friend) {
        this.item = item
        viewBinding.run {
            tvName.text = item.name
            item.image?.let { ivImage.setImageResource(it) }

            changeLikeBtnStatus(isChecked = item.isLiked)
        }

    }

    fun changeLikeBtnStatus(isChecked: Boolean) {
        val likeDrawable = if (isChecked) R.drawable.ic_like_red else R.drawable.ic_like_gray
        viewBinding.btnIvLike.setImageResource(likeDrawable)
    }
}
