package com.bderoo.ghubsearch.service

import com.bderoo.ghubsearch.api.GitHubApi
import com.bderoo.ghubsearch.model.Repo
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface GitHubService {
    fun getPopularReposByOrg(orgName: String): Single<List<Repo>>
}

class GitHubServiceImpl @Inject constructor(
    private val gitHubApi: GitHubApi
) : GitHubService {
    // TODO Rework for multiple fetches for orgs whose repo counts exceed the search max
    override fun getPopularReposByOrg(orgName: String): Single<List<Repo>> {
        return gitHubApi.getReposByOrg(orgName)
            .map { repoList ->
                repoList.sortedByDescending { repo -> repo.stargazers_count }
            }
    }
}