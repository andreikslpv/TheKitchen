package com.andreikslpv.thekitchen.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.andreikslpv.thekitchen.data.db.RoomConstants
import com.andreikslpv.thekitchen.data.models.UnitLocal
import kotlinx.coroutines.flow.Flow

@Dao
interface UnitDao {
    @Query("SELECT * FROM ${RoomConstants.TABLE_CACHED_UNIT} WHERE ${RoomConstants.COLUMN_UNIT_ID} = :id")
    fun getUnitById(id: String): Flow<UnitLocal>
}