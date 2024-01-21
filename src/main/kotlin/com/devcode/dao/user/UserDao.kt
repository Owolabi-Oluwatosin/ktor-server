package com.devcode.dao.user

import com.devcode.model.SignUpParams

interface UserDao {
    suspend fun insert(params: SignUpParams): UserRow?
    suspend fun findByEmail(email: String): UserRow?
    suspend fun findById(userId: Long): UserRow?
    suspend fun updateUser(userId: Long, name: String, bio: String, imageUrl: String?): Boolean
    suspend fun updateFollowsCount(follower: Long, following: Long, isFollowing: Boolean): Boolean
}