package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.local.entity.CourseEntity
import com.example.data.local.entity.OrderEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.UserEntity
import com.example.ui.components.TechBadge
import com.example.ui.theme.TechCyan
import com.example.ui.theme.TechEmerald
import com.example.ui.viewmodel.Screen

@Composable
fun AccountScreen(
    user: UserEntity?,
    enrolledCourses: List<CourseEntity>,
    purchasedProducts: List<ProductEntity>,
    orders: List<OrderEntity>,
    onCourseClick: (CourseEntity) -> Unit,
    onProductClick: (ProductEntity) -> Unit,
    onResumeCourse: (CourseEntity) -> Unit,
    onUpdateProfile: (String, String, String) -> Unit,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showEditDialog by remember { mutableStateOf(false) }

    var editName by remember { mutableStateOf(user?.fullName ?: "") }
    var editHeadline by remember { mutableStateOf(user?.headline ?: "") }
    var editBio by remember { mutableStateOf(user?.bio ?: "") }

    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Edit Learner Profile", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Full Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editHeadline,
                        onValueChange = { editHeadline = it },
                        label = { Text("Professional Headline") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editBio,
                        onValueChange = { editBio = it },
                        label = { Text("Bio") },
                        minLines = 2,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateProfile(editName, editHeadline, editBio)
                        showEditDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TechCyan, contentColor = Color(0xFF091426))
                ) {
                    Text("Save Changes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) { Text("Cancel") }
            }
        )
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User Profile Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth().testTag("user_profile_card")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(listOf(TechCyan, TechEmerald))
                                    )
                            ) {
                                Text(
                                    text = (user?.fullName?.take(1) ?: "A").uppercase(),
                                    fontWeight = FontWeight.Black,
                                    fontSize = 22.sp,
                                    color = Color(0xFF061426)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = user?.fullName ?: "Learner",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = user?.email ?: "learner@growthup.tech",
                                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                            }
                        }

                        IconButton(onClick = {
                            editName = user?.fullName ?: ""
                            editHeadline = user?.headline ?: ""
                            editBio = user?.bio ?: ""
                            showEditDialog = true
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Profile", tint = TechCyan)
                        }
                    }

                    if (!user?.headline.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = user?.headline ?: "",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TechCyan,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    // Quick App Tools Shortcuts
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedButton(
                            onClick = { onNavigate(Screen.AiMentor) },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("AI Tutor", fontSize = 12.sp)
                        }

                        OutlinedButton(
                            onClick = { onNavigate(Screen.AiImageGen) },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("AI Banners", fontSize = 12.sp)
                        }

                        OutlinedButton(
                            onClick = { onNavigate(Screen.Admin) },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.AdminPanelSettings, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Admin", fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Tabs: Enrolled Courses, My Downloads, Order History
        item {
            val tabs = listOf("Courses (${enrolledCourses.size})", "Downloads (${purchasedProducts.size})", "Orders (${orders.size})")
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clip(RoundedCornerShape(12.dp))
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title, fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                    )
                }
            }
        }

        // Tab Content
        when (selectedTab) {
            0 -> {
                // Enrolled Courses
                if (enrolledCourses.isEmpty()) {
                    item {
                        EmptyStateCard(
                            title = "No Courses Enrolled",
                            desc = "Explore our course library to start mastering full-stack web development.",
                            btnText = "Browse Courses",
                            onBtnClick = { onNavigate(Screen.Courses) }
                        )
                    }
                } else {
                    items(enrolledCourses) { course ->
                        val completedCount = if (course.completedLessons.isBlank()) 0 else course.completedLessons.split(",").filter { it.isNotBlank() }.size
                        val progress = if (course.lessonsCount > 0) completedCount.toFloat() / course.lessonsCount.toFloat() else 0f
                        val percentage = (progress * 100).toInt()

                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            modifier = Modifier.fillMaxWidth().clickable { onResumeCourse(course) }
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    TechBadge(text = course.category, color = TechCyan)
                                    Text(
                                        text = "$percentage% Done",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = TechEmerald)
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = course.title,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                LinearProgressIndicator(
                                    progress = { progress },
                                    color = TechEmerald,
                                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp))
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Button(
                                    onClick = { onResumeCourse(course) },
                                    colors = ButtonDefaults.buttonColors(containerColor = TechCyan, contentColor = Color(0xFF091426)),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = if (percentage == 100) "Review / Certificate" else "Resume Learning", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
            1 -> {
                // Downloads
                if (purchasedProducts.isEmpty()) {
                    item {
                        EmptyStateCard(
                            title = "No Digital Products Purchased",
                            desc = "Grab our SaaS launchpad, UI kits, or 100+ JS project bundles to accelerate your workflow.",
                            btnText = "View Digital Products",
                            onBtnClick = { onNavigate(Screen.Products) }
                        )
                    }
                } else {
                    items(purchasedProducts) { product ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    TechBadge(text = product.format, color = TechEmerald)
                                    Text(text = product.fileSize, style = MaterialTheme.typography.labelSmall)
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = product.title,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Button(
                                    onClick = { onProductClick(product) },
                                    colors = ButtonDefaults.buttonColors(containerColor = TechEmerald),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Download Package", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
            2 -> {
                // Order History
                if (orders.isEmpty()) {
                    item {
                        EmptyStateCard(
                            title = "No Past Orders",
                            desc = "Your purchase invoices, GST receipts, and transaction IDs will appear here.",
                            btnText = "Explore Catalog",
                            onBtnClick = { onNavigate(Screen.Courses) }
                        )
                    }
                } else {
                    items(orders) { order ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = order.orderId,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = TechCyan
                                    )
                                    TechBadge(text = order.paymentStatus, color = TechEmerald)
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = order.itemsSummary,
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(text = order.dateFormatted, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(text = "Total: ₹${order.totalAmount.toInt()}", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TechEmerald)
                                }

                                Text(
                                    text = "Txn ID: ${order.transactionId} • Method: ${order.paymentMethod}",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyStateCard(
    title: String,
    desc: String,
    btnText: String,
    onBtnClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp).fillMaxWidth()
        ) {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = desc,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onBtnClick,
                colors = ButtonDefaults.buttonColors(containerColor = TechCyan, contentColor = Color(0xFF091426)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(btnText, fontWeight = FontWeight.Bold)
            }
        }
    }
}
