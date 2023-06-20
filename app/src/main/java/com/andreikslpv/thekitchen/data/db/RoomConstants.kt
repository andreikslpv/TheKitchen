package com.andreikslpv.thekitchen.data.db

object RoomConstants {
    const val DATABASE_NAME = "recipe_db"

    const val TABLE_CACHED_CATEGORY_TYPE = "cached_category_type"
    const val COLUMN_CATEGORY_TYPE_ID = "id"
    const val COLUMN_CATEGORY_TYPE_NAME = "name"

    const val TABLE_CACHED_CATEGORY = "cached_category"
    const val COLUMN_CATEGORY_ID = "id"
    const val COLUMN_CATEGORY_NAME = "name"
    const val COLUMN_CATEGORY_TYPE = "type"
    const val COLUMN_CATEGORY_IMAGE = "image"

    const val TABLE_CACHED_UNIT = "cached_unit"
    const val COLUMN_UNIT_ID = "id"
    const val COLUMN_UNIT_NAME = "name"
    const val COLUMN_IS_SHOW = "show_when_adding"

    const val TABLE_CACHED_PRODUCT = "cached_product"
    const val COLUMN_PRODUCT_ID = "id"
    const val COLUMN_PRODUCT_NAME = "name"

}