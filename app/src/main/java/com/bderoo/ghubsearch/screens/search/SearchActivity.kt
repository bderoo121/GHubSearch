package com.bderoo.ghubsearch.screens.search

import androidx.activity.viewModels
import com.bderoo.ghubsearch.base.BaseActivity
import com.bderoo.ghubsearch.databinding.ActivitySearchBinding

class SearchActivity: BaseActivity<ActivitySearchBinding>() {
    override fun getViewBinding() = ActivitySearchBinding.inflate(layoutInflater)

    private val viewModel by viewModels<SearchViewModel>()
}