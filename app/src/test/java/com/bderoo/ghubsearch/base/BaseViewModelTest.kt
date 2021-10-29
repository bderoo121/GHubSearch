package com.bderoo.ghubsearch.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule
import org.junit.rules.TestRule

abstract class BaseViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
}