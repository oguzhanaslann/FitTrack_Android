package com.oguzhanaslann.common

fun Boolean?.orFalse() = this ?: false
fun Boolean?.orTrue() = this ?: true
fun Boolean?.isTrue() = this == true
fun Boolean?.isFalse() = this == false
fun Int?.orZero() = this ?: 0
fun Long?.orZero() = this ?: 0L
fun Float?.orZero() = this ?: 0f
fun Double?.orZero() = this ?: 0.0
