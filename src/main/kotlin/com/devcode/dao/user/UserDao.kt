package com.devcode.dao.user

import com.devcode.model.SignUpParams
import com.devcode.model.User

interface UserDao {
    suspend fun insert(params: SignUpParams): User?
    suspend fun findByEmail(email: String): User?
}