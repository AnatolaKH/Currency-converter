package akh.presentation.ui.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

abstract class BaseHolder<in T>(private val view: View) : RecyclerView.ViewHolder(view), LayoutContainer {

    override val containerView: View? get() = view

    abstract fun bind(item: T)

}