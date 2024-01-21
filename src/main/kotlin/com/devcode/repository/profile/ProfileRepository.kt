package com.devcode.repository.profile

import com.devcode.model.ProfileResponse
import com.devcode.model.UpdateUserParams
import com.devcode.util.Responce

interface ProfileRepository {
    suspend fun getUserById(userId: Long, currentUserId: Long): Responce<ProfileResponse>
    suspend fun updateUser(updateUserParams: UpdateUserParams): Responce<ProfileResponse>
}