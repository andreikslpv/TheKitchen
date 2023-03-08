package com.andreikslpv.thekitchen

import android.app.Application
import com.andreikslpv.thekitchen.di.AppComponent
import com.andreikslpv.thekitchen.di.DaggerAppComponent

class App : Application() {
    lateinit var dagger: AppComponent

    override fun onCreate() {
        super.onCreate()

        //Инициализируем экземпляр App, через который будем получать доступ к остальным переменным
        instance = this
        //Создаем компонент
        dagger = DaggerAppComponent.factory().create(this)
    }

    companion object {
        //Здесь статически хранится ссылка на экземпляр App
        lateinit var instance: App
            //Приватный сеттер, чтобы нельзя было в эту переменную присвоить что-либо другое
            private set
    }
}