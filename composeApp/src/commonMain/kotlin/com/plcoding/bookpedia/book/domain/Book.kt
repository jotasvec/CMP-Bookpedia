package com.plcoding.bookpedia.book.domain

data class Book(
    val id: String,
    val title: String,
    val imageUrl: String,
    val authors: List<String>,
    val description: String,
    val language: List<String>,
    val firstPublishedDate: String?,
    val ratingAverage: Double?,
    val ratingCount: Int?,
    val numPages: Int?,
    val numEditions: Int?,

)

val books = (1..10).map {
    Book(
        id = it.toString(),
        title = "Book $it",
        imageUrl = "https://s3.amazonaws.com/books/$it",
        authors = listOf("jotasvec"),
        description = """About Book $it""",
        firstPublishedDate = null,
        ratingAverage = 4.123,
        language = listOf("en"),
        ratingCount = null,
        numPages = 100,
        numEditions = 1
    )
}