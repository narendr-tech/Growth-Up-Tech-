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
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DesignServices
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.TechBadge
import com.example.ui.theme.TechCyan
import com.example.ui.theme.TechEmerald

data class TechServiceItem(
    val id: String,
    val title: String,
    val startingPrice: String,
    val icon: ImageVector,
    val timeline: String,
    val description: String,
    val deliverables: List<String>
)

@Composable
fun ServicesScreen(
    onSubmitInquiry: (String, String, String, String, String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val services = listOf(
        TechServiceItem(
            id = "web_dev",
            title = "Custom Website Design & Development",
            startingPrice = "From ₹14,999",
            icon = Icons.Default.DesignServices,
            timeline = "1-2 Weeks",
            description = "High-performance, mobile-responsive custom websites built with clean semantic code, modern animations, and SEO best practices.",
            deliverables = listOf("Responsive on Mobile/Tablet/Desktop", "Dark & Light Mode Support", "SEO Optimized Architecture", "Fast 95+ PageSpeed score", "Source Code Handover")
        ),
        TechServiceItem(
            id = "fullstack_app",
            title = "Full-Stack Web App & SaaS MVP",
            startingPrice = "From ₹29,999",
            icon = Icons.Default.RocketLaunch,
            timeline = "3-4 Weeks",
            description = "Turn your startup idea into reality with a working SaaS MVP including database schemas, user authentication, and payment flows.",
            deliverables = listOf("Next.js or Node.js Backend", "PostgreSQL / MongoDB Database", "Authentication (JWT / OAuth)", "Payment Gateway Integration", "Admin Dashboard")
        ),
        TechServiceItem(
            id = "api_backend",
            title = "Backend Engineering & Microservices",
            startingPrice = "From ₹19,999",
            icon = Icons.Default.Code,
            timeline = "2 Weeks",
            description = "Scalable REST APIs and cloud services with Redis caching, rate limiting, and Docker containerization.",
            deliverables = listOf("RESTful API Documentation", "Redis Caching Layer", "Database Migrations & Indices", "Docker & Cloud Setup")
        ),
        TechServiceItem(
            id = "performance_audit",
            title = "Code Review & Performance Optimization",
            startingPrice = "From ₹4,999",
            icon = Icons.Default.Speed,
            timeline = "3-5 Days",
            description = "In-depth code architecture review, security vulnerability audit, query optimization, and Lighthouse score boosting.",
            deliverables = listOf("Comprehensive PDF Audit Report", "Security Risk Breakdown", "Database Query Analysis", "Actionable Refactoring PR")
        )
    )

    var selectedServiceForQuote by remember { mutableStateOf<TechServiceItem?>(null) }
    var clientName by remember { mutableStateOf("") }
    var clientEmail by remember { mutableStateOf("") }
    var budgetRange by remember { mutableStateOf("₹15,000 - ₹30,000") }
    var projectTimeline by remember { mutableStateOf("2-4 Weeks") }
    var projectDetails by remember { mutableStateOf("") }

    if (selectedServiceForQuote != null) {
        val s = selectedServiceForQuote!!
        AlertDialog(
            onDismissRequest = { selectedServiceForQuote = null },
            title = {
                Text(text = "Request a Quote", fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Service: ${s.title}",
                        style = MaterialTheme.typography.bodySmall.copy(color = TechCyan, fontWeight = FontWeight.Bold)
                    )

                    OutlinedTextField(
                        value = clientName,
                        onValueChange = { clientName = it },
                        label = { Text("Your Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = clientEmail,
                        onValueChange = { clientEmail = it },
                        label = { Text("Your Email") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = projectDetails,
                        onValueChange = { projectDetails = it },
                        label = { Text("Project Details / Requirements") },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (clientName.isNotBlank() && clientEmail.contains("@")) {
                            onSubmitInquiry(
                                s.title,
                                clientName,
                                clientEmail,
                                budgetRange,
                                projectTimeline,
                                projectDetails
                            )
                            selectedServiceForQuote = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TechCyan, contentColor = Color(0xFF091426)),
                    modifier = Modifier.testTag("submit_quote_btn")
                ) {
                    Text("Submit Inquiry")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedServiceForQuote = null }) {
                    Text("Cancel")
                }
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
                Text(
                    text = "Professional Tech Services",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "High-quality engineering services tailored for creators, startups, and growing businesses",
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
            }
        }

        items(services) { item ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth().testTag("service_card_${item.id}")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(TechCyan.copy(alpha = 0.15f))
                        ) {
                            Icon(imageVector = item.icon, contentDescription = null, tint = TechCyan)
                        }

                        TechBadge(text = item.startingPrice, color = TechEmerald)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = item.description,
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Key Deliverables:",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    item.deliverables.forEach { deliv ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = TechEmerald,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = deliv,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { selectedServiceForQuote = item },
                        colors = ButtonDefaults.buttonColors(containerColor = TechCyan, contentColor = Color(0xFF091426)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().testTag("request_quote_btn_${item.id}")
                    ) {
                        Text("Request a Free Quote & Timeline", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
