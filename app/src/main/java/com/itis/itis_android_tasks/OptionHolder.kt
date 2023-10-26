package com.itis.itis_android_tasks


import android.view.View
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.itis.itis_android_tasks.databinding.ItemQuestionBinding

class OptionHolder(
    private val viewBinding: ItemQuestionBinding,
    private val onItemChecked: ((Int, Boolean) -> Unit)? = null,
    private val questionNumber: Int,
    finishButton: MaterialButton
) : RecyclerView.ViewHolder(viewBinding.root) {

    init {
        if (checkedCheckBoxes.size == questionNumber - 1) {
            checkedCheckBoxes.add(viewBinding.cbOption)


            if (questionNumber != 1) {
                buttons.last().visibility = View.INVISIBLE
            }

            buttons.add(finishButton)

            buttons.last().visibility = View.VISIBLE
        }


        with (viewBinding) {
            cbOption.setOnCheckedChangeListener { _, isChecked ->
                onItemChecked?.invoke(adapterPosition, isChecked)

                if (isChecked) {
                    val checkedBox = checkedCheckBoxes[questionNumber - 1]

                    checkedBox.isEnabled = true
                    checkedBox.isChecked = false

                    checkedCheckBoxes[questionNumber - 1] = cbOption
                    cbOption.isEnabled = false

                    if (isCompleted()) {
                        buttons.forEach{
                            b -> b.visibility = View.VISIBLE
                            b.isEnabled = true
                        }
                    }
                }
            }
        }
    }

    private fun isCompleted() : Boolean {
        checkedCheckBoxes.forEach{
            if (!it.isChecked) {
                return false
            }
        }
        return true
    }

    fun bindItem(item: OptionData, itemCount: Int) {
        with(viewBinding) {
            cbOption.text = item.option
            cbOption.isChecked = item.isChecked

        }
    }

    companion object {
        private var checkedCheckBoxes: MutableList<CheckBox> = ArrayList()
        private var buttons: MutableList<MaterialButton> = ArrayList()
    }
}
