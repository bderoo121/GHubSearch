package com.bderoo.ghubsearch.screens.search

import androidx.lifecycle.Observer
import com.bderoo.ghubsearch.R
import com.bderoo.ghubsearch.base.BaseViewModelTest
import com.bderoo.ghubsearch.model.Repo
import com.bderoo.ghubsearch.service.GitHubService
import com.bderoo.ghubsearch.util.StringResource
import com.bderoo.ghubsearch.util.createRepo
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.rxjava3.core.Single
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

class SearchViewModelTest : BaseViewModelTest() {
    @MockK
    lateinit var gitHubService: GitHubService

    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = SearchViewModel(gitHubService)
    }

    @Test
    fun `Search Button text updates with search input`() {
        viewModel.searchButtonText.test {
            assertEquals(StringResource(R.string.search_button), viewModel.searchButtonText.value)

            viewModel.onOrganizationTextChanged("Test")
            assertEquals(
                StringResource(R.string.search_button_with_text, "Test"),
                viewModel.searchButtonText.value
            )

            viewModel.onOrganizationTextChanged("T")
            assertEquals(
                StringResource(R.string.search_button_with_text, "T"),
                viewModel.searchButtonText.value
            )

            viewModel.onOrganizationTextChanged("")
            assertEquals(StringResource(R.string.search_button), viewModel.searchButtonText.value)
        }
    }

    @Test
    fun `Loading state is shown first when searching`() {
        every { gitHubService.getPopularReposByOrg(any()) } returns Single.just(listOf(createRepo()))

        val loadingStates = mutableListOf<Boolean>()
        val observer = Observer<Boolean> { isLoading -> loadingStates.add(isLoading) }
        viewModel.showLoadingState.test(observer) {
            assertEquals(false, viewModel.showLoadingState.value)
            viewModel.onSearchPressed()

            assertEquals(listOf(false, true, false), loadingStates)
        }
    }

    @Test
    fun `Loading state is shown before receiving an error`() {
        every { gitHubService.getPopularReposByOrg(any()) } returns Single.error(Throwable("Unknown Repo Name"))

        val loadingStates = mutableListOf<Boolean>()
        val observer = Observer<Boolean> { isLoading -> loadingStates.add(isLoading) }
        viewModel.showLoadingState.test(observer) {
            assertEquals(false, viewModel.showLoadingState.value)
            viewModel.onSearchPressed()

            assertEquals(listOf(false, true, false), loadingStates)
        }
    }

    @Test
    fun `Error state is never shown when successfully fetching repos`() {
        every { gitHubService.getPopularReposByOrg(any()) } returns Single.just(listOf(createRepo()))

        var wasErrorShown = false
        val observer = Observer<Boolean> { isError -> wasErrorShown = wasErrorShown || isError }
        viewModel.showErrorState.test(observer) {
            assertEquals(false, viewModel.showErrorState.value)
            viewModel.onSearchPressed()

            assertFalse(wasErrorShown)
        }
    }

    @Test
    fun `Error state is shown when receiving an error`() {
        every { gitHubService.getPopularReposByOrg(any()) } returns Single.error(Throwable("Unknown Repo Name"))

        val errorStates = mutableListOf<Boolean>()
        val observer = Observer<Boolean> { isError -> errorStates.add(isError) }
        viewModel.showErrorState.test(observer) {
            assertEquals(false, viewModel.showErrorState.value)
            viewModel.onSearchPressed()

            assertEquals(listOf(false, false, true), errorStates)
        }
    }

    @Test
    fun `Clear error state when closing the error modal`() {
        every { gitHubService.getPopularReposByOrg(any()) } returns Single.error(Throwable("Unknown Repo Name"))

        viewModel.showErrorState.test {
            assertEquals(false, viewModel.showErrorState.value)

            viewModel.onSearchPressed()
            assertEquals(true, viewModel.showErrorState.value)

            viewModel.onCloseErrorModal()
            assertEquals(false, viewModel.showErrorState.value)
        }
    }

    @Test
    fun `Search result description when there are different numbers of results`() {
        val allResults = List(8) { i -> createRepo(id = i) }
        every { gitHubService.getPopularReposByOrg(any()) } returnsMany listOf(
            Single.just(emptyList()),
            Single.just(allResults.take(1)),
            Single.just(allResults.take(2)),
            Single.just(allResults.take(3)),
            Single.just(allResults.take(4)),
            Single.just(allResults)
        )

        viewModel.searchResultDescription.test {
            viewModel.onSearchPressed()
            assertEquals(
                StringResource.plural(R.plurals.search_results_few, 0, 0),
                viewModel.searchResultDescription.value
            )

            viewModel.onSearchPressed()
            assertEquals(
                StringResource.plural(R.plurals.search_results_few, 1, 1),
                viewModel.searchResultDescription.value
            )

            viewModel.onSearchPressed()
            assertEquals(
                StringResource.plural(R.plurals.search_results_few, 2, 2),
                viewModel.searchResultDescription.value
            )

            viewModel.onSearchPressed()
            assertEquals(
                StringResource.plural(R.plurals.search_results_few, 3, 3),
                viewModel.searchResultDescription.value
            )

            viewModel.onSearchPressed()
            assertEquals(
                StringResource(R.string.search_results_many, 4),
                viewModel.searchResultDescription.value
            )

            viewModel.onSearchPressed()
            assertEquals(
                StringResource(R.string.search_results_many, 8),
                viewModel.searchResultDescription.value
            )
        }
    }

    @Test
    fun `Display up to 3 repos`() {
        val allResults = List(8) { i -> createRepo(id = i) }
        every { gitHubService.getPopularReposByOrg(any()) } returnsMany listOf(
            Single.just(emptyList()),
            Single.just(allResults.take(1)),
            Single.just(allResults.take(3)),
            Single.just(allResults.take(4)),
            Single.just(allResults.subList(2, 6)),
            Single.just(allResults)
        )

        viewModel.displayedRepos.test {
            viewModel.onSearchPressed()
            assertEquals(emptyList<Repo>(), viewModel.displayedRepos.value)

            viewModel.onSearchPressed()
            assertEquals(allResults.take(1), viewModel.displayedRepos.value)

            viewModel.onSearchPressed()
            assertEquals(allResults.take(3), viewModel.displayedRepos.value)

            viewModel.onSearchPressed()
            assertEquals(allResults.take(3), viewModel.displayedRepos.value)

            viewModel.onSearchPressed()
            assertEquals(allResults.subList(2, 5), viewModel.displayedRepos.value)

            viewModel.onSearchPressed()
            assertEquals(allResults.take(3), viewModel.displayedRepos.value)
        }
    }

    @Test
    fun `Open webpage when a repo is selected`() {
        val repo1 = createRepo(url = "www.testUrl.com?id=1")
        val repo2 = createRepo(url = "www.adifferenturl.io/some/path2")
        val repo3 = createRepo(url = "www.zombo.com")

        every { gitHubService.getPopularReposByOrg(any()) } returnsMany listOf(
            Single.just(listOf(repo1, repo2)),
            Single.just(listOf(repo3))
        )

        viewModel.openWebpageEvent.test {
            viewModel.onSearchPressed()
            viewModel.onRepoSelected(repo1)
            assertEquals("www.testUrl.com?id=1", viewModel.openWebpageEvent.value)

            viewModel.onRepoSelected(repo2)
            assertEquals("www.adifferenturl.io/some/path2", viewModel.openWebpageEvent.value)

            viewModel.onSearchPressed()
            viewModel.onRepoSelected(repo3)
            assertEquals("www.zombo.com", viewModel.openWebpageEvent.value)
        }
    }
}