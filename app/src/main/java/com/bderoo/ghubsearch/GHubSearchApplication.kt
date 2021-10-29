package com.bderoo.ghubsearch

import android.app.Application
import com.bderoo.ghubsearch.service.GitHubApi
import com.bderoo.ghubsearch.service.GitHubService
import com.bderoo.ghubsearch.service.GitHubServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@HiltAndroidApp
class GHubSearchApplication : Application() {

    @Module
    @InstallIn(SingletonComponent::class)
    object ApiModule {

        @Singleton
        @Provides
        fun bindGitHubRetrofit(): GitHubApi = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(GitHubApi::class.java)
    }

    @Module
    @InstallIn(SingletonComponent::class)
    object ServiceModule {

        @Singleton
        @Provides
        fun bindGitHubService(api: GitHubApi): GitHubService = GitHubServiceImpl(api)
    }
}