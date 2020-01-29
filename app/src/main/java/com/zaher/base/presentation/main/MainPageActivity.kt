package com.zaher.base.presentation.main

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zaher.base.R
import com.zaher.base.databinding.ActivityMainPageBinding
import com.zaher.base.entities.SourceItem
import com.zaher.base.presentation.main.adapter.NewsListItemAdapter
import com.zaher.framework.core.BaseActivity
import com.zaher.framework.core.EndlessRecyclerViewScrollListener
import com.zaher.framework.core.NetworkState
import com.zaher.framework.core.ViewDataBindingOwner
import com.zaher.framework.widget.LoadingView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * @author achmad.zaher
 * @date 21-May-19
 */
class MainPageActivity : BaseActivity(),
    MainPageView,
    ViewDataBindingOwner<ActivityMainPageBinding> {


    private var countrySpinner: Spinner? = null
    private var newsSpinner: Spinner? = null
    var spinnerListForNews = ArrayList<String>()
    var idListForNews = ArrayList<String>()
    var spinnerListForCountries = ArrayList<String>()
    var newsSourceContainer = ArrayList<SourceItem>()
    var flag = false


    override fun getViewLayoutResId(): Int {
        return com.zaher.base.R.layout.activity_main_page
    }

    companion object {
        fun startThisActivity(context: Context) {
            val intent = Intent(context, MainPageActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var viewModel: MainPageViewModel
    override lateinit var binding: ActivityMainPageBinding

    private lateinit var listAdapter: NewsListItemAdapter

    override var retryListener: LoadingView.OnRetryListener = object : LoadingView.OnRetryListener {
        override fun onRetry() {
            viewModel.getNewsFromApi(true)
        }
    }

    private var doubleBackPressed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = MainPageViewModel(this)
        viewModel = binding.vm!!

        initUI()

        viewModel.getNewsFromApi(true)
        observeNews()
        observeSources()
        observeNetworkState()

        viewModel.getSourceFromApi(false)


    }

    private fun initUI() {
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(false)
            it.setHomeButtonEnabled(false)
        }

        title = getString(com.zaher.base.R.string.main_title)

        listAdapter = NewsListItemAdapter()
        binding.rvList.adapter = listAdapter
    }

    private fun observeNetworkState() {
        observeData(viewModel.getNetworkState()) { networkState ->
            networkState?.let {
                when (it) {
                    NetworkState.LOADING -> {
                        viewModel.showLoadingView.set(true)
                    }
                    NetworkState.LOADED -> {
                        viewModel.showLoadingView.set(false)
                        viewModel.showProgressBar.set(false)
                    }
                    NetworkState.EMPTY -> {
                        viewModel.showLoadingView.set(true)
                        binding.loadingView.showEmpty(
                            getString(com.zaher.base.R.string.empty_title),
                            getString(com.zaher.base.R.string.empty_msg),
                            null,
                            com.zaher.base.R.drawable.img_alert,
                            false
                        )
                    }
                    else -> it.exception?.let { e ->
                        viewModel.showProgressBar.set(false)
                        viewModel.showLoadingView.set(true)
                        binding.loadingView.showError(
                            e,
                            getString(com.zaher.base.R.string.error_title),
                            getString(com.zaher.base.R.string.error_msg)
                        )
                    }
                }
            }
        }
    }

    private fun observeNews() {
        observeData(viewModel.news) { result ->
            result?.articles?.let {
                if (viewModel.firstPage.get()!!) {
                    listAdapter.setData(it)
                    binding.rvList.clearOnScrollListeners()
                    binding.rvList.layoutManager?.let {
                        binding.rvList.addOnScrollListener(
                            createScrollListener()
                        )
                    }
                } else {
                    listAdapter.addData(it)
                }
                viewModel.showProgressBar.set(false)
                viewModel.loadMore.set(false)
            }
        }
    }

    private fun observeSources() {
        observeData(viewModel.newsSource) { result ->
            result?.sources?.let {
                newsSourceContainer = result.sources as ArrayList<SourceItem>
                fillListWithRequiredData(result.sources)
            }

        }
    }

    private fun fillListWithRequiredData(arrayList: java.util.ArrayList<SourceItem>) {
        spinnerListForNews.add("Select News Source")
        spinnerListForCountries.add("Select Country ")
        for (i in arrayList.indices) {
            spinnerListForNews.add(arrayList[i].id)
            spinnerListForCountries.add(arrayList[i].country)
        }
    }


    private fun createScrollListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(binding.rvList.layoutManager as LinearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                if (!viewModel.lastPage.get()!! && !viewModel.loadMore.get()!!) {
                    viewModel.loadMore.set(true)
                    viewModel.showProgressBar.set(true)
                    viewModel.page++
                    viewModel.getNewsFromApi(false)
                }
            }

            override fun onLoadPosition(lastPosition: Int, view: RecyclerView) {
                //ignore
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(com.zaher.base.R.menu.filter, menu)
        val filterItem = menu.findItem(com.zaher.base.R.id.menu_filter)
        return super.onCreateOptionsMenu(menu)
        // Retrieve the SearchView and plug it into SearchManager
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // Handle presses on the action bar menu items
        if (item != null) {
            when (item.itemId) {
                R.id.menu_filter -> {
                    // newFilterAlert();
                    showFilterDialog()
                    return true
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showFilterDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.cutsom_filter_layout)
        val filterBtn = dialog.findViewById(R.id.filter_button) as Button
        //  val newsSelector = dialog.findViewById(R.id.news_selector) as EditText
        val countryRadioBtn = dialog.findViewById(R.id.radioButton1) as RadioButton
        val newsRadioBtn = dialog.findViewById(R.id.radioButton2) as RadioButton
        newsSpinner = dialog.findViewById(R.id.news_selector_spinner) as Spinner
        countrySpinner = dialog.findViewById(R.id.country_selector_spinner) as Spinner

        val newsDataAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, spinnerListForNews
        )
        newsDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        newsSpinner!!.adapter = newsDataAdapter

        val countryDataAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, spinnerListForCountries
        )
        countryDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        countrySpinner!!.adapter = countryDataAdapter

        //addItemsOnSpinner2();
        val cancelBtn = dialog.findViewById(R.id.cancel_button) as Button
        val closeBtn = dialog.findViewById(R.id.imageView) as ImageView
        filterBtn.setOnClickListener {
            if (newsRadioBtn.isChecked) {
                flag = true
                val selectedNewId = newsSpinner!!.selectedItem
                callGetNewsApi(selectedNewId as String)
                dialog.dismiss()

            } else if (countryRadioBtn.isChecked) {
                val selectedItem = countrySpinner!!.selectedItem
                callGetNewsApi(selectedItem as String)
                dialog.dismiss()
            }
        }
        cancelBtn.setOnClickListener { dialog.dismiss() }
        closeBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()


    }

    private fun callGetNewsApi(query: String) {
        viewModel.page = 1
        title = query
        if (flag) {
            title = "News Source: " + query
            // this mean we will call source news
            viewModel.query = query
            viewModel.getNewsSourceFromApi(true)
            flag = false


        } else {
            title = "Country : " + query
            // this mean we will call country news
            viewModel.country = query
            viewModel.getNewsFromApi(true)

        }
    }

    override fun onBackPressed() {
        if (doubleBackPressed) {
            super.onBackPressed()
            return
        }
        this.doubleBackPressed = true
        Toast.makeText(this, getString(R.string.app_msg_close), Toast.LENGTH_SHORT).show()

        GlobalScope.launch(Dispatchers.Main) {
            delay(2000)
            doubleBackPressed = false
        }
    }
}