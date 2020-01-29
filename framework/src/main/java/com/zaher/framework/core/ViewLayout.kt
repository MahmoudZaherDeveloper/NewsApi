package com.zaher.framework.core

import android.view.View
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull

/**
 * @author achmad.zaher
 * @date 21-Nov-18
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ViewLayout(@LayoutRes @NonNull val value: Int = View.NO_ID)
