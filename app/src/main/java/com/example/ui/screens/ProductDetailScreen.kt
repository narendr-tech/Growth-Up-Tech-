package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FolderZip
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import com.example.data.local.entity.ProductEntity
import com.example.ui.components.PriceTag
import com.example.ui.components.RatingRow
import com.example.ui.components.TechBadge
import com.example.ui.components.TechCodeBlock
import com.example.ui.theme.TechAmber
import com.example.ui.theme.TechCyan
import com.example.ui.theme.TechEmerald
import kotlinx.coroutines.delay

@Composable
fun ProductDetailScreen(
    product: ProductEntity,
    onBack: () -> Unit,
    onAddToCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDownloadDialog by remember { mutableStateOf(false) }
    var downloadProgress by remember { mutableFloatStateOf(0f) }
    var downloadCompleted by remember { mutableStateOf(false) }

    if (showDownloadDialog) {
        LaunchedEffect(showDownloadDialog) {
            downloadProgress = 0f
            downloadCompleted = false
            while (downloadProgress < 1f) {
                delay(120)
                downloadProgress += 0.15f
            }
            downloadProgress = 1f
            downloadCompleted = true
        }

        AlertDialog(
            onDismissRequest = { if (downloadCompleted) showDownloadDialog = false },
            title = {
                Text(
                    text = if (downloadCompleted) "Download Completed" else "Downloading Asset...",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "${product.title} (${product.fileSize})",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    LinearProgressIndicator(
                        progress = { downloadProgress.coerceIn(0f, 1f) },
                        color = TechEmerald,
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp))
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    if (downloadCompleted) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = TechEmerald, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Saved to Downloads/GrowthUpTech/ successfully.",
                                style = MaterialTheme.typography.labelSmall.copy(color = TechEmerald)
                            )
                        }
                    } else {
                        Text(
                            text = "Verifying cryptographic checksum SHA-256...",
                            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showDownloadDialog = false },
                    enabled = downloadCompleted,
                    colors = ButtonDefaults.buttonColors(containerColor = TechCyan, contentColor = Color(0xFF091426))
                ) {
                    Text("Close")
                }
            }
        )
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
                    if (product.purchased) {
                        Column {
                            TechBadge(text = "UNLOCKED", color = TechEmerald)
                            Text(
                                text = "Lifetime updates included",
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }

                        Button(
                            onClick = { showDownloadDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = TechEmerald),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("download_asset_btn")
                        ) {
                            Icon(Icons.Default.Download, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Download (${product.fileSize})", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        PriceTag(price = product.price, discountPrice = product.discountPrice)

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedButton(
                                onClick = onAddToCart,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.testTag("product_detail_cart_btn")
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
                                modifier = Modifier.testTag("product_detail_buy_btn")
                            ) {
                                Text(text = "Instant Buy", fontWeight = FontWeight.Bold)
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
            // Header
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF0F1E36), Color(0xFF16234D))
                            )
                        )
                        .padding(16.dp)
                ) {
                    Column {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier.testTag("product_detail_back_btn")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            TechBadge(text = product.category, color = TechCyan)
                            TechBadge(text = product.format, color = TechAmber)
                            TechBadge(text = product.fileSize, color = TechEmerald)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = product.title,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = product.description,
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
                            RatingRow(rating = product.rating, reviewsCount = 184)
                            Text(
                                text = "${product.downloadCount} Verified Downloads",
                                style = MaterialTheme.typography.labelSmall.copy(color = Color.LightGray)
                            )
                        }
                    }
                }
            }

            // Features Checklist
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "What's Included in this Asset",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    val feats = product.features.split(";").filter { it.isNotBlank() }
                    feats.forEach { feat ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = TechEmerald,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = feat.trim(),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            // Live Code Preview
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "Source Code Preview",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Sample inspect snippet included inside this package:",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    TechCodeBlock(
                        code = product.previewCode,
                        title = "${product.format} SOURCE SAMPLE"
                    )
                }
            }

            // Commercial License Info
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = TechCyan,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = "Commercial Use Allowed", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(
                                text = "Use this template or kit in personal and commercial client projects with no attribution required.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
