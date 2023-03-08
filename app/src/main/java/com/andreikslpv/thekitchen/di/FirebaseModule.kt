package com.andreikslpv.thekitchen.di

import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuthInstance() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseDatabaseInstance() =
        FirebaseDatabase.getInstance(App.instance.getString(R.string.firebase_database_url))

}