package com.plcoding.bookpedia.book.presentation.book_details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key.Companion.R
import androidx.room.util.TableInfo
import org.jetbrains.compose.resources.stringResource

@Composable
fun TitleContent(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
        )
        content()
    }
}