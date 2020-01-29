package com.zaher.base.presentation.mainpage

import com.zaher.base.BuildConfig
import com.zaher.base.network.RetrofitFactory
import com.zaher.base.presentation.main.MainPageViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Created by achmad.zaher on 18-Mar-19
 */
class MainPageViewModelTest {
    @Mock
    lateinit var viewModel: MainPageViewModel

    private val service = RetrofitFactory.makeRetrofitServiceForTest()

    @Before
    fun setUp() {
        Dispatchers.resetMain()
        MockitoAnnotations.initMocks(this)
        viewModel = MainPageViewModel(null)
    }

    @Test
    fun getNewsFromApi() {
        Dispatchers.setMain(Dispatchers.Unconfined)

        // expected
        val expectedResult = "ok"

        // actual
        val actualRespone = runBlocking {
            service.getNewsAsync(
                BuildConfig.API_KEY,
                "id",
                10,
                1,
                null
            ).await()
        }
        val actualResult = actualRespone.body()

        delay()

        Assert.assertEquals(expectedResult, actualResult?.status)
    }

    private fun delay() {
        try {
            Thread.sleep(3000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}