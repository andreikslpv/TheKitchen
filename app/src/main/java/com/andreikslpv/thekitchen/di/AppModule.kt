package com.andreikslpv.thekitchen.di

import android.content.Intent
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

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