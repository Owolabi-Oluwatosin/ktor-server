package com.devcode.di

import com.devcode.dao.follows.FollowsDao
import com.devcode.dao.follows.FollowsDaoImpl
import com.devcode.dao.user.UserDao
import com.devcode.dao.user.UserDaoImpl
import com.devcode.repository.auth.AuthRepository
import com.devcode.repository.auth.AuthRepositoryImpl
import com.devcode.repository.follows.FollowsRepository
import com.devcode.repository.follows.FollowsRepositoryImpl
import org.koin.dsl.module

val appModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get())}
    single<UserDao> { UserDaoImpl()}
    single<FollowsRepository> { FollowsRepositoryImpl(get(), get()) }
    single<FollowsDao> { FollowsDaoImpl()}
}