package com.plcoding.bookpedia.book.data.mappers

import com.plcoding.bookpedia.book.data.database.BookEntity
import com.plcoding.bookpedia.book.data.dto.SearchBookDto
import com.plcoding.bookpedia.book.domain.Book
import kotlin.String

fun SearchBookDto.toBook(): Book {
    return Book(
        id = id.substringAfterLast('/'),
        title = title,
        imageUrl = if (coverKey != null) {
            "https://covers.openlibrary.org/b/olid/${coverKey}-L.jpg"
        }else {
            "https://covers.openlibrary.org/b/id/${coverAlternativeKey}-L.jpg"
        },
        authors = authorNames ?: emptyList(),
        description = "",
        languages = languages ?: emptyList(),
        firstPublishedDate = firstPublishYear?.toString(),
        ratingAverage = ratingsAverage,
        ratingCount = ratingsCount,
        numPages = numPagesMedian,
        numEditions = editionCount,
    )
}

fun Book.toBookEntity() : BookEntity{
    return BookEntity(
        id = id,
        title = title,
        imageUrl = imageUrl,
        authors = authors,
        description = description,
        languages = languages,
        firstPublishedDate = firstPublishedDate,
        ratingAverage = ratingAverage,
        ratingCount = ratingCount,
        numPages = numPages,
        numEditions = numEditions,
    )
}

fun BookEntity.toBook() : Book{
    return Book(
        id = id,
        title = title,
        imageUrl = imageUrl,
        authors = authors,
        description = description,
        languages = languages,
        firstPublishedDate = firstPublishedDate,
        ratingAverage = ratingAverage,
        ratingCount = ratingCount,
        numPages = numPages,
        numEditions = numEditions,
    )
}
