package com.itis.itis_android_tasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.itis.itis_android_tasks.databinding.FragmentQuizBinding

const val ARG_OBJECT = "object"

class QuizFragment : Fragment(R.layout.fragment_quiz) {
    private val viewBinding: FragmentQuizBinding by viewBinding(FragmentQuizBinding::bind)

    private var rvAdapter: QuizAdapter? = null



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        with(viewBinding) {
            val questionNumber = requireArguments().getInt(ARG_OBJECT)

            btnFinish.setOnClickListener {
                Toast.makeText(activity, "You've successfully completed the quiz!", Toast.LENGTH_SHORT).show()
            }

            val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            rvQuiz.layoutManager = layoutManager
            rvAdapter = QuizAdapter(

                items = OptionGenerator.getOptions(requireContext()).map { OptionData(option = it) }
                    .toMutableList(),

                onItemChecked = { position, isChecked ->
                    rvAdapter?.items?.let {
                        it[position].isChecked = isChecked
                    }
                },
                questionNumber = questionNumber,
                finishButton = btnFinish
            )

            tvQuestion.text = QuestionGenerator.getQuestion(requireContext())
            tvQuestionNumber.text = questionNumber.toString()

            rvQuiz.adapter = rvAdapter
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        rvAdapter = null
    }

    companion object {
        fun newInstance() = QuizFragment()

    }
}
