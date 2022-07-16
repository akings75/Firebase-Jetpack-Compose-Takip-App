package com.akings.jetpackcomposedeneme.Book

data class BookState(val isLoading: Boolean = false,
                          val book: Book? = null,
                          val error: String = "")