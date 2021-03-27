package akh.presentation.ui.base

import akh.core.app.AppThemeProvider
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import javax.inject.Inject
import javax.inject.Provider

abstract class BaseFragment : DaggerFragment() {

    @Inject
    lateinit var appThemeProvider: Provider<AppThemeProvider>

    fun switchTheme() = appThemeProvider.get().switchTheme()

    open val insetsListener: ((View, WindowInsetsCompat, Rect) -> WindowInsetsCompat)? = null

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? =
            cloneInflater(inflater)
                    .inflate(getLayoutID(), container, false)
                    .also(::checkInsetListener)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI(savedInstanceState)
        observeViewModel()

    }

    @LayoutRes
    abstract fun getLayoutID(): Int

    protected abstract fun setUI(savedInstanceState: Bundle?)

    protected abstract fun observeViewModel()

    private fun cloneInflater(inflater: LayoutInflater): LayoutInflater =
            inflater.cloneInContext(
                    ContextThemeWrapper(
                            requireContext(),
                            appThemeProvider.get().getCurrentTheme()
                    )
            )

    private fun checkInsetListener(view: View) {
        val listener = insetsListener ?: return

        val initialPadding = recordInitialPaddingForView(view)

        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            listener.invoke(v, insets, initialPadding)
        }

        requestApplyInsetsWhenAttached(view)
    }

    private fun recordInitialPaddingForView(view: View): Rect =
            Rect(view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom)

    private fun requestApplyInsetsWhenAttached(view: View) {
        if (view.isAttachedToWindow) {
            view.requestApplyInsets()
        } else {
            view.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(v: View) {
                    v.removeOnAttachStateChangeListener(this)
                    v.requestApplyInsets()
                }

                override fun onViewDetachedFromWindow(v: View) = Unit
            })
        }
    }
}