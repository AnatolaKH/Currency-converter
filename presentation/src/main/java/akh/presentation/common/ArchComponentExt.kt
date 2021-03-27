package akh.presentation.common

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LifecycleOwner.observe(liveData: LiveData<T>, action: (t: T) -> Unit) =
    liveData.observe(this, Observer { it?.let(action) })

fun <T> Fragment.observe(liveData: LiveData<T>, action: (t: T) -> Unit) =
    viewLifecycleOwner.observe(liveData, action)