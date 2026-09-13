package com.example.ui.screens

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.CourseEntity
import com.example.ui.components.TechBadge
import com.example.ui.components.TechCodeBlock
import com.example.ui.components.VerifiableCertificateView
import com.example.ui.theme.TechCyan
import com.example.ui.theme.TechEmerald
import org.json.JSONArray

@Composable
fun CoursePlayerScreen(
    course: CourseEntity,
    activeLessonIndex: Int,
    userName: String,
    onBack: () -> Unit,
    onSelectLesson: (Int) -> Unit,
    onCompleteLesson: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

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
            list.add(Triple("1. Introduction", "30 min", "// Introduction code"))
        }
        list
    }

    val currentLesson = curriculumList.getOrNull(activeLessonIndex) ?: curriculumList.firstOrNull()
    val completedSet = remember(course.completedLessons) {
        if (course.completedLessons.isBlank()) emptySet()
        else course.completedLessons.split(",").filter { it.isNotBlank() }.map { it.trim() }.toSet()
    }

    val isCurrentLessonCompleted = completedSet.contains(activeLessonIndex.toString())
    val progress = if (curriculumList.isNotEmpty()) completedSet.size.toFloat() / curriculumList.size.toFloat() else 0f
    val isAllCompleted = completedSet.size >= curriculumList.size

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Classroom Bar
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("classroom_back_btn")) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = course.title,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        maxLines = 1
                    )
                    Text(
                        text = "Lesson ${activeLessonIndex + 1} of ${curriculumList.size}",
                        style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }
            }
        }

        // Progress bar
        LinearProgressIndicator(
            progress = { progress },
            color = TechEmerald,
            trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
            modifier = Modifier.fillMaxWidth().height(4.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Simulated High-Tech Video Player Container
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.radialGradient(
                            listOf(Color(0xFF1E293B), Color(0xFF0A1128))
                        )
                    )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(TechCyan)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play Video",
                            tint = Color(0xFF0A1128),
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = currentLesson?.first ?: "Loading lesson...",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        ),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "HD Video Streaming • ${currentLesson?.second ?: ""}",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF94A3B8))
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lesson Controls
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = {
                        if (activeLessonIndex > 0) onSelectLesson(activeLessonIndex - 1)
                    },
                    enabled = activeLessonIndex > 0,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.ChevronLeft, contentDescription = null)
                    Text("Previous")
                }

                Button(
                    onClick = {
                        onCompleteLesson()
                        if (activeLessonIndex < curriculumList.size - 1) {
                            onSelectLesson(activeLessonIndex + 1)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCurrentLessonCompleted) TechEmerald else TechCyan,
                        contentColor = Color(0xFF081326)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("mark_lesson_done_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isCurrentLessonCompleted) "Completed (Next)" else "Mark Complete & Next",
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = {
                        if (activeLessonIndex < curriculumList.size - 1) onSelectLesson(activeLessonIndex + 1)
                    },
                    enabled = activeLessonIndex < curriculumList.size - 1,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Next")
                    Icon(Icons.Default.ChevronRight, contentDescription = null)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Code practice section
            Text(
                text = "Interactive Code Laboratory",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "Review, copy, and run the practical code for this module:",
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )

            Spacer(modifier = Modifier.height(8.dp))

            TechCodeBlock(
                code = currentLesson?.third ?: "// No code preview",
                title = "MODULE CODE"
            )

            // Certificate if completed
            if (isAllCompleted) {
                Spacer(modifier = Modifier.height(24.dp))
                VerifiableCertificateView(
                    studentName = userName,
                    courseTitle = course.title,
                    issueDate = if (course.certificateDate.isNotBlank()) course.certificateDate else "12 September 2026",
                    certId = "GUT-CERT-${course.id}-8923",
                    onShare = {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "I just mastered '${course.title}' on Growth Up Tech! Verification ID: GUT-CERT-${course.id}-8923. Learn. Build. Grow."
                            )
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(sendIntent, "Share Certificate"))
                    }
                )
            }
        }
    }
}
