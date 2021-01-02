package com.saharv.samnetworktest.module.main.fragments.articles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.saharv.samnetworktest.R
import com.saharv.samnetworktest.data.model.ArticleItem
import com.saharv.samnetworktest.widgets.glideimageloader.loadImageProgress
import kotlinx.android.synthetic.main.article_item.view.*


class ArticlesAdapter : ListAdapter<ArticleItem, ArticlesAdapter.ViewHolder>(DiffCallback()) {

    val articleItemClick = MutableLiveData<ArticleItemClickResult>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlesAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.article_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ArticlesAdapter.ViewHolder, position: Int) = holder.bind()


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imageView = itemView.article_image_view
        private val progressBar = itemView.progress_image
        private val titleTxt = itemView.title

        init {
            itemView.setOnClickListener {
                if(getItem(adapterPosition) != null)
                articleItemClick.postValue(
                    ArticleItemClickResult(adapterPosition, itemView,
                    getItem(adapterPosition)))
            }
        }

        fun bind(){
            val item = getItem(adapterPosition)


            item.apply {
                imageView.transitionName = title
                imageView.loadImageProgress(progressBar, urlToImage)
                titleTxt.text = title
            }

        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ArticleItem>() {
        override fun areItemsTheSame(oldItem: ArticleItem, newItem: ArticleItem): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: ArticleItem, newItem: ArticleItem): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }
}

class ArticleItemClickResult(val position:Int,val view:View,val item:ArticleItem){

    val imageView: ImageView get() = view.findViewById(R.id.article_image_view)
}