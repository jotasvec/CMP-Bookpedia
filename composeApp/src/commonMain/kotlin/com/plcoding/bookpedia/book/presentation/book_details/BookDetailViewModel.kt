package com.plcoding.bookpedia.book.presentation.book_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.plcoding.bookpedia.app.Route
import com.plcoding.bookpedia.book.domain.BookRepository
import com.plcoding.bookpedia.core.domain.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookDetailViewModel(
    private val bookRepository : BookRepository,
    private val savedStateHandle : SavedStateHandle
): ViewModel() {
    private val _state = MutableStateFlow(BookDetailState())
    val state = _state
        .onStart {
            fetchBookDescription()
            observeFavoriteStatus()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    private val _bookId = MutableStateFlow("")
    val bookID = savedStateHandle.toRoute<Route.BookDetail>().id


    fun onAction(action: BookDetailAction) {
        when (action) {
            is BookDetailAction.OnBackClicked -> {
                
            }
            is BookDetailAction.OnFavoriteClicked -> {
                viewModelScope.launch {
                    if (state.value.isFavorite){
                        bookRepository.deleteFromFavorites(bookID)
                    } else {
                        state.value.book?.let { book ->
                            bookRepository.markAsFavorite(book)
                        }
                    }
                }
            }
            is BookDetailAction.OnSelectedBookChange -> {
                _state.update { it.copy(
                    book = action.book,
                ) }
            }
        }
    }

    private fun observeFavoriteStatus(){
        bookRepository.isBookFavorite(bookID)
            .onEach { isFavorite ->
                _state.update { it.copy( isFavorite = isFavorite ) }
            }.launchIn(viewModelScope)
    }

    private fun fetchBookDescription(){
        viewModelScope.launch {
            bookRepository.getBookDescription(bookID)
                .onSuccess { description ->
                    _state.update { it.copy(
                        book = it.book?.copy(
                            description = description.toString()
                        ),
                        isLoading = false
                    ) }
                }
        }
    }



}