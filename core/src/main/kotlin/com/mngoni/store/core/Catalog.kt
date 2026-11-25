package com.mngoni.store.core

import kotlinx.serialization.Serializable

@Serializable
enum class Category(val label: String, val emoji: String) {
    SHOES("Viatu", "👟"),
    SANDALS("Sandals", "🩴"),
    MEN_CLOTHING("Nguo za kiume", "👔"),
    WOMEN_CLOTHING("Nguo za kike", "👗"),
    CHILDREN_CLOTHING("Nguo za watoto", "🧒"),
    JEWELLERY("Jewels", "💍"),
}

@Serializable
data class Product(
    val id: String,
    val name: String,
    val category: Category,
    val price: Money,
    val description: String,
    val stock: Int,
    val imageUrl: String? = null,
)
