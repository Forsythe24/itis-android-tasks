package com.itis.itis_android_tasks

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.itis.itis_android_tasks.databinding.FragmentStartBinding
import org.w3c.dom.Text

class StartFragment : Fragment(R.layout.fragment_start) {
    private val viewBinding: FragmentStartBinding by viewBinding(FragmentStartBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        with(viewBinding) {
            etPhoneNumber.setOnFocusChangeListener {_, b ->
                if (b && etPhoneNumber.text.length < 5) {
                    etPhoneNumber.setText("+7 (9")
                }
            }

            val regex = Regex("\\+7 \\(9\\d{2}\\)-\\d{3}(-\\d{2}){2}")

            fun setDisabledButtonOnClickListener() {
                btnToQuizScreen.isEnabled = false

                btnToQuizScreen.setOnClickListener {
                    if (!regex.matches(etPhoneNumber.text)|| etNumberOfQuestions.text.isNullOrEmpty() || etNumberOfQuestions.text.toString().toInt() !in 1..30) {
                        if (!etPhoneNumber.text.matches(regex)) {
                            Toast.makeText(
                                activity,
                                "Invalid phone number. Valid phone number format: +7 (9**)-***-**-**.",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                activity,
                                "The quiz can consist only of 1 - 30 questions.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
            
            fun setEnabledButtonOnClickListener() {
                btnToQuizScreen.isEnabled = true

                btnToQuizScreen.setOnClickListener {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, ViewPagerFragment.newInstance(etNumberOfQuestions.text.toString().toInt()))
                        .commit()
                }
            }

            setDisabledButtonOnClickListener()

            etPhoneNumber.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(input: CharSequence?, start: Int, count: Int, after: Int) {}
                
                override fun onTextChanged(input: CharSequence?, start: Int, before: Int, count: Int) {

                    if (!etPhoneNumber.text.matches(regex) || etNumberOfQuestions.text.isNullOrEmpty() || etNumberOfQuestions.text.toString().toInt() !in 1..30) {
                            setDisabledButtonOnClickListener()
                    } else {
                        setEnabledButtonOnClickListener()
                    }


                    fun setSelectionToLast() = etPhoneNumber.setSelection(etPhoneNumber.text?.length ?: 0)

                    fun autofill(suffix: String) = etPhoneNumber.setText("${input}${suffix}")

                    input?.let {
                        if (before == 0) {
                            when (input.length) {

                                7 -> {
                                    autofill(")-")
                                    setSelectionToLast()
                                }

                                12, 15 -> {
                                    autofill("-")
                                    setSelectionToLast()
                                }
                            }

                        } else {
                            when (before) {

                                9 -> {
                                    etPhoneNumber.removeTextChangedListener(this)
                                    etPhoneNumber.setText("${input.dropLast(2)}")
                                    etPhoneNumber.addTextChangedListener(this)
                                    setSelectionToLast()
                                }

                                13, 16 -> {
                                    etPhoneNumber.removeTextChangedListener(this)
                                    etPhoneNumber.setText("${input.dropLast(1)}")
                                    etPhoneNumber.addTextChangedListener(this)
                                    setSelectionToLast()
                                }
                            }
                        }
                    }
                }
                
                override fun afterTextChanged(input: Editable?) {}
            }
            )
            
            etNumberOfQuestions.addTextChangedListener (object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (!regex.matches(etPhoneNumber.text) || etNumberOfQuestions.text.isNullOrEmpty() || etNumberOfQuestions.text.toString().toInt() !in 0 .. 30) {
                        setDisabledButtonOnClickListener()
                    } else {
                        setEnabledButtonOnClickListener()
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            }
            )
        }
    }

    companion object {
        fun newInstance() = StartFragment()
    }
}
