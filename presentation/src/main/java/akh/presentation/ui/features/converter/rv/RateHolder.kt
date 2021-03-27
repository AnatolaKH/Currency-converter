package akh.presentation.ui.features.converter.rv

import akh.core.model.RateModel
import akh.presentation.R
import akh.presentation.common.hideKeyboard
import akh.presentation.common.loadImage
import akh.presentation.common.setOnSingleClickListener
import akh.presentation.common.setTextFutureExt
import akh.presentation.common.showKeyboard
import akh.presentation.ui.base.BaseHolder
import android.annotation.SuppressLint
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.row_rate.*


class RateHolder(
    view: View,
    private val action: (target: RateModel) -> Unit,
    private val exchange: (value: String) -> Unit
) : BaseHolder<RateModel>(view), TextWatcher, View.OnTouchListener {

    private val numberInputType = InputType.TYPE_CLASS_NUMBER or
            InputType.TYPE_NUMBER_FLAG_DECIMAL or
            InputType.TYPE_NUMBER_FLAG_SIGNED

    fun bind(exchange: String) {
        etExchange?.removeTextChangedListener(this)
        etExchange?.setTextFutureExt(exchange)
        etExchange?.setSelection(etExchange?.text?.length ?: 0)
        etExchange?.addTextChangedListener(this)
    }

    fun bind(isTarget: Boolean) {
        updateInputType(isTarget)
    }

    fun bind(isTarget: Boolean, item: RateModel) {
        bind(item)
        updateInputType(isTarget)
        if (isTarget) etExchange?.requestFocus()
    }

    override fun bind(item: RateModel) {
        etExchange?.removeTextChangedListener(this)
        itemView.setOnSingleClickListener { selectItem(item) }
        ivCountry.loadImage(item.countryFlag)
        tvCountryCode?.setTextFutureExt(item.countryCode)
        tvCountryTitle?.setTextFutureExt(item.countryName)
        etExchange?.setTextFutureExt(item.exchange)
        etExchange?.addTextChangedListener(this)
    }

    private fun updateInputType(isTarget: Boolean) {
        etExchange?.inputType = if (isTarget) numberInputType else InputType.TYPE_NULL
        etExchange?.setSelection(etExchange?.text?.length ?: 0)
        etExchange?.setOnTouchListener(if (isTarget) null else this)
    }

    private fun selectItem(item: RateModel) {
        etExchange?.inputType = numberInputType
        etExchange?.requestFocus()
        etExchange?.showKeyboard()
        action(item)
    }

    override fun afterTextChanged(s: Editable?) {
        if (etExchange?.isFocused == true) exchange(s?.toString() ?: "")
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        etExchange?.hideKeyboard()
        return true
    }

    companion object {
        fun create(
            parent: ViewGroup,
            action: (target: RateModel) -> Unit,
            exchange: (value: String) -> Unit
        ): RateHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_rate, parent, false)
            return RateHolder(view, action, exchange)
        }
    }

}