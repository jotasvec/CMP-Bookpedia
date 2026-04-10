package com.plcoding.bookpedia.book.domain

import com.plcoding.bookpedia.core.domain.DataError
import com.plcoding.bookpedia.core.domain.Result
import kotlinx.coroutines.flow.Flow

//Abstraction to use it on the ViewModel
interface BookRepository {
    suspend fun searchBooks(query: String): Result<List<Book>, DataError.Remote>
    suspend fun getBookDescription(bookID: String) : Result<String?, DataError>

    fun getAllFavoriteBooks() : Flow<List<Book>>

    fun isBookFavorite(id: String): Flow<Boolean>
    suspend fun markAsFavorite(book: Book) : Result<Unit, DataError.Local>
    suspend fun deleteFromFavorites(id: String)
}