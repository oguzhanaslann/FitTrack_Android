package com.oguzhanaslann.commonui

import android.net.Uri

object DeeplinkBuilder {
    const val ONBOARD_DEEPLINK = "https://com.oguzhanaslann.fittrack/feature_onboard"
    const val AUTHENTICATION_DEEPLINK = "https://com.oguzhanaslann.fittrack/feature_authentication"
    const val HOME_DEEPLINK = "https://com.oguzhanaslann.fittrack/feature_home"
    const val CREATE_WORKOUT_DEEPLINK = "https://com.oguzhanaslann.fittrack/feature_create_workout"
    const val WORKOUT_DETAIL_DEEPLINK = "https://com.oguzhanaslann.fittrack/workoutDetail/"

    fun asUri(deeplink: String): Uri = Uri.parse(deeplink)

    fun asUri(deeplink: String, vararg params: Pair<String, String>): Uri {
        val builder = Uri.parse(deeplink).buildUpon()
        params.forEach { (key, value) ->
            builder.appendQueryParameter(key, value)
        }
        return builder.build()
    }
}

fun workoutId(workoutId: String): Pair<String, String> = "workoutId" to workoutId
