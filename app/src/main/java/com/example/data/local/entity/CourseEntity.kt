package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courses")
data class CourseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val category: String,
    val description: String,
    val price: Double,
    val discountPrice: Double,
    val duration: String,
    val difficulty: String,
    val lessonsCount: Int,
    val instructorName: String,
    val instructorRole: String,
    val rating: Double,
    val reviewsCount: Int,
    val whatYouWillLearn: String,
    val requirements: String,
    val curriculumJson: String,
    val isFeatured: Boolean = false,
    val enrolled: Boolean = false,
    val completedLessons: String = "",
    val certificateIssued: Boolean = false,
    val certificateDate: String = ""
)
