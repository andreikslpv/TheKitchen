package com.andreikslpv.thekitchen.di

import com.andreikslpv.thekitchen.data.repository.AuthRepository
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth, googleSignInClient: GoogleSignInClient,): AuthRepository {
        return AuthRepository(auth, googleSignInClient)
    }

//    @Provides
//    @Singleton
//    fun provideMainRepository(
//        googleSignInClient: GoogleSignInClient,
//        auth: FirebaseAuth
//    ): MainRepository {
//        return MainRepository(googleSignInClient, auth)
//    }
}