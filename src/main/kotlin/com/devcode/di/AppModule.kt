package com.devcode.di

import com.devcode.dao.follows.FollowsDao
import com.devcode.dao.follows.FollowsDaoImpl
import com.devcode.dao.post.PostDao
import com.devcode.dao.post.PostDaoImpl
import com.devcode.dao.post_likes.PostLikesDao
import com.devcode.dao.post_likes.PostLikesDaoImpl
import com.devcode.dao.user.UserDao
import com.devcode.dao.user.UserDaoImpl
import com.devcode.repository.auth.AuthRepository
import com.devcode.repository.auth.AuthRepositoryImpl
import com.devcode.repository.follows.FollowsRepository
import com.devcode.repository.follows.FollowsRepositoryImpl
import com.devcode.repository.post.PostRepository
import com.devcode.repository.post.PostRepositoryImpl
import com.devcode.repository.profile.ProfileRepository
import com.devcode.repository.profile.ProfileRepositoryImpl
import org.koin.dsl.module

val appModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get())}
    single<UserDao> { UserDaoImpl()}
    single<FollowsRepository> { FollowsRepositoryImpl(get(), get()) }
    single<FollowsDao> { FollowsDaoImpl()}
    single<PostLikesDao> { PostLikesDaoImpl() }
    single<PostDao> { PostDaoImpl() }
    single<PostRepository> { PostRepositoryImpl(get(), get(), get()) }
    single<ProfileRepository> { ProfileRepositoryImpl(get(), get()) }
}