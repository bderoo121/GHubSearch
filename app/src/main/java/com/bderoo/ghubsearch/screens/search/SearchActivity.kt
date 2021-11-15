package com.bderoo.ghubsearch.screens.search

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.widget.doAfterTextChanged
import com.bderoo.ghubsearch.base.BaseActivity
import com.bderoo.ghubsearch.databinding.ActivitySearchBinding
import com.bderoo.ghubsearch.util.getString
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : BaseActivity<ActivitySearchBinding>() {
    override val inflater: (LayoutInflater) -> ActivitySearchBinding
        get() = ActivitySearchBinding::inflate
    private val viewModel by viewModels<SearchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.openWebpageEvent.observe(this, { url ->
            val builder = CustomTabsIntent.Builder().build()
            builder.launchUrl(this, Uri.parse(url))
        })

        binding.organizationSearch.apply {
            doAfterTextChanged { text ->
                viewModel.onOrganizationTextChanged(text.toString())
            }
            setOnEditorActionListener { _, actionId, _ ->
                return@setOnEditorActionListener if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    onSearchPressed(this)
                    true
                } else false
            }
        }

        val searchButton = binding.searchButton
        searchButton.setOnClickListener { onSearchPressed(searchButton) }
        viewModel.searchButtonText.observe(this, { text ->
            searchButton.text = getString(text)
        })

        val loadingView = binding.loadingIndicatorContainer
        viewModel.showLoadingState.observe(this, { isLoading ->
            loadingView.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        val errorView = binding.errorModalContainer
        errorView.setOnClickListener { viewModel.onCloseErrorModal() }
        viewModel.showErrorState.observe(this, { hasError ->
            errorView.visibility = if (hasError) View.VISIBLE else View.GONE
        })

        val resultsDescription = binding.searchResultsDescription
        viewModel.searchResultDescription.observe(this, { description ->
            resultsDescription.text = getString(description)
        })

        val repoList = binding.repoList
        val repoAdapter = SearchResultAdapter(
            itemClickListener = { repo -> viewModel.onRepoSelected(repo) })
        repoList.adapter = repoAdapter
        viewModel.displayedRepos.observe(this, { repos ->
            repoAdapter.setResults(repos)
        })
    }

    private fun onSearchPressed(view: View) {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputManager?.hideSoftInputFromWindow(view.windowToken, 0)
        viewModel.onSearchPressed()
    }
}