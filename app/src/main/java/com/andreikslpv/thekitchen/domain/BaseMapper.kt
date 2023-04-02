package com.andreikslpv.thekitchen.domain

interface BaseMapper<in A, out B> {

    fun map(type: A?): B
}