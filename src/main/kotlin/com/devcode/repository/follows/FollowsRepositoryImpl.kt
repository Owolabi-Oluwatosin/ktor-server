package com.devcode.repository.follows

import com.devcode.dao.follows.FollowsDao
import com.devcode.dao.user.UserDao
import com.devcode.model.FollowAndUnfollowResponse
import com.devcode.util.Responce
import io.ktor.http.*

class FollowsRepositoryImpl(
    private val userDao: UserDao,
    private val followsDao: FollowsDao
) : FollowsRepository {
    override suspend fun followUser(follower: Long, following: Long): Responce<FollowAndUnfollowResponse> {
        return if(followsDao.isAlreadyFollowing(follower, following)){
            Responce.Error(
                code = HttpStatusCode.Forbidden,
                data =  FollowAndUnfollowResponse(
                    success = false,
                    message = "You are already following this user"
                )
            )
        }else{
            val success = followsDao.followUser(follower, following)
            if (success){
                userDao.updateFollowsCount(follower, following, isFollowing = true)
                Responce.Success(
                    data =  FollowAndUnfollowResponse(
                        success = true,
                        message = "You are now following this user"
                    )
                )
            }else{
                Responce.Error(
                    code = HttpStatusCode.InternalServerError,
                    data =  FollowAndUnfollowResponse(
                        success = false,
                        message = "Oops, something went wrong on our side, please try again later!"
                    )
                )
            }
        }
    }

    override suspend fun unfollowUser(follower: Long, following: Long): Responce<FollowAndUnfollowResponse> {
        val success = followsDao.unfollowUser(follower, following)
        return if(success){
            userDao.updateFollowsCount(follower, following, isFollowing = false)
            Responce.Success(
                data =  FollowAndUnfollowResponse(
                    success = true,
                    message = "You have unfollow this user"
                )
            )
        }else{
            Responce.Error(
                code = HttpStatusCode.InternalServerError,
                data =  FollowAndUnfollowResponse(
                    success = false,
                    message = "Oops, something went wrong on our side, please try again later!"
                )
            )
        }
    }
}