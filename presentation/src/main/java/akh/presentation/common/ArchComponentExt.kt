package akh.presentation.common

import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

inline fun <T> Fragment.observe(flow: Flow<T>, crossinline block: (T) -> Unit) =
    lifecycleScope.launch {
        flow.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .collect { action -> block.invoke(action) }
    }