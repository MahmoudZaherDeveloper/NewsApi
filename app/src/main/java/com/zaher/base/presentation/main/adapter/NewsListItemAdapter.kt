package com.zaher.base.presentation.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zaher.base.R
import com.zaher.base.databinding.ItemNewsBinding
import com.zaher.base.entities.NewsItem
import com.zaher.base.presentation.newsdetails.NewsDetailsActivity
import com.zaher.framework.core.BaseRecycleViewAdapter
import com.zaher.framework.core.BaseViewHolder
import com.zaher.framework.core.GlideApp
import com.zaher.framework.core.ViewDataBindingOwner
import com.zaher.framework.util.DateHelper

/**
 * @author achmad.zaher
 * @date 21-May-19
 */
class NewsListItemAdapter : BaseRecycleViewAdapter<NewsItem>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<NewsItem> {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return ViewHolder(parent.context, view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<NewsItem>, position: Int) {
        (holder as ViewHolder).bindData(getItem(position))
    }

    class ViewHolder(context: Context, view: View) :
        BaseViewHolder<NewsItem>(context, view),
        NewsListItemView,
        ViewDataBindingOwner<ItemNewsBinding> {

        override lateinit var binding: ItemNewsBinding
        private var viewModel: NewsListItemViewModel? = null
        private var data: NewsItem? = null

        init {
            binding.vm = NewsListItemViewModel()
            binding.view = this
            viewModel = binding.vm
        }

        override fun bindData(data: NewsItem) {
            data.let {
                viewModel?.bTextTitle?.set(it.title)
                viewModel?.bTextTime?.set(
                    DateHelper.changeFormat(
                        it.publishedAt,
                        DateHelper.yyyy_MM_dd_T_HHmmss_Z,
                        DateHelper.dd_MMMM_yyyy
                    )
                )
                viewModel?.bTextContent?.set(it.content)
                viewModel?.bImageUrl?.set(it.urlToImage)
                GlideApp.with(context)
                    .load(it.urlToImage)
                    .thumbnail(0.1f)
                    .placeholder(R.drawable.bg_placeholder)
                    .error(R.drawable.bg_placeholder)
                    .into(binding.imgNews)
            }
        }

        override fun onClickItem(view: View) {
            //Toast.makeText(context, "Test Click", Toast.LENGTH_LONG).show()
            val header = viewModel?.bTextTitle?.get()
            val desc = viewModel?.bTextContent?.get()
            val date = viewModel?.bTextTime?.get()
            val imageUrl = viewModel?.bImageUrl?.get()
            NewsDetailsActivity.startThisActivity(
                context,
                header.toString(),
                desc.toString(),
                date.toString(),
                imageUrl.toString()
            )

        }
    }
}