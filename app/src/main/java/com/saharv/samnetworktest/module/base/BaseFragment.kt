package com.saharv.samnetworktest.module.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.*
import com.saharv.samnetworktest.utils.extenstion.throttleFirst
import com.saharv.samnetworktest.application.service.ConnectivityType
import com.saharv.samnetworktest.module.main.MainViewModel
import com.saharv.samnetworktest.utils.extenstion.logDebug
import com.saharv.samnetworktest.widgets.SamAlert

abstract class BaseFragment<T : BaseViewModel> : Fragment(), BaseScreen<T> {

    //Observing network connectivity [connectivityTypeLiveData]
    private val mainViewModel: MainViewModel by activityViewModels()

    protected val viewModel: T by lazy { ViewModelProvider(this).get(getViewModelClass()) }
    protected abstract fun getViewModelClass(): Class<T>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutResource(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logDebug("~~~~~~~~~~ Fragment onViewCreated ${this.javaClass.simpleName} ~~~~~~~~~~")
        //Init all views as:click listeners,adapters and more....
        setupViews()

        //Base function for setup all event subject
        bindViewModel()
    }

    override fun onStart() {
        super.onStart()
        mainViewModel.appBarTitleLiveData.postValue(getToolBarTitle())
        logDebug("~~~~~~~~~~ Fragment onStart ${this.javaClass.simpleName} ~~~~~~~~~~")
    }

    override fun onResume() {
        super.onResume()
        logDebug("~~~~~~~~~~ Fragment onResume ${this.javaClass.simpleName} ~~~~~~~~~~")
    }

    override fun onStop() {
        super.onStop()
        logDebug("~~~~~~~~~~ Fragment onStop ${this.javaClass.simpleName} ~~~~~~~~~~")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        logDebug("~~~~~~~~~~ Fragment onDestroyView ${this.javaClass.simpleName} ~~~~~~~~~~")
    }

    override fun onDestroy() {
        super.onDestroy()
        logDebug("~~~~~~~~~~ Fragment onDestroy ${this.javaClass.simpleName} ~~~~~~~~~~")
    }

    override fun onDetach() {
        super.onDetach()
        logDebug("~~~~~~~~~~ Fragment onDetach ${this.javaClass.simpleName} ~~~~~~~~~~")
    }

    @CallSuper
    override fun bindViewModel() {

        viewModel.serverExceptionLiveData
            .asFlow()
            .throttleFirst(300)
            .asLiveData()
            .subscribe {
                SamAlert.OpsSomethingWentWrong().show(parentFragmentManager)
            }

        mainViewModel.connectivityTypeLiveData
            .distinctUntilChanged()
            .map { it == ConnectivityType.CONNECTED }
            .subscribe { viewModel.updateOnline(it) }
    }

    protected inline fun <T> LiveData<T>.subscribe(crossinline block: (T) -> Unit) {
        observe(viewLifecycleOwner) { block(it) }
    }

}

