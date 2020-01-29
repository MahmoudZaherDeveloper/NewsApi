package com.zaher.base.presentation.main

import android.content.Context
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.zaher.base.BuildConfig
import com.zaher.base.entities.News
import com.zaher.base.entities.NewsSource
import com.zaher.base.network.RetrofitFactory
import com.zaher.framework.core.BaseViewModel
import com.zaher.framework.core.NetworkState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author achmad.zaher
 * @date 21-May-19
 */
class MainPageViewModel(context: Context?) : BaseViewModel() {

    private val service =
        if (context == null) RetrofitFactory.makeRetrofitServiceForTest()
        else RetrofitFactory.makeRetrofitService(context)

    //private var countries = "id"
    private val pageSize = 10
    var page = 1
    var query: String? = null
    var country: String = "id"

    val loadMore = ObservableField<Boolean>(false)
    val lastPage = ObservableField<Boolean>(false)
    val firstPage = ObservableField<Boolean>(true)

    val showLoadingView = ObservableField<Boolean>(true)
    val showProgressBar = ObservableField<Boolean>(false)

    var news: MutableLiveData<News> = MutableLiveData()

    fun getNewsFromApi(showLoading: Boolean) {
        if (showLoading) {
            networkState.value = NetworkState.LOADING
        }

        GlobalScope.launch(Dispatchers.Main)
        {
            val request = service.getNewsAsync(
                BuildConfig.API_KEY,
                country,
                pageSize,
                page,
                query
            )
            try {
                val response = request.await()

                val totalPage = (response.body()!!.totalResults + pageSize - 1) / pageSize
                lastPage.set(page == totalPage)
                firstPage.set(page == 1)

                news.value = response.body()!!

                if (response.body()!!.articles.isEmpty()) {
                    networkState.value = NetworkState.EMPTY
                } else {
                    networkState.value = NetworkState.LOADED
                }
            } catch (e: Exception) {
                networkState.value = NetworkState.error(e)
            }
        }
    }

    fun getNewsSourceFromApi(showLoading: Boolean) {
        if (showLoading) {
            networkState.value = NetworkState.LOADING
        }

        GlobalScope.launch(Dispatchers.Main)
        {
            val request = service.getNewsAsync(
                BuildConfig.API_KEY,
                pageSize,
                page,
                query
            )
            try {
                val response = request.await()

                val totalPage = (response.body()!!.totalResults + pageSize - 1) / pageSize
                lastPage.set(page == totalPage)
                firstPage.set(page == 1)

                news.value = response.body()!!

                if (response.body()!!.articles.isEmpty()) {
                    networkState.value = NetworkState.EMPTY
                } else {
                    networkState.value = NetworkState.LOADED
                }
            } catch (e: Exception) {
                networkState.value = NetworkState.error(e)
            }
        }
    }


    var newsSource: MutableLiveData<NewsSource> = MutableLiveData()

    fun getSourceFromApi(showLoading: Boolean) {
        if (showLoading) {
            networkState.value = NetworkState.LOADING
        }

        GlobalScope.launch(Dispatchers.Main)
        {
            val request = service.getDataSource(
                BuildConfig.API_KEY
            )
            try {
                val response = request.await()

                newsSource.value = response.body()!!

                if (response.body()!!.sources.isEmpty()) {
                    networkState.value = NetworkState.EMPTY
                } else {
                    networkState.value = NetworkState.LOADED
                }
            } catch (e: Exception) {
                networkState.value = NetworkState.error(e)
            }
        }
    }


}