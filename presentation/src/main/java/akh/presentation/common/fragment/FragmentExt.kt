package akh.presentation.common.fragment

import androidx.fragment.app.Fragment

fun Fragment.reattach() =
    requireActivity().supportFragmentManager
        .beginTransaction()
        .detach(this)
        .attach(this)
        .commit()