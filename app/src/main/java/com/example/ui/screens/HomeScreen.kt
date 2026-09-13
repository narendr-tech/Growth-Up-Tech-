package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.entity.CourseEntity
import com.example.data.local.entity.ProductEntity
import com.example.ui.components.CourseCard
import com.example.ui.components.FaqAccordionItem
import com.example.ui.components.ProductCard
import com.example.ui.components.SectionHeader
import com.example.ui.components.TechBadge
import com.example.ui.theme.TechAmber
import com.example.ui.theme.TechCyan
import com.example.ui.theme.TechEmerald
import com.example.ui.viewmodel.Screen

@Composable
fun HomeScreen(
    courses: List<CourseEntity>,
    products: List<ProductEntity>,
    onNavigate: (Screen) -> Unit,
    onCourseClick: (CourseEntity) -> Unit,
    onProductClick: (ProductEntity) -> Unit,
    onAddToCart: (Long, String, String, Double, String) -> Unit,
    onShowSnack: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf("All", "Web Development", "JavaScript", "Backend", "HTML", "Website templates", "UI kits", "Coding notes")
    var selectedCategory by remember { mutableStateOf("All") }
    var newsletterEmail by remember { mutableStateOf("") }
    var newsletterSubscribed by remember { mutableStateOf(false) }

    val featuredCourses = courses.filter { it.isFeatured }
    val featuredProducts = products.filter { it.isFeatured }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // 1. HERO SECTION
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFF091426),
                                Color(0xFF0F172A),
                                MaterialTheme.colorScheme.background
                            )
                        )
                    )
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Modern Tech Tag
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(TechCyan.copy(alpha = 0.15f))
                            .border(1.dp, TechCyan.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = TechCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "GROWTH UP TECH ACADEMY & MARKETPLACE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = TechCyan,
                                letterSpacing = 1.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Hero Banner Graphic
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.hero_growth_tech),
                            contentDescription = "Growth Up Tech Hero Banner",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Learn. Build. Grow.",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = (-0.5).sp,
                            color = Color.White
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Learn coding, web development and practical digital skills through beginner-friendly courses, projects and resources.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFF94A3B8),
                            lineHeight = 22.sp
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Action buttons
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { onNavigate(Screen.Courses) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = TechCyan,
                                contentColor = Color(0xFF061426)
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("explore_courses_btn")
                        ) {
                            Text(text = "Explore Courses", fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        OutlinedButton(
                            onClick = { onNavigate(Screen.Products) },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = TechEmerald
                            ),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                brush = Brush.horizontalGradient(listOf(TechEmerald, TechCyan))
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("view_products_btn")
                        ) {
                            Text(text = "View Products", fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Trust metrics bar
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF131F37))
                            .padding(vertical = 12.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "15,000+", fontWeight = FontWeight.Bold, color = TechCyan, fontSize = 14.sp)
                            Text(text = "Active Learners", fontSize = 10.sp, color = Color(0xFF94A3B8))
                        }
                        Box(modifier = Modifier.width(1.dp).height(24.dp).background(Color(0xFF2E3E75)))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "100+ Assets", fontWeight = FontWeight.Bold, color = TechEmerald, fontSize = 14.sp)
                            Text(text = "Verified Code", fontSize = 10.sp, color = Color(0xFF94A3B8))
                        }
                        Box(modifier = Modifier.width(1.dp).height(24.dp).background(Color(0xFF2E3E75)))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "4.9 / 5.0", fontWeight = FontWeight.Bold, color = TechAmber, fontSize = 14.sp)
                            Text(text = "Real Reviews", fontSize = 10.sp, color = Color(0xFF94A3B8))
                        }
                    }
                }
            }
        }

        // 2. QUICK CATEGORIES
        item {
            Column(modifier = Modifier.padding(top = 16.dp)) {
                Text(
                    text = "Browse By Technology",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                }
            }
        }

        // 3. FEATURED COURSES
        item {
            SectionHeader(
                title = "Featured Courses",
                subtitle = "Comprehensive curriculum with practical coding modules",
                actionLabel = "View All (${courses.size})",
                onActionClick = { onNavigate(Screen.Courses) }
            )
        }

        items(featuredCourses) { course ->
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                CourseCard(
                    course = course,
                    onClick = { onCourseClick(course) },
                    onAddToCart = {
                        onAddToCart(course.id, "COURSE", course.title, course.discountPrice, course.duration)
                    }
                )
            }
        }

        // 4. FEATURED DIGITAL PRODUCTS
        item {
            Spacer(modifier = Modifier.height(16.dp))
            SectionHeader(
                title = "Featured Digital Products",
                subtitle = "Production-ready templates, UI kits and source code",
                actionLabel = "Browse All",
                onActionClick = { onNavigate(Screen.Products) }
            )
        }

        items(featuredProducts) { product ->
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                ProductCard(
                    product = product,
                    onClick = { onProductClick(product) },
                    onAddToCart = {
                        onAddToCart(product.id, "PRODUCT", product.title, product.discountPrice, product.format)
                    }
                )
            }
        }

        // 5. WHY CHOOSE GROWTH UP TECH
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Why Choose Growth Up Tech?",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Real technical skills without fake earning claims or deceptive promises.",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    WhyFeatureRow(
                        icon = Icons.Default.Code,
                        title = "Production-Grade Code",
                        desc = "Learn modern industry practices: clean architecture, TypeScript, REST API best practices, and automated testing."
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    WhyFeatureRow(
                        icon = Icons.Default.Payments,
                        title = "Legitimate Value & Fair Pricing",
                        desc = "No exaggerated hype. You pay for well-engineered courses and digital assets with lifetime update access."
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    WhyFeatureRow(
                        icon = Icons.Default.Security,
                        title = "Secure Indian & Global Payments",
                        desc = "Seamless UPI, Cards, NetBanking, and Instant Receipt verification without storing sensitive data."
                    )
                }
            }
        }

        // 6. STUDENT TESTIMONIALS
        item {
            Spacer(modifier = Modifier.height(20.dp))
            SectionHeader(
                title = "What Our Students Say",
                subtitle = "Authentic reviews from engineers and creators"
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val testimonials = listOf(
                    Triple("Rahul Verma", "Frontend Developer at SoftCorp", "The Full-Stack 2026 course taught me more practical async JS and Node architecture than 6 months of scattered YouTube tutorials. The certificate was accepted in my technical interviews!"),
                    Triple("Ananya Sen", "Freelance UI Engineer", "The SaaS Launchpad template and animated CSS kit saved our agency over 40 hours of development time. Clean, commented code!"),
                    Triple("Karthik M.", "Self-Taught Programmer", "I love that Growth Up Tech has zero fake income promises. Just pure, disciplined coding education and verifiable projects.")
                )

                items(testimonials) { (name, role, text) ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.width(280.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(TechCyan.copy(alpha = 0.2f))
                                ) {
                                    Text(
                                        text = name.take(1),
                                        fontWeight = FontWeight.Bold,
                                        color = TechCyan
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(text = name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text(text = role, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "“$text”",
                                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp)
                            )
                        }
                    }
                }
            }
        }

        // 7. FREQUENTLY ASKED QUESTIONS
        item {
            Spacer(modifier = Modifier.height(24.dp))
            SectionHeader(
                title = "Frequently Asked Questions",
                subtitle = "Clear answers regarding access, payments, and certificates"
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                FaqAccordionItem(
                    question = "How do I access courses after payment?",
                    answer = "Immediately after payment verification, courses are unlocked in your 'My Account > My Courses' tab. You can stream lessons, view interactive code snippets, and track progress anytime."
                )
                FaqAccordionItem(
                    question = "How do digital downloads work?",
                    answer = "Purchased templates, source code files, notes, and UI kits become available instantly under 'My Purchases' with secure 1-click download access and in-app code preview."
                )
                FaqAccordionItem(
                    question = "Are the certificates verified?",
                    answer = "Yes. Upon completing all lessons in a course, Growth Up Tech issues an authentic certificate with a unique Verification Credential ID."
                )
                FaqAccordionItem(
                    question = "What payment methods are supported?",
                    answer = "We support UPI (Google Pay, PhonePe, Paytm, BHIM), Debit & Credit Cards, Net Banking across major Indian banks, and digital wallets."
                )
            }
        }

        // 8. NEWSLETTER SUBSCRIPTION
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F1E36)),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.horizontalGradient(listOf(TechCyan.copy(alpha = 0.4f), TechEmerald.copy(alpha = 0.4f)))
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = TechCyan,
                        modifier = Modifier.size(32.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Join 15,000+ Tech Learners",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )

                    Text(
                        text = "Subscribe to get weekly developer cheat sheets, new project releases, and receive 20% off your first purchase.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF94A3B8),
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                    )

                    if (newsletterSubscribed) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(TechEmerald.copy(alpha = 0.2f))
                                .padding(horizontal = 16.dp, vertical = 10.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = TechEmerald)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Subscribed! Use code WELCOME20 for 20% discount.",
                                color = TechEmerald,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    } else {
                        OutlinedTextField(
                            value = newsletterEmail,
                            onValueChange = { newsletterEmail = it },
                            placeholder = { Text("Enter your email address", color = Color(0xFF64748B)) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = TechCyan,
                                unfocusedBorderColor = Color(0xFF334155)
                            ),
                            modifier = Modifier.fillMaxWidth().testTag("newsletter_email_input")
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                if (newsletterEmail.contains("@")) {
                                    newsletterSubscribed = true
                                    onShowSnack("Welcome! Use coupon code WELCOME20 for 20% discount.")
                                } else {
                                    onShowSnack("Please enter a valid email address.")
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = TechCyan, contentColor = Color(0xFF0F1E36)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth().testTag("newsletter_submit_btn")
                        ) {
                            Text(text = "Get 20% Off Coupon", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // 9. BOTTOM QUICK LINKS & FOOTER
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "About Us",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp,
                        modifier = Modifier.clickable { onNavigate(Screen.Legal) }.padding(8.dp)
                    )
                    Text(
                        text = "•",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Text(
                        text = "Contact",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp,
                        modifier = Modifier.clickable { onNavigate(Screen.Contact) }.padding(8.dp)
                    )
                    Text(
                        text = "•",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Text(
                        text = "Blog & Tips",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp,
                        modifier = Modifier.clickable { onNavigate(Screen.Blog) }.padding(8.dp)
                    )
                    Text(
                        text = "•",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Text(
                        text = "Policies",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp,
                        modifier = Modifier.clickable { onNavigate(Screen.Legal) }.padding(8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "© 2026 Growth Up Tech. All rights reserved.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Learn. Build. Grow. • Genuine digital education & resources.",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun WhyFeatureRow(
    icon: ImageVector,
    title: String,
    desc: String
) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(TechCyan.copy(alpha = 0.15f))
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = TechCyan, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(text = desc, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 18.sp)
        }
    }
}
