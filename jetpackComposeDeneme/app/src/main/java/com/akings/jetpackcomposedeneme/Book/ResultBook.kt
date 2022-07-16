package com.akings.jetpackcomposedeneme.Book

sealed class ResultBook<T>(
    val data: T? = null,
    val message: String? = null
){

    class Success<T>(data: T?) : ResultBook<T>(data)

    class Error<T>(message: String?, data: T? = null) : ResultBook<T>(data, message)

    class Loading<T>(data: T? = null) : ResultBook<T>(data)
}