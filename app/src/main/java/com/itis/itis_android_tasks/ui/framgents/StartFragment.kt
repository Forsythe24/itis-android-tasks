package com.itis.itis_android_tasks.ui.framgents

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.itis.itis_android_tasks.R
import com.itis.itis_android_tasks.databinding.FragmentStartBinding
import com.itis.itis_android_tasks.utils.FriendsRepository

class StartFragment : Fragment(R.layout.fragment_start) {
    private val viewBinding: FragmentStartBinding by viewBinding(FragmentStartBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        with (viewBinding) {
            FriendsRepository.getCurrentItems().clear()

            btnToFriends.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(
                        R.id.fragment_container,
                        FriendsListFragment.newInstance(etNumberOfFriends.text.toString().toInt())
                    )
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    companion object {
        fun newInstance() = StartFragment()
    }
}
