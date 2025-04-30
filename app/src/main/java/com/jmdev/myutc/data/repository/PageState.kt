package com.jmdev.myutc.data.repository

sealed class PageState<out T> {
    data class Success<out T>(val data: T) : PageState<T>()
    object Loading : PageState<Nothing>()
    data class Error(val exception: Throwable) : PageState<Nothing>()
}