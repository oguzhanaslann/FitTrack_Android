package com.oguzhanaslann.common

fun interface Mapper<in I, out O> {
    suspend fun map(input: I): O
}

suspend fun <T, R> Iterable<T>.mapBy(mapper: Mapper<T,R>): List<R> {
    return this.map { mapper.map(it) }
}
