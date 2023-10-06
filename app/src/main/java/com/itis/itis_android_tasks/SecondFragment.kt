package com.itis.itis_android_tasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.itis.itis_android_tasks.databinding.FragmentSecondBinding

class SecondFragment : Fragment() {
    private var _binding: FragmentSecondBinding? = null
    private val binding: FragmentSecondBinding
        get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }
    private fun init() {
        val message = arguments?.getString(ParamsKey.MESSAGE_TEXT_KEY)
        if (!message.isNullOrEmpty()) {
            binding.tvScreen2.text = message
        }

        binding.btnFromScreen2ToScreen1.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.btnFromScreen2ToScreen3.setOnClickListener {
            parentFragmentManager.apply {
                popBackStack()
                beginTransaction()
                    .replace(
                        R.id.fragment_container,
                        ThirdFragment.newInstance(message),
                        ThirdFragment.THIRD_FRAGMENT_TAG
                    )
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        const val SECOND_FRAGMENT_TAG = "SECOND_FRAGMENT_TAG"
        fun newInstance(message: String?): SecondFragment =
            SecondFragment().apply {
                arguments = bundleOf(ParamsKey.MESSAGE_TEXT_KEY to message)
            }
    }
}

