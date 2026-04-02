package com.plcoding.bookpedia


import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.plcoding.bookpedia.book.presentation.book_list.BookListScreenRoot
import com.plcoding.bookpedia.book.presentation.book_list.BookListViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    MaterialTheme{
        val viewModel = koinViewModel<BookListViewModel>()
        BookListScreenRoot(
            viewModel = viewModel,
            onBookClicked = {book ->
                println("Clicked ${book.title}")
            },
        )
        // val viewModel: BookListViewModel = koinViewModel<BookListViewModel>()

    }
}