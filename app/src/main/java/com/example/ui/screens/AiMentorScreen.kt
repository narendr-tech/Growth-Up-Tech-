package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.remote.ChatMessage
import com.example.ui.components.TechCodeBlock
import com.example.ui.theme.TechCyan
import com.example.ui.theme.TechCyanDark
import com.example.ui.theme.TechEmerald

@Composable
fun AiMentorScreen(
    messages: List<ChatMessage>,
    isLoading: Boolean,
    onSendMessage: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf("") }
    var isComplexTask by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    val suggestions = listOf(
        "Explain CSS Grid vs Flexbox",
        "How to use async/await with try/catch?",
        "Recommend my first course",
        "What are Node.js worker threads?"
    )

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Mentor Header
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 3.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Brush.linearGradient(listOf(TechCyan, TechEmerald)))
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color(0xFF091426),
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = "Growth Up AI Mentor",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = if (isComplexTask) "Powered by Gemini 3.1 Pro Preview" else "Powered by Gemini 3.5 Flash",
                                style = MaterialTheme.typography.labelSmall.copy(color = TechCyan)
                            )
                        }
                    }

                    // Pro mode toggle for complex reasoning
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Pro Mode",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isComplexTask) TechEmerald else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Switch(
                            checked = isComplexTask,
                            onCheckedChange = { isComplexTask = it },
                            modifier = Modifier.testTag("ai_pro_mode_switch")
                        )
                    }
                }

                // Suggestions row
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(suggestions) { prompt ->
                        FilterChip(
                            selected = false,
                            onClick = { onSendMessage(prompt, isComplexTask) },
                            label = { Text(prompt, fontSize = 11.sp) }
                        )
                    }
                }
            }
        }

        // Messages list
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(messages) { msg ->
                val isModel = msg.role == "model"
                Row(
                    horizontalArrangement = if (isModel) Arrangement.Start else Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isModel) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(TechCyan.copy(alpha = 0.2f))
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = TechCyan, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    Card(
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isModel) 2.dp else 16.dp,
                            bottomEnd = if (isModel) 16.dp else 2.dp
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isModel) MaterialTheme.colorScheme.surfaceVariant else TechCyanDark
                        ),
                        modifier = Modifier.widthIn(max = 300.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            // Extract any code block enclosed in ```
                            val text = msg.text
                            if (text.contains("```")) {
                                val parts = text.split("```")
                                parts.forEachIndexed { idx, part ->
                                    if (idx % 2 == 1) {
                                        // Code segment
                                        val lines = part.trim().lines()
                                        val codeLines = if (lines.isNotEmpty() && !lines.first().contains(" ")) lines.drop(1) else lines
                                        val cleanCode = codeLines.joinToString("\n")
                                        TechCodeBlock(code = cleanCode, title = "SNIPPET")
                                    } else if (part.isNotBlank()) {
                                        Text(
                                            text = part.trim(),
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                lineHeight = 20.sp,
                                                color = if (isModel) MaterialTheme.colorScheme.onSurface else Color.White
                                            )
                                        )
                                    }
                                }
                            } else {
                                Text(
                                    text = text,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        lineHeight = 20.sp,
                                        color = if (isModel) MaterialTheme.colorScheme.onSurface else Color.White
                                    )
                                )
                            }
                        }
                    }

                    if (!isModel) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(TechEmerald.copy(alpha = 0.2f))
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = TechEmerald, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }

            if (isLoading) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 36.dp)
                    ) {
                        CircularProgressIndicator(
                            color = TechCyan,
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "AI Mentor is generating response...",
                            style = MaterialTheme.typography.bodySmall.copy(color = TechCyan)
                        )
                    }
                }
            }
        }

        // Input Field Bar
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("Ask coding question or course advice...") },
                    maxLines = 3,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("mentor_chat_input")
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            val textToSend = inputText
                            inputText = ""
                            onSendMessage(textToSend, isComplexTask)
                        }
                    },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(TechCyan)
                        .testTag("mentor_chat_send_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = Color(0xFF091426)
                    )
                }
            }
        }
    }
}
