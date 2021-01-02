package com.saharv.samnetworktest.utils.extenstion

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.view.View
import android.view.ViewAnimationUtils
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.tabs.TabLayout
import kotlin.math.hypot


fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun View.updateVisibility(show: Boolean, invisible: Boolean = false) {
    when {
        show -> show()
        invisible -> hide()
        else -> gone()
    }
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.isVisible(): Boolean = this.visibility == View.VISIBLE

/**
 * Enable view or not
 * in case true enable view and change his alpha to his default color with animation
 * in case false disable view and change his alpha and color with animation
 */
fun View.enable(enable: Boolean, animate: Boolean = true) {
    if (animate) animate().alpha(if (enable) 1f else 0.5f).apply { duration = 300 }.start()
    else alpha = if (enable) 1f else 0.5f
    isEnabled = enable
}

//fun ViewGroup.setFont() {
//    val type = TypefaceUtil.getFont(context)
//    forEachChild {
//        if (it is TextView) it.typeface = type
//        else if (it is ViewGroup) it.setFont()
//    }
//}

fun View.circularReveal(show: Boolean) {
    val view = this
    this.post {
        val cx = view.width /// 2
        val cy = view.height /// 2
        val radius = hypot(cx.toDouble(), cy.toDouble()).toFloat()

        try {
            if (show && view.visibility != View.VISIBLE) {
                val animator = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, radius)
                animator.duration = 500
                view.show()
                animator.start()
            } else if (!show && view.visibility == View.VISIBLE) {
                val animator = ViewAnimationUtils.createCircularReveal(view, cx, cy, radius, 0f)
                animator.duration = 400
                animator.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        view.hide()
                        animator.removeAllListeners()
                    }
                })
                animator.start()
            }

        } catch (e: Exception) {
            //Timber.e(e, "Unable to perform circular reveal")
        }
    }

}

fun ImageView.setColorFilterExt(@ColorRes color: Int) {
    setColorFilter(ContextCompat.getColor(context, color))
}

fun AppCompatActivity?.hideKeyboard() {
    val view: View? = this?.currentFocus
    if (view != null) {
        val imm = this?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    } else {
        if (this is Activity) {
            this.window.setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        }
    }
}

fun FragmentActivity?.hideKeyboard() {
    val view: View? = this?.currentFocus
    if (view != null) {
        val imm = this?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    } else {
        if (this is Activity) {
            this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        }
    }
}

fun Fragment.hideKeyboard() {
    activity?.hideKeyboard()
}

fun View?.hideKeyboard() {
    this?.apply {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

}

fun AppCompatEditText.showKeyboard() {
    postDelayed({
        requestFocus()
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }, 300)
}

fun EditText.placeCursorToEnd() {
    this.setSelection(this.text.length)
}

fun TabLayout.addOnTabSelectedListenerExt(action: (position: Int?) -> Unit) {
    addOnTabSelectedListener((object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) = Unit
        override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
        override fun onTabSelected(tab: TabLayout.Tab?) {
            action(tab?.position)
        }
    }))
}


fun ScrollView.scrollToViewCenter(view: View) {
    val vTop = view.top
    val vBottom = view.bottom
    val sHeight = height
    smoothScrollTo(0, ((vBottom + vTop) / 2) - (sHeight / 2))
}

fun TextView.setUnderLine() {
    paintFlags = Paint.UNDERLINE_TEXT_FLAG
}




