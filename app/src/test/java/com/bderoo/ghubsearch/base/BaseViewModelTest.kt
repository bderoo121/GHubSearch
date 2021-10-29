package com.bderoo.ghubsearch.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import org.junit.Rule
import org.junit.rules.TestRule

abstract class BaseViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    /*
     * Observes a [LiveData] until the `block` is done executing. Pass in a custom [Observer] to
     * track multiple emissions or other behaviors.
     */
    protected fun <T> LiveData<T>.test(observer: Observer<T> = Observer<T> {}, block: () -> Unit) {
        try {
            observeForever(observer)
            block()
        } finally {
            removeObserver(observer)
        }
    }
}