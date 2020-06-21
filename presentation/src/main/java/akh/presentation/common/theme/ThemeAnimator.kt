package akh.presentation.common.theme

import android.animation.Animator
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import androidx.core.os.bundleOf

const val CX_ANIM = "cxAnim"
const val CY_ANIM = "cyAnim"
const val ANIMATION_DURATION = 450L

fun Bundle.getExitAnimator(view: View): Animator {

    var cx = getFloat(CX_ANIM, 0f).toInt()
    var cy = getFloat(CY_ANIM, 0f).toInt()

    if (cx == 0 && cy == 0) {
        cx = (view.left + view.right) / 2
        cy = view.top + view.bottom / 2
    }

    val startRadius = (view.width * 1.5f).coerceAtLeast(view.height * 1.5f)

    view.visibility = View.GONE

    return ViewAnimationUtils.createCircularReveal(view, cx, cy, startRadius, 0f).apply {
        duration = ANIMATION_DURATION
    }
}

fun getBundleWithCoordinate(x: Float, y: Float) = bundleOf(
    CX_ANIM to x,
    CY_ANIM to y
)