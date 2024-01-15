package com.devcode.util

import io.ktor.http.*

sealed class Responce<T>(
    val code: HttpStatusCode = HttpStatusCode.OK,
    val data: T
){
    class Success<T>(data: T): Responce<T>(data = data)
    class Error<T>(
        code: HttpStatusCode,
        data: T
    ):Responce<T>(
        code, data
    )
}