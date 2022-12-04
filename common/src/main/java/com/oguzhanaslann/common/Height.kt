package com.oguzhanaslann.common

import java.io.Serializable

sealed class Height(
    val magnitudeAsCm: Float,
    val displayText: String,
    val measurementUnit: MeasurementUnit,
) : Serializable {

    abstract fun fromCmMagnitude(): Float

    fun toPair(): Pair<Float, String> {
        return pairOf(this.magnitudeAsCm, this.displayText)
    }

    class Feet(magnitude: Float) : Height(magnitude, displayText, MeasurementUnit.Imperial) {
        override fun fromCmMagnitude(): Float = magnitudeAsCm * FEET_CM_RATIO

        companion object {
            const val FEET_CM_RATIO = 0.0328084f
            const val displayText = "feet"
        }
    }

    class Cm(magnitude: Float) : Height(magnitude, displayText, MeasurementUnit.Metric) {
        override fun fromCmMagnitude(): Float = magnitudeAsCm

        companion object {
            const val displayText = "cm"
        }
    }

    override fun toString(): String {
        return this.fromCmMagnitude().toString() + " " + this.displayText
    }

    companion object {

        val empty = Cm(0f)

        private const val DEFAULT_HEIGHT_IN_CM = 170F // 170 cm

        val default = Cm(DEFAULT_HEIGHT_IN_CM)

        fun parseFrom(height: String?, measurementUnit: MeasurementUnit): Height? {
            val heightText = height?.lowercase()
            return when (measurementUnit) {
                MeasurementUnit.Imperial -> {
                    val heightValue = heightText?.let { getHeightMagnitude(it) }
                    heightValue?.let { Feet(it) }
                }
                MeasurementUnit.Metric -> {
                    val heightValue = heightText?.let { getHeightMagnitude(it) }
                    heightValue?.let { Cm(it) }
                }
            }
        }

        private fun getHeightMagnitude(heightText: String) = heightText.trim()
            .takeIf { it.isNotNullOrEmpty() }
            ?.toFloatOrNull()

        fun fromPair(heightPair: Pair<Float, String>): Height? {
            return when (heightPair.second) {
                Feet.displayText, MeasurementUnit.imperial -> Feet(heightPair.first)
                Cm.displayText, MeasurementUnit.metric -> Cm(heightPair.first)
                else -> null
            }
        }

        fun from(height:Float, measurementUnit: MeasurementUnit): Height? {
            return when (measurementUnit) {
                MeasurementUnit.Imperial -> Feet(height)
                MeasurementUnit.Metric -> Cm(height)
            }
        }
    }
}

fun Height?.isEmpty() = this == null || this.magnitudeAsCm == 0f
fun Height?.isNotEmpty() = this.isEmpty().not()
fun Height?.orDefault() = this ?: Height.default
fun Height?.orEmpty() = this ?: Height.empty
