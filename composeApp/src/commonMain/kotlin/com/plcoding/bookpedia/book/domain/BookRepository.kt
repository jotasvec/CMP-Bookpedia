package com.plcoding.bookpedia.book.domain

import com.plcoding.bookpedia.core.domain.DataError
import com.plcoding.bookpedia.core.domain.Result

//Abstraction to use it on the ViewModel
interface BookRepository {
    suspend fun searchBooks(query: String): Result<List<Book>, DataError.Remote>
}