package com.plcoding.bookpedia.book.presentation.book_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Start
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmp_bookpedia.composeapp.generated.resources.Res
import cmp_bookpedia.composeapp.generated.resources.description_unavailable
import cmp_bookpedia.composeapp.generated.resources.languages
import cmp_bookpedia.composeapp.generated.resources.pages
import cmp_bookpedia.composeapp.generated.resources.rating
import cmp_bookpedia.composeapp.generated.resources.synopsis
import com.plcoding.bookpedia.book.presentation.book_details.components.BookChip
import com.plcoding.bookpedia.book.presentation.book_details.components.BookDetailImageBackground
import com.plcoding.bookpedia.book.presentation.book_details.components.ChipSize
import com.plcoding.bookpedia.book.presentation.book_details.components.TitleContent
import com.plcoding.bookpedia.core.presentation.SandYellow
import io.ktor.util.reflect.typeInfo
import org.jetbrains.compose.resources.stringResource
import kotlin.math.round
import kotlin.reflect.typeOf

@Composable
fun BookDetailScreenRoot(
    viewModel: BookDetailViewModel,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    BookDetailScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is BookDetailAction.OnBackClicked -> onBackClicked()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun BookDetailScreen(
    state: BookDetailState,
    onAction: (BookDetailAction) -> Unit,
) {
    BookDetailImageBackground(
        imageUrl = state.book?.imageUrl,
        isFavorite = state.isFavorite,
        onBackClick = { onAction(BookDetailAction.OnBackClicked) },
        onFavoriteClick = { onAction(BookDetailAction.OnFavoriteClicked) },
        modifier = Modifier.fillMaxSize()
    ){
        if(state.book != null ){
            val book = state.book
            Column(
                modifier = Modifier
                    .widthIn(max = 700.dp)
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = book.authors.joinToString(),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
                Row(
                    modifier = Modifier.padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    book.ratingAverage?.let { rating ->
                        TitleContent(stringResource(Res.string.rating)){
                            BookChip {
                                Text("${round( rating * 10) / 10.0 }")
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = SandYellow
                                )
                            }
                        }
                    }
                    book.numPages?.let { pages ->
                        TitleContent(stringResource(Res.string.pages)){
                            BookChip {
                                Text("$pages")
                            }
                        }
                    }
                }
                if (book.languages.isNotEmpty() ){
                    TitleContent(
                        title = stringResource(Res.string.languages),
                        modifier = Modifier.padding(vertical = 16.dp)
                    ){
                        FlowRow(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.wrapContentSize(Alignment.Center)
                        ) {
                            book.languages.forEach { language ->
                                BookChip(
                                    size = ChipSize.SMALL,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)) {
                                    Text(
                                        text = language.uppercase(),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
                Text(
                    text = stringResource(Res.string.synopsis),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .fillMaxWidth()
                        .padding(top = 24.dp, bottom = 8.dp)
                )
                val isBlank = book.description.isNullOrBlank() || book.description.equals("null", ignoreCase = true)

                if(state.isLoading){
                    Box(
                        modifier = Modifier.fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ){
                        CircularProgressIndicator()
                    }
                } else {
                    Text(
                        text = if (isBlank) {
                            stringResource(Res.string.description_unavailable)
                        } else {
                            book.description
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Justify,
                        color = if (isBlank) Color.Black.copy(alpha = 0.4f) else Color.Black,
                        modifier = Modifier.padding(vertical = 8.dp)

                    )
                }
            }
        }
    }
}