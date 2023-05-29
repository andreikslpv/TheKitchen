package com.andreikslpv.thekitchen.presentation.utils

import kotlin.math.pow
import kotlin.math.roundToInt

fun Double.roundTo(numFractionDigits: Int): Double {
    val factor = 10.0.pow(numFractionDigits.toDouble())
    return (this * factor).roundToInt() / factor
}

fun Double.ingredientCountToString(): String {
    if (this == 0.0) return ""
    return if (this % 1 == 0.0) this.toInt().toString()
    else this.toString()
}