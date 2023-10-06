package com.itis.itis_android_tasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.itis.itis_android_tasks.databinding.FragmentFirstBinding

class FirstFragment : Fragment() {
    private var _binding: FragmentFirstBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        with (binding) {
            btnFromScreen1ToScreen3.setOnClickListener {
                val screen1Input = etScreen1Input.text.toString()
                parentFragmentManager
                    .beginTransaction()
                    .replace(
                        R.id.fragment_container,
                        SecondFragment.newInstance(screen1Input),
                        SecondFragment.SECOND_FRAGMENT_TAG
                    )
                    .addToBackStack(null)
                    .commit()

                parentFragmentManager
                    .beginTransaction()
                    .replace(
                        R.id.fragment_container,
                        ThirdFragment.newInstance(screen1Input),
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
        const val FIRST_FRAGMENT_TAG = "FIRST_FRAGMENT_TAG"
    }

}
