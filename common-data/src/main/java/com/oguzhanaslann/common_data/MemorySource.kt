package com.oguzhanaslann.common_data

interface MemorySource {
    fun <T> get(key: String): T?
    fun <T> set(key: String, value: T)
    fun remove(key: String)
}
