package com.saharv.samnetworktest.utils.extenstion

import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.saharv.samnetworktest.widgets.fragmentanimation.FragmentAnimationType
import org.jetbrains.anko.toast


/**
 * The `fragment` is added to the container view with id `frameId`. The operation is
 * performed by the `supportFragmentManager`.
 */
fun Fragment.replaceFragmentInFragment(
    fragment: Fragment,
    @IdRes frameId: Int,
    addToBackStack: Boolean = false,
    sharedElement:Boolean = false,
    sharedElementView: View?= null,
    fragmentAnimationType: FragmentAnimationType? = FragmentAnimationType.LEFT_IN_RIGHT_OUT) {

    childFragmentManager.transact {
        setReorderingAllowed(true)
        if (addToBackStack) addToBackStack(fragment::class.qualifiedName)
        if(!sharedElement) fragmentAnimationType?.init(this)
        else addSharedElement(sharedElementView!!, sharedElementView.transitionName)
        replace(frameId, fragment)
    }
}

/**
 * Runs a FragmentTransaction, then calls commit().
 */
inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commit()
}

/**
 * Get which fragment is displayed
 */
fun Fragment.getCurrentFragment(@IdRes frameId: Int): Fragment? = this.childFragmentManager.findFragmentById(frameId)


fun FragmentManager.clearBackStack() {
    if (backStackEntryCount == 0) return
    popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
}

/**
 * Toast from fragments
 */
fun Fragment.toast(message: String) {
    activity?.toast(message)
}

fun Fragment.toast(message: Int) {
    activity?.toast(message)
}

