package com.saharv.samnetworktest.widgets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.asFlow
import com.saharv.samnetworktest.R
import com.saharv.samnetworktest.utils.SingleLiveEvent
import kotlinx.android.synthetic.main.widget_fragmentalertdialog_layout.*
import kotlinx.android.synthetic.main.widget_fragmentalertdialog_layout.view.*

class AlertDialogFragment : AppCompatDialogFragment() {


    private val positiveClickSubject = SingleLiveEvent<Any>()
    private val negativeClickSubject = SingleLiveEvent<Any>()

    val positiveClick = positiveClickSubject.asFlow()
    val negativeClick = negativeClickSubject.asFlow()


    companion object {

        private const val EXTRA_STRING = "EXTRA_STRING"
        private const val EXTRA_ICON = "EXTRA_ICON"
        private const val EXTRA_TITLE = "EXTRA_TITLE"
        private const val EXTRA_MESSAGE = "EXTRA_MESSAGE"
        private const val EXTRA_POSITIVE_BUTTON = "EXTRA_POSITIVE_BUTTON"
        private const val EXTRA_NEGATIVE_BUTTON = "EXTRA_NEGATIVE_BUTTON"
        private const val EXTRA_CANCELABLE = "EXTRA_CANCELABLE"

        fun newInstance(fragmentManager: FragmentManager, manpowerAlert: SamAlert): AlertDialogFragment {
            return manpowerAlert.run {
                newInstance(fragmentManager,
                        icon,
                        title, message,
                        positiveButton, negativeButton,
                        cancelable)
            }
        }

        /**
         * Create and show a DialogFragment with the given message.
         *
         * @param fragmentManager originating Activity
         * @param icon displayed icon, if any
         * @param title displayed title, if any
         * @param message displayed message
         * @param positiveButton label for second button, if any.  If non-null
         * @param negativeButton label for second button, if any.  If non-null
         */
        fun newInstance(fragmentManager: FragmentManager,
                        @DrawableRes icon: Int,
                        @StringRes title: Int,
                        @StringRes message: Int,
                        @StringRes positiveButton: Int = R.string.general_empty,
                        @StringRes negativeButton: Int = R.string.general_empty,
                        cancelable: Boolean = false): AlertDialogFragment {
            val fragment = AlertDialogFragment()
            fragment.arguments = Bundle().apply {
                putInt(EXTRA_ICON, icon)
                putInt(EXTRA_TITLE, title)
                putInt(EXTRA_MESSAGE, message)
                putInt(EXTRA_POSITIVE_BUTTON, positiveButton)
                putInt(EXTRA_NEGATIVE_BUTTON, negativeButton)
                putBoolean(EXTRA_CANCELABLE, cancelable)
            }
            fragment.show(fragmentManager, AlertDialogFragment::class.simpleName)
            return fragment
        }

        /**
         * Create and show a DialogFragment with the given message.
         *
         * @param fragmentManager originating Activity
         * @param icon displayed icon, if any
         * @param title displayed title, if any
         * @param message displayed message
         * @param positiveButton label for second button, if any.  If non-null
         * @param negativeButton label for second button, if any.  If non-null
         */
        fun newInstance(fragmentManager: FragmentManager,
                        @DrawableRes icon: Int,
                        title: String,
                        message: String,
                        positiveButton: String = "",
                        negativeButton: String = "",
                        cancelable: Boolean = false,
                        show: Boolean = true): AlertDialogFragment {

            val fragment = AlertDialogFragment()
            fragment.arguments = Bundle().apply {
                putBoolean(EXTRA_STRING, true)
                putInt(EXTRA_ICON, icon)
                putString(EXTRA_TITLE, title)
                putString(EXTRA_MESSAGE, message)
                putString(EXTRA_POSITIVE_BUTTON, positiveButton)
                putString(EXTRA_NEGATIVE_BUTTON, negativeButton)
                putBoolean(EXTRA_CANCELABLE, cancelable)
            }
            fragment.show(fragmentManager, AlertDialogFragment::class.simpleName)
            return fragment
        }


        fun dismiss(fragmentManager: FragmentManager?) {
            (fragmentManager?.findFragmentByTag(AlertDialogFragment::class.simpleName) as? AlertDialogFragment)?.dismiss()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, 0)
        isCancelable = arguments?.getBoolean(EXTRA_CANCELABLE, false) ?: false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.widget_fragmentalertdialog_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.decorView?.setBackgroundResource(android.R.color.transparent)

        arguments?.apply {
            getInt(EXTRA_ICON).also { if (it > 0) view.alert_image.setImageResource(it) }

            if (getBoolean(EXTRA_STRING, false)) {
                alert_title.text = getString(EXTRA_TITLE)
                alert_subtitle.text = getString(EXTRA_MESSAGE)
                alert_positive_button.text = getString(EXTRA_POSITIVE_BUTTON)
                alert_negative_label.text = getString(EXTRA_NEGATIVE_BUTTON)
            } else {
                alert_title.text = context?.getString(getInt(EXTRA_TITLE))
                alert_subtitle.text = context?.getString(getInt(EXTRA_MESSAGE))
                alert_positive_button.text = context?.getString(getInt(EXTRA_POSITIVE_BUTTON))
                alert_negative_label.text = context?.getString(getInt(EXTRA_NEGATIVE_BUTTON))
            }
        }

        alert_negative_label.setOnClickListener {
            negativeClickSubject.postValue(Any())
            dismissAllowingStateLoss()
        }

        alert_positive_button.setOnClickListener {
            positiveClickSubject.postValue(Any())
            dismissAllowingStateLoss()
        }
    }

}
