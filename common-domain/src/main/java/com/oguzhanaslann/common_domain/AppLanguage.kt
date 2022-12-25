package com.oguzhanaslann.common_domain

sealed class AppLanguage(val code: String) {
    object English : AppLanguage("en")
    object Turkish : AppLanguage("tr")

    companion object {
        fun fromLanguage(language: String): AppLanguage {
            return when (language) {
                English.code -> English
                Turkish.code -> Turkish
                else -> English
            }
        }
    }
}
