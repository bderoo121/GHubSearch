package com.bderoo.ghubsearch.base

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

// TODO: Extend this to a BaseMvvmActivity
// TODO: Use viewBinding
// TODO: Determine why this isn't working when being subclassed
abstract class BaseActivity : AppCompatActivity() {
    //    abstract fun getViewBinding(): B
    //    lateinit var binding: ViewBinding
    abstract val layoutResourceId: Int

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(layoutResourceId)
    }

    private val liveDataLifecycleOwner: LifecycleOwner get() = this

    fun <T> LiveData<T>.observe(observer: (T) -> Unit) {
        observe(liveDataLifecycleOwner, { observer(it) })
    }

    fun LiveData<Boolean>.bindToVisibility(view: View) {
        observe { view.visibility = if (it) View.VISIBLE else View.GONE }
    }

    fun LiveData<String>.bindToText(view: TextView) {
        observe { view.text = it }
    }
}