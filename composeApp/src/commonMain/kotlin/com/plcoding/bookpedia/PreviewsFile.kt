package com.plcoding.bookpedia

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.domain.books
import com.plcoding.bookpedia.book.presentation.book_list.BookListScreen
import com.plcoding.bookpedia.book.presentation.book_list.BookListScreenRoot
import com.plcoding.bookpedia.book.presentation.book_list.BookListState
import com.plcoding.bookpedia.book.presentation.book_list.components.BookList
import com.plcoding.bookpedia.book.presentation.book_list.components.BookSearchBar

@Preview
@Composable
fun PreviewBookSearchBar() {
    Box(
        modifier = Modifier.fillMaxWidth().background(Color.White),
        contentAlignment = Alignment.Center
    ){
        BookSearchBar(
            searchQuery = "",
            onSearchQueryChange = {},
            onImeSearch = {},
            modifier = Modifier.padding(8.dp).fillMaxWidth()
        )
    }
}


@Composable
@Preview
fun BookListPreview() {
    BookListScreen(
        state = BookListState(
            searchResult = books,
            selectedTabIndex = 0,
        ),
        onActions = {},
    )
}




