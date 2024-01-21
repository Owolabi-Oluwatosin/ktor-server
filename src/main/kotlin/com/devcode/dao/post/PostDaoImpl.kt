package com.devcode.dao.post

import com.devcode.dao.DatabaseFactory
import com.devcode.dao.DatabaseFactory.dbQuery
import com.devcode.dao.follows.FollowsTable
import com.devcode.dao.user.UserTable
import com.devcode.security.hashPassword
import com.devcode.util.IdGenerator
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList

class PostDaoImpl : PostDao {
    override suspend fun createPost(caption: String, imageUrl: String, userId: Long): Boolean {
        return dbQuery {
            val insertStatement = PostTable.insert {
                it[postId] = IdGenerator.generateId()
                it[PostTable.caption] = caption
                it[PostTable.imageUrl] = imageUrl
                it[likesCount] = 0
                it[commentsCount] = 0
                it[PostTable.userId] = userId
            }
            insertStatement.resultedValues?.singleOrNull() != null
        }
    }

    override suspend fun getFeedsPost(
        userId: Long,
        follows: List<Long>,
        pageNumber: Int,
        pageSize: Int
    ): List<PostRow> {
        return dbQuery {
            if (follows.size > 1){
                PostTable.join(
                    otherTable = UserTable,
                    onColumn = PostTable.userId,
                    otherColumn = UserTable.id,
                    joinType = JoinType.INNER
                ).select(where = PostTable.userId inList follows)
                    .orderBy(column = PostTable.createAt, SortOrder.DESC)
                    .limit(n = pageSize, offset = ((pageNumber - 1) * pageSize).toLong())
                    .map { toPostRow(it) }
            }else{
                PostTable.join(
                    otherTable = UserTable,
                    onColumn = PostTable.userId,
                    otherColumn = UserTable.id,
                    joinType = JoinType.INNER
                ).selectAll()
                    .orderBy(column = PostTable.likesCount, SortOrder.DESC)
                    .limit(n = pageSize, offset = ((pageNumber - 1) * pageSize).toLong())
                    .map { toPostRow(it) }
            }
        }
    }

    override suspend fun getPostByUser(userId: Long, pageNumber: Int, pageSize: Int): List<PostRow> {
        return dbQuery {
            PostTable.join(
                otherTable = UserTable,
                onColumn = PostTable.userId,
                otherColumn = UserTable.id,
                joinType = JoinType.INNER
            ).select(where = PostTable.userId eq userId)
                .orderBy(column = PostTable.createAt, SortOrder.DESC)
                .limit(n = pageSize, offset = ((pageNumber - 1) * pageSize).toLong())
                .map { toPostRow(it) }
        }
    }

    override suspend fun getPost(postId: Long): PostRow? {
        return dbQuery {
            PostTable.join(
                otherTable = UserTable,
                onColumn = PostTable.userId,
                otherColumn = UserTable.id,
                joinType = JoinType.INNER
            ).select { PostTable.postId eq postId }
                .singleOrNull()?.let { toPostRow(it) }
        }
    }

    override suspend fun deletePost(postId: Long): Boolean {
        return dbQuery {
            PostTable.deleteWhere { PostTable.postId eq postId} > 0
        }
    }
}

private fun toPostRow(row: ResultRow): PostRow{
    return PostRow(
        postId = row[PostTable.postId],
        caption = row[PostTable.caption],
        imageUrl = row[PostTable.caption],
        createAt = row[PostTable.caption].toString(),
        likesCount = row[PostTable.likesCount],
        commentsCount = row[PostTable.commentsCount],
        userId = row[PostTable.userId],
        userName = row[UserTable.name],
        userImageUrl = row[UserTable.imageUrl],
    )
}