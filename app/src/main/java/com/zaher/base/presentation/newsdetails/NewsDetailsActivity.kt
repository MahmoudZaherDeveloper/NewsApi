package com.zaher.base.presentation.newsdetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.zaher.base.R
import com.zaher.framework.core.BaseActivity

class NewsDetailsActivity : BaseActivity() {

    companion object {
        fun startThisActivity(context: Context, headline: String, desc: String, pubDate: String, url: String) {
            val intent = Intent(context, NewsDetailsActivity::class.java)
            intent.putExtra("headline", headline)
            intent.putExtra("desc", desc)
            intent.putExtra("date", pubDate)
            intent.putExtra("imageUrl", url)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_details)
        initializeResources()

    }


    private fun initializeResources() {
        title = "News Details"
        val tvHeader: TextView = findViewById<TextView>(R.id.tv_header)
        val tvDesc: TextView = findViewById<TextView>(R.id.tv_desc)
        val tvSourceUrl: TextView = findViewById<TextView>(R.id.tv_source_url)
        val tvAuthor: TextView = findViewById<TextView>(R.id.tv_author)

        val bundle: Bundle? = intent.extras
        var headline = bundle!!.getString("headline") // 1
        var desc = bundle.getString("desc") // 1
        var date = bundle.getString("date") // 1
        var imgUrl = bundle.getString("imageUrl") // 1

        tvHeader.text = headline
        tvDesc.text = desc
        tvAuthor.text = date
        tvSourceUrl.text = "Source URL: " + imgUrl
    }


}
