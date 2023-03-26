package com.andreikslpv.thekitchen.domain

import com.andreikslpv.thekitchen.domain.models.SettingsBooleanType
import com.andreikslpv.thekitchen.domain.models.SettingsIntType
import com.andreikslpv.thekitchen.domain.models.SettingsStringType


interface SettingsRepository {

    fun getSettingBooleanValue(setting: SettingsBooleanType): Boolean

    fun setSettingBooleanValue(setting: SettingsBooleanType, value: Boolean)

    fun getSettingStringValue(setting: SettingsStringType): String

    fun setSettingStringValue(setting: SettingsStringType, value: String)

    fun getSettingIntValue(setting: SettingsIntType): Int

    fun setSettingIntValue(setting: SettingsIntType, value: Int)
}