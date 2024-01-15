package com.devcode.di

import com.devcode.dao.user.UserDao
import com.devcode.dao.user.UserDaoImpl
import com.devcode.repository.user.UserRepository
import com.devcode.repository.user.UserRepositoryImpl
import org.koin.dsl.module

val appModule = module {
    single<UserRepository> { UserRepositoryImpl(get())}
    single<UserDao> { UserDaoImpl()}
}