package com.plcoding.bookpedia.book.presentation.book_list

import com.plcoding.bookpedia.book.domain.Book

sealed interface BookListActions {
    data class  OnSearchQueryChanged(val query: String) : BookListActions
    data class  OnBookClicked(val book: Book) : BookListActions
    data class  OnTabSelected(val index: Int) : BookListActions
}
