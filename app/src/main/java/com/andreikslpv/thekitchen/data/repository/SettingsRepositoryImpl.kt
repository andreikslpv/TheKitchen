package com.andreikslpv.thekitchen.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.andreikslpv.thekitchen.domain.SettingsRepository
import com.andreikslpv.thekitchen.domain.models.SettingsBooleanType
import com.andreikslpv.thekitchen.domain.models.SettingsIntType
import com.andreikslpv.thekitchen.domain.models.SettingsStringType
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(context: Context) : SettingsRepository {
    private val preference: SharedPreferences =
        context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    init {
        //Логика для первого запуска приложения, чтобы положить дефолтные настройки,
        if (preference.getBoolean(
                SettingsBooleanType.FIRST_LAUNCH.key,
                SettingsBooleanType.FIRST_LAUNCH.defaultValue
            )
        ) {
            setSettingIntValue(
                SettingsIntType.VERSION_CATEGORY,
                SettingsIntType.VERSION_CATEGORY.defaultValue
            )
            setSettingIntValue(
                SettingsIntType.VERSION_CATEGORY_TYPE,
                SettingsIntType.VERSION_CATEGORY_TYPE.defaultValue
            )
            setSettingIntValue(
                SettingsIntType.VERSION_PRODUCT,
                SettingsIntType.VERSION_PRODUCT.defaultValue
            )
            setSettingIntValue(
                SettingsIntType.VERSION_UNIT,
                SettingsIntType.VERSION_UNIT.defaultValue
            )
            preference.edit().putBoolean(SettingsBooleanType.FIRST_LAUNCH.key, false).apply()
        }
    }

    override fun getSettingBooleanValue(setting: SettingsBooleanType): Boolean {
        return preference.getBoolean(setting.key, setting.defaultValue)
    }

    override fun setSettingBooleanValue(setting: SettingsBooleanType, value: Boolean) {
        preference.edit().putBoolean(setting.key, value).apply()
    }

    override fun getSettingStringValue(setting: SettingsStringType): String {
        return preference.getString(setting.key, setting.defaultValue).toString()
    }

    override fun setSettingStringValue(setting: SettingsStringType, value: String) {
        preference.edit().putString(setting.key, value).apply()
    }

    override fun getSettingIntValue(setting: SettingsIntType): Int {
        return preference.getInt(setting.key, setting.defaultValue)
    }

    override fun setSettingIntValue(setting: SettingsIntType, value: Int) {
        preference.edit().putInt(setting.key, value).apply()
    }
}