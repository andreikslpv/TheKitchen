package com.andreikslpv.thekitchen.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.andreikslpv.thekitchen.data.db.RoomConstants

@Entity(
    tableName = RoomConstants.TABLE_CACHED_UNIT,
    indices = [Index(value = [RoomConstants.COLUMN_UNIT_ID], unique = true)]
)
data class UnitLocal(
    @PrimaryKey
    @ColumnInfo(name = RoomConstants.COLUMN_UNIT_ID) val id: String = "un00000",
    @ColumnInfo(name = RoomConstants.COLUMN_UNIT_NAME) val name: String = "unit",
)

