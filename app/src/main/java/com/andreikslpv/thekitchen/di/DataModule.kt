package com.andreikslpv.thekitchen.di

import com.andreikslpv.thekitchen.data.repository.AuthRepository
import com.andreikslpv.thekitchen.data.repository.MainRepository
import com.andreikslpv.thekitchen.data.repository.SplashRepository
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth, database: FirebaseDatabase): AuthRepository {
        return AuthRepository(auth, database)
    }

    @Provides
    @Singleton
    fun provideSplashRepository(auth: FirebaseAuth): SplashRepository {
        return SplashRepository(auth)
    }

    @Provides
    @Singleton
    fun provideMainRepository(
        googleSignInClient: GoogleSignInClient,
        auth: FirebaseAuth
    ): MainRepository {
        return MainRepository(googleSignInClient, auth)
    }
}