package com.oguzhanaslann.common

import android.provider.ContactsContract.Data

sealed class State<out T> {
    object Loading : State<Nothing>()
    object Initial : State<Nothing>()
    data class Success<T>(val data: T) : State<T>()
    data class Error(val exception: String) : State<Nothing>()
}

val State<*>.isLoading: Boolean
    get() = this is State.Loading

val State<*>.isSuccess: Boolean
    get() = this is State.Success

val State<*>.isFinalized: Boolean
    get() = (this != State.Loading || this != State.Initial)

inline fun <T> State<T>.isLoading(crossinline block: (isLoading: Boolean) -> Unit): State<T> {
    block(this is State.Loading)
    return this
}

inline fun <T> State<T>.onLoading(crossinline block: () -> Unit): State<T> {
    if (this is State.Loading) {
        block()
    }
    return this
}

inline fun <T> State<T>.onInitial(crossinline block: () -> Unit): State<T> {
    if (this is State.Initial) {
        block()
    }
    return this
}

fun <T> State<T>.onSuccess(block: (T) -> Unit): State<T> {
    if (this is State.Success) {
        block(this.data)
    }
    return this
}

inline fun <T> State<T>.onError(crossinline block: (error: String) -> Unit): State<T> {
    if (this is State.Error) {
        block(this.exception)
    }
    return this
}

inline fun <T> State<T>.isFinalized(crossinline block: () -> Unit): State<T> {
    if (isFinalized) {
        block()
    }
    return this
}

inline fun <reified T, reified R> State<T>.mapByState(
    nullDataError: String = "Data is null",
    crossinline block: (T) -> R?
): State<R> {
    return when (this) {
        is State.Error -> State.Error(this.exception)
        State.Initial -> State.Initial
        State.Loading -> State.Loading
        is State.Success -> {
            when (val r = block(this.data)) {
                null -> State.Error(nullDataError)
                else -> State.Success(r)
            }
        }
    }
}

suspend inline fun <reified T, reified R> State<T>.mapByStateSuspend(
    nullDataError: String = "Data is null",
    crossinline block: suspend (T) -> R?
): State<R> {
    return when (this) {
        is State.Error -> State.Error(this.exception)
        State.Initial -> State.Initial
        State.Loading -> State.Loading
        is State.Success -> {
            when (val r = block(this.data)) {
                null -> State.Error(nullDataError)
                else -> State.Success(r)
            }
        }
    }
}

fun <T> State<T?>.reduceToNotNull(nullException : String): State<T> {
    return when (this) {
        is State.Error -> State.Error(this.exception)
        State.Initial -> State.Initial
        State.Loading -> State.Loading
        is State.Success -> {
            when (val r = this.data) {
                null -> State.Error(nullException)
                else -> State.Success(r)
            }
        }
    }
}

//
//suspend fun <T, K> State<T>.mappedBy(
//    mapper: Mapper<T, K>
//): State<K> {
//    return when (this) {
//        is State.Error -> State.Error(this.exception)
//        State.Initial -> State.Initial
//        State.Loading -> State.Loading
//        is State.Success -> State.Success(mapper(this.data))
//    }
//}

fun <T> State<T>.data(): T? {
    return if (this is State.Success) this.data else null
}


fun <T> Result<T>.toState(): State<T> {
    val finalState = when {
        isSuccess -> State.Success(getOrNull()!!)
        isFailure -> State.Error(exceptionOrNull()!!.message.toString())
        else -> State.Error("Unknown Error")
    }

    return finalState
}

fun <T> Result<T?>.toStateNullable(): State<T?> {
    val finalState = when {
        isSuccess -> State.Success(getOrNull())
        isFailure -> State.Error(exceptionOrNull()!!.message.toString())
        else -> State.Error("Unknown Error")
    }

    return finalState
}
