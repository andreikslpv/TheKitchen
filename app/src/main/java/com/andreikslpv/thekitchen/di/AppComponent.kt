package com.andreikslpv.thekitchen.di

 import android.content.Context
 import com.andreikslpv.thekitchen.presentation.ui.activity.AuthActivity
 import com.andreikslpv.thekitchen.presentation.ui.activity.MainActivity
 import com.andreikslpv.thekitchen.presentation.vm.AuthViewModel
 import com.andreikslpv.thekitchen.presentation.vm.MainViewModel
 import dagger.BindsInstance
 import dagger.Component
 import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DataModule::class, FirebaseModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance context: Context): AppComponent
    }

    //методы для того, чтобы появилась возможность внедрять зависимости в требуемые классы
    fun inject(authActivity: AuthActivity)
    fun inject(mainActivity: MainActivity)
    fun inject(authViewModel: AuthViewModel)
    fun inject(mainViewModel: MainViewModel)
}