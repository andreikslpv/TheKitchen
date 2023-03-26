package com.andreikslpv.thekitchen.domain.models

enum class SettingsStringType(val key: String, val defaultValue: String) {
    VERSION_CATEGORY("version_category", "0"),
    VERSION_CATEGORY_TYPE("version_category_type", "0"),
    VERSION_PRODUCT("version_product", "0"),
    VERSION_UNIT("version_unit", "0"),
}