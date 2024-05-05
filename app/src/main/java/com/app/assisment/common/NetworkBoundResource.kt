package com.app.assisment.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = flow {
    val data = query().first()

    val flow = if (shouldFetch(data)) {
        emit(DataStatus.Loading(data))

        try {
            saveFetchResult(fetch())
            query().map { DataStatus.Success(it) }
        } catch (throwable: Throwable) {
            query().map { DataStatus.Error(throwable, it) }
        }
    } else {
        query().map { DataStatus.Success(it) }
    }

    emitAll(flow)
}