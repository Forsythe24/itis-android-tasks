package com.itis.itis_android_tasks

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import by.kirich1409.viewbindingdelegate.viewBinding
import com.itis.itis_android_tasks.databinding.FragmentViewPagerBinding



open class ViewPagerFragment : Fragment(R.layout.fragment_view_pager) {

    private val viewBinding : FragmentViewPagerBinding by viewBinding(FragmentViewPagerBinding::bind)

    private var vpAdapter: FragmentAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
    }


    private fun initAdapter() {
        val numberOfQuestions: Int? = arguments?.getInt(ParamsKey.NUMBER_OF_QUESTIONS_KEY)
        vpAdapter = FragmentAdapter(
            parentFragmentManager,
            lifecycle,
            numberOfQuestions!!,
        )

        with (viewBinding.vpFragment) {
            adapter = vpAdapter
            offscreenPageLimit = numberOfQuestions + 2


            if (this.currentItem == 0) {
                this.setCurrentItem(1, false)
            }

            this.registerOnPageChangeCallback(object : OnPageChangeCallback() {

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    when (this@with.currentItem) {
                            numberOfQuestions + 2 - 1 -> this@with.setCurrentItem(1, false)
                            0 -> this@with.setCurrentItem(numberOfQuestions, false)
                    }
                }
            })
        }

    }



    override fun onDestroyView() {
        super.onDestroyView()
        vpAdapter = null
    }


    companion object {
        fun newInstance(numberOfQuestions: Int) = ViewPagerFragment().apply {
            arguments = bundleOf(ParamsKey.NUMBER_OF_QUESTIONS_KEY to numberOfQuestions)
        }
    }
}
