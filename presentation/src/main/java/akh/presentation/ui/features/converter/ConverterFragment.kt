package akh.presentation.ui.features.converter

import akh.core.model.RateModel
import akh.core.model.RatesState
import akh.presentation.R
import akh.presentation.common.*
import akh.presentation.common.fragment.reattach
import akh.presentation.common.theme.getBundleWithCoordinate
import akh.presentation.common.theme.getExitAnimator
import akh.presentation.ui.base.BaseFragment
import akh.presentation.ui.features.converter.rv.RateListAdapter
import android.animation.Animator
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_converter.*
import javax.inject.Inject

class ConverterFragment : BaseFragment() {

    companion object {
        const val ITEM_VIEW_CACHE_SIZE = 20
        val TOOLBAR_ELEVATION = 6.dp()

        fun newInstance() = ConverterFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<ConverterViewModel> { viewModelFactory }

    private val rateAdapter: RateListAdapter by androidLazy {
        RateListAdapter(::changeTarget, ::exchange)
    }

    private fun setRatesState(ratesState: RatesState) {
        progress.toggleGone(ratesState.showProgress)
        rateAdapter.submitList(ratesState.rates)
        retryButton.toggleGone(ratesState.failure != null)
    }

    private fun changeTarget(rateModel: RateModel) {
        viewModel.setTarget(rateModel)
    }

    private fun exchange(exchange: String) {
        viewModel.exchange(exchange)
    }

    override fun getLayoutID(): Int = R.layout.fragment_converter

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

        retryButton.setOnSingleClickListener { viewModel.updateRates() }

        swipeToRefresh.setColorSchemeColors(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorPrimary
            )
        )
        swipeToRefresh.setOnRefreshListener {
            swipeToRefresh.isRefreshing = false
            viewModel.updateRates()
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

    override fun observeViewModel() {
        observe(viewModel.rateLiveData, ::setRatesState)
    }

    override fun onCreateAnimator(transit: Int, enter: Boolean, nextAnim: Int): Animator? =
        if (enter.not()) {
            getSettingsForAnimation()?.let {
                val (view, arguments) = it
                arguments.getExitAnimator(view)
            } ?: super.onCreateAnimator(transit, enter, nextAnim)
        } else super.onCreateAnimator(transit, enter, nextAnim)

    private fun getSettingsForAnimation(): Pair<View, Bundle>? =
        view?.let { safeView ->
            arguments?.takeIf { ViewCompat.isAttachedToWindow(safeView) }?.let { safeArguments ->
                safeArguments.getExitAnimator(safeView)
                Pair(safeView, safeArguments)
            }
        }
}
