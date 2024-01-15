package com.devcode.repository.user

import com.devcode.model.AuthResponce
import com.devcode.model.SignInParams
import com.devcode.model.SignUpParams
import com.devcode.util.Responce

interface UserRepository {
    suspend fun signUp(params: SignUpParams): Responce<AuthResponce>
    suspend fun signIn(params: SignInParams): Responce<AuthResponce>
}