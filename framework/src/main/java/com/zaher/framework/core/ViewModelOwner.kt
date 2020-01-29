package com.zaher.framework.core

/**
 * @author achmad.zaher
 * @date 21-Nov-18
 */
interface ViewModelOwner<T : BaseViewModel> {
    val viewModel: T
}
