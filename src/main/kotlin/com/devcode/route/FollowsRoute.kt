package com.devcode.route

import com.devcode.model.AuthResponce
import com.devcode.model.FollowAndUnfollowResponse
import com.devcode.model.FollowsParams
import com.devcode.repository.follows.FollowsRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Routing.followsRouting(){
    val repository by inject<FollowsRepository>()

    authenticate {
        route(path = "/follow"){
            post {
                val params = call.receiveNullable<FollowsParams>()
                if(params == null){
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = FollowAndUnfollowResponse(
                            success = false,
                            message = "Oops, something went wrong!"
                        )
                    )
                    return@post
                }

                val result  = if(params.isFollowing){
                    repository.followUser(follower = params.follower, following = params.following)
                }else{
                    repository.unfollowUser(follower = params.follower, following = params.following)
                }

                call.respond(
                    status = result.code,
                    message = result.data
                )
            }
        }
    }
}