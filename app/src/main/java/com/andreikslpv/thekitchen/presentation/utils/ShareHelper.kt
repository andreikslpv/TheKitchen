package com.andreikslpv.thekitchen.presentation.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity

object ShareHelper {

    fun shareThis(context: Context, text: String, title: String) {
        //Создаем интент
        val intent = Intent()
        //Указываем action с которым он запускается
        intent.action = Intent.ACTION_SEND
        //Кладем данные о нашем фильме
        intent.putExtra(Intent.EXTRA_TEXT, text)
        //Указываем MIME тип, чтобы система знала, какое приложения предложить
        intent.type = "text/plain"
        //Запускаем наше активити
        startActivity(
            context,
            Intent.createChooser(intent, title),
            null
        )
    }

}