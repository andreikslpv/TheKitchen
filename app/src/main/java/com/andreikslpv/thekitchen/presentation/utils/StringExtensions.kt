package com.andreikslpv.thekitchen.presentation.utils

import android.content.Context
import android.widget.Toast

fun String.makeToast(context: Context) {
    Toast.makeText(
        context,
        this,
        Toast.LENGTH_LONG
    ).show()
}

fun String.getOnlyDigital(): Int {
    val temp = this.filter {
        it in listOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
    }
    return try {
        temp.toInt()
    } catch (e: Exception) {
        0
    }
}