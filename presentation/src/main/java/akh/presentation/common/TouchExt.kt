package akh.presentation.common

import android.os.SystemClock
import android.util.Log
import android.view.MotionEvent
import android.view.View

const val DEBOUNCE_CLICK_TIME = 750L

private class SafeClickListener(
    private var defaultInterval: Long = DEBOUNCE_CLICK_TIME,
    private val onSafeCLick: (View) -> Unit
) : View.OnClickListener {

    private var lastTimeClicked: Long = 0

    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) return

        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeCLick(v)
    }
}

fun View.setOnSingleClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}