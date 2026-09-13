package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.BlogEntity
import com.example.data.local.entity.CartItemEntity
import com.example.data.local.entity.CouponEntity
import com.example.data.local.entity.CourseEntity
import com.example.data.local.entity.OrderEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.ServiceInquiryEntity
import com.example.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // --- Courses ---
    @Query("SELECT * FROM courses ORDER BY id ASC")
    fun getAllCourses(): Flow<List<CourseEntity>>

    @Query("SELECT * FROM courses WHERE id = :id LIMIT 1")
    fun getCourseById(id: Long): Flow<CourseEntity?>

    @Query("SELECT * FROM courses WHERE enrolled = 1 ORDER BY id ASC")
    fun getEnrolledCourses(): Flow<List<CourseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: CourseEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourses(courses: List<CourseEntity>)

    @Update
    suspend fun updateCourse(course: CourseEntity)

    @Query("DELETE FROM courses WHERE id = :id")
    suspend fun deleteCourse(id: Long)

    @Query("UPDATE courses SET enrolled = :enrolled WHERE id = :id")
    suspend fun updateCourseEnrollment(id: Long, enrolled: Boolean)

    @Query("UPDATE courses SET completedLessons = :completed, certificateIssued = :certIssued, certificateDate = :certDate WHERE id = :id")
    suspend fun updateCompletedLessons(id: Long, completed: String, certIssued: Boolean, certDate: String)

    // --- Products ---
    @Query("SELECT * FROM products ORDER BY id ASC")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    fun getProductById(id: Long): Flow<ProductEntity?>

    @Query("SELECT * FROM products WHERE purchased = 1 ORDER BY id ASC")
    fun getPurchasedProducts(): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Query("DELETE FROM products WHERE id = :id")
    suspend fun deleteProduct(id: Long)

    @Query("UPDATE products SET purchased = :purchased, downloadCount = :downloads WHERE id = :id")
    suspend fun updateProductPurchased(id: Long, purchased: Boolean, downloads: Int)

    // --- Cart ---
    @Query("SELECT * FROM cart_items ORDER BY id DESC")
    fun getCartItems(): Flow<List<CartItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(item: CartItemEntity)

    @Query("UPDATE cart_items SET quantity = :quantity WHERE id = :id")
    suspend fun updateCartItemQuantity(id: Long, quantity: Int)

    @Query("DELETE FROM cart_items WHERE id = :id")
    suspend fun deleteCartItem(id: Long)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()

    // --- Orders ---
    @Query("SELECT * FROM orders ORDER BY timestamp DESC")
    fun getAllOrders(): Flow<List<OrderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)

    @Query("UPDATE orders SET paymentStatus = :status WHERE orderId = :orderId")
    suspend fun updateOrderStatus(orderId: String, status: String)

    // --- Coupons ---
    @Query("SELECT * FROM coupons ORDER BY code ASC")
    fun getAllCoupons(): Flow<List<CouponEntity>>

    @Query("SELECT * FROM coupons WHERE code = :code LIMIT 1")
    fun getCouponByCode(code: String): Flow<CouponEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoupon(coupon: CouponEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoupons(coupons: List<CouponEntity>)

    @Query("DELETE FROM coupons WHERE code = :code")
    suspend fun deleteCoupon(code: String)

    // --- Blogs ---
    @Query("SELECT * FROM blogs ORDER BY id DESC")
    fun getAllBlogs(): Flow<List<BlogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlogs(blogs: List<BlogEntity>)

    // --- Services ---
    @Query("SELECT * FROM service_inquiries ORDER BY timestamp DESC")
    fun getAllInquiries(): Flow<List<ServiceInquiryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInquiry(inquiry: ServiceInquiryEntity)

    // --- User Profile ---
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfile(): Flow<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUser(user: UserEntity)
}
