package com.devcode.repository.post

import com.devcode.dao.follows.FollowsDao
import com.devcode.dao.post.PostDao
import com.devcode.dao.post.PostRow
import com.devcode.dao.post.PostTable
import com.devcode.dao.post_likes.PostLikesDao
import com.devcode.dao.user.UserDao
import com.devcode.dao.user.UserTable
import com.devcode.model.Post
import com.devcode.model.PostResponce
import com.devcode.model.PostTextParams
import com.devcode.model.PostsResponce
import com.devcode.util.Responce
import io.ktor.http.*

class PostRepositoryImpl(
    private val postDao: PostDao,
    private val followsDao: FollowsDao,
    private val postLikesDao: PostLikesDao
) : PostRepository {
    override suspend fun createPost(imageUrl: String, postTextParams: PostTextParams): Responce<PostResponce> {
        val postIsCreated = postDao.createPost(
            caption = postTextParams.caption,
            imageUrl = imageUrl,
            userId = postTextParams.userId
        )
        return if (postIsCreated){
            Responce.Success(
                data = PostResponce(success = true, message = "Post created successfully!")
            )
        }else{
            Responce.Error(
                code = HttpStatusCode.InternalServerError,
                data = PostResponce(
                    success = false,
                    message = "Oops, post couldn't be create, please try again later!"
                )
            )
        }
    }

    override suspend fun getFeedPosts(userId: Long, pageNumber: Int, pageSize: Int): Responce<PostsResponce> {
        val followingUsers = followsDao.getAllFollowing(userId = userId).toMutableList()
        followingUsers.add(userId)
        val postsRows = postDao.getFeedsPost(
            userId = userId,
            follows = followingUsers,
            pageNumber = pageNumber,
            pageSize = pageSize
        )
        val posts = postsRows.map {
            toPost(
                postRow = it,
                isPostLiked = postLikesDao.isPostLikeByUser(postId = it.postId, userId = userId),
                isOwnPost = it.userId == userId
            )
        }
        return Responce.Success(
            data = PostsResponce(
                success = true,
                posts = posts
            )
        )
    }

    override suspend fun getPostByUser(
        postsOwnerId: Long,
        currentUserId: Long,
        pageNumber: Int,
        pageSize: Int
    ): Responce<PostsResponce> {
        val postsRows = postDao.getPostByUser(
            userId = postsOwnerId,
            pageNumber = pageNumber,
            pageSize = pageSize
        )
        val posts = postsRows.map {
            toPost(
                postRow = it,
                isPostLiked = postLikesDao.isPostLikeByUser(postId = it.postId, userId = currentUserId),
                isOwnPost = it.userId == currentUserId
            )
        }
        return Responce.Success(
            data = PostsResponce(
                success = true,
                posts = posts
            )
        )
    }

    override suspend fun getPost(postId: Long, currentUserId: Long): Responce<PostResponce> {
        val post = postDao.getPost(postId = postId)
        return if (post == null){
            Responce.Error(
                code = HttpStatusCode.InternalServerError,
                data = PostResponce(
                    success = false,
                    message = "Could not retrieve post from the database!"
                )
            )
        }else{
            val isPostLiked = postLikesDao.isPostLikeByUser(postId, currentUserId)
            val isOwnPost = post.postId == currentUserId
            Responce.Success(
                data = PostResponce(
                    success = true,
                    toPost(post, isPostLiked = isPostLiked, isOwnPost = isOwnPost)
                )
            )
        }
    }

    override suspend fun deletePost(postId: Long): Responce<PostResponce> {
        val postIsDeleted = postDao.deletePost(
            postId = postId
        )
        return if (postIsDeleted){
            Responce.Success(
                data = PostResponce(success = true, message = "Your post have been deleted successfully!")
            )
        }else{
            Responce.Error(
                code = HttpStatusCode.InternalServerError,
                data = PostResponce(
                    success = false,
                    message = "Oops, post couldn't be delete, please try again later!"
                )
            )
        }
    }

    private fun toPost(postRow: PostRow, isPostLiked: Boolean, isOwnPost: Boolean): Post {
        return Post(
            postId = postRow.postId,
            caption = postRow.caption,
            imageUrl = postRow.imageUrl,
            createAt = postRow.createAt,
            likesCount = postRow.likesCount,
            commentsCount = postRow.commentsCount,
            userId = postRow.userId,
            userImageUrl = postRow.userImageUrl,
            userName = postRow.userName,
            isLiked = isPostLiked,
            isOwnPost = isOwnPost
        )
    }
}