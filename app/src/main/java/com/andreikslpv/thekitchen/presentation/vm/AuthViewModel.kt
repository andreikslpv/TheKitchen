package com.andreikslpv.thekitchen.presentation.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.andreikslpv.thekitchen.App
import com.andreikslpv.thekitchen.data.repository.AuthRepository
import com.andreikslpv.thekitchen.domain.UserRepository
import com.andreikslpv.thekitchen.domain.models.User
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class AuthViewModel : ViewModel() {

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var remoteConfig: FirebaseRemoteConfig

    var privacyPolicy = MutableLiveData("")

    private val field = "privacy_policy"

    init {
        App.instance.dagger.inject(this)

        remoteConfig.fetchAndActivate().addOnSuccessListener {
            privacyPolicy.postValue(remoteConfig.getString(field))
        }
    }

    fun signInWithGoogle(idToken: String?) = liveData(Dispatchers.IO) {
        authRepository.firebaseSignInWithGoogle(idToken).collect { response ->
            emit(response)
        }
    }

    fun createUser() = liveData(Dispatchers.IO) {
        val user = User()
        authRepository.getCurrentUser()?.apply {
            user.uid = this.uid
        }
        userRepository.createUser(user).collect { response ->
            emit(response)
        }
    }

}