package com.andreikslpv.thekitchen.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.andreikslpv.thekitchen.data.db.RoomConstants

@Entity(
    tableName = RoomConstants.TABLE_CACHED_PRODUCT,
    indices = [Index(value = [RoomConstants.COLUMN_PRODUCT_ID], unique = true)]
)
data class ProductLocal(
    @PrimaryKey
    @ColumnInfo(name = RoomConstants.COLUMN_PRODUCT_ID) val id: String = "pr00000",
    @ColumnInfo(name = RoomConstants.COLUMN_PRODUCT_NAME) val name: String = "product",
    @ColumnInfo(name = RoomConstants.COLUMN_PRODUCT_UNIT) val saleUnit: String = "un00000",
)
