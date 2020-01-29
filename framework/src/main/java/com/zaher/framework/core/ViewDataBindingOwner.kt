package com.zaher.framework.core

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * @author achmad.zaher
 * @date 21-Nov-18
 */
interface ViewDataBindingOwner<T : ViewDataBinding> {

    var binding: T

    fun setViewBinding(view: View) {
        binding = DataBindingUtil.bind(view)!!
    }

    fun setContentViewBinding(activity: Activity, layoutResId: Int) {
        binding = DataBindingUtil.setContentView<T>(activity, layoutResId)
    }

    fun inflateContentViewBinding(
        inflater: LayoutInflater, container: ViewGroup?,
        layoutResId: Int
    ): View {
        binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        return binding.root
    }
}
