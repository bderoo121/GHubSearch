package com.bderoo.ghubsearch.screens.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.bderoo.ghubsearch.R
import com.bderoo.ghubsearch.base.BaseViewModel
import com.bderoo.ghubsearch.util.StringResource

class SearchViewModel : BaseViewModel() {
    private val organizationName = MutableLiveData("")

    val searchButtonText: LiveData<StringResource> = Transformations.map(organizationName) { name ->
        if (name.isNullOrBlank()) StringResource(R.string.search_button)
        else StringResource(R.string.search_button_with_text, name)
    }

    fun onOrganizationTextChanged(text: String) {
        organizationName.value = text
    }
}