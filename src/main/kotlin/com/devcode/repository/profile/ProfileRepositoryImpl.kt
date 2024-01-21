package com.devcode.repository.profile

import com.devcode.dao.follows.FollowsDao
import com.devcode.dao.user.UserDao
import com.devcode.dao.user.UserRow
import com.devcode.model.Profile
import com.devcode.model.ProfileResponse
import com.devcode.model.UpdateUserParams
import com.devcode.util.Responce
import io.ktor.http.*

class ProfileRepositoryImpl(
    private val userDao: UserDao,
    private val followsDao: FollowsDao
) : ProfileRepository {
    override suspend fun getUserById(userId: Long, currentUserId: Long): Responce<ProfileResponse> {
        val userRow = userDao.findById(userId = userId)

        return if (userRow == null){
            Responce.Error(
                code = HttpStatusCode.NotFound,
                data = ProfileResponse(success = false, message = "Could not finduser with id: $userId")
            )
        }else{
            val isFollowing = followsDao.isAlreadyFollowing(follower = currentUserId, following = userId)
            val isOwnProfile = userId == currentUserId
            Responce.Success(
                data = ProfileResponse(
                    success = true,
                    profile = toProfile(userRow, isFollowing, isOwnProfile)
                )
            )
        }
    }

    override suspend fun updateUser(updateUserParams: UpdateUserParams): Responce<ProfileResponse> {
        val userExist = userDao.findById(userId = updateUserParams.userId) != null

        if (userExist){
            val userUpdated = userDao.updateUser(
                    userId = updateUserParams.userId,
                    name = updateUserParams.name,
                    bio = updateUserParams.bio,
                    imageUrl = updateUserParams.imageUrl
            )
            return if (userUpdated){
                Responce.Success(
                    data = ProfileResponse(success = true)
                )
            }else{
                Responce.Error(
                    code = HttpStatusCode.Conflict,
                    data = ProfileResponse(
                        success = false,
                        message = "Could not update user: ${updateUserParams.userId}"
                    )
                )
            }
        }else{
            return Responce.Error(
                code = HttpStatusCode.NotFound,
                data = ProfileResponse(
                    success = false,
                    message = "Could not find user: ${updateUserParams.userId}"
                )
            )
        }
    }

    private fun toProfile(userRow: UserRow, isFollowing: Boolean, isOwnProfile: Boolean): Profile{
        return Profile(
            id = userRow.id,
            name = userRow.name,
            bio = userRow.bio,
            imageUrl = userRow.imageUrl,
            followersCount = userRow.followersCount,
            followingCount = userRow.followingCount,
            isFollowing = isFollowing,
            isOwnProfile = isOwnProfile
        )
    }
}