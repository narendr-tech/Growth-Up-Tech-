package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.AppDao
import com.example.data.local.entity.BlogEntity
import com.example.data.local.entity.CartItemEntity
import com.example.data.local.entity.CouponEntity
import com.example.data.local.entity.CourseEntity
import com.example.data.local.entity.OrderEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.ServiceInquiryEntity
import com.example.data.local.entity.UserEntity

@Database(
    entities = [
        CourseEntity::class,
        ProductEntity::class,
        CartItemEntity::class,
        OrderEntity::class,
        CouponEntity::class,
        BlogEntity::class,
        ServiceInquiryEntity::class,
        UserEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "growth_up_tech.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
