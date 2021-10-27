package com.bderoo.ghubsearch.screens.search

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.bderoo.ghubsearch.R
import com.bderoo.ghubsearch.util.getString

class SearchActivity : AppCompatActivity() {
    private val viewModel by viewModels<SearchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        findViewById<EditText>(R.id.organization_search).doAfterTextChanged { text ->
            viewModel.onOrganizationTextChanged(text.toString())
        }

        val searchButton = findViewById<Button>(R.id.search_button)
        searchButton.setOnClickListener { viewModel.onSearchPressed() }
        viewModel.searchButtonText.observe(this, { text ->
            searchButton.text = getString(text)
        })

        val loadingView = findViewById<FrameLayout>(R.id.loading_indicator_container)
        viewModel.showLoadingState.observe(this, { isLoading ->
            loadingView.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        val errorView = findViewById<FrameLayout>(R.id.error_modal_container)
        errorView.setOnClickListener { viewModel.onCloseErrorModal() }
        viewModel.showErrorState.observe(this, { hasError ->
            errorView.visibility = if (hasError) View.VISIBLE else View.GONE
        })
    }
}