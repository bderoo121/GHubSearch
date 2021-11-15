package com.bderoo.ghubsearch.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

// TODO: Extend this to a BaseMvvmActivity
// TODO: Use viewBinding
// TODO: Determine why this isn't working when being subclassed
abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    abstract val inflater: (LayoutInflater) -> VB

    protected lateinit var binding: VB
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflater.invoke(layoutInflater)
        setContentView(binding.root)
    }
}