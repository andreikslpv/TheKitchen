package com.andreikslpv.thekitchen.data

import com.andreikslpv.thekitchen.data.models.CategoryLocal
import com.andreikslpv.thekitchen.data.models.ProductLocal
import com.andreikslpv.thekitchen.data.models.UnitLocal
import com.andreikslpv.thekitchen.domain.BaseMapper
import com.andreikslpv.thekitchen.domain.models.Category
import com.andreikslpv.thekitchen.domain.models.Product
import com.andreikslpv.thekitchen.domain.models.Unit

object Mappers {

//    object CategoryTypeToLocalListMapper : BaseMapper<List<CategoryTypeDB>, List<CategoryTypeLocal>> {
//        override fun map(type: List<CategoryTypeDB>?): List<CategoryTypeLocal> {
//            return type?.map {
//                CategoryTypeLocal(
//                    id = it.id,
//                    name = it.name
//                )
//            } ?: listOf()
//        }
//    }

    object CategoryToLocalListMapper : BaseMapper<List<Category>, List<CategoryLocal>> {
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

    object UnitToLocalListMapper : BaseMapper<List<Unit>, List<UnitLocal>> {
        override fun map(type: List<Unit>?): List<UnitLocal> {
            return type?.map {
                UnitLocal(
                    id = it.id,
                    name = it.name,
                )
            } ?: listOf()
        }
    }

    object LocalToUnitListMapper : BaseMapper<List<UnitLocal>, List<Unit>> {
        override fun map(type: List<UnitLocal>?): List<Unit> {
            return type?.map {
                Unit(
                    id = it.id,
                    name = it.name,
                )
            } ?: listOf()
        }
    }

    object LocalToUnitMapper : BaseMapper<UnitLocal, Unit> {
        override fun map(type: UnitLocal?): Unit {
            return Unit(
                id = type?.id ?: "un00000",
                name = type?.name ?: "unit",
            )
        }
    }

    object ProductToLocalListMapper : BaseMapper<List<Product>, List<ProductLocal>> {
        override fun map(type: List<Product>?): List<ProductLocal> {
            return type?.map {
                ProductLocal(
                    id = it.id,
                    name = it.name,
                    saleUnit = it.saleUnit,
                )
            } ?: listOf()
        }
    }

    object LocalToProductMapper : BaseMapper<ProductLocal, Product> {
        override fun map(type: ProductLocal?): Product {
            return Product(
                id = type?.id ?: "pr00000",
                name = type?.name ?: "product",
                saleUnit = type?.saleUnit ?: "un00000",
            )
        }
    }

    object LocalToProductListMapper : BaseMapper<List<ProductLocal>, List<Product>> {
        override fun map(type: List<ProductLocal>?): List<Product> {
            return type?.map {
                Product(
                    id = it.id,
                    name = it.name,
                    saleUnit = it.saleUnit,
                )
            } ?: listOf()
        }
    }

}