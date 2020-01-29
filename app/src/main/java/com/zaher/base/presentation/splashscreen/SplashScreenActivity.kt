package com.zaher.base.presentation.splashscreen

import android.os.Bundle
import com.zaher.base.R
import com.zaher.base.databinding.ActivitySplashScreenBinding
import com.zaher.base.presentation.main.MainPageActivity
import com.zaher.framework.core.BaseActivity
import com.zaher.framework.core.ViewDataBindingOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @author achmad.zaher
 * @date 21-May-19
 */
class SplashScreenActivity : BaseActivity(),
    SplashScreenView,
    ViewDataBindingOwner<ActivitySplashScreenBinding> {

    override fun getViewLayoutResId(): Int {
        return R.layout.activity_splash_screen
    }

    private lateinit var viewModel: SplashScreenViewModel
    override lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = SplashScreenViewModel()
        viewModel = binding.vm!!

        GlobalScope.launch(Dispatchers.Main) {
            delay(2000)
            MainPageActivity.startThisActivity(this@SplashScreenActivity)
            finish()
        }
    }
}