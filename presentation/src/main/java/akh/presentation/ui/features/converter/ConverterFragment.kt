package akh.presentation.ui.features.converter

import akh.core.model.RatesState
import akh.presentation.R
import akh.presentation.common.androidLazy
import akh.presentation.common.applyElevationOnScroll
import akh.presentation.common.dp
import akh.presentation.common.fragment.reattach
import akh.presentation.common.hideKeyboard
import akh.presentation.common.observe
import akh.presentation.common.setOnSingleClickListener
import akh.presentation.common.theme.getBundleWithCoordinate
import akh.presentation.common.theme.getExitAnimator
import akh.presentation.ui.base.BaseFragment
import akh.presentation.ui.features.converter.rv.RateListAdapter
import android.animation.Animator
import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_converter.*
import javax.inject.Inject

class ConverterFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<ConverterViewModel> { viewModelFactory }

    private val rateAdapter: RateListAdapter by androidLazy {
        RateListAdapter(viewModel::onSetTarget, viewModel::onExchange)
    }

    override val insetsListener: ((View, WindowInsetsCompat, Rect) -> WindowInsetsCompat) =
        { view, insets, padding ->
            view.updatePadding(
                top = padding.top + insets.systemWindowInsetTop,
                bottom = padding.bottom + insets.systemWindowInsetBottom
            )
            insets
        }

    override fun getLayoutID(): Int = R.layout.fragment_converter

    @SuppressLint("ClickableViewAccessibility")
    override fun setUI(savedInstanceState: Bundle?) {
        ratesList.apply {
            ratesList.setItemViewCacheSize(ITEM_VIEW_CACHE_SIZE)
            layoutManager = LinearLayoutManager(this.context)
            setHasFixedSize(true)
            adapter = rateAdapter
        }
        ratesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                recyclerView.hideKeyboard()
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        ratesList.applyElevationOnScroll(converterTitle, TOOLBAR_ELEVATION)

        rateAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                ratesList?.layoutManager?.scrollToPosition(0)
            }
        })

        retryButton.setOnSingleClickListener { viewModel.onUpdateRates() }

        swipeToRefresh.setColorSchemeColors(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorPrimary
            )
        )
        swipeToRefresh.setOnRefreshListener {
            swipeToRefresh.isRefreshing = false
            viewModel.onUpdateRates()
        }

        converterTitle.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP && event.rawY in v.y..(v.y + v.height)) {
                switchTheme()
                arguments = getBundleWithCoordinate(event.rawX, event.rawY)
                reattach()
            }
            true
        }
    }

    override fun observeData() {
        observe(viewModel.stateFlow, ::render)
    }

    override fun onCreateAnimator(transit: Int, enter: Boolean, nextAnim: Int): Animator? =
        if (enter.not()) {
            getSettingsForAnimation()?.let {
                val (view, arguments) = it
                arguments.getExitAnimator(view)
            } ?: super.onCreateAnimator(transit, enter, nextAnim)
        } else super.onCreateAnimator(transit, enter, nextAnim)

    private fun render(ratesState: RatesState) {
        progress.isVisible = ratesState.showProgress
        rateAdapter.submitList(ratesState.rates)
        retryButton.isVisible = ratesState.failure != null
    }

    private fun getSettingsForAnimation(): Pair<View, Bundle>? =
        view?.let { safeView ->
            arguments?.takeIf { ViewCompat.isAttachedToWindow(safeView) }?.let { safeArguments ->
                safeArguments.getExitAnimator(safeView)
                Pair(safeView, safeArguments)
            }
        }

    companion object {
        const val ITEM_VIEW_CACHE_SIZE = 20
        val TOOLBAR_ELEVATION = 6.dp()

        fun newInstance() = ConverterFragment()
    }
}
