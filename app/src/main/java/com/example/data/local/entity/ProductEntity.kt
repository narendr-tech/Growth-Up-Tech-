package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val category: String,
    val description: String,
    val price: Double,
    val discountPrice: Double,
    val format: String,
    val fileSize: String,
    val features: String,
    val previewCode: String,
    val isFeatured: Boolean = false,
    val purchased: Boolean = false,
    val downloadCount: Int = 0,
    val rating: Double = 4.9
)
