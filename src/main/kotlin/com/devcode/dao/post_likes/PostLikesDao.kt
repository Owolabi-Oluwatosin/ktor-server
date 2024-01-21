package com.devcode.dao.post_likes

interface PostLikesDao {
    suspend fun addLike(postId: Long, userId: Long): Boolean
    suspend fun removeLike(postId: Long, userId: Long): Boolean
    suspend fun isPostLikeByUser(postId: Long, userId: Long): Boolean
}