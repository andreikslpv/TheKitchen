package com.andreikslpv.thekitchen.domain.usecases

import com.andreikslpv.thekitchen.data.db.StorageConstants
import com.andreikslpv.thekitchen.domain.models.Response
import com.andreikslpv.thekitchen.presentation.utils.Constants
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class TryToDeleteAvatarUseCase(
    private val storage: FirebaseStorage,
) {

    fun execute(uid: String) = flow {
        try {
            emit(Response.Loading)
            if (uid.isNotBlank()) {
                // формируем ссылку в storage по которой находится аватарка
                val ref = storage.reference.child("${StorageConstants.PATH_USERS}/${uid}")
                // удаляем файл в storage по сформированной ссылке
                ref.delete().await().also {
                    emit(Response.Success(true))
                }
            } else {
                emit(Response.Failure("Require auth"))
            }
        } catch (e: Exception) {
            emit(Response.Failure(e.message ?: Constants.ERROR_MESSAGE))
        }
    }

}