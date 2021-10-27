package com.bderoo.ghubsearch.screens.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.bderoo.ghubsearch.R
import com.bderoo.ghubsearch.base.BaseViewModel
import com.bderoo.ghubsearch.service.GitHubService
import com.bderoo.ghubsearch.service.Repo
import com.bderoo.ghubsearch.util.StringResource

class SearchViewModel : BaseViewModel() {
//    TODO: This should be an injected dependency
    private val gitHubService = GitHubService()

    private val organizationName = MutableLiveData("")
    private val repoList = MutableLiveData<List<Repo>>(emptyList())
    private val networkState = MutableLiveData(NetworkState.NONE)

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

    // Do we really need this?
    val showEmptyState = MediatorLiveData<Boolean>().apply {
        val combiner = {
            this.value = networkState.value == NetworkState.NONE && repoList.value.isNullOrEmpty()
        }
        addSource(networkState) { combiner() }
        addSource(repoList) { combiner() }
    }

    fun onOrganizationTextChanged(text: String) {
        organizationName.value = text
    }

    fun onSearchPressed() {
        networkState.value = NetworkState.LOADING
        repoList.value = emptyList()
        val orgName = organizationName.value.orEmpty()

        disposables.add(
            gitHubService.getReposByOrg(orgName)
                .subscribe(
                    { repos ->
                        println(repos.toString())
                        networkState.postValue(NetworkState.NONE)
                        repoList.postValue(repos)
                    },
                    { networkState.postValue(NetworkState.ERROR) }
                )
        )
    }

    fun onCloseErrorModal() {
        networkState.value = NetworkState.NONE
    }
}

// TODO: Extract to a general-use models file
enum class NetworkState {
    NONE,
    LOADING,
    ERROR
}