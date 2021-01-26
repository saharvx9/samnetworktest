package com.saharv.samnetworktest.module.main.fragments.articleinfo

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.core.app.SharedElementCallback
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.saharv.samnetworktest.R
import com.saharv.samnetworktest.data.model.ArticleItem
import com.saharv.samnetworktest.module.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_article_info.*

@AndroidEntryPoint
class ArticleInfoFragment : BaseFragment<ArticlesInfoViewModel>() {


    companion object {
        private const val EXTRA_TRANSITION = "EXTRA_TRANSITION"

        fun newInstance(transitionName: String): ArticleInfoFragment {
            return ArticleInfoFragment().apply {
                arguments = Bundle().apply { putString(EXTRA_TRANSITION, transitionName) }
            }
        }
    }

    override fun getLayoutResource(): Int = R.layout.fragment_article_info

    override fun getViewModelClass(): Class<ArticlesInfoViewModel> =
        ArticlesInfoViewModel::class.java

    override fun getToolBarTitle(): Int = R.string.article_info_module_app_bar_title

    override fun setupViews() {
        article_image.transitionName = arguments?.getString(EXTRA_TRANSITION)
        prepareSharedElementTransition()
        postponeEnterTransition()
    }

    override fun bindViewModel() {
        super.bindViewModel()
        viewModel.articleLiveData.subscribe { initArticleItemData(it) }

        viewModel.start()
    }


    private fun initArticleItemData(item: ArticleItem) {

        article_title.text = item.title
        article_subtitle.text = item.description ?: "none description"

        Glide.with(this)
            .load(item.urlToImage ?: R.drawable.ic_placeholder)
            .fitCenter()
            .apply(RequestOptions().dontTransform())
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable?>,
                    isFirstResource: Boolean
                ): Boolean {
                    // The postponeEnterTransition is called on the parent ImagePagerFragment, so the
                    // startPostponedEnterTransition() should also be called on it to get the transition
                    // going in case of a failure.
                    startPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any,
                    target: Target<Drawable?>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    // The postponeEnterTransition is called on the parent ImagePagerFragment, so the
                    // startPostponedEnterTransition() should also be called on it to get the transition
                    // going when the image is ready.
                    startPostponedEnterTransition()
                    return false
                }
            }).into(article_image)
    }

    /**
     * Prepares the shared element transition from and back to the grid fragment.
     */
    private fun prepareSharedElementTransition() {
        val transition = TransitionInflater.from(context)
            .inflateTransition(R.transition.image_shared_element_transition)
        sharedElementEnterTransition = transition

        // A similar mapping is set at the GridFragment with a setExitSharedElementCallback.
//        setEnterSharedElementCallback(
//            object : SharedElementCallback() {
//                override fun onMapSharedElements(
//                    names: List<String>,
//                    sharedElements: MutableMap<String, View>
//                ) {
//                    // Map the first shared element name to the child ImageView.
//
//
//                    // Map the first shared element name to the child ImageView.
//                    sharedElements[names[0]] = article_image
//                }
//            })
    }


}