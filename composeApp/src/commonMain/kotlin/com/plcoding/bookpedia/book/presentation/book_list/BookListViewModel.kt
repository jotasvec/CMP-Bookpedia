package com.plcoding.bookpedia.book.presentation.book_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.plcoding.bookpedia.book.domain.Book

import com.plcoding.bookpedia.book.domain.BookRepository
import com.plcoding.bookpedia.core.domain.onError
import com.plcoding.bookpedia.core.domain.onSuccess
import com.plcoding.bookpedia.core.presentation.toUiText
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookListViewModel(
    private val bookRepository: BookRepository
): ViewModel() {
    private var cachedBooks = emptyList<Book>()
    private var searchJob: Job? = null
    private var observeFavoriteJob: Job? = null
    private val _state = MutableStateFlow(BookListState())
    val state = _state
        .onStart {
            if (cachedBooks.isEmpty()) observeSearchQuery()
            observeFavoriteBooks()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _state.value
        )

    fun onAction(action: BookListActions) {
        when (action) {
            is BookListActions.OnBookClicked -> {
                // BookDetails

            }
            is BookListActions.OnSearchQueryChanged -> {
                _state.update {
                    it.copy( searchQuery = action.query )
                }
            }
            is BookListActions.OnTabSelected -> {
                _state.update {
                    it.copy( selectedTabIndex = action.index )
                }
            }
        }
    }

    private fun observeFavoriteBooks() {
        observeFavoriteJob?.cancel()
        observeFavoriteJob = bookRepository.getAllFavoriteBooks()
            .onEach { favoriteBooks ->
                _state.update { it.copy(
                    favoriteBooks = favoriteBooks
                )}
            }.launchIn(viewModelScope)

    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery(){
        state.map { it.searchQuery }
            .distinctUntilChanged()
            .debounce(timeoutMillis = 500L)
            .onEach {query ->
                when{
                    query.length <= 3  ->
                        _state.update { it.copy(
                            errorMessage = null,
                            searchResult = cachedBooks
                        )
                    }
                    else -> {
                        searchJob?.cancel()
                        searchJob = searchBooks(query)
                    }
                }
            }
            .launchIn(viewModelScope)

    }

    private fun searchBooks(query: String) = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }
        bookRepository
            .searchBooks(query)
            .onSuccess { remoteBooks ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        searchResult = remoteBooks,
                        errorMessage = null,
                    )
                }
            }
            .onError { error ->
                _state.update { it.copy(
                    searchResult = emptyList(),
                    isLoading = false,
                    errorMessage = error.toUiText(),
                ) }
        }
    }

}