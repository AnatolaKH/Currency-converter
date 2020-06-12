package akh.presentation.ui.base

import akh.core.app.AppThemeProvider
import akh.presentation.R
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.appcompat.view.ContextThemeWrapper
import androidx.coordinatorlayout.widget.CoordinatorLayout
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import javax.inject.Inject
import javax.inject.Provider

abstract class BaseFragment : DaggerFragment() {

    @Inject
    lateinit var appThemeProvider: Provider<AppThemeProvider>

    fun switchTheme() = appThemeProvider.get().switchTheme()

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val contextThemeWrapper = ContextThemeWrapper(
            requireContext(),
            appThemeProvider.get().getCurrentTheme()
        )
        val themeInflater = inflater.cloneInContext(contextThemeWrapper)
        return CoordinatorLayout(contextThemeWrapper).apply {
            layoutParams = ViewGroup.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            fitsSystemWindows = true
            addView(themeInflater.inflate(getLayoutID(), this, false))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI(savedInstanceState)
        observeViewModel()
    }

    @LayoutRes
    abstract fun getLayoutID(): Int

    protected abstract fun setUI(savedInstanceState: Bundle?)

    protected abstract fun observeViewModel()
}