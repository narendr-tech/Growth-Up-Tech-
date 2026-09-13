package com.example.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.remote.GeneratedAiImage
import com.example.ui.components.TechBadge
import com.example.ui.theme.TechAmber
import com.example.ui.theme.TechCyan
import com.example.ui.theme.TechEmerald

@Composable
fun AiImageGenScreen(
    prompt: String,
    selectedResolution: String, // "1K", "2K", "4K"
    isGenerating: Boolean,
    generatedImage: GeneratedAiImage?,
    onPromptChange: (String) -> Unit,
    onResolutionChange: (String) -> Unit,
    onGenerateClick: () -> Unit,
    onSaveImage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val resolutions = listOf("1K", "2K", "4K")
    val presets = listOf(
        "Futuristic cybernetic web development laptop glowing with cyan lines",
        "Modern minimalist 3D dark glassmorphic coding dashboard UI mockup",
        "High-tech server room with emerald lights and floating holographic data",
        "Abstract neon geometric code syntax background for web developer portfolio"
    )

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = "AI Graphic & Banner Studio",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Generate high-resolution course banners & UI mockups with Gemini",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                    }
                    TechBadge(text = "gemini-3-pro-image-preview", color = TechCyan)
                }
            }
        }

        // Resolution Selector: 1K, 2K, 4K affordance
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.HighQuality, contentDescription = null, tint = TechCyan, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Choose Image Quality / Resolution",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        resolutions.forEach { res ->
                            val isSelected = selectedResolution == res
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (isSelected) TechCyan.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface
                                    )
                                    .border(
                                        1.dp,
                                        if (isSelected) TechCyan else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { onResolutionChange(res) }
                                    .padding(vertical = 10.dp)
                                    .testTag("res_option_$res")
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = res,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) TechCyan else MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = when (res) {
                                            "1K" -> "Standard"
                                            "2K" -> "Ultra Sharp"
                                            else -> "Cinema 4K"
                                        },
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Prompt Input Field & Presets
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Image Prompt",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = prompt,
                        onValueChange = onPromptChange,
                        placeholder = { Text("Describe the graphic, banner, or UI concept...") },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth().testTag("ai_image_prompt_input")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Quick Inspiration Presets:",
                        style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    presets.forEach { preset ->
                        Text(
                            text = "✨ $preset",
                            style = MaterialTheme.typography.labelSmall.copy(color = TechCyan),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onPromptChange(preset) }
                                .padding(vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onGenerateClick,
                        enabled = !isGenerating && prompt.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = TechCyan, contentColor = Color(0xFF091426)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().testTag("generate_image_btn")
                    ) {
                        if (isGenerating) {
                            CircularProgressIndicator(
                                color = Color(0xFF091426),
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Rendering with Gemini ($selectedResolution)...", fontWeight = FontWeight.Bold)
                        } else {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Generate $selectedResolution Graphic", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Generated Image Preview Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Canvas Result",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        if (generatedImage != null) {
                            TechBadge(text = "${generatedImage.resolution} RESOLUTION", color = TechEmerald)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (generatedImage?.bitmap != null) {
                        Image(
                            bitmap = generatedImage.bitmap.asImageBitmap(),
                            contentDescription = generatedImage.prompt,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = onSaveImage,
                            colors = ButtonDefaults.buttonColors(containerColor = TechEmerald),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Download, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Save Image to Device Gallery", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF0F172A))
                                .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(12.dp))
                                .padding(16.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.Image,
                                    contentDescription = null,
                                    tint = TechCyan.copy(alpha = 0.6f),
                                    modifier = Modifier.size(44.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = if (generatedImage?.errorMessage != null) {
                                        generatedImage.errorMessage
                                    } else {
                                        "Select resolution (1K, 2K, 4K), type your prompt, and tap Generate to render AI assets."
                                    },
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = if (generatedImage?.errorMessage != null) TechAmber else Color(0xFF94A3B8)
                                    ),
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
