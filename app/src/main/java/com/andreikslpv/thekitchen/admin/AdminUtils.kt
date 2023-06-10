package com.andreikslpv.thekitchen.admin

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

object AdminUtils {

    suspend fun getURL(path: String, storage: FirebaseStorage): String {
        var result = ""
        try {
            result = storage.reference.child(path).downloadUrl.await().toString()
        } catch (e: Exception) { }
        return result
    }

    fun uploadDb(firestore: FirebaseFirestore, storage: FirebaseStorage) {
        CoroutineScope(Dispatchers.IO).launch {
//            Recipe4.addToDb(firestore, storage)
//            Recipe5.addToDb(firestore, storage)
//            Categories.addToDb(firestore, storage)
//            Products.addToDb(firestore)
        }
    }
}