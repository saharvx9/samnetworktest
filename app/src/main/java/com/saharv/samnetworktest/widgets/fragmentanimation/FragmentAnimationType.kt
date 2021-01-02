package com.saharv.samnetworktest.widgets.fragmentanimation

import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.fragment.app.FragmentTransaction
import com.saharv.samnetworktest.R

enum class FragmentAnimationType(@AnimatorRes @AnimRes private val enter: Int = 0,
                                 @AnimatorRes @AnimRes private val exit: Int = 0,
                                 @AnimatorRes @AnimRes private val popEnter: Int? = null,
                                 @AnimatorRes @AnimRes private val popExit: Int? = null) {
    FADE(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out),
    LEFT_IN_RIGHT_OUT(R.anim.enter_from_left, R.anim.exit_to_right),
    RIGHT_IN_LEFT_OUT(R.anim.enter_from_right, R.anim.exit_to_left),
    SLIDE_UP(R.anim.slide_up,R.anim.slide_down);




    fun init(fragmentTransaction: FragmentTransaction) {
        if (popEnter != null && popExit != null) fragmentTransaction.setCustomAnimations(enter, exit, popEnter, popExit)
        else fragmentTransaction.setCustomAnimations(enter, exit)
    }
}
