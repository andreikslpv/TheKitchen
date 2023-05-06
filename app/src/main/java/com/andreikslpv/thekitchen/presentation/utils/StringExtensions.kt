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
