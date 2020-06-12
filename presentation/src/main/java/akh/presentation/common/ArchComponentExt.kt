package akh.presentation.common

import androidx.fragment.app.Fragment
import androidx.lifecycle.*

fun <T> LifecycleOwner.observe(liveData: LiveData<T>, action: (t: T) -> Unit) {
    liveData.observe(this, Observer { it?.let { t -> action(t) } })
}

fun <T> Fragment.observe(liveData: LiveData<T>, action: (t: T) -> Unit) =
    viewLifecycleOwner.observe(liveData, action)

inline fun <reified T : ViewModel> Fragment.getViewModel(viewModelFactory: ViewModelProvider.Factory): T =
    ViewModelProviders.of(this, viewModelFactory)[T::class.java]