package dev.somnath.onlynews.utils.state

sealed class DataState<out R> {
    data class Success<out T>(val data: T): DataState<T>()
    data class Error(val exception: String): DataState<Nothing>()
    data class Extra<out T>(val data: T): DataState<T>()
    object Loading: DataState<Nothing>()
}