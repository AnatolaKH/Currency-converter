package akh.presentation.ui.base

import akh.core.app.AppThemeProvider
import android.os.Bundle
import androidx.annotation.LayoutRes
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject
import javax.inject.Provider

abstract class BaseActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var appThemeProvider: Provider<AppThemeProvider>

    open var customTheme: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        setTheme(customTheme ?: appThemeProvider.get().getCurrentTheme())
        super.onCreate(savedInstanceState)
        getLayoutID()?.let { layoutID -> setContentView(layoutID) }
        setUI(savedInstanceState)
    }

    @LayoutRes
    abstract fun getLayoutID(): Int?

    abstract fun setUI(savedInstanceState: Bundle?)

}