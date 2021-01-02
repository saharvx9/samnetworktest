package com.saharv.samnetworktest.module.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.saharv.samnetworktest.R
import com.saharv.samnetworktest.application.service.ConnectivityType.*
import com.saharv.samnetworktest.module.main.fragments.articles.ArticlesFragment
import com.saharv.samnetworktest.utils.extenstion.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindViewModel()
        replaceFragments(ArticlesFragment())
    }

    private fun replaceFragments(fragment: Fragment) {
        addFragmentToActivity(fragment,R.id.container,addToBackStack = true)
    }


    private fun bindViewModel() {

        viewModel.appBarTitleLiveData
            .subscribe {
                title_app_bar.setText(it)
            }

        viewModel.connectivityTypeLiveData.subscribe {
            this@MainActivity.logInfo("show type :$it")
            when (it) {
                CONNECTED, DISCONNECTED -> {
                    progress_bar.gone()
                    status_network_image.apply {
                        show()
                        setImageResource(it.image)
                    }
                }
                CONNECTING, DISCONNECTING -> {
                    progress_bar.show()
                    status_network_image.gone()
                }
                else -> Unit
            }
        }
        viewModel.startBind()
    }


    private inline fun <T> LiveData<T>.subscribe(crossinline block: (T) -> Unit) {
        observe(this@MainActivity) { block(it) }
    }
}