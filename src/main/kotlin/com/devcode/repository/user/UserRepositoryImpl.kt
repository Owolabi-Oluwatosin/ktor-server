package com.devcode.repository.user

import com.devcode.dao.user.UserDao
import com.devcode.model.AuthResponce
import com.devcode.model.AuthResponceData
import com.devcode.model.SignInParams
import com.devcode.model.SignUpParams
import com.devcode.plugins.generateToken
import com.devcode.security.hashPassword
import com.devcode.util.Responce
import io.ktor.http.*

class UserRepositoryImpl(
    private val userDao: UserDao
) : UserRepository {
    override suspend fun signUp(params: SignUpParams): Responce<AuthResponce> {
        return if (userAlreadyExist(params.email)){
            Responce.Error(
                code = HttpStatusCode.Conflict,
                data = AuthResponce(
                    errorMessage = "User with this email already exist!"
                )
            )
        }else{
            val insertedUser = userDao.insert(params)
            if (insertedUser == null){
                Responce.Error(
                    code = HttpStatusCode.InternalServerError,
                    data = AuthResponce(
                        errorMessage = "Oops, sorry we could not register the user, try again later!"
                    )
                )
            }else{
                Responce.Success(
                    data = AuthResponce(
                        data = AuthResponceData(
                            id = insertedUser.id,
                            name = insertedUser.name,
                            bio = insertedUser.bio,
                            token = generateToken(params.email),
                        )
                    )
                )
            }
        }
    }

    override suspend fun signIn(params: SignInParams): Responce<AuthResponce> {
        val user = userDao.findByEmail(params.email)
        return if (user == null){
            Responce.Error(
                code = HttpStatusCode.NotFound,
                data = AuthResponce(
                    errorMessage = "Invalid credentials, no user with this email!"
                )
            )
        }else{
            val hashPassword = hashPassword(params.password)
            if (user.password == hashPassword){
                Responce.Success(
                    data = AuthResponce(
                        data = AuthResponceData(
                            id = user.id,
                            name = user.name,
                            bio = user.bio,
                            token = generateToken(params.email),
                        )
                    )
                )
            }else{
                Responce.Error(
                    code = HttpStatusCode.Forbidden,
                    data = AuthResponce(
                        errorMessage = "Invalid credentials, wrong email or password!"
                    )
                )
            }
        }
    }

    private suspend fun userAlreadyExist(email: String): Boolean{
        return userDao.findByEmail(email) != null
    }
}