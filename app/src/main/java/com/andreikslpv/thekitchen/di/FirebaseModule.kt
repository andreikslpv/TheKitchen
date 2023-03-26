package com.andreikslpv.thekitchen.di

import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
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

    @Provides
    @Singleton
    fun provideRemoteConfigInstance(): FirebaseRemoteConfig {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val settings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 600
        }
        remoteConfig.setConfigSettingsAsync(settings)
        return remoteConfig
    }

}