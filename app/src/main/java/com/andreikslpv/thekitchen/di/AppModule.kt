package com.andreikslpv.thekitchen.di

import android.app.Application
import android.content.Context
import android.content.Intent
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.R
import com.andreikslpv.thekitchen.presentation.ui.activity.AuthActivity
import com.andreikslpv.thekitchen.presentation.ui.activity.MainActivity
import com.andreikslpv.thekitchen.presentation.ui.activity.SplashActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    @SplashIntent
    fun provideSplashIntent(context: Context): Intent {
        return Intent(context, SplashActivity::class.java)
    }

    @Provides
    @Singleton
    @AuthIntent
    fun provideAuthIntent(context: Context): Intent {
        return Intent(context, AuthActivity::class.java)
    }

    @Provides
    @Singleton
    @MainIntent
    fun provideMainIntent(context: Context): Intent {
        return Intent(context, MainActivity::class.java)
    }

//    @Singleton
//    @MainIntent
//    fun provideApplication(context: Context): Application {
//        return context.
//    }

    @Provides
    @Singleton
    fun provideGoogleSignInOptions(): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(App.instance.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    @Provides
    @Singleton
    fun provideGoogleSignInClient(
//        application: Application,
        options: GoogleSignInOptions
    ): GoogleSignInClient {
        return GoogleSignIn.getClient(App.instance, options)
    }

    @Provides
    @Singleton
    fun provideSignInIntent(googleSignInClient: GoogleSignInClient): Intent {
        return googleSignInClient.signInIntent
    }
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class SplashIntent

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AuthIntent

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class MainIntent