package com.bderoo.ghubsearch.service

import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

class GitHubService {
    private val gitHubApi: GitHubApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(GitHubApi::class.java)
    }

    // TODO Rework for multiple fetches for orgs whose repo counts exceed the search max
    fun getPopularReposByOrg(orgName: String): Single<List<Repo>> {
        return gitHubApi.getReposByOrg(orgName)
            .map { repoList ->
                val sortedRepos = repoList.sortedByDescending { repo -> repo.stargazers_count }
                print("Sorted RepoList: $sortedRepos")
                sortedRepos
            }
    }
}

interface GitHubApi {
    @Headers("Accept: application/vnd.github.v3+json")
    @GET("orgs/{org}/repos")
    fun getReposByOrg(
        @Path("org") orgName: String,
        @Query("per_page") perPage: Int = 100,
    ): Single<List<Repo>>
}

// TODO move this into a model directory?
data class Repo(
    val id: Int,
    val name: String,
    val full_name: String,
    val html_url: String,
    val description: String,
    val forks_count: Int,
    val stargazers_count: Int,
    val size: Int,
    val topics: List<String>,
    val archived: Boolean,
    val disabled: Boolean,
)