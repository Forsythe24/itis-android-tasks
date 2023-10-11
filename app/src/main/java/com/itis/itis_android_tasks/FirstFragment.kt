package com.itis.itis_android_tasks

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.itis.itis_android_tasks.databinding.FragmentFirstBinding

class FirstFragment : Fragment() {
    private var _binding: FragmentFirstBinding? = null
    private val binding
        get() = _binding!!

    private val dataModel: DataModel by activityViewModels()

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
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(
                        R.id.fragment_container,
                        SecondFragment.newInstance(screen1Input),
                        SecondFragment.SECOND_FRAGMENT_TAG
                    )
                    .addToBackStack(null)
                    .commit()

                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(
                        R.id.fragment_container,
                        ThirdFragment.newInstance(screen1Input),
                        ThirdFragment.THIRD_FRAGMENT_TAG
                    )
                    .addToBackStack(null)
                    .commit()
            }

            btnSave.setOnClickListener {
                val screen1Input = etScreen1Input.text.toString()
                if (!screen1Input.isEmpty()) {
                    dataModel.add(screen1Input)
                } else {
                    Toast.makeText(activity, "The input field is empty", Toast.LENGTH_SHORT).show()
                }
                etScreen1Input.setText("");
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        const val FIRST_FRAGMENT_TAG = "FIRST_FRAGMENT_TAG"
        fun newInstance() = FirstFragment()
    }

}
