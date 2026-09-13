package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.CourseEntity
import com.example.ui.theme.TechCyan
import com.example.ui.theme.TechEmerald

@Composable
fun CourseCard(
    course: CourseEntity,
    onClick: () -> Unit,
    onAddToCart: () -> Unit,
    onResumeLesson: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("course_card_${course.id}")
    ) {
        Column {
            // Header visual banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFF0F172A),
                                Color(0xFF1E293B)
                            )
                        )
                    )
                    .padding(12.dp)
            ) {
                // Category & Difficulty
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TechBadge(text = course.category, color = TechCyan)
                    TechBadge(text = course.difficulty, color = TechEmerald)
                }

                // Title snippet & Icon
                Row(
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(TechCyan.copy(alpha = 0.2f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            tint = TechCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${course.lessonsCount} Modules • ${course.duration}",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = Color(0xFF94A3B8),
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }

            // Body
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = course.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        lineHeight = 22.sp
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = course.description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Instructor & Rating
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "By ${course.instructorName}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    RatingRow(rating = course.rating, reviewsCount = course.reviewsCount)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // If enrolled, show progress bar
                if (course.enrolled) {
                    val completedCount = if (course.completedLessons.isBlank()) 0 else course.completedLessons.split(",").filter { it.isNotBlank() }.size
                    val progress = if (course.lessonsCount > 0) completedCount.toFloat() / course.lessonsCount.toFloat() else 0f
                    val percentage = (progress * 100).toInt()

                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Course Progress", style = MaterialTheme.typography.labelSmall)
                            Text(text = "$percentage%", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = TechEmerald))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { progress },
                            color = TechEmerald,
                            trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp))
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { onResumeLesson?.invoke() ?: onClick() },
                        colors = ButtonDefaults.buttonColors(containerColor = TechEmerald),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().testTag("resume_course_${course.id}")
                    ) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = if (percentage == 100) "View Certificate / Review" else "Continue Learning", fontWeight = FontWeight.Bold)
                    }
                } else {
                    // Pricing & Actions
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        PriceTag(price = course.price, discountPrice = course.discountPrice)

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = onAddToCart,
                                modifier = Modifier.testTag("add_course_cart_${course.id}")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AddShoppingCart,
                                    contentDescription = "Add to Cart",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }

                            Button(
                                onClick = onClick,
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.testTag("view_course_btn_${course.id}")
                            ) {
                                Text(text = "Details", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
