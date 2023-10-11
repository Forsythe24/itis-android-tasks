package com.itis.itis_android_tasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.itis.itis_android_tasks.databinding.FragmentFourthBinding

class FourthFragment : Fragment() {
    private var _binding: FragmentFourthBinding? = null
    private val binding
        get() = _binding!!

    private val dataModel: DataModel by activityViewModels()
    private var textNumber = 0;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFourthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        dataModel.messageData.observe(viewLifecycleOwner, {messageDataList ->
            updateTextViews(messageDataList)
        })
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateTextViews(messages: List<String?>) {
        binding.apply {
            when (textNumber) {
                1 -> tv1Screen4.text = messages.last()
                2 -> tv2Screen4.text = messages.last()
                3 -> {
                    tv3Screen4.text = messages.last()
                    textNumber = 0
                }
            }
            textNumber++
        }
    }


    companion object {
        const val FOURTH_FRAGMENT_TAG = "FOURTH_FRAGMENT_TAG"
        fun newInstance() = FourthFragment()
    }
}
