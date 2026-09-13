package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val itemId: Long,
    val itemType: String, // COURSE or PRODUCT
    val title: String,
    val price: Double,
    val quantity: Int = 1,
    val formatOrDuration: String
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey
    val orderId: String,
    val timestamp: Long,
    val dateFormatted: String,
    val itemsSummary: String,
    val subtotal: Double,
    val discountAmount: Double,
    val taxAmount: Double,
    val totalAmount: Double,
    val paymentMethod: String,
    val paymentStatus: String, // SUCCESS, PENDING, REFUNDED
    val transactionId: String,
    val customerEmail: String
)

@Entity(tableName = "coupons")
data class CouponEntity(
    @PrimaryKey
    val code: String,
    val discountPercentage: Int = 0,
    val flatDiscount: Double = 0.0,
    val minPurchase: Double = 0.0,
    val expiryDate: String,
    val usageLimit: Int = 100,
    val usedCount: Int = 0,
    val description: String = ""
)

@Entity(tableName = "blogs")
data class BlogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val category: String,
    val readTime: String,
    val date: String,
    val author: String,
    val summary: String,
    val content: String,
    val tags: String
)

@Entity(tableName = "service_inquiries")
data class ServiceInquiryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val serviceName: String,
    val clientName: String,
    val clientEmail: String,
    val budgetRange: String,
    val timeline: String,
    val projectDetails: String,
    val timestamp: Long,
    val status: String = "Submitted"
)

@Entity(tableName = "user_profile")
data class UserEntity(
    @PrimaryKey
    val id: Long = 1,
    val fullName: String,
    val email: String,
    val headline: String,
    val bio: String = "",
    val githubUrl: String = "",
    val isLoggedIn: Boolean = true
)
