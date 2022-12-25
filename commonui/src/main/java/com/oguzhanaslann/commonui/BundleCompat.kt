package com.oguzhanaslann.commonui

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

object BundleCompat {
    // get serializable
    inline fun <reified T : java.io.Serializable> getSerializable(bundle: Bundle?, key: String): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle?.getSerializable(key, T::class.java) // new api
        } else {
            bundle?.getSerializable(key) as T? // old api
        }
    }

    // get serializable get T class from params
    inline fun <reified T : Serializable> getSerializable(
        bundle: Bundle?,
        key: String,
        clazz: Class<T>
    ): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle?.getSerializable(key, clazz) // new api
        } else {
            bundle?.getSerializable(key) as T? // old api
        }
    }

    // parcelable
    inline fun <reified T : Parcelable> getParcelable(bundle: Bundle?, key: String): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle?.getParcelable(key, T::class.java) // new api
        } else {
            bundle?.getParcelable(key) as T? // old api
        }
    }

    inline fun <reified T : Parcelable> getParcelable(
        bundle: Bundle?,
        key: String,
        clazz: Class<T>
    ): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle?.getParcelable(key, clazz) // new api
        } else {
            bundle?.getParcelable(key) as T? // old api
        }
    }
}
