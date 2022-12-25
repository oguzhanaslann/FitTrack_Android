package com.oguzhanaslann.common_data

object FitTrackMemorySource : MemorySource {
    private val memory = mutableMapOf<String, Any>()
    override fun <T> get(key: String): T? {
        return memory[key] as? T
    }

    override fun <T> set(key: String, value: T) {
        memory[key] = value as Any
    }

    override fun remove(key: String) {
        if (memory.containsKey(key)) {
            memory.remove(key)
        }
    }
}
