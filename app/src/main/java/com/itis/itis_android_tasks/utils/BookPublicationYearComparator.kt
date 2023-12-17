package com.itis.itis_android_tasks.utils

import com.itis.itis_android_tasks.model.Book

class BookPublicationYearComparator: Comparator<Book> {
    override fun compare(book1: Book, book2: Book): Int {
        return book1.publicationYear - book2.publicationYear
    }
}
