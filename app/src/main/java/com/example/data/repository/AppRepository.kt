package com.example.data.repository

import com.example.data.local.dao.AppDao
import com.example.data.local.entity.BlogEntity
import com.example.data.local.entity.CartItemEntity
import com.example.data.local.entity.CouponEntity
import com.example.data.local.entity.CourseEntity
import com.example.data.local.entity.OrderEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.ServiceInquiryEntity
import com.example.data.local.entity.UserEntity
import com.example.data.remote.ChatMessage
import com.example.data.remote.GeminiService
import com.example.data.remote.GeneratedAiImage
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class AppRepository(private val appDao: AppDao) {

    val allCourses: Flow<List<CourseEntity>> = appDao.getAllCourses()
    val enrolledCourses: Flow<List<CourseEntity>> = appDao.getEnrolledCourses()
    val allProducts: Flow<List<ProductEntity>> = appDao.getAllProducts()
    val purchasedProducts: Flow<List<ProductEntity>> = appDao.getPurchasedProducts()
    val cartItems: Flow<List<CartItemEntity>> = appDao.getCartItems()
    val allOrders: Flow<List<OrderEntity>> = appDao.getAllOrders()
    val allCoupons: Flow<List<CouponEntity>> = appDao.getAllCoupons()
    val allBlogs: Flow<List<BlogEntity>> = appDao.getAllBlogs()
    val allInquiries: Flow<List<ServiceInquiryEntity>> = appDao.getAllInquiries()
    val userProfile: Flow<UserEntity?> = appDao.getUserProfile()

    fun getCourseById(id: Long): Flow<CourseEntity?> = appDao.getCourseById(id)
    fun getProductById(id: Long): Flow<ProductEntity?> = appDao.getProductById(id)

    suspend fun addToCart(itemId: Long, itemType: String, title: String, price: Double, formatOrDuration: String) {
        val item = CartItemEntity(
            itemId = itemId,
            itemType = itemType,
            title = title,
            price = price,
            quantity = 1,
            formatOrDuration = formatOrDuration
        )
        appDao.insertCartItem(item)
    }

    suspend fun updateCartQuantity(id: Long, qty: Int) {
        if (qty <= 0) {
            appDao.deleteCartItem(id)
        } else {
            appDao.updateCartItemQuantity(id, qty)
        }
    }

    suspend fun removeFromCart(id: Long) {
        appDao.deleteCartItem(id)
    }

    suspend fun clearCart() {
        appDao.clearCart()
    }

    suspend fun completeCheckout(
        cartItems: List<CartItemEntity>,
        subtotal: Double,
        discount: Double,
        tax: Double,
        total: Double,
        paymentMethod: String,
        customerEmail: String
    ): OrderEntity {
        val randomNum = (10000..99999).random()
        val orderId = "GUT-ORD-$randomNum"
        val txnId = "TXN-RAZOR-${UUID.randomUUID().toString().take(10).uppercase()}"
        val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        val dateString = dateFormat.format(Date())

        val summary = cartItems.joinToString(", ") { "${it.title} (x${it.quantity})" }

        val order = OrderEntity(
            orderId = orderId,
            timestamp = System.currentTimeMillis(),
            dateFormatted = dateString,
            itemsSummary = summary,
            subtotal = subtotal,
            discountAmount = discount,
            taxAmount = tax,
            totalAmount = total,
            paymentMethod = paymentMethod,
            paymentStatus = "SUCCESS",
            transactionId = txnId,
            customerEmail = customerEmail
        )

        appDao.insertOrder(order)

        // Unlock purchased courses and products
        for (item in cartItems) {
            if (item.itemType == "COURSE") {
                appDao.updateCourseEnrollment(item.itemId, true)
            } else if (item.itemType == "PRODUCT") {
                appDao.updateProductPurchased(item.itemId, true, 1)
            }
        }

        // Clear cart after successful checkout
        appDao.clearCart()
        return order
    }

    suspend fun markLessonCompleted(courseId: Long, lessonIndex: Int, currentCompleted: String, totalLessons: Int) {
        val completedSet = if (currentCompleted.isBlank()) {
            mutableSetOf()
        } else {
            currentCompleted.split(",").filter { it.isNotBlank() }.map { it.trim() }.toMutableSet()
        }
        completedSet.add(lessonIndex.toString())
        val updatedString = completedSet.joinToString(",")

        val isNowCompleted = completedSet.size >= totalLessons
        val certDate = if (isNowCompleted) {
            SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date())
        } else ""

        appDao.updateCompletedLessons(
            id = courseId,
            completed = updatedString,
            certIssued = isNowCompleted,
            certDate = certDate
        )
    }

    suspend fun submitServiceInquiry(
        serviceName: String,
        clientName: String,
        clientEmail: String,
        budget: String,
        timeline: String,
        details: String
    ) {
        val inquiry = ServiceInquiryEntity(
            serviceName = serviceName,
            clientName = clientName,
            clientEmail = clientEmail,
            budgetRange = budget,
            timeline = timeline,
            projectDetails = details,
            timestamp = System.currentTimeMillis()
        )
        appDao.insertInquiry(inquiry)
    }

    suspend fun updateUserProfile(user: UserEntity) {
        appDao.insertOrUpdateUser(user)
    }

    suspend fun saveCourse(course: CourseEntity) {
        if (course.id == 0L) {
            appDao.insertCourse(course)
        } else {
            appDao.updateCourse(course)
        }
    }

    suspend fun deleteCourse(id: Long) {
        appDao.deleteCourse(id)
    }

    suspend fun saveProduct(product: ProductEntity) {
        if (product.id == 0L) {
            appDao.insertProduct(product)
        } else {
            appDao.updateProduct(product)
        }
    }

    suspend fun deleteProduct(id: Long) {
        appDao.deleteProduct(id)
    }

    suspend fun saveCoupon(coupon: CouponEntity) {
        appDao.insertCoupon(coupon)
    }

    suspend fun deleteCoupon(code: String) {
        appDao.deleteCoupon(code)
    }

    suspend fun sendChatMessage(history: List<ChatMessage>, message: String, isComplex: Boolean): String {
        return GeminiService.sendMessage(history, message, isComplex)
    }

    suspend fun generateAiImage(prompt: String, size: String): GeneratedAiImage {
        return GeminiService.generateCourseBanner(prompt, size)
    }
}
