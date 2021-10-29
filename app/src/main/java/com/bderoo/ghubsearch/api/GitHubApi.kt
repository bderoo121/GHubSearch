package com.bderoo.ghubsearch.api

import com.bderoo.ghubsearch.model.Repo
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApi {
    @Headers("Accept: application/vnd.github.v3+json")
    @GET("orgs/{org}/repos")
    fun getReposByOrg(
        @Path("org") orgName: String,
        @Query("per_page") perPage: Int = 100,
    ): Single<List<Repo>>
}