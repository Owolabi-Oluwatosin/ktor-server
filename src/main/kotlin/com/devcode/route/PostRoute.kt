package com.devcode.route

import com.devcode.model.PostResponce
import com.devcode.model.PostTextParams
import com.devcode.model.PostsResponce
import com.devcode.repository.post.PostRepository
import com.devcode.util.Constants
import com.devcode.util.getLongParameter
import com.devcode.util.saveFile
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject
import java.io.File

fun Routing.postRouting(){
    val repository by inject<PostRepository>()
    authenticate {
        route(path = "/post") {
            post(path = "create") {
                var fileName = ""
                var postTextParams: PostTextParams? = null
                val multiPartData = call.receiveMultipart()

                multiPartData.forEachPart { partData ->
                    when(partData){
                        is PartData.FileItem -> {
                            fileName = partData.saveFile(folderPath = Constants.POST_IMAGES_FOLDER_PATH)
                        }
                        is PartData.FormItem -> {
                            if (partData.name == "post_data"){
                                postTextParams = Json.decodeFromString(partData.value)
                            }
                        }
                        else -> {}
                    }
                    partData.dispose()
                }

                val imageUrl = "${Constants.BASE_URL}${Constants.POST_IMAGES_FOLDER}$fileName"
                if (postTextParams == null){
                    File("${Constants.POST_IMAGES_FOLDER_PATH}/$fileName").delete()
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = PostResponce(
                            success = false,
                            message = "Could not parse post data"
                        )
                    )
                }else{
                    val result = repository.createPost(imageUrl, postTextParams!!)
                    call.respond(status = result.code, message = result.data)
                }
            }
            get(path = "/{postId}"){
                try {
                    val postId = call.getLongParameter(name = "postId")
                    val currentUserId = call.getLongParameter(name = "currentUserId", isQueryParameter = true)

                    val result = repository.getPost(postId = postId, currentUserId = currentUserId)
                    call.respond(status = result.code, message = result.data)
                }catch (badRequestError: BadRequestException){
                    return@get
                }catch (anyError: Throwable){
                    call.respond(
                        status = HttpStatusCode.InternalServerError,
                        message = PostResponce(
                            success = false,
                            message = "An unexpected error has occurred, try again later!"
                        )
                    )
                }
            }
            delete(path = "/{postId}"){
                try {
                    val postId = call.getLongParameter(name = "postId")
                    val result = repository.deletePost(postId = postId)
                    call.respond(
                        status = result.code,
                        message = result.data
                    )
                }catch (badRequestError: BadRequestException){
                    return@delete
                }catch (anyError: Throwable){
                    call.respond(
                        status = HttpStatusCode.InternalServerError,
                        message = PostResponce(
                            success = false,
                            message = "An unexpected error has occurred, try again later!"
                        )
                    )
                }
            }
        }

        route(path = "/posts"){
            get(path = "/feed"){
                try {
                    val currentUserId = call.getLongParameter(name = "currentUserId", isQueryParameter = true)
                    val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 0
                    val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: Constants.DEFAULT_PAGE_SIZE

                    val result = repository.getFeedPosts(pageNumber = page, pageSize = limit, userId = currentUserId)
                    call.respond(status = result.code, message = result.data)
                }catch (badRequestError: BadRequestException){
                    return@get
                }catch (anyError: Throwable){
                    call.respond(
                        status = HttpStatusCode.InternalServerError,
                        message = PostsResponce(
                            success = false,
                            message = "An unexpected error has occurred, try again later!"
                        )
                    )
                }
            }

            get(path = "/{userId}"){
                try {
                    val postsOwnerId = call.getLongParameter(name = "userId")
                    val currentUserId = call.getLongParameter(
                        name = "currentUserId",
                        isQueryParameter = true
                    )
                    val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 0
                    val limit = call.request.queryParameters[
                        "limit"
                    ]?.toIntOrNull() ?: Constants.DEFAULT_PAGE_SIZE

                    val result = repository.getPostByUser(
                        postsOwnerId = postsOwnerId,
                        currentUserId = currentUserId,
                        pageNumber = page,
                        pageSize = limit,
                    )

                    call.respond(
                        status = result.code,
                        message = result.data
                    )

                }catch (badRequestError: BadRequestException){
                    return@get
                }catch (anyError: Throwable){
                    call.respond(
                        status = HttpStatusCode.InternalServerError,
                        message = PostsResponce(
                            success = false,
                            message = "An unexpected error has occurred, try again later!"
                        )
                    )
                }
            }
        }
    }
}