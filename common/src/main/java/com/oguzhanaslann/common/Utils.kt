package com.oguzhanaslann.common

import android.os.Bundle
import org.json.JSONObject
import java.util.*

fun <A, B> pairOf(first: A, second: B): Pair<A, B> = first to second

fun <T> generateListOf(
    size: Int,
    block: () -> T,
): List<T> {
    val mutableList = mutableListOf<T>()
    repeat(size) {
        mutableList.add(block())
    }
    return mutableList
}

fun <T> generateListOfIndexed(
    size: Int,
    block: (Int) -> T,
): List<T> {
    val mutableList = mutableListOf<T>()
    repeat(size) {
        mutableList.add(block(it))
    }
    return mutableList
}

fun uriToMap(uri: String?): Map<String, String> {
    var uriParamPart = uri?.substringAfterLast("/")
    uriParamPart = uriParamPart?.substringAfterLast("?")
    val parameters = uriParamPart?.split("&")
    val dictionary = mutableMapOf<String, String>()

    parameters?.forEach { param ->
        val pair = param.split("=")
        if (pair.size == 2) {
            val key = pair[0]
            val value = pair[1]
            dictionary[key] = value
        }
    }

    return dictionary
}

inline fun onlyIf(block: () -> Boolean): Boolean? {
    return if (block()) true else null
}

fun <T> List<T>.secondOrNull(): T? {
    return if (this.size > 1) {
        this[1]
    } else {
        null
    }
}

fun <T> List<T>.second(): T {
    return if (this.size > 1) {
        this[1]
    } else {
        throw IllegalArgumentException("List has less than 2 elements")
    }
}

fun String.firstLetterOrEmpty(): String {
    return firstOrNull()?.toString() ?: ""
}

fun String.capitalized(): String {
    return replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}

fun <K, V> Map<out K, V>.toJsonObject(): JSONObject {
    return JSONObject(this.toMap())
}

fun String.indexRangeOf(sub: String): Pair<Int, Int>? {
    val start = indexOf(sub)
    return when (start != -1) {
        true -> Pair(start, start + sub.length - 1)
        false -> null
    }
}

/**
 * Extension function to convert map to bundle
 * The map most of the time contains small number of key value pairs. So it is safe to use loop to convert it to bundle
 * */
fun Map<String, Any>.toBundle(): Bundle {
    val bundle = Bundle()
    forEach {
        bundle.putString(it.key, it.value.toString())
    }
    return bundle
}



fun List<Int>.sum(): Int {
    var sum = 0
    for (i in this) {
        sum += i
    }
    return sum
}

val Number.isNegative: Boolean
    get() = this.toInt() < 0
