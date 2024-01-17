package com.devcode.repository.follows

import com.devcode.model.FollowAndUnfollowResponse
import com.devcode.util.Responce

interface FollowsRepository {
    suspend fun followUser(follower: Long, following: Long): Responce<FollowAndUnfollowResponse>
    suspend fun unfollowUser(follower: Long, following: Long): Responce<FollowAndUnfollowResponse>
}