package com.bderoo.ghubsearch.screens.search

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import com.bderoo.ghubsearch.R
import com.bderoo.ghubsearch.util.StringResource
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
        val searchButtonTextObs = Observer<StringResource> { text ->
            searchButton.text = getString(text)
        }
        viewModel.searchButtonText.observe(this, searchButtonTextObs)
    }
}