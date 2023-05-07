package com.andreikslpv.thekitchen.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.firebase.storage.FirebaseStorage
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
    fun provideFirebaseCrashlyticsInstance() = FirebaseCrashlytics.getInstance()

    @Provides
    fun provideFirebaseFirestore() = FirebaseFirestore.getInstance()

    @Provides
    fun provideFirebaseStorage() = FirebaseStorage.getInstance()

//    @Provides
//    @Named(USERS_REF)
//    fun provideUsersRef(db: FirebaseFirestore) = db.collection(USERS_REF)
//
//    @Provides
//    @Named(MOVIES_REF)
//    fun provideCloudFirestoreMoviesRef(db: FirebaseFirestore) = db.collection(MOVIES_REF)
//
//    @Provides
//    @Named(MOVIES_REF)
//    fun provideRealtimeDatabaseMoviesRef(db: FirebaseDatabase) = db.reference.child(MOVIES_REF)

    @Provides
    @Singleton
    fun provideRemoteConfigInstance(): FirebaseRemoteConfig {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val settings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 60
        }
        remoteConfig.setConfigSettingsAsync(settings)
        return remoteConfig
    }

}