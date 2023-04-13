package com.andreikslpv.thekitchen.admin

import com.andreikslpv.thekitchen.data.db.FirestoreConstants.PATH_PRODUCT
import com.andreikslpv.thekitchen.domain.models.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object Products {

    suspend fun addToDb(firestore: FirebaseFirestore) {

        val collection = firestore.collection(PATH_PRODUCT)
        var id = "pr00000"
        var document = collection.document(id)
        document.set(Product()).await()

        // pr01xxx - разное -------------------------------

        id = "pr01001"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Вода",
                saleUnit = "un00009",   // мл.
            )
        ).await()

        id = "pr01002"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Масло растительное",
                saleUnit = "un00009",   // мл.
            )
        ).await()


        // pr02xxx - рыба и морепродукты ------------------

        id = "pr02001"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Форель радужная",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        id = "pr02002"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Скумбрия",
                saleUnit = "un00007",   // шт.
            )
        ).await()

        id = "pr02003"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Семга соленая",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        id = "pr02004"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Кальмар",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        id = "pr02005"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Мидии замороженные",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        id = "pr02006"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Креветки",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        id = "pr02007"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Икра красная",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        // pr03xxx - мясо (кроме свинины) -----------------
        id = "pr03001"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Филе куриное",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        id = "pr03002"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Курица копченая",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        id = "pr03003"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Говядина",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        id = "pr03004"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Окорочок куриный",
                saleUnit = "un00007",   // шт.
            )
        ).await()

        id = "pr03005"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Печень куриная",
                saleUnit = "un00003",   // гр.
            )
        ).await()


        // pr04xxx - мясо свинина -------------------------

        id = "pr04001"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Свинина",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        id = "pr04002"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Сало свиное",
                saleUnit = "un00003",   // гр.
            )
        ).await()


        // pr05xxx - овощи и зелень (кроме лука)-----------

        id = "pr05001"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Картофель",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        id = "pr05002"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Морковь",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        id = "pr05003"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Шпинат",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        id = "pr05004"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Укроп",
                saleUnit = "un00008",   // пучок
            )
        ).await()


        id = "pr05005"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Петрушка",
                saleUnit = "un00008",   // пучок
            )
        ).await()

        id = "pr05006"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Капуста белокачанная",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        id = "pr05007"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Брокколи",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        id = "pr05008"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Огурец",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        id = "pr05009"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Редис",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        id = "pr05010"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Корень сельдерея",
                saleUnit = "un00007",   // шт.
            )
        ).await()

        id = "pr05011"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Базилик",
                saleUnit = "un00008",   // пучок
            )
        ).await()

        id = "pr05012"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Перец горький",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        id = "pr05013"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Перец сладкий",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        id = "pr05014"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Помидор",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        id = "pr05015"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Тыква",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        id = "pr05016"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Салат зеленый",
                saleUnit = "un00008",   // пучок
            )
        ).await()

        // pr06xxx - лук ----------------------------------

        id = "pr06001"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Лук репчатый",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        id = "pr06002"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Лук зеленый",
                saleUnit = "un00008",   // пучок
            )
        ).await()


        // pr07xxx - молоко и молокосодержащие ------------

        id = "pr07001"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Сливки",
                saleUnit = "un00009",   // мл.
            )
        ).await()

        id = "pr07002"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Масло сливочное",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        id = "pr07003"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Кефир",
                saleUnit = "un00009",   // мл.
            )
        ).await()

        id = "pr07004"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Сыр творожный",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        id = "pr07005"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Сыр плавленый",
                saleUnit = "un00003",   // гр.
            )
        ).await()


        // pr08xxx - специи -----------------------------
        id = "pr08001"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Соль",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        id = "pr08002"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Перец черный молотый",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        id = "pr08003"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Перец душистый",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        id = "pr08004"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Лавровый лист",
                saleUnit = "un00007",   // шт.
            )
        ).await()

        id = "pr08005"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Чеснок",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        id = "pr08006"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Кориандр",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        id = "pr08007"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Хмели-сунели",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        id = "pr08008"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Перец черный горошек",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        // pr09xxx - соусы --------------------------------

        id = "pr09001"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Паста томатная",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        id = "pr09002"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Соус Ткемали",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        id = "pr09003"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Майонез",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        // pr10xxx - крупы и производные без глютена--------

        id = "pr10001"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Рис шлифованный",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        id = "pr10002"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Рис басмати",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        id = "pr10003"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Рис пропаренный",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        id = "pr10004"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Мука кукурузная",
                saleUnit = "un00003",   // гр.
            )
        ).await()


        // pr11xxx - крупы и производные с глютеном---------

        id = "pr11001"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Крупа перловая",
                saleUnit = "un00003",   // гр.
            )
        ).await()


        // pr12xxx - яйца и прочее животного происхождения-

        id = "pr12001"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Яйцо куриное",
                saleUnit = "un00007",   // шт.
            )
        ).await()

        id = "pr12002"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Яйцо перепелиное",
                saleUnit = "un00007",   // шт.
            )
        ).await()


        // pr13xxx - сахар и сахаросодержащие продукты------


        // pr14xxx - грибы ---------------------------------

        id = "pr14001"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Шампиньоны",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        // pr15xxx - соленья, консервы --------------------------------

        id = "pr15001"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Капуста квашеная",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        id = "pr15002"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Кукуруза консервированная",
                saleUnit = "un00016",   // банка
            )
        ).await()

        id = "pr15003"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Огурец маринованный",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        id = "pr15004"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Тушенка говяжья",
                saleUnit = "un00016",   // банка
            )
        ).await()

        id = "pr15005"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Огурец соленый",
                saleUnit = "un00003",   // гр.
            )
        ).await()


        // pr16xxx - полуфабрикаты ----------------------------

        id = "pr16001"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Колбаса вареная",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        id = "pr16002"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Крабовые палочки",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        // pr17xxx - фрукты и производные ---------------------

        id = "pr17001"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Авокадо",
                saleUnit = "un00007",   // шт.
            )
        ).await()

        id = "pr17002"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Сок лимонный",
                saleUnit = "un00009",   // мл.
            )
        ).await()

        id = "pr17003"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Банан",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        id = "pr17004"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Сок апельсиновый",
                saleUnit = "un00009",   // мл.
            )
        ).await()

        // pr18xxx - орехи сухофрукты ---------------------

        id = "pr18001"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Чернослив",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        id = "pr18002"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Орех грецкий",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        id = "pr18003"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Орех мускатный",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        id = "pr18004"
        document = collection.document(id)
        document.set(
            Product(
                id = id,
                name = "Фундук",
                saleUnit = "un00003",   // гр.
            )
        ).await()

        //-----------------------------------------
        //-----------------------------------------

    }


}