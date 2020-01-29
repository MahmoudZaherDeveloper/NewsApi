package com.zaher.framework.core

/**
 * @author achmad.zaher
 * @date 3-Mar-19
 *
 * ComponentProvider
 * Interface for single component provider to inject from
 */
interface ComponentOwner<T> {

    var binding: T

//    @Suppress("UNCHECKED_CAST")
//    fun <C : T> componentAs(classOfT: Class<C>): C = binding as C

//    fun buildComponent(): T
}
