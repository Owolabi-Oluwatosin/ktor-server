package com.devcode.model

import kotlinx.serialization.Serializable

@Serializable
data class PostTextParams(
    val caption: String,
    val userId: Long
)

@Serializable
data class Post(
    val postId: Long,
    val caption: String,
    val imageUrl: String,
    val createAt: String,
    val likesCount: Int,
    val commentsCount: Int,
    val userId: Long,
    val userName: String,
    val userImageUrl: String?,
    val isLiked: Boolean,
    val isOwnPost: Boolean
)

@Serializable
data class PostResponce(
    val success: Boolean,
    val post: Post? = null,
    val message: String? = null
)

@Serializable
data class PostsResponce(
    val success: Boolean,
    val posts: List<Post> = listOf(),
    val message: String? = null
)
