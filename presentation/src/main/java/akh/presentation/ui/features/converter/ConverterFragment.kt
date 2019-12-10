package akh.presentation.ui.features.converter


import akh.core.model.RateModel
import akh.core.model.RatesState
import akh.presentation.R
import akh.presentation.common.*
import akh.presentation.ui.base.BaseFragment
import akh.presentation.ui.features.converter.rv.RateRVListAdapter
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_converter.*
import javax.inject.Inject


class ConverterFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by androidLazy { getViewModel<ConverterViewModel>(viewModelFactory) }

    private val rateAdapter: RateRVListAdapter by androidLazy {
        RateRVListAdapter(::changeTarget, ::exchange)
    }

    private fun setRatesState(ratesState: RatesState) {
        when (ratesState) {
            is RatesState.SuccessState -> setRates(ratesState.rates)
            is RatesState.LoadingState -> showProgress()
            is RatesState.FailureState -> showRetry()
        }
    }

    private fun setRates(rates: List<RateModel>) {
        pbRates.toGone()
        rvRate.toVisible()
        btnRetry.toGone()
        rateAdapter.submitList(rates)
    }

    private fun showProgress() {
        pbRates.toVisible()
        rvRate.toInvisible()
        btnRetry.toGone()
    }

    private fun showRetry() {
        pbRates.toGone()
        rvRate.toInvisible()
        btnRetry.toVisible()
    }

    private fun changeTarget(rateModel: RateModel) {
        viewModel.setTarget(rateModel)
    }

    private fun exchange(exchange: String) {
        viewModel.exchange(exchange)
    }

    override fun getLayoutID(): Int = R.layout.fragment_converter

    override fun setUI(savedInstanceState: Bundle?) {
        rvRate.apply {
            rvRate.setItemViewCacheSize(20)
            adapter = rateAdapter
            layoutManager = LinearLayoutManager(this.context)
            setHasFixedSize(true)
        }
        rvRate.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                recyclerView.hideKeyboard()
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

        rateAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                rvRate?.layoutManager?.scrollToPosition(0)
            }
        })

        btnRetry.setOnClickListener { viewModel.getRates() }
    }

    override fun observeViewModel() {
        observe(viewModel.rateLiveData, ::setRatesState)
    }

}