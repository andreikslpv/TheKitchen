package com.andreikslpv.thekitchen.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.andreikslpv.thekitchen.data.db.RoomConstants

@Entity(
    tableName = RoomConstants.TABLE_CACHED_CATEGORY_TYPE,
    indices = [Index(value = [RoomConstants.COLUMN_CATEGORY_TYPE_ID], unique = true)]
)
data class CategoryTypeLocal(
    @PrimaryKey
    @ColumnInfo(name = RoomConstants.COLUMN_CATEGORY_TYPE_ID) val id: String = "ct00000",
    @ColumnInfo(name = RoomConstants.COLUMN_CATEGORY_TYPE_NAME) val name: String = "category_type",
)