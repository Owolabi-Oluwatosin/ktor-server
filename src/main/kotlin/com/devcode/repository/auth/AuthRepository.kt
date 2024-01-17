package com.devcode.repository.auth

import com.devcode.model.AuthResponce
import com.devcode.model.SignInParams
import com.devcode.model.SignUpParams
import com.devcode.util.Responce

interface AuthRepository {
    suspend fun signUp(params: SignUpParams): Responce<AuthResponce>
    suspend fun signIn(params: SignInParams): Responce<AuthResponce>
}