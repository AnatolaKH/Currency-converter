package akh.presentation.ui.features.converter.rv

import akh.core.model.RateModel
import akh.presentation.ui.base.BaseHolder
import akh.presentation.ui.base.BaseRVListAdapter
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil

class RateListAdapter(
    private val action: (target: RateModel) -> Unit,
    private val exchange: (value: String) -> Unit
) : BaseRVListAdapter<RateModel>(RateDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<RateModel> =
         RateHolder.create(parent, action, exchange)

    override fun onBindViewHolder(holder: BaseHolder<RateModel>, position: Int) {
        if (holder is RateHolder) holder.bind(position == 0, getItem(position))
    }

    override fun onBindViewHolder(
        holder: BaseHolder<RateModel>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        payloads.takeIf { it.isNotEmpty() }?.firstOrNull()?.let {
            checkPayload(holder, position, it)
        } ?: super.onBindViewHolder(holder, position, payloads)
    }

    private fun checkPayload(holder: BaseHolder<RateModel>, position: Int, payload: Any) {
        when (payload) {
            is String -> if (position != 0) (holder as RateHolder).bind(payload)
            is Boolean -> (holder as RateHolder).bind(payload)
        }
    }

    class RateDiffCallback : DiffUtil.ItemCallback<RateModel>() {

        override fun areItemsTheSame(oldItem: RateModel, newItem: RateModel): Boolean =
            oldItem.countryCode == newItem.countryCode

        override fun areContentsTheSame(oldItem: RateModel, newItem: RateModel): Boolean =
            oldItem.exchange == newItem.exchange && oldItem.isBase == newItem.isBase

        override fun getChangePayload(oldItem: RateModel, newItem: RateModel): Any? =
            when {
                oldItem.exchange != newItem.exchange -> newItem.exchange
                oldItem.isBase != newItem.isBase -> newItem.isBase
                else -> null
            }
    }

}