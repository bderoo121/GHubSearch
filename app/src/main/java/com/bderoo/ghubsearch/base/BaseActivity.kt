package com.bderoo.ghubsearch.base

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding

//TODO: Extend this to a BaseMvvmActivity
abstract class BaseActivity<B: ViewBinding>: AppCompatActivity() {
    abstract fun getViewBinding(): B
    lateinit var binding: B
        private set

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = getViewBinding()
        setContentView(binding.root)
    }

    private val liveDataLifecycleOwner: LifecycleOwner get() = this

    fun <T> LiveData<T>.observe(observer: (T) -> Unit) {
        observe(liveDataLifecycleOwner, Observer { observer(it) })
    }

    fun LiveData<Boolean>.bindToVisibility(view: View) {
        observe { view.visibility = if (it) View.VISIBLE else View.GONE }
    }

    fun LiveData<String>.bindToText(view: TextView) {
        observe { view.text = it }
    }
}