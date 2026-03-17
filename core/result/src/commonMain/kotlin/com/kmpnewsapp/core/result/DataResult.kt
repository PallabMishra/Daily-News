package com.kmpnewsapp.core.result

sealed class DataResult<out T> {
    data class Success<T>(val data: T) : DataResult<T>()
    data class Error(val exception: Throwable, val message: String? = null) : DataResult<Nothing>()
    object Loading : DataResult<Nothing>()
}
