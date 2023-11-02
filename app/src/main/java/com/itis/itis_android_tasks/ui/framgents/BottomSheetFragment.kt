package com.itis.itis_android_tasks.ui.framgents

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.itis.itis_android_tasks.R
import com.itis.itis_android_tasks.adapter.FriendAdapter
import com.itis.itis_android_tasks.databinding.FragmentBottomSheetBinding
import com.itis.itis_android_tasks.utils.FriendsRepository
import kotlin.random.Random

class BottomSheetFragment(
    private val adapter: FriendAdapter
) : Fragment(R.layout.fragment_bottom_sheet) {
    private val viewBinding: FragmentBottomSheetBinding by viewBinding(FragmentBottomSheetBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        with (viewBinding) {
            btnAdd.setOnClickListener{
                var newFriendsNumber = etAdd.text.toString().toInt()

                val items = FriendsRepository.getCurrentItems()

                if (items.size == 1) {
                    items.add(FriendsRepository.getFriend())
                    newFriendsNumber--
                }

                repeat(newFriendsNumber) {
                    val newFriend = FriendsRepository.getFriend()

                    items.add(Random.nextInt(1, items.size), newFriend)
                }
                adapter.setItems(items)

            }
        }
    }

    companion object {
        const val BOTTOM_SHEET_FRAGMENT_TAG = "BOTTOM_SHEET_FRAGMENT_TAG"
    }
}
