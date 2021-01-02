package com.saharv.samnetworktest.module.main.fragments.articles

import android.os.Bundle
import android.view.View
import androidx.core.app.SharedElementCallback
import androidx.lifecycle.map
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import androidx.transition.TransitionSet
import com.bumptech.glide.Glide
import com.saharv.samnetworktest.utils.extenstion.filter
import com.saharv.samnetworktest.R
import com.saharv.samnetworktest.module.base.BaseFragment
import com.saharv.samnetworktest.module.main.MainActivity
import com.saharv.samnetworktest.module.main.fragments.articleinfo.ArticleInfoFragment
import com.saharv.samnetworktest.module.main.fragments.articles.ArticlesState.*
import com.saharv.samnetworktest.utils.extenstion.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.articles_frgament.*
import kotlinx.coroutines.runBlocking


@AndroidEntryPoint
class ArticlesFragment : BaseFragment<ArticlesViewModel>() {

    private val articlesAdapter = ArticlesAdapter()

    override fun getViewModelClass(): Class<ArticlesViewModel> = ArticlesViewModel::class.java

    override fun getLayoutResource(): Int = R.layout.articles_frgament

    override fun getToolBarTitle(): Int = R.string.articles_module_app_bar_title

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.start()
    }

    override fun setupViews() {
        prepareTransitions()
        postponeEnterTransition()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        articles_recyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = articlesAdapter
        }

        articlesAdapter.articleItemClick
            .filter { it!=null  }
            .map {
                runBlocking { viewModel.saveArticleItemCache(it.item) }
                viewModel.currentPosition = it.position
                it
            }
            .subscribe {
                logInfo("Replace fragemnts!!!")
                articlesAdapter.articleItemClick.value = null
                navigateToArticleInfoFragment(it)
        }
    }


    override fun bindViewModel() {
        super.bindViewModel()

        viewModel.articlesStateLiveData.subscribe {
            when (it) {
                is LoadingState -> {
                    progress_bar.show()
                    articles_recyclerview.gone()
                }
                is ArticlesListState -> {
                    progress_bar.gone()
                    articles_recyclerview.show()
                    articlesAdapter.submitList(it.list)
                }
                is ErrorState -> {
                    progress_bar.gone()
                    articles_recyclerview.gone()
                }
            }
        }
    }


    private fun navigateToArticleInfoFragment(result: ArticleItemClickResult){
        startPostponedEnterTransition()
        (exitTransition as TransitionSet).excludeTarget(result.view, true)
        val fragment = ArticleInfoFragment.newInstance(result.item.title?:"")
        (activity as MainActivity).replaceFragmentInActivity(
            fragment,
            R.id.container,
            addToBackStack = true,
            sharedElement = true,
            sharedElementView = result.imageView)
    }

    /**
     * Prepares the shared element transition to the pager fragment, as well as the other transitions
     * that affect the flow.
     */
    private fun prepareTransitions() {
        exitTransition = TransitionInflater.from(context)
            .inflateTransition(R.transition.grid_exit_transition)

        setExitSharedElementCallback(
            object : SharedElementCallback() {
                override fun onMapSharedElements(
                    names: List<String?>,
                    sharedElements: MutableMap<String?, View?>
                ) {
                    this@ArticlesFragment.logInfo("show currentPosition: ${viewModel.currentPosition}")
                    viewModel.currentPosition?.let {
                        // Locate the ViewHolder for the clicked position.
                        val selectedViewHolder: RecyclerView.ViewHolder = articles_recyclerview
                            .findViewHolderForAdapterPosition(it) ?: return

                        this@ArticlesFragment.logInfo("map the first shared element ")
                        // Map the first shared element name to the child ImageView.
                        sharedElements[names[0]] = selectedViewHolder.itemView.findViewById(R.id.card_view)
                    }
                }
            })
    }



}