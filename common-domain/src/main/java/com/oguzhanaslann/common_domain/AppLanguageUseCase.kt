package com.oguzhanaslann.common_domain

import com.oguzhanaslann.common_data.MemorySource

class AppLanguageUseCase(
    private val memorySource: MemorySource,
) {

    fun saveAppLanguageIntoMemory(appLanguage: AppLanguage) {
        memorySource.set(APP_LANGUAGE_KEY, appLanguage)
    }

    fun getAppLanguage(): AppLanguage {
        return memorySource.get(APP_LANGUAGE_KEY) ?: AppLanguage.English
    }

    fun getAppLanguageCode(): String {
        return getAppLanguage().code
    }

    companion object {
        const val APP_LANGUAGE_KEY = "APP_LANGUAGE_KEY"
    }
}
