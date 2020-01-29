package com.zaher.base.presentation.main

import com.zaher.framework.core.BaseView
import com.zaher.framework.widget.LoadingView

/**
 * @author achmad.zaher
 * @date 21-May-19
 */
interface MainPageView : BaseView {
    var retryListener: LoadingView.OnRetryListener
}