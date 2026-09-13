package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.CourseEntity
import com.example.ui.components.PriceTag
import com.example.ui.components.RatingRow
import com.example.ui.components.TechBadge
import com.example.ui.components.TechCodeBlock
import com.example.ui.theme.TechAmber
import com.example.ui.theme.TechCyan
import com.example.ui.theme.TechEmerald
import org.json.JSONArray

@Composable
fun CourseDetailScreen(
    course: CourseEntity,
    onBack: () -> Unit,
    onAddToCart: () -> Unit,
    onStartLearning: () -> Unit,
    modifier: Modifier = Modifier
) {
    val curriculumList = remember(course.curriculumJson) {
        val list = mutableListOf<Triple<String, String, String>>()
        try {
            val jsonArray = JSONArray(course.curriculumJson)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val title = obj.optString("title", "Lesson ${i + 1}")
                val duration = obj.optString("duration", "45 min")
                val code = obj.optString("previewCode", "// Example code snippet")
                list.add(Triple(title, duration, code))
            }
        } catch (e: Exception) {
            list.add(Triple("1. Course Introduction", "30 min", "// Introduction code"))
        }
        list
    }

    Scaffold(
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    if (course.enrolled) {
                        Column {
                            TechBadge(text = "ENROLLED", color = TechEmerald)
                            Text(
                                text = "Full Access Granted",
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }
                        Button(
                            onClick = onStartLearning,
                            colors = ButtonDefaults.buttonColors(containerColor = TechEmerald),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("start_classroom_btn")
                        ) {
                            Icon(Icons.Default.PlayCircle, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Go to Classroom", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        PriceTag(price = course.price, discountPrice = course.discountPrice)

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedButton(
                                onClick = onAddToCart,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.testTag("detail_add_cart_btn")
                            ) {
                                Icon(Icons.Default.AddShoppingCart, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Add to Cart")
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = onAddToCart,
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.testTag("detail_enroll_btn")
                            ) {
                                Text(text = "Enroll Now", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Header Banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF0A1128), Color(0xFF16234D))
                            )
                        )
                        .padding(16.dp)
                ) {
                    Column {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier.testTag("course_detail_back_btn")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            TechBadge(text = course.category, color = TechCyan)
                            TechBadge(text = course.difficulty, color = TechEmerald)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = course.title,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = course.description,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color(0xFF94A3B8),
                                lineHeight = 22.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            RatingRow(rating = course.rating, reviewsCount = course.reviewsCount)
                            Text(
                                text = "Duration: ${course.duration} • ${course.lessonsCount} Modules",
                                style = MaterialTheme.typography.labelMedium.copy(color = Color.LightGray)
                            )
                        }
                    }
                }
            }

            // Instructor Card
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(TechCyan.copy(alpha = 0.2f))
                        ) {
                            Icon(Icons.Default.School, contentDescription = null, tint = TechCyan)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(text = "Taught by", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = course.instructorName, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text(text = course.instructorRole, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            // What You'll Learn
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "What You Will Learn",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    val items = course.whatYouWillLearn.split(";").filter { it.isNotBlank() }
                    items.forEach { point ->
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = TechEmerald,
                                modifier = Modifier.size(18.dp).padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = point.trim(),
                                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp)
                            )
                        }
                    }
                }
            }

            // Requirements
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "Requirements",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    val reqs = course.requirements.split(";").filter { it.isNotBlank() }
                    reqs.forEach { req ->
                        Text(
                            text = "• ${req.trim()}",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }

            // Course Curriculum
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Course Curriculum (${curriculumList.size} Lessons)",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            itemsIndexed(curriculumList) { index, (lessonTitle, duration, codePreview) ->
                var expanded by remember { mutableStateOf(index == 0) }

                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .clickable { expanded = !expanded }
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Icon(
                                    imageVector = Icons.Default.PlayCircle,
                                    contentDescription = null,
                                    tint = if (expanded) TechCyan else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = lessonTitle,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                    )
                                    Text(
                                        text = duration,
                                        style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    )
                                }
                            }
                            Icon(
                                imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = null
                            )
                        }

                        AnimatedVisibility(visible = expanded) {
                            Column(modifier = Modifier.padding(top = 10.dp)) {
                                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                                Spacer(modifier = Modifier.height(8.dp))
                                TechCodeBlock(
                                    code = codePreview,
                                    title = "EXERCISE SNIPPET"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
