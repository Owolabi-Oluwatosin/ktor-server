package com.devcode.repository.post

import com.devcode.model.PostResponce
import com.devcode.model.PostTextParams
import com.devcode.model.PostsResponce
import com.devcode.util.Responce

interface PostRepository {
    suspend fun createPost(imageUrl: String, postTextParams: PostTextParams): Responce<PostResponce>
    suspend fun getFeedPosts(userId: Long, pageNumber: Int, pageSize: Int): Responce<PostsResponce>
    suspend fun getPostByUser(
        postsOwnerId: Long,
        currentUserId: Long,
        pageNumber: Int,
        pageSize: Int
    ): Responce<PostsResponce>

    suspend fun getPost(postId: Long, currentUserId: Long): Responce<PostResponce>
    suspend fun deletePost(postId: Long): Responce<PostResponce>
}