package com.itis.itis_android_tasks.ui.framgents

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.itis.androidsummerpractice.FriendInfoFragment
import com.itis.itis_android_tasks.R
import com.itis.itis_android_tasks.adapter.FriendAdapter
import com.itis.itis_android_tasks.adapter.decorations.VerticalDecorator
import com.itis.itis_android_tasks.databinding.FragmentFriendsListBinding
import com.itis.itis_android_tasks.model.Friend
import com.itis.itis_android_tasks.utils.FriendsRepository
import com.itis.itis_android_tasks.utils.FriendsRepository.getItemsList
import com.itis.itis_android_tasks.utils.ParamsKey
import com.itis.itis_android_tasks.utils.getValueInPx

class FriendsListFragment : Fragment(R.layout.fragment_friends_list) {

    private val viewBinding: FragmentFriendsListBinding by viewBinding(FragmentFriendsListBinding::bind)

    private var friendAdapter: FriendAdapter? = null


    private var itemsList = mutableListOf<Any>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        friendAdapter = null
    }



    private fun initAdapter() {
        val numberOfFriends = requireArguments().getInt(ParamsKey.NUMBER_OF_FRIENDS_KEY)
        friendAdapter = FriendAdapter(
                onFriendClicked = ::onFriendClicked,
                onLikeClicked = ::onLikeClicked,
            )

        with(viewBinding) {
            itemsList = getItemsList(numberOfFriends)

            friendAdapter?.setItems(itemsList)

            val layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)


            if (numberOfFriends <= 12) {
                layoutManager.spanCount = 1
                rvFriendsList.layoutManager = layoutManager
                rvFriendsList.adapter = friendAdapter
            } else {
                layoutManager.spanCount = 2
                rvFriendsList.layoutManager = layoutManager
                rvFriendsList.adapter = friendAdapter


            }
            val marginValue = 12.getValueInPx(resources.displayMetrics)
            rvFriendsList.addItemDecoration(VerticalDecorator(itemOffset = marginValue))

            btnBottomSheet.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, BottomSheetFragment(friendAdapter!!))
                    .addToBackStack(null)
                    .commit()
            }
        }
    }




    private fun onFriendClicked (friend: Friend) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, FriendInfoFragment.newInstance(friend.id))
            .addToBackStack(null)
            .commit()
    }

    private fun onLikeClicked(position: Int, friend: Friend) {
        FriendsRepository.addToFavorites(friend)
        friendAdapter?.updateItem(position, friend)
    }

    companion object {
        fun newInstance(numberOfFriends: Int) = FriendsListFragment().apply {
            arguments = bundleOf(ParamsKey.NUMBER_OF_FRIENDS_KEY to numberOfFriends)
        }


    }
}
