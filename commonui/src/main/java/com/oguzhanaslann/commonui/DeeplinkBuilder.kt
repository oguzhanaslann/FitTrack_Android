package com.oguzhanaslann.commonui

import android.net.Uri

object DeeplinkBuilder {
    const val ONBOARD_DEEPLINK = "https://com.oguzhanaslann.fittrack/feature_onboard"
    const val AUTHENTICATION_DEEPLINK = "https://com.oguzhanaslann.fittrack/feature_authentication"
    const val HOME_DEEPLINK = "https://com.oguzhanaslann.fittrack/feature_home"

    fun asUri(deeplink: String): Uri = Uri.parse(deeplink)
}
