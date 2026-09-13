package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.GrowthUpBottomNav
import com.example.ui.components.GrowthUpTopBar
import com.example.ui.screens.AccountScreen
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.AiImageGenScreen
import com.example.ui.screens.AiMentorScreen
import com.example.ui.screens.BlogScreen
import com.example.ui.screens.CartScreen
import com.example.ui.screens.ContactScreen
import com.example.ui.screens.CourseDetailScreen
import com.example.ui.screens.CoursePlayerScreen
import com.example.ui.screens.CoursesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LegalScreen
import com.example.ui.screens.ProductDetailScreen
import com.example.ui.screens.ProductsScreen
import com.example.ui.screens.ServicesScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.GrowthUpViewModel
import com.example.ui.viewmodel.Screen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: GrowthUpViewModel = viewModel()
            val isDarkTheme by viewModel.isDarkTheme.collectAsState()

            MyApplicationTheme(darkTheme = isDarkTheme) {
                GrowthUpApp(viewModel)
            }
        }
    }
}

@Composable
fun GrowthUpApp(viewModel: GrowthUpViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val isDarkTheme by viewModel.isDarkTheme.collectAsState()
    val isAdminMode by viewModel.isAdminMode.collectAsState()

    val courses by viewModel.allCourses.collectAsState()
    val enrolledCourses by viewModel.enrolledCourses.collectAsState()
    val products by viewModel.allProducts.collectAsState()
    val purchasedProducts by viewModel.purchasedProducts.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()
    val orders by viewModel.allOrders.collectAsState()
    val coupons by viewModel.allCoupons.collectAsState()
    val blogs by viewModel.allBlogs.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    val selectedCourse by viewModel.selectedCourse.collectAsState()
    val selectedProduct by viewModel.selectedProduct.collectAsState()
    val selectedBlog by viewModel.selectedBlog.collectAsState()
    val activeLessonIndex by viewModel.activeLessonIndex.collectAsState()

    val appliedCoupon by viewModel.appliedCoupon.collectAsState()
    val couponMessage by viewModel.couponMessage.collectAsState()
    val isCheckingOut by viewModel.isCheckingOut.collectAsState()
    val lastCreatedOrder by viewModel.lastCreatedOrder.collectAsState()

    val chatMessages by viewModel.chatMessages.collectAsState()
    val isChatLoading by viewModel.isChatLoading.collectAsState()

    val aiImagePrompt by viewModel.aiImagePrompt.collectAsState()
    val selectedResolution by viewModel.selectedResolution.collectAsState()
    val generatedImage by viewModel.generatedImage.collectAsState()
    val isGeneratingImage by viewModel.isGeneratingImage.collectAsState()

    val snackMessage by viewModel.snackMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackMessage) {
        snackMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnack()
        }
    }

    // Handle back button behavior
    BackHandler(enabled = currentScreen != Screen.Home) {
        when (currentScreen) {
            Screen.CourseDetail, Screen.CoursePlayer -> viewModel.navigateTo(Screen.Courses)
            Screen.ProductDetail -> viewModel.navigateTo(Screen.Products)
            Screen.BlogDetail -> viewModel.navigateTo(Screen.Blog)
            else -> viewModel.navigateTo(Screen.Home)
        }
    }

    Scaffold(
        topBar = {
            GrowthUpTopBar(
                cartCount = cartItems.sumOf { it.quantity },
                isDarkTheme = isDarkTheme,
                isAdminMode = isAdminMode,
                onBrandClick = { viewModel.navigateTo(Screen.Home) },
                onCartClick = { viewModel.navigateTo(Screen.Cart) },
                onThemeToggle = { viewModel.toggleDarkTheme() },
                onAdminToggle = { viewModel.toggleAdminMode() },
                onAiMentorClick = { viewModel.navigateTo(Screen.AiMentor) }
            )
        },
        bottomBar = {
            GrowthUpBottomNav(
                currentScreen = currentScreen,
                onSelectScreen = { screen -> viewModel.navigateTo(screen) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        val screenModifier = Modifier.padding(innerPadding)

        when (currentScreen) {
            Screen.Home -> {
                HomeScreen(
                    courses = courses,
                    products = products,
                    onNavigate = { viewModel.navigateTo(it) },
                    onCourseClick = { viewModel.viewCourseDetails(it) },
                    onProductClick = { viewModel.viewProductDetails(it) },
                    onAddToCart = { id, type, title, price, format ->
                        viewModel.addToCart(id, type, title, price, format)
                    },
                    onShowSnack = { viewModel.showSnack(it) },
                    modifier = screenModifier
                )
            }
            Screen.Courses -> {
                CoursesScreen(
                    courses = courses,
                    onCourseClick = { viewModel.viewCourseDetails(it) },
                    onResumeCourse = { viewModel.openCoursePlayer(it) },
                    onAddToCart = { id, type, title, price, duration ->
                        viewModel.addToCart(id, type, title, price, duration)
                    },
                    modifier = screenModifier
                )
            }
            Screen.CourseDetail -> {
                selectedCourse?.let { course ->
                    CourseDetailScreen(
                        course = course,
                        onBack = { viewModel.navigateTo(Screen.Courses) },
                        onAddToCart = {
                            viewModel.addToCart(course.id, "COURSE", course.title, course.discountPrice, course.duration)
                        },
                        onStartLearning = { viewModel.openCoursePlayer(course) },
                        modifier = screenModifier
                    )
                } ?: viewModel.navigateTo(Screen.Courses)
            }
            Screen.CoursePlayer -> {
                selectedCourse?.let { course ->
                    CoursePlayerScreen(
                        course = course,
                        activeLessonIndex = activeLessonIndex,
                        userName = userProfile?.fullName ?: "Alex Developer",
                        onBack = { viewModel.navigateTo(Screen.Courses) },
                        onSelectLesson = { viewModel.setActiveLesson(it) },
                        onCompleteLesson = { viewModel.markCurrentLessonCompleted() },
                        modifier = screenModifier
                    )
                } ?: viewModel.navigateTo(Screen.Courses)
            }
            Screen.Products -> {
                ProductsScreen(
                    products = products,
                    onProductClick = { viewModel.viewProductDetails(it) },
                    onAddToCart = { id, type, title, price, format ->
                        viewModel.addToCart(id, type, title, price, format)
                    },
                    modifier = screenModifier
                )
            }
            Screen.ProductDetail -> {
                selectedProduct?.let { product ->
                    ProductDetailScreen(
                        product = product,
                        onBack = { viewModel.navigateTo(Screen.Products) },
                        onAddToCart = {
                            viewModel.addToCart(product.id, "PRODUCT", product.title, product.discountPrice, product.format)
                        },
                        modifier = screenModifier
                    )
                } ?: viewModel.navigateTo(Screen.Products)
            }
            Screen.Services -> {
                ServicesScreen(
                    onSubmitInquiry = { service, name, email, budget, timeline, details ->
                        viewModel.submitServiceInquiry(service, name, email, budget, timeline, details)
                    },
                    modifier = screenModifier
                )
            }
            Screen.Cart -> {
                CartScreen(
                    cartItems = cartItems,
                    appliedCoupon = appliedCoupon,
                    couponMessage = couponMessage,
                    isCheckingOut = isCheckingOut,
                    lastCreatedOrder = lastCreatedOrder,
                    onUpdateQuantity = { id, qty -> viewModel.updateCartItemQuantity(id, qty) },
                    onRemoveItem = { id -> viewModel.removeCartItem(id) },
                    onApplyCoupon = { code, subtotal -> viewModel.applyCoupon(code, subtotal) },
                    onRemoveCoupon = { viewModel.removeCoupon() },
                    onExecuteCheckout = { items, subtotal, discount, tax, total, method ->
                        viewModel.executeCheckout(items, subtotal, discount, tax, total, method) {
                            // Checked out callback
                        }
                    },
                    onGoToPurchases = { viewModel.navigateTo(Screen.Account) },
                    modifier = screenModifier
                )
            }
            Screen.Account -> {
                AccountScreen(
                    user = userProfile,
                    enrolledCourses = enrolledCourses,
                    purchasedProducts = purchasedProducts,
                    orders = orders,
                    onCourseClick = { viewModel.viewCourseDetails(it) },
                    onProductClick = { viewModel.viewProductDetails(it) },
                    onResumeCourse = { viewModel.openCoursePlayer(it) },
                    onUpdateProfile = { name, headline, bio -> viewModel.updateUser(name, headline, bio) },
                    onNavigate = { viewModel.navigateTo(it) },
                    modifier = screenModifier
                )
            }
            Screen.Admin -> {
                AdminScreen(
                    courses = courses,
                    products = products,
                    orders = orders,
                    coupons = coupons,
                    onSaveCourse = { viewModel.saveCourse(it) },
                    onDeleteCourse = { viewModel.deleteCourse(it) },
                    onSaveProduct = { viewModel.saveProduct(it) },
                    onDeleteProduct = { viewModel.deleteProduct(it) },
                    onSaveCoupon = { code, pct, min, expiry, desc ->
                        viewModel.saveCoupon(code, pct, min, expiry, desc)
                    },
                    onDeleteCoupon = { viewModel.deleteCoupon(it) },
                    modifier = screenModifier
                )
            }
            Screen.AiMentor -> {
                AiMentorScreen(
                    messages = chatMessages,
                    isLoading = isChatLoading,
                    onSendMessage = { text, isComplex -> viewModel.sendChatMessage(text, isComplex) },
                    modifier = screenModifier
                )
            }
            Screen.AiImageGen -> {
                AiImageGenScreen(
                    prompt = aiImagePrompt,
                    selectedResolution = selectedResolution,
                    isGenerating = isGeneratingImage,
                    generatedImage = generatedImage,
                    onPromptChange = { viewModel.setAiImagePrompt(it) },
                    onResolutionChange = { viewModel.setSelectedResolution(it) },
                    onGenerateClick = { viewModel.generateCourseBanner() },
                    onSaveImage = { viewModel.showSnack("Banner image saved to your local gallery!") },
                    modifier = screenModifier
                )
            }
            Screen.Blog, Screen.BlogDetail -> {
                BlogScreen(
                    blogs = blogs,
                    selectedBlog = selectedBlog,
                    onBlogClick = { viewModel.viewBlog(it) },
                    onBackToList = { viewModel.navigateTo(Screen.Blog) },
                    modifier = screenModifier
                )
            }
            Screen.Contact -> {
                ContactScreen(
                    onSubmitMessage = { name, email, msg ->
                        viewModel.showSnack("Thank you $name! Your message has been sent to our tech team.")
                    },
                    modifier = screenModifier
                )
            }
            Screen.Legal -> {
                LegalScreen(modifier = screenModifier)
            }
        }
    }
}
