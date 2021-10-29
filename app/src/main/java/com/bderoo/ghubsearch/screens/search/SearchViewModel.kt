package com.bderoo.ghubsearch.screens.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.bderoo.ghubsearch.R
import com.bderoo.ghubsearch.base.BaseViewModel
import com.bderoo.ghubsearch.model.NetworkState
import com.bderoo.ghubsearch.model.Repo
import com.bderoo.ghubsearch.service.GitHubService
import com.bderoo.ghubsearch.util.SingleLiveEvent
import com.bderoo.ghubsearch.util.StringResource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val gitHubService: GitHubService
) : BaseViewModel() {
    private val organizationName = MutableLiveData("")
    private val repoList = MutableLiveData<List<Repo>>(emptyList())
    private val networkState = MutableLiveData(NetworkState.NONE)

    val openWebpageEvent = SingleLiveEvent<String>()

    val searchButtonText: LiveData<StringResource> = Transformations.map(organizationName) { name ->
        if (name.isNullOrBlank()) StringResource(R.string.search_button)
        else StringResource(R.string.search_button_with_text, name)
    }
    val showLoadingState: LiveData<Boolean> = Transformations.map(networkState) { state ->
        state == NetworkState.LOADING
    }
    val showErrorState: LiveData<Boolean> = Transformations.map(networkState) { state ->
        state == NetworkState.ERROR
    }
    val searchResultDescription: LiveData<StringResource> = Transformations.map(repoList) { repos ->
        val repoCount = repos.orEmpty().size
        if (repoCount <= 3) StringResource.plural(
            R.plurals.search_results_few, repoCount, repoCount
        ) else StringResource(R.string.search_results_many, repoCount)
    }
    val displayedRepos: LiveData<List<Repo>> = Transformations.map(repoList) { repos ->
        repos.take(3)
    }

    fun onOrganizationTextChanged(text: String) {
        organizationName.value = text
    }

    fun onSearchPressed() {
        networkState.value = NetworkState.LOADING
        repoList.value = emptyList()
        val orgName = organizationName.value.orEmpty()

        disposables.add(
            gitHubService.getPopularReposByOrg(orgName)
                .subscribe(
                    { repos ->
                        networkState.postValue(NetworkState.NONE)
                        repoList.postValue(repos)
                    },
                    { networkState.postValue(NetworkState.ERROR) }
                )
        )
    }

    fun onCloseErrorModal() {
        if (networkState.value == NetworkState.ERROR) {
            networkState.value = NetworkState.NONE
        }
    }

    fun onRepoSelected(repo: Repo) {
        openWebpageEvent.postValue(repo.html_url)
    }
}
