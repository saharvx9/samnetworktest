package com.saharv.samnetworktest.widgets.glideimageloader

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.saharv.samnetworktest.R
import com.saharv.samnetworktest.utils.extenstion.gone
import com.saharv.samnetworktest.utils.extenstion.show
import com.saharv.samnetworktest.widgets.glideimageloader.ProgressAppGlideModule.UIonProgressListener

class GlideImageLoader(private val mImageView: ImageView?, private val mProgressBar: ProgressBar?) {

    fun load(url: String?, options: RequestOptions?) {
        if (url == null || options == null) return
        onConnecting()

        //set Listener & start
        ProgressAppGlideModule.expect(url, object : UIonProgressListener {
            override fun onProgress(bytesRead: Long, expectedLength: Long) {
                if (mProgressBar != null) {
                    mProgressBar.progress = (100 * bytesRead / expectedLength).toInt()
                }
            }

            override fun getGranualityPercentage(): Float {
                return 1.0f
            }
        })
        //Get Image
        Glide.with(mImageView!!.context)
            .load(url)
            .transition(DrawableTransitionOptions.withCrossFade())
            //.apply(options.skipMemoryCache(true))
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable?>,
                    isFirstResource: Boolean
                ): Boolean {
                    ProgressAppGlideModule.forget(url)
                    onFinished()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any,
                    target: Target<Drawable?>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    ProgressAppGlideModule.forget(url)
                    onFinished()
                    return false
                }
            })
            .into(mImageView)
    }

    private fun onConnecting() {
        if (mProgressBar != null) mProgressBar.visibility = View.VISIBLE
    }

    private fun onFinished() {
        if (mProgressBar != null && mImageView != null) {
            mProgressBar.visibility = View.GONE
            mImageView.visibility = View.VISIBLE
        }
    }
}

fun ImageView.loadImageProgress(
    progressBar: ProgressBar, url: String?,
    options: RequestOptions = RequestOptions()
        .apply(RequestOptions().circleCrop())
        .placeholder(R.drawable.ic_placeholder)
        .error(R.drawable.ic_placeholder)
        .diskCacheStrategy(DiskCacheStrategy.DATA) // support offline
        .priority(Priority.HIGH)) {
    if(url.isNullOrEmpty()){
        show()
        setImageResource(R.drawable.ic_placeholder)
        progressBar.gone()
    }else{
        GlideImageLoader(this, progressBar).load(url, options)
    }
}