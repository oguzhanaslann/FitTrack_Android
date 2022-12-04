package com.oguzhanaslann.common

import com.oguzhanaslann.common.MeasurementUnit.Companion.default
import java.io.Serializable

sealed class MeasurementUnit : Serializable {
    object Metric : MeasurementUnit() {
        override fun toString(): String = metric
    }

    object Imperial : MeasurementUnit() {
        override fun toString(): String = imperial
    }

    companion object {

        const val metric = "metric"

        const val imperial = "imperial"

        @JvmStatic
        val default = Metric

        @JvmStatic
        fun parseFrom(unit: String): MeasurementUnit? {
            return when (unit) {
                metric -> Metric
                imperial -> Imperial
                else -> null
            }
        }

        @JvmStatic
        fun parseFrom(unit: String, defaultValue: MeasurementUnit): MeasurementUnit {
            return when (unit) {
                metric -> Metric
                imperial -> Imperial
                else -> defaultValue
            }
        }
    }
}

fun MeasurementUnit?.orDefault(): MeasurementUnit {
    return this ?: default
}
