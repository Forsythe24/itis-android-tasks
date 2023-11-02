package com.itis.androidsummerpractice

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.itis.itis_android_tasks.R
import com.itis.itis_android_tasks.databinding.FragmentFriendInfoBinding
import com.itis.itis_android_tasks.model.Friend
import com.itis.itis_android_tasks.utils.FriendsRepository
import com.itis.itis_android_tasks.utils.ParamsKey

class FriendInfoFragment: Fragment(R.layout.fragment_friend_info) {
    private val viewBinding: FragmentFriendInfoBinding by viewBinding(FragmentFriendInfoBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val friend: Friend = findFriendById(arguments?.getInt(ParamsKey.FRIEND_ID_KEY))!!
        extractValues(friend)

    }

    private fun findFriendById(id: Int?) : Friend? {
        FriendsRepository.getList().forEach{
            if (it.id == id) {
                return it
            }
        }
        return null
    }

    private fun extractValues(friend: Friend) {
        with (viewBinding) {
            tvName.text = friend.name
            tvInfo.text = friend.info
            ivImage.setImageResource(friend.image!!);
        }
    }

    companion object {
        fun newInstance(id: Int) = FriendInfoFragment().apply {
            arguments = bundleOf(ParamsKey.FRIEND_ID_KEY to id)
        }
    }
}
