package com.yusuf.paparafinalcase.core.rootResult

sealed class RootResult <out T> {
    data class Success<out R>(val data: R?) : RootResult<R>()
    data class Error(val message:String) : RootResult<Nothing>()
    data object Loading : RootResult<Nothing>()
}