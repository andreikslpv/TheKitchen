package com.andreikslpv.thekitchen.admin

import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

object AdminUtils {

    suspend fun getURL(path: String, storage: FirebaseStorage): String {
        var result = ""
        try {
            result = storage.reference.child(path).downloadUrl.await().toString()
        } catch (e: Exception) { }
        return result
    }
}