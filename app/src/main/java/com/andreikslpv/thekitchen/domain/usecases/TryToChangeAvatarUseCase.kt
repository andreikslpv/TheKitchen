package com.andreikslpv.thekitchen.domain.usecases

import android.net.Uri
import com.andreikslpv.thekitchen.data.db.StorageConstants.PATH_USERS
import com.andreikslpv.thekitchen.data.repository.AuthRepository
import com.andreikslpv.thekitchen.domain.models.Response
import com.andreikslpv.thekitchen.presentation.utils.Constants
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class TryToChangeAvatarUseCase(
    private val authRepository: AuthRepository,
    private val storage: FirebaseStorage,
) {

    fun execute(localUri: Uri) = flow {
        try {
            emit(Response.Loading)
            val user = authRepository.getCurrentUser()
            if (user != null) {
                // формируем ссылку в storage по которой будет находиться аватарка
                val ref = storage.reference.child("$PATH_USERS/${user.uid}")
                // загружаем локальный файл в storage по сформированной ссылке
                ref.putFile(localUri).await().also { taskSnapshot ->
                    // получаем внешнюю ссылку на загруженный файл
                    taskSnapshot.metadata?.reference?.downloadUrl?.await().also {
                        it?.let {
                            // если ссылка не null меняем аватарку в Firebase Auth
                            authRepository.changeAvatar(it).collect { response ->
                                when (response) {
                                    is Response.Success -> emit(Response.Success(true))
                                    is Response.Failure -> emit(Response.Failure(response.errorMessage))
                                    is Response.Loading -> {}
                                }
                            }
                        }
                    }

                }
            } else {
                emit(Response.Failure("Require auth"))
            }
        } catch (e: Exception) {
            emit(Response.Failure(e.message ?: Constants.ERROR_MESSAGE))
        }
    }

}