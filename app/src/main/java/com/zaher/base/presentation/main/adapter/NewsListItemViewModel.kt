package com.zaher.base.presentation.main.adapter

import androidx.databinding.ObservableField
import com.zaher.framework.core.BaseViewModel

/**
 * @author achmad.zaher
 * @date 21-May-19
 */
class NewsListItemViewModel : BaseViewModel() {
    var bTextTitle = ObservableField<String>()
    var bTextTime = ObservableField<String>()
    var bTextContent = ObservableField<String>()
    var bImageUrl = ObservableField<String>()
}