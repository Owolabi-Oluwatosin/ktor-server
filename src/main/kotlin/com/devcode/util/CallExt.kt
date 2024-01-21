package com.devcode.util

import com.devcode.model.PostResponce
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*

suspend fun ApplicationCall.getLongParameter(name: String, isQueryParameter: Boolean = false): Long{
     val parameter = if (isQueryParameter){
         request.queryParameters[name]?.toLongOrNull()
     }else{
         parameters[name]?.toLongOrNull()
     } ?: kotlin.run {
         respond(
             status = HttpStatusCode.BadRequest,
             message = PostResponce(
                 success = false,
                 message = "Invalid request, parameter $name is missing!"
             )
         )
         throw BadRequestException("Invalid request, parameter $name is missing!")
     }
    return parameter
}