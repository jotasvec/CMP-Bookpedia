package com.plcoding.bookpedia.book.presentation.book_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmp_bookpedia.composeapp.generated.resources.Res
import cmp_bookpedia.composeapp.generated.resources.error_message
import cmp_bookpedia.composeapp.generated.resources.favorites
import cmp_bookpedia.composeapp.generated.resources.search_results
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.presentation.book_list.components.BookList
import com.plcoding.bookpedia.book.presentation.book_list.components.BookSearchBar
import com.plcoding.bookpedia.core.presentation.DarkBlue
import com.plcoding.bookpedia.core.presentation.DesertWhite
import com.plcoding.bookpedia.core.presentation.SandYellow
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BookListScreenRoot(
    viewModel: BookListViewModel,
    //viewModel: BookListViewModel = remember { BookListViewModel() },
    onBookClicked: (Book) -> Unit,
){
    val state by viewModel.state.collectAsStateWithLifecycle()
    BookListScreen(
        state = state,
        onActions = {actions ->
            when(actions){
                is BookListActions.OnBookClicked -> onBookClicked(actions.book)
                else -> Unit
            }
            viewModel.onAction(actions)
        }
    )
}

@Composable
fun BookListScreen(
    state: BookListState,
    onActions: (BookListActions) -> Unit,
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val pagerState = rememberPagerState{2}
    val searchLazyListState = rememberLazyListState()
    val favoriteLazyListState = rememberLazyListState()

    LaunchedEffect(state.searchResult){
        searchLazyListState.animateScrollToItem(0)
    }
    LaunchedEffect(state.selectedTabIndex) {
        if (pagerState.currentPage != state.selectedTabIndex) {
            pagerState.animateScrollToPage(state.selectedTabIndex)
        }
    }

    LaunchedEffect(pagerState.settledPage) {
        if (state.selectedTabIndex != pagerState.settledPage) {
            onActions(BookListActions.OnTabSelected(pagerState.settledPage))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        BookSearchBar(
            searchQuery = state.searchQuery,
            onSearchQueryChange = {
                onActions(BookListActions.OnSearchQueryChanged(it))
            },
            onImeSearch = {
                keyboardController?.hide()
            },
            modifier = Modifier.widthIn(max = 400.dp).fillMaxWidth().padding(16.dp)
        )

        Surface(
            modifier = Modifier.fillMaxWidth().weight(1f),
            color = DesertWhite,
            shape = MaterialTheme.shapes.extraLarge,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ){

                PrimaryTabRow(
                    selectedTabIndex = state.selectedTabIndex,
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .widthIn(max = 700.dp)
                        .fillMaxWidth(),
                    contentColor = SandYellow,
                    containerColor = DesertWhite,
                    indicator = {
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(selectedTabIndex = state.selectedTabIndex, matchContentSize = false),
                            color = SandYellow,
                    ) },
                ){
                    Tab(
                        modifier = Modifier.weight(1f),
                        selected = state.selectedTabIndex == 0,
                        onClick = {
                            println("search results Pressed")
                            onActions(BookListActions.OnTabSelected(0))
                            println("El estado actual en la UI es: ${state.selectedTabIndex}")
                        },
                        selectedContentColor = SandYellow,
                        unselectedContentColor = Color.Gray,
                        text = { Text(text = stringResource(Res.string.search_results), style = MaterialTheme.typography.bodyLarge  ) }
                    )
                    Tab(
                        modifier = Modifier.weight(1f),
                        selected = state.selectedTabIndex == 1,
                        onClick = {
                            println("Favorite Pressed")
                            onActions(BookListActions.OnTabSelected(1))
                            println("El estado actual en la UI es: ${state.selectedTabIndex}")
                        },
                        selectedContentColor = SandYellow,
                        unselectedContentColor = Color.Gray,
                        text = { Text(text = stringResource(Res.string.favorites), style = MaterialTheme.typography.bodyLarge ) },
                    )

                }
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth().weight(1f),
                ){ pageIndex ->
                    when (pageIndex) {
                        0 -> {
                            if(state.isLoading){
                                CircularProgressIndicator()
                            } else {
                                when {
                                    state.errorMessage != null -> {
                                        Text(
                                            text = state.errorMessage.asString(),
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.headlineSmall,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                    state.searchResult.isEmpty() -> {
                                        Text(
                                            text = stringResource(Res.string.error_message, "no results found"),
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.headlineSmall,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                    else -> {
                                        BookList(
                                            booksList = state.searchResult,
                                            onBookClick = { onActions(BookListActions.OnBookClicked(it)) },
                                            modifier = Modifier.fillMaxWidth(),
                                            scrollState = searchLazyListState,
                                        )
                                    }
                                }
                            }
                        } // end index tab 0
                        1 -> {
                            if(state.isLoading){
                                CircularProgressIndicator()
                            } else {
                                when {
                                    state.errorMessage != null -> {
                                        Text(
                                            text = state.errorMessage.asString(),
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.headlineSmall,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }

                                    state.favoriteBooks.isEmpty() -> {
                                        Text(
                                            text = stringResource(
                                                Res.string.error_message,
                                                "no favorite books were added"
                                            ),
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.headlineSmall,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }

                                    else -> {
                                        BookList(
                                            booksList = state.favoriteBooks,
                                            onBookClick = { onActions(BookListActions.OnBookClicked(it)) },
                                            modifier = Modifier.fillMaxWidth(),
                                            scrollState = favoriteLazyListState,
                                        )
                                    }
                                }
                            }
                        }
                        else -> {
                            throw IllegalArgumentException("Invalid pageIndex $pageIndex")
                        }
                    }

                }

            }
        }
    }
}