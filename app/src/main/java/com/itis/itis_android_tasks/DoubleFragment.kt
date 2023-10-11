package com.itis.itis_android_tasks

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.itis.itis_android_tasks.databinding.FragmentMainBinding

class DoubleFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

//        val currOrientation = resources.configuration.orientation
//        if (currOrientation == Configuration.ORIENTATION_PORTRAIT) {
//            binding.placeholder2.visibility = View.GONE
//        }
//        else {
//            binding.placeholder2.visibility = View.GONE
//        }

        val currOrientation = resources.configuration.orientation
        if (currOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.placeholder2.visibility = View.VISIBLE
        }
        else {
            binding.placeholder2.visibility = View.GONE
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }


    private fun init() {
        if (childFragmentManager.findFragmentByTag(FirstFragment.FIRST_FRAGMENT_TAG) == null) {
            childFragmentManager
                .beginTransaction()
                .replace(R.id.placeholder_1, FirstFragment.newInstance())
                .replace(R.id.placeholder_2, FourthFragment.newInstance())
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val DOUBLE_FRAGMENT_TAG = "DOUBLE_FRAGMENT_TAG"
        fun newInstance() = DoubleFragment()
    }
}
