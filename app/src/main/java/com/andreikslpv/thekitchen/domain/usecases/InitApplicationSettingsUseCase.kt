package com.andreikslpv.thekitchen.domain.usecases

import com.andreikslpv.thekitchen.data.db.DbConstants
import com.andreikslpv.thekitchen.domain.RecipeRepository
import com.andreikslpv.thekitchen.domain.SettingsRepository
import com.andreikslpv.thekitchen.domain.models.SettingsIntType
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InitApplicationSettingsUseCase(
    private val settingsRepository: SettingsRepository,
    private val recipeRepository: RecipeRepository,
    private val remoteConfig: FirebaseRemoteConfig
) {
    fun execute() {
        CoroutineScope(Dispatchers.IO).launch {
            checkForUpdates(SettingsIntType.VERSION_CATEGORY_TYPE, DbConstants.PATH_CATEGORY_TYPE)
            checkForUpdates(SettingsIntType.VERSION_CATEGORY, DbConstants.PATH_CATEGORY)
            checkForUpdates(SettingsIntType.VERSION_UNIT, DbConstants.PATH_UNIT)
            checkForUpdates(SettingsIntType.VERSION_PRODUCT, DbConstants.PATH_PRODUCT)
        }
    }

    private fun checkForUpdates(setting: SettingsIntType, path: String) {
        val localValue = settingsRepository.getSettingIntValue(setting)
        val remoteValue = remoteConfig.getLong(setting.key).toInt()
        println("I/o $localValue $remoteValue")
        if (localValue != remoteValue)
            recipeRepository.updateLocalData(path)
    }
}