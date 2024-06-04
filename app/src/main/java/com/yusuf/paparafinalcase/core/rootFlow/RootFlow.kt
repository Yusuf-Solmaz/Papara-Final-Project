package com.yusuf.paparafinalcase.core.rootFlow

import com.yusuf.paparafinalcase.core.rootResult.RootResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

fun <T> rootFlow(call: suspend () -> Response<T>?): Flow<RootResult<T>> = flow {
    emit(RootResult.Loading)
    try {
        val c = call()
        c?.let {
            if (c.isSuccessful) {
                it.body()?.let {
                    emit(RootResult.Success(c.body()))
                }
            } else {
                emit(RootResult.Error(c.errorBody()?.toString() ?: "Something went wrong"))
            }
        }
    }
    catch (e:Exception){
        emit(RootResult.Error(e.message ?: "Something went wrong"))
    }
}.flowOn(Dispatchers.IO)