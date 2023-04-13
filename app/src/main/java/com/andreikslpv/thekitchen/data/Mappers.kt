package com.andreikslpv.thekitchen.data

import com.andreikslpv.thekitchen.data.models.CategoryLocal
import com.andreikslpv.thekitchen.data.models.CategoryTypeLocal
import com.andreikslpv.thekitchen.data.models.ProductLocal
import com.andreikslpv.thekitchen.data.models.UnitLocal
import com.andreikslpv.thekitchen.domain.BaseMapper
import com.andreikslpv.thekitchen.domain.models.Category
import com.andreikslpv.thekitchen.domain.models.CategoryType
import com.andreikslpv.thekitchen.domain.models.Product
import com.andreikslpv.thekitchen.domain.models.Unit

object Mappers {

    object CategoryTypeToLocalListMapper :
        BaseMapper<List<CategoryType>, List<CategoryTypeLocal>> {
        override fun map(type: List<CategoryType>?): List<CategoryTypeLocal> {
            return type?.map {
                CategoryTypeLocal(
                    id = it.id,
                    name = it.name
                )
            } ?: listOf()
        }
    }

    object CategoryToLocalListMapper :
        BaseMapper<List<Category>, List<CategoryLocal>> {
        override fun map(type: List<Category>?): List<CategoryLocal> {
            return type?.map {
                CategoryLocal(
                    id = it.id,
                    name = it.name,
                    type = it.type,
                    image = it.image
                )
            } ?: listOf()
        }
    }

    object LocalToCategoryListMapper :
        BaseMapper<List<CategoryLocal>, List<Category>> {
        override fun map(type: List<CategoryLocal>?): List<Category> {
            return type?.map {
                Category(
                    id = it.id,
                    name = it.name,
                    type = it.type,
                    image = it.image
                )
            } ?: listOf()
        }
    }

    object UnitToLocalListMapper :
        BaseMapper<List<Unit>, List<UnitLocal>> {
        override fun map(type: List<Unit>?): List<UnitLocal> {
            return type?.map {
                UnitLocal(
                    id = it.id,
                    name = it.name,
                )
            } ?: listOf()
        }
    }

//    object ProductToLocalListMapper :
//        BaseMapper<List<Product>, List<ProductLocal>> {
//        override fun map(type: List<Product>?): List<ProductLocal> {
//            return type?.map {
//                ProductLocal(
//                    id = it.id,
//                    name = it.name,
//                    unit = it.unit,
//                )
//            } ?: listOf()
//        }
//    }

}