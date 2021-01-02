package com.saharv.samnetworktest.widgets

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentManager
import com.saharv.samnetworktest.R


sealed class SamAlert(@DrawableRes val icon: Int,
                      @StringRes val title: Int,
                      @StringRes val message: Int,
                      @StringRes val positiveButton: Int = R.string.general_module_ok,
                      @StringRes val negativeButton: Int = R.string.general_empty,
                      val cancelable: Boolean = false) {


    class OpsSomethingWentWrong : SamAlert(R.drawable.ic_error_big,
            R.string.dialog_module_general_error_title,
            R.string.dialog_module_general_error_subtitle,)

    fun show(fragmentManager: FragmentManager) = AlertDialogFragment.newInstance(fragmentManager, this)


    fun dismiss(fragmentManager: FragmentManager) {
        (fragmentManager.findFragmentByTag(AlertDialogFragment::class.simpleName) as? AlertDialogFragment)
                ?.dismiss()
    }

}


