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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.CouponEntity
import com.example.data.local.entity.CourseEntity
import com.example.data.local.entity.OrderEntity
import com.example.data.local.entity.ProductEntity
import com.example.ui.components.TechBadge
import com.example.ui.theme.TechAmber
import com.example.ui.theme.TechCyan
import com.example.ui.theme.TechEmerald

@Composable
fun AdminScreen(
    courses: List<CourseEntity>,
    products: List<ProductEntity>,
    orders: List<OrderEntity>,
    coupons: List<CouponEntity>,
    onSaveCourse: (CourseEntity) -> Unit,
    onDeleteCourse: (Long) -> Unit,
    onSaveProduct: (ProductEntity) -> Unit,
    onDeleteProduct: (Long) -> Unit,
    onSaveCoupon: (String, Int, Double, String, String) -> Unit,
    onDeleteCoupon: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var adminTab by remember { mutableIntStateOf(0) }
    var showCouponDialog by remember { mutableStateOf(false) }

    // New Coupon inputs
    var newCouponCode by remember { mutableStateOf("") }
    var newCouponPct by remember { mutableStateOf("25") }
    var newCouponMin by remember { mutableStateOf("499") }
    var newCouponDesc by remember { mutableStateOf("Special discount") }

    val totalRevenue = orders.sumOf { it.totalAmount }

    if (showCouponDialog) {
        AlertDialog(
            onDismissRequest = { showCouponDialog = false },
            title = { Text("Create Discount Coupon", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newCouponCode,
                        onValueChange = { newCouponCode = it.uppercase() },
                        label = { Text("Coupon Code (e.g. FLASH30)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newCouponPct,
                        onValueChange = { newCouponPct = it },
                        label = { Text("Discount Percentage (%)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newCouponMin,
                        onValueChange = { newCouponMin = it },
                        label = { Text("Min Order Value (₹)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newCouponDesc,
                        onValueChange = { newCouponDesc = it },
                        label = { Text("Description") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newCouponCode.isNotBlank()) {
                            onSaveCoupon(
                                newCouponCode,
                                newCouponPct.toIntOrNull() ?: 20,
                                newCouponMin.toDoubleOrNull() ?: 0.0,
                                "31 Dec 2026",
                                newCouponDesc
                            )
                            showCouponDialog = false
                            newCouponCode = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TechCyan, contentColor = Color(0xFF091426))
                ) {
                    Text("Create Coupon")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCouponDialog = false }) { Text("Cancel") }
            }
        )
    }

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
                            text = "Admin Control Dashboard",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Manage courses, digital products, coupons, and orders",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                    }
                    TechBadge(text = "ADMIN ACTIVE", color = TechEmerald)
                }
            }
        }

        // Summary Metric Cards
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                AdminStatCard(
                    title = "Revenue",
                    value = "₹${totalRevenue.toInt()}",
                    icon = Icons.Default.AttachMoney,
                    color = TechEmerald,
                    modifier = Modifier.weight(1f)
                )
                AdminStatCard(
                    title = "Orders",
                    value = "${orders.size}",
                    icon = Icons.Default.ShoppingBag,
                    color = TechCyan,
                    modifier = Modifier.weight(1f)
                )
                AdminStatCard(
                    title = "Courses",
                    value = "${courses.size}",
                    icon = Icons.Default.School,
                    color = TechAmber,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Admin Tabs
        item {
            val tabs = listOf("Courses (${courses.size})", "Products (${products.size})", "Coupons (${coupons.size})")
            TabRow(
                selectedTabIndex = adminTab,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clip(RoundedCornerShape(10.dp))
            ) {
                tabs.forEachIndexed { idx, title ->
                    Tab(
                        selected = adminTab == idx,
                        onClick = { adminTab = idx },
                        text = { Text(title, fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                    )
                }
            }
        }

        when (adminTab) {
            0 -> {
                // Courses Management
                items(courses) { course ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.padding(14.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    TechBadge(text = course.category, color = TechCyan)
                                    TechBadge(text = "₹${course.discountPrice.toInt()}", color = TechEmerald)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = course.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(
                                    text = "Instructor: ${course.instructorName} • ${course.lessonsCount} lessons",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            IconButton(onClick = { onDeleteCourse(course.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFFF5F56))
                            }
                        }
                    }
                }
            }
            1 -> {
                // Products Management
                items(products) { product ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.padding(14.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    TechBadge(text = product.format, color = TechAmber)
                                    TechBadge(text = "₹${product.discountPrice.toInt()}", color = TechEmerald)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = product.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(
                                    text = "${product.fileSize} • ${product.downloadCount} downloads",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            IconButton(onClick = { onDeleteProduct(product.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFFF5F56))
                            }
                        }
                    }
                }
            }
            2 -> {
                // Coupons
                item {
                    Button(
                        onClick = { showCouponDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = TechCyan, contentColor = Color(0xFF091426)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().testTag("add_coupon_btn")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Create New Coupon Code", fontWeight = FontWeight.Bold)
                    }
                }

                items(coupons) { coupon ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.padding(14.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = coupon.code,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 15.sp,
                                    color = TechCyan
                                )
                                Text(
                                    text = "${coupon.discountPercentage}% Discount • Min order: ₹${coupon.minPurchase.toInt()}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = coupon.description,
                                    fontSize = 11.sp,
                                    color = TechEmerald
                                )
                            }

                            IconButton(onClick = { onDeleteCoupon(coupon.code) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFFF5F56))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminStatCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = value, fontWeight = FontWeight.Black, fontSize = 16.sp, color = color)
            Text(text = title, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
