package com.itis.itis_android_tasks.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.itis.itis_android_tasks.R
import com.itis.itis_android_tasks.database.entity.RatingEntity
import com.itis.itis_android_tasks.databinding.FragmentBookInfoBinding
import com.itis.itis_android_tasks.di.ServiceLocator
import com.itis.itis_android_tasks.model.Book
import com.itis.itis_android_tasks.utils.ParamsKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookInfoFragment : Fragment(R.layout.fragment_book_info) {

    private val viewBinding: FragmentBookInfoBinding by viewBinding(FragmentBookInfoBinding::bind)
    private lateinit var bookId: String
    private lateinit var userId: String
    private var isRatedBeforeByThisUser: Boolean = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookId = requireArguments().getString(ParamsKey.BOOK_ID_KEY)!!
        userId = requireArguments().getString(ParamsKey.USER_ID_KEY)!!

        setCurrentRatingByThisUser()

        with(viewBinding) {
            rateBtn.setOnClickListener {
                val rating = bookRatingRb.rating.toDouble()
                addBookRating(rating)
            }


            lifecycleScope.launch (Dispatchers.IO) {
                val bookEntity = ServiceLocator.getDbInstance().bookDao.getBookById(bookId)

                bookEntity!!.let {
                    val book = Book(it.id, it.title, it.description, it.publicationYear, it.rating, false)

                    withContext(Dispatchers.Main) {
                        titleTv.text = book.title
                        descriptionTv.text = book.description
                        publicationYearTv.text = book.publicationYear.toString()

                    }
                }


            }

        }
    }

    private fun addBookRating(rating: Double) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                ServiceLocator.getDbInstance().ratingDao.addRating(
                    RatingEntity(
                        0,
                        bookId,
                        userId,
                        rating
                    )
                )

                updateRating(rating)
            }
        }
    }

    private fun updateRating(currRating: Double){
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {

                val allBookRatings = ServiceLocator.getDbInstance().ratingDao.getAllBookRatings(bookId)


                if (allBookRatings != null) {
                    val ratingsSum = allBookRatings.sumOf { ratingEntity -> ratingEntity.rating }

                    println(allBookRatings)
                    println(ratingsSum)
                    println(currRating)

                    val rating = ratingsSum / allBookRatings.size

                    ServiceLocator.getDbInstance().bookDao.updateBookRating(bookId, rating)

                } else {
                    ServiceLocator.getDbInstance().bookDao.updateBookRating(bookId, currRating)
                }
            }
        }
    }

    private fun setCurrentRatingByThisUser() {
        lifecycleScope.launch (Dispatchers.IO) {
            val ratingEntity = ServiceLocator.getDbInstance().ratingDao.getRatingByUserAndBookIds(userId, bookId)
            if (ratingEntity != null) {
                isRatedBeforeByThisUser = true
                withContext(Dispatchers.Main) {
                    viewBinding.bookRatingRb.rating = ratingEntity.rating.toFloat()
                }
            }
        }.onJoin
    }

    companion object {
        fun newInstance() = BookInfoFragment()
    }
}
