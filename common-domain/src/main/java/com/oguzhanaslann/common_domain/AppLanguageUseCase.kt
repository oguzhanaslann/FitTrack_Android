package com.oguzhanaslann.common_domain

import android.util.Log
import com.oguzhanaslann.common_data.MemorySource

class AppLanguageUseCase(
    private val memorySource: MemorySource
) {

    fun saveAppLanguageIntoMemory(appLanguage: AppLanguage) {
        memorySource.set(APP_LANGUAGE_KEY, appLanguage)
    }

    fun getAppLanguageFromMemory(): AppLanguage {
        return memorySource.get(APP_LANGUAGE_KEY) ?: AppLanguage.English
    }

    companion object {
        const val APP_LANGUAGE_KEY = "APP_LANGUAGE_KEY"
    }
}
