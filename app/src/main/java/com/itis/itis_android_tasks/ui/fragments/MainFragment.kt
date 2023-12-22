package com.itis.itis_android_tasks.ui.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.button.MaterialButton
import com.itis.itis_android_tasks.R
import com.itis.itis_android_tasks.database.entity.UserBookEntity
import com.itis.itis_android_tasks.databinding.FragmentMainBinding
import com.itis.itis_android_tasks.di.ServiceLocator
import com.itis.itis_android_tasks.model.Book
import com.itis.itis_android_tasks.ui.adapter.BookAdapter
import com.itis.itis_android_tasks.ui.adapter.decorations.SimpleHorizontalDecoration
import com.itis.itis_android_tasks.ui.adapter.decorations.SimpleVerticalDecoration
import com.itis.itis_android_tasks.utils.comparator.BookPublicationYearComparator
import com.itis.itis_android_tasks.utils.ParamsKey
import com.itis.itis_android_tasks.utils.getValueInPx
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainFragment : Fragment(R.layout.fragment_main), AdapterView.OnItemSelectedListener {

    private val viewBinding: FragmentMainBinding by viewBinding(FragmentMainBinding::bind)

    private lateinit var userId: String

    private var bookItems = mutableListOf<Book>()

    private var favoriteItems = mutableListOf<Book>()

    private var allBooksAdapter: BookAdapter? = null

    private var favoritesAdapter: BookAdapter? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
          when (pos) {
              0 -> {
                  sortByPublicationYearDescending(bookItems)
                  allBooksAdapter!!.setItems(bookItems)
              }

              1 -> {
                  sortByPublicationYearAscending(bookItems)
                  allBooksAdapter!!.setItems(bookItems)
              }

              2 -> {
                  bookItems.sortByDescending { book ->
                      book.rating
                  }
                  allBooksAdapter!!.setItems(bookItems)
              }

              3 -> {
                  bookItems.sortBy { book ->
                      book.rating
                  }
                  allBooksAdapter!!.setItems(bookItems)
              }
         }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
    }

    private fun init() {
        with (viewBinding) {
            userId = requireArguments().getString(ParamsKey.USER_ID_KEY)!!

            ArrayAdapter.createFromResource(
                requireContext(),
                R.array.sort_types_array,
                android.R.layout.simple_spinner_item
            ).also { 
                arrayAdapter ->
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                bookSortSpinner.adapter = arrayAdapter
            }

            bookSortSpinner.onItemSelectedListener = this@MainFragment

            val allBooksLayoutManager = GridLayoutManager(requireContext(), 2)
            val favoritesLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            val offset = 4.getValueInPx(resources.displayMetrics)

            allBooksRv.addItemDecoration(SimpleVerticalDecoration(offset))
            allBooksRv.addItemDecoration(SimpleHorizontalDecoration(offset))

            favoritesRv.addItemDecoration(SimpleVerticalDecoration(offset))
            favoritesRv.addItemDecoration(SimpleHorizontalDecoration(offset))


            allBooksRv.layoutManager = allBooksLayoutManager
            favoritesRv.layoutManager = favoritesLayoutManager

            allBooksAdapter = BookAdapter(onBookClicked = ::onBookClicked, onBookLongClicked = ::onBookLongClicked)
            favoritesAdapter = BookAdapter(onBookClicked = ::onBookClicked, onBookLongClicked = ::onBookLongClicked)


            lifecycleScope.launch {
                withContext(Dispatchers.IO) {

                    val db = ServiceLocator.getDbInstance()

                    bookItems  = db.bookDao.getAllBooks()?.let {
                        it.map { bookEntity -> Book(bookEntity.id, bookEntity.title, bookEntity.description, bookEntity.publicationYear, bookEntity.rating, false)
                        }
                    } as MutableList<Book>

                    db.bookDao.getAllBooks()?.let {
                        allBooksAdapter!!.setItems(
                            bookItems
                        )
                    }

                    favoriteItems = db.userBookDao.getUserFavoriteBooks(userId).books.map {
                            book -> Book(book.id, book.title, book.description, book.publicationYear, book.rating, true)
                    } as MutableList<Book>

                    sortByPublicationYearAscending(favoriteItems)

                    favoritesAdapter!!.setItems(
                        favoriteItems
                    )

                }
            }

            allBooksRv.adapter = allBooksAdapter
            favoritesRv.adapter = favoritesAdapter

            val itemTouchHelperCallback =
                object :
                    ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT, ItemTouchHelper.UP or ItemTouchHelper.DOWN) {
                    override fun onMove(
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder
                    ): Boolean {
                        return false
                    }


                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val position = viewHolder.adapterPosition


                        lifecycleScope.launch {

                            withContext(Dispatchers.IO) {
                                ServiceLocator.getDbInstance().userBookDao.deleteUserBookByIds(
                                    userId,
                                    favoriteItems[position].id
                                )
                            }
                            favoriteItems[position].isFavorite = false

                            favoriteItems.removeAt(position)
                            favoritesAdapter!!.setItems(favoriteItems)
                        }


                    }
                }

            val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
            itemTouchHelper.attachToRecyclerView(favoritesRv)


            toProfileBtn.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_profileFragment, bundleOf(ParamsKey.USER_ID_KEY to userId))

            }

            toBookAddingBtn.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_bookAddingFragment)
            }
        }
    }

    private fun onBookLongClicked(book: Book, adapterPosition: Int) {
        val dialog = Dialog(requireContext())

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog.setCancelable(true)

        dialog.setContentView(R.layout.two_option_dialog)

        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()

        val btnDeleteBook = dialog.findViewById<MaterialButton>(R.id.first_option_btn)
        val btnAddToFavoritesBook = dialog.findViewById<MaterialButton>(R.id.second_option_btn)


        btnDeleteBook.setOnClickListener {

            lifecycleScope.launch(Dispatchers.IO) {
                
                ServiceLocator.getDbInstance().bookDao.deleteBookById(book.id)
                val removedBook = bookItems.removeAt(adapterPosition)

                if (favoriteItems.remove(removedBook)) {
                    ServiceLocator.getDbInstance().userBookDao.deleteUserBookByIds(userId, book.id)
                }



                withContext(Dispatchers.Main) {
                    dialog.hide()
                    favoritesAdapter!!.setItems(favoriteItems)
                    allBooksAdapter!!.setItems(bookItems)
                }
            }
        }
        
        btnAddToFavoritesBook.setOnClickListener {
            
            lifecycleScope.launch(Dispatchers.IO) {
                ServiceLocator.getDbInstance().userBookDao.addUserBook(UserBookEntity(userId, book.id))
                book.isFavorite = true

                withContext(Dispatchers.Main) {
                    dialog.hide()
                    favoriteItems.add(book)

                    sortByPublicationYearAscending(favoriteItems)

                    favoritesAdapter!!.setItems(favoriteItems)
                }
            }
        }
    }

    private fun onBookClicked(book: Book) {
        findNavController().navigate(R.id.action_mainFragment_to_bookInfoFragment, bundleOf(ParamsKey.BOOK_ID_KEY to book.id, ParamsKey.USER_ID_KEY to userId))
    }

    private fun sortByPublicationYearAscending(items: MutableList<Book>) {
        items.sortWith(BookPublicationYearComparator())
    }
    
    private fun sortByPublicationYearDescending(items: MutableList<Book>) {
        items.sortByDescending { book ->
            book.publicationYear
        }
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}
