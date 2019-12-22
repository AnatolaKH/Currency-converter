package akh.presentation.ui.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        getLayoutID()?.let { layoutID -> setContentView(layoutID) }
        setUI(savedInstanceState)
    }

    @LayoutRes
    abstract fun getLayoutID(): Int?

    abstract fun setUI(savedInstanceState: Bundle?)

}