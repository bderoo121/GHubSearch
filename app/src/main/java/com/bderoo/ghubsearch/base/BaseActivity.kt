package com.bderoo.ghubsearch.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity

// TODO: Extend this to a BaseMvvmActivity
// TODO: Use viewBinding
// TODO: Determine why this isn't working when being subclassed
abstract class BaseActivity : AppCompatActivity() {
    abstract val layoutResourceId: Int

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(layoutResourceId)
    }
}