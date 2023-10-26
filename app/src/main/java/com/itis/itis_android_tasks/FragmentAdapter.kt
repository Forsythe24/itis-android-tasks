package com.itis.itis_android_tasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentAdapter(manager: FragmentManager, lifecycle: Lifecycle, val numberOfQuestions: Int) : FragmentStateAdapter(manager, lifecycle) {

    override fun getItemCount(): Int = numberOfQuestions + 2


    override fun getItemId(position: Int): Long {
        if (isFirstTime) {
            isFirstTime = false
            createFragmentList()
        }
        return super.getItemId(position)
    }



    override fun createFragment(position: Int): Fragment {
        return extendedFragments[position]
    }

    private fun createFragmentList() {
        for (index in 0 until numberOfQuestions) {
            val fragment = QuizFragment.newInstance()
            fragment.arguments = Bundle().apply {
                putInt(ARG_OBJECT, index + 1)
            }
            fragments.add(fragment)
        }
        
        val fakeFragment1 = QuizFragment.newInstance()
        fakeFragment1.arguments = Bundle().apply {
            putInt(ARG_OBJECT, numberOfQuestions)
        }
        
        val fakeFragment2 = QuizFragment.newInstance()
        fakeFragment2.arguments = Bundle().apply {
            putInt(ARG_OBJECT, 1)
        }
        
        extendedFragments = listOf(fakeFragment1) + fragments + listOf(fakeFragment2)
    }




    companion object {
        private var fragments: MutableList<Fragment> = ArrayList()

        private lateinit var extendedFragments: List<Fragment>

        private var isFirstTime = true

    }
}
