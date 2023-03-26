package com.andreikslpv.thekitchen.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.andreikslpv.thekitchen.data.db.RoomConstants

@Entity(
    tableName = RoomConstants.TABLE_CACHED_CATEGORY,
    indices = [Index(value = [RoomConstants.COLUMN_CATEGORY_ID], unique = true)]
)
data class CategoryLocal(
    @PrimaryKey
    @ColumnInfo(name = RoomConstants.COLUMN_CATEGORY_ID) val id: String = "",
    @ColumnInfo(name = RoomConstants.COLUMN_CATEGORY_NAME) val name: String = "",
    @ColumnInfo(name = RoomConstants.COLUMN_CATEGORY_TYPE) val type: String = "",
)
