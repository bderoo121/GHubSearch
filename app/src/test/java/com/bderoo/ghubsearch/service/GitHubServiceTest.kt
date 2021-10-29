package com.bderoo.ghubsearch.service

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bderoo.ghubsearch.api.GitHubApi
import com.bderoo.ghubsearch.util.createRepo
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class GitHubServiceTest {
    @MockK
    lateinit var gitHubApi: GitHubApi

    private lateinit var service: GitHubServiceImpl

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        service = GitHubServiceImpl(gitHubApi)
    }

    @Test
    fun `Get repos by organization name`() {
        val repoList1 = listOf(createRepo(id = 1), createRepo(id = 2))
        val repoList2 = listOf(createRepo(id = 3), createRepo(id = 4))

        every { gitHubApi.getReposByOrg("Org1") } returns Single.just(repoList1)
        every { gitHubApi.getReposByOrg("Org2") } returns Single.just(repoList2)

        service.getPopularReposByOrg("Org1").test().assertValue(repoList1)
        service.getPopularReposByOrg("Org2").test().assertValue(repoList2)
    }

    @Test
    fun `Get repos by organization name, sorted by populartity`() {
        val repo1 = createRepo(id = 1, stars = 400)
        val repo2 = createRepo(id = 2, stars = 4)
        val repo3 = createRepo(id = 3, stars = 40)
        val repo4 = createRepo(id = 4, stars = 4000)
        val repoList = listOf(
            repo1, repo2, repo3, repo4
        )
        every { gitHubApi.getReposByOrg(any()) } returns Single.just(repoList)

        service.getPopularReposByOrg("Org").test().assertValue(
            listOf(repo4, repo1, repo3, repo2)
        )
    }
}