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
import androidx.compose.ui.tooling.preview.Preview import androidx.compose.ui.unit.dp
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.presentation.book_details.BookDetailScreen
import com.plcoding.bookpedia.book.presentation.book_details.BookDetailState
import com.plcoding.bookpedia.book.presentation.book_list.BookListScreen
import com.plcoding.bookpedia.book.presentation.book_list.BookListState
import com.plcoding.bookpedia.book.presentation.book_list.components.BookSearchBar

@Preview
@Composable
fun PreviewBookSearchBar() {
    MaterialTheme {
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
}
private val books = (1..10).map {
    Book(
        id = it.toString(),
        title = "Book $it",
        imageUrl = "https://s3.amazonaws.com/books/$it",
        authors = listOf("jotasvec"),
        description = """About Book $it""",
        firstPublishedDate = null,
        ratingAverage = 4.123,
        languages = listOf("en"),
        ratingCount = null,
        numPages = 100,
        numEditions = 1
    )
}

@Composable
@Preview(apiLevel = 34)
fun BookListPreview() {
    MaterialTheme{
        BookListScreen(
            state = BookListState(
                searchResult = books,
                selectedTabIndex = 0,
            ),
            onActions = {},
        )
    }
}

@Composable
@Preview
fun BookDetailsPreview() {
    MaterialTheme {
        BookDetailScreen(
            state = BookDetailState(
                isFavorite = false,
                isLoading = false,
                book = Book(
                    id = "122234",
                    title = "Book Title",
                    imageUrl = "/Users/jotasvec/Development/playground/Kotlin/CMP-Bookpedia/composeApp/src/commonMain/composeResources/drawable/0013764495-L.jpg",//"https://ia801909.us.archive.org/view_archive.php?archive=/31/items/l_covers_0013/l_covers_0013_76.zip&file=0013764495-L.jpg",
                    authors = listOf("jotasvec"),
                    description = """About Book """,
                    firstPublishedDate = null,
                    ratingAverage = 4.123,
                    languages = listOf("en","es","de","fr"),
                    ratingCount = null,
                    numPages = 100,
                    numEditions = 1
                )
            ),
            onAction = {}
        )
    }
}




