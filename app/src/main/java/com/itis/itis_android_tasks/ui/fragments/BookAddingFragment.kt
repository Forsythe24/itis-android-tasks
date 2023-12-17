package com.itis.itis_android_tasks.ui.fragments

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.itis.itis_android_tasks.R
import com.itis.itis_android_tasks.database.entity.BookEntity
import com.itis.itis_android_tasks.databinding.FragmentBookAddingBinding
import com.itis.itis_android_tasks.di.ServiceLocator
import com.itis.itis_android_tasks.utils.ParamsKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookAddingFragment : Fragment(R.layout.fragment_book_adding) {
    private val viewBinding: FragmentBookAddingBinding by viewBinding(FragmentBookAddingBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        with (viewBinding) {
            printBtn.setOnClickListener {
                lifecycleScope.launch(Dispatchers.IO) {

                    val pref = ServiceLocator.getSharedPreferences()

                    val lastBookId = pref.getString(ParamsKey.LAST_BOOK_ID_KEY, null) ?: "0"

                    val currentBookId = (lastBookId.toInt() + 1).toString()

                    pref.edit()
                        .putString(ParamsKey.LAST_BOOK_ID_KEY, currentBookId)
                        .apply()

                    val bookEntity = BookEntity(
                        currentBookId,
                        titleEt.text.toString(),
                        descriptionEt.text.toString(),
                        publicationYearEt.text.toString().toInt(),
                        0.0
                    )
                    try {
                        ServiceLocator.getDbInstance().bookDao.addBook(bookEntity)

                        withContext(Dispatchers.Main) {
                            titleEt.text.clear()
                            descriptionEt.text.clear()
                            publicationYearEt.text.clear()
                        }
                    } catch (e: SQLiteConstraintException) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.duplicate_book_warning),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    }

    companion object {
        fun newInstance() = BookAddingFragment()
    }
}
