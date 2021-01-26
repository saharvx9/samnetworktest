

package com.saharv.samnetworktest.utils.extenstion


import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.transition.TransitionSet
import com.saharv.samnetworktest.widgets.fragmentanimation.FragmentAnimationType


/**
 * The `fragment` is added to the container view with id `frameId`. The operation is
 * performed by the `supportFragmentManager`.
 */
fun AppCompatActivity.replaceFragmentInActivity(
    fragment: Fragment,
    @IdRes frameId: Int,
    addToBackStack: Boolean = false,
    sharedElement:Boolean = false,
    sharedElementView: View?= null,
    fragmentAnimationType: FragmentAnimationType? = FragmentAnimationType.RIGHT_IN_LEFT_OUT) {

    if ((getCurrentFragment(frameId) ?: Fragment())::class.java.simpleName == fragment::class.java.simpleName) {
        logDebug("The fragment is already active")
        return
    }
    supportFragmentManager.transact {

        if (addToBackStack) addToBackStack(fragment::class.qualifiedName)
        if(!sharedElement) fragmentAnimationType?.init(this)
        else {
            setReorderingAllowed(true)
            addSharedElement(sharedElementView!!, sharedElementView.transitionName)
        }
        replace(frameId, fragment)
    }
}


/**
 * The `fragment` is added to the container view with TAG. The operation is
 * performed by the `fragmentManager`.
 */
fun AppCompatActivity.addFragmentToActivity(
    fragment: Fragment,
    @IdRes frameId: Int,
    addToBackStack: Boolean = false,
    fragmentAnimationType: FragmentAnimationType? = FragmentAnimationType.LEFT_IN_RIGHT_OUT
) {
    supportFragmentManager.transact {
        if (addToBackStack) addToBackStack(fragment::class.qualifiedName)
        fragmentAnimationType?.init(this)
        add(frameId,fragment, fragment::class.java.simpleName)
    }
}

fun Activity.openKeyboard(view: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

fun AppCompatActivity.getCurrentFragment(@IdRes frameId: Int): Fragment? = supportFragmentManager.findFragmentById(frameId)

fun AppCompatActivity.changeToLandScape(value: Boolean) {
    if (value) requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
}
