package com.oguzhanaslann.common
import java.io.Serializable

sealed class Weight(
    val magnitudeAsKg: Float,
    val displayText: String,
    val measurementUnit: MeasurementUnit
) : Serializable {

    abstract fun fromKgMagnitude(): Float

    fun toPair(): Pair<Float, String> {
        return pairOf(magnitudeAsKg,displayText);
    }

    class Kg(magnitudeAsKg: Float) : Weight(magnitudeAsKg, displayText, MeasurementUnit.Metric) {
        override fun fromKgMagnitude(): Float = magnitudeAsKg

        companion object {
            const val displayText = "kg"
        }

    }

    class Lbs(magnitudeAsKg: Float) : Weight(magnitudeAsKg, displayText, MeasurementUnit.Imperial) {
        override fun fromKgMagnitude(): Float = magnitudeAsKg * LBS_KG_RATIO

        companion object {
            const val LBS_KG_RATIO = 2.204623f
            const val displayText = "lbs"
        }
    }

    override fun toString(): String {
        return this.fromKgMagnitude().toString() + " " + this.displayText
    }


    companion object {

        val empty = Kg(0f)

        const val DEFAULT_WEIGHT_IN_KGS = 70F

        val default = Kg(DEFAULT_WEIGHT_IN_KGS)


        fun parseFrom(height: String?, measurementUnit: MeasurementUnit): Weight? {
            val weightText = height?.lowercase()
            return when (measurementUnit) {
                MeasurementUnit.Imperial -> {
                    val heightValue = weightText?.let { getWeightMagnitude(it) }
                    heightValue?.let { Lbs(it) }
                }
                MeasurementUnit.Metric -> {
                    val heightValue = weightText?.let { getWeightMagnitude(it) }
                    heightValue?.let { Kg(it) }
                }
            }
        }

        private fun getWeightMagnitude(heightText: String) = heightText.trim()
            .takeIf { it.isNotNullOrEmpty() }
            ?.toFloatOrNull()


        fun fromPair(weightPair: Pair<Float, String>): Weight? {
            return when (weightPair.second) {
                Kg.displayText, MeasurementUnit.metric -> Kg(weightPair.first)
                Lbs.displayText, MeasurementUnit.imperial -> Lbs(weightPair.first)
                else -> null
            }
        }

        fun from(weight:Float,measurementUnit: MeasurementUnit): Weight? {
            return when (measurementUnit) {
                MeasurementUnit.Imperial -> Lbs(weight)
                MeasurementUnit.Metric -> Kg(weight)
            }
        }
    }
}

fun Weight?.isEmpty() = this == null || this.magnitudeAsKg == 0f
fun Weight?.isNotEmpty() = this.isEmpty().not()
fun Weight?.orDefault() = this ?: Weight.default
fun Weight?.orEmpty() = this ?: Weight.empty
