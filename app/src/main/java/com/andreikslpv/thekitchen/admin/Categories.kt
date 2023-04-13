package com.andreikslpv.thekitchen.admin

import com.andreikslpv.thekitchen.data.db.FirestoreConstants
import com.andreikslpv.thekitchen.domain.models.Category
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

object Categories {
    
    suspend fun addToDb(firestore: FirebaseFirestore, storage: FirebaseStorage) {
        val collection = firestore.collection(FirestoreConstants.PATH_CATEGORY)
        var id = "ca00000"
        var document = collection.document(id)
        document.set(
            Category(
                id = id,
                name = "category",
                type = "ct00000",
                image = AdminUtils.getURL("category/${id}.png", storage),
            )
        ).await()

        id = "ca00001"
        document = collection.document(id)
        document.set(
            Category(
                id = id,
                name = "До 30 минут",
                type = "ct00001",
                image = AdminUtils.getURL("category/${id}.png", storage),
            )
        ).await()

        id = "ca00002"
        document = collection.document(id)
        document.set(
            Category(
                id = id,
                name = "До 40 минут",
                type = "ct00001",
                image = AdminUtils.getURL("category/${id}.png", storage),
            )
        ).await()

        id = "ca00003"
        document = collection.document(id)
        document.set(
            Category(
                id = id,
                name = "До 60 минут",
                type = "ct00001",
                image = AdminUtils.getURL("category/${id}.png", storage),
            )
        ).await()

        id = "ca00004"
        document = collection.document(id)
        document.set(
            Category(
                id = id,
                name = "Супы",
                type = "ct00002",
                image = AdminUtils.getURL("category/${id}.png", storage),
            )
        ).await()

        id = "ca00005"
        document = collection.document(id)
        document.set(
            Category(
                id = id,
                name = "Салаты",
                type = "ct00002",
                image = AdminUtils.getURL("category/${id}.png", storage),
            )
        ).await()

        id = "ca00006"
        document = collection.document(id)
        document.set(
            Category(
                id = id,
                name = "Завтраки",
                type = "ct00002",
                image = AdminUtils.getURL("category/${id}.png", storage),
            )
        ).await()

        id = "ca00007"
        document = collection.document(id)
        document.set(
            Category(
                id = id,
                name = "Выпечка",
                type = "ct00002",
                image = AdminUtils.getURL("category/${id}.png", storage),
            )
        ).await()

        id = "ca00008"
        document = collection.document(id)
        document.set(
            Category(
                id = id,
                name = "Горячее",
                type = "ct00002",
                image = AdminUtils.getURL("category/${id}.png", storage),
            )
        ).await()

        id = "ca00009"
        document = collection.document(id)
        document.set(
            Category(
                id = id,
                name = "Десерты",
                type = "ct00002",
                image = AdminUtils.getURL("category/${id}.png", storage),
            )
        ).await()

        id = "ca00010"
        document = collection.document(id)
        document.set(
            Category(
                id = id,
                name = "Запеканки",
                type = "ct00002",
                image = AdminUtils.getURL("category/${id}.png", storage),
            )
        ).await()

        id = "ca00011"
        document = collection.document(id)
        document.set(
            Category(
                id = id,
                name = "Заготовки",
                type = "ct00002",
                image = AdminUtils.getURL("category/${id}.png", storage),
            )
        ).await()

        id = "ca00012"
        document = collection.document(id)
        document.set(
            Category(
                id = id,
                name = "Без мяса",
                type = "ct00003",
                image = AdminUtils.getURL("category/${id}.png", storage),
            )
        ).await()

        id = "ca00013"
        document = collection.document(id)
        document.set(
            Category(
                id = id,
                name = "Без сахара",
                type = "ct00003",
                image = AdminUtils.getURL("category/${id}.png", storage),
            )
        ).await()

        id = "ca00014"
        document = collection.document(id)
        document.set(
            Category(
                id = id,
                name = "Без лактозы",
                type = "ct00003",
                image = AdminUtils.getURL("category/${id}.png", storage),
            )
        ).await()

        id = "ca00015"
        document = collection.document(id)
        document.set(
            Category(
                id = id,
                name = "Без глютена",
                type = "ct00003",
                image = AdminUtils.getURL("category/${id}.png", storage),
            )
        ).await()

        id = "ca00016"
        document = collection.document(id)
        document.set(
            Category(
                id = id,
                name = "Без лука",
                type = "ct00003",
                image = AdminUtils.getURL("category/${id}.png", storage),
            )
        ).await()

        id = "ca00017"
        document = collection.document(id)
        document.set(
            Category(
                id = id,
                name = "Без свинины",
                type = "ct00003",
                image = AdminUtils.getURL("category/${id}.png", storage),
            )
        ).await()
    }

}