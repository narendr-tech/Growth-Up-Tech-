package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.DataSeeder
import com.example.data.local.entity.BlogEntity
import com.example.data.local.entity.CartItemEntity
import com.example.data.local.entity.CouponEntity
import com.example.data.local.entity.CourseEntity
import com.example.data.local.entity.OrderEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.UserEntity
import com.example.data.remote.ChatMessage
import com.example.data.remote.GeneratedAiImage
import com.example.data.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class Screen(val route: String, val title: String) {
    object Home : Screen("home", "Home")
    object Courses : Screen("courses", "Courses")
    object CourseDetail : Screen("course_detail", "Course Details")
    object CoursePlayer : Screen("course_player", "Classroom")
    object Products : Screen("products", "Digital Store")
    object ProductDetail : Screen("product_detail", "Product Details")
    object Services : Screen("services", "Tech Services")
    object Cart : Screen("cart", "Cart & Checkout")
    object Account : Screen("account", "My Account")
    object Admin : Screen("admin", "Admin Dashboard")
    object Blog : Screen("blog", "Articles & Tips")
    object BlogDetail : Screen("blog_detail", "Read Article")
    object Contact : Screen("contact", "Contact Us")
    object Legal : Screen("legal", "Policies & Disclaimer")
    object AiMentor : Screen("ai_mentor", "AI Mentor")
    object AiImageGen : Screen("ai_image_gen", "AI Banner Creator")
}

class GrowthUpViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = AppRepository(db.appDao())

    init {
        viewModelScope.launch {
            DataSeeder.seedInitialData(db.appDao())
        }
    }

    // Database flows
    val allCourses: StateFlow<List<CourseEntity>> = repository.allCourses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val enrolledCourses: StateFlow<List<CourseEntity>> = repository.enrolledCourses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allProducts: StateFlow<List<ProductEntity>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val purchasedProducts: StateFlow<List<ProductEntity>> = repository.purchasedProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cartItems: StateFlow<List<CartItemEntity>> = repository.cartItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allOrders: StateFlow<List<OrderEntity>> = repository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allCoupons: StateFlow<List<CouponEntity>> = repository.allCoupons
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allBlogs: StateFlow<List<BlogEntity>> = repository.allBlogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userProfile: StateFlow<UserEntity?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Navigation and screen selection state
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Home)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _selectedCourse = MutableStateFlow<CourseEntity?>(null)
    val selectedCourse: StateFlow<CourseEntity?> = _selectedCourse.asStateFlow()

    private val _selectedProduct = MutableStateFlow<ProductEntity?>(null)
    val selectedProduct: StateFlow<ProductEntity?> = _selectedProduct.asStateFlow()

    private val _activeLessonIndex = MutableStateFlow(0)
    val activeLessonIndex: StateFlow<Int> = _activeLessonIndex.asStateFlow()

    private val _selectedBlog = MutableStateFlow<BlogEntity?>(null)
    val selectedBlog: StateFlow<BlogEntity?> = _selectedBlog.asStateFlow()

    // Filters and search
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    // Theming & Admin
    private val _isDarkTheme = MutableStateFlow(true)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    private val _isAdminMode = MutableStateFlow(false)
    val isAdminMode: StateFlow<Boolean> = _isAdminMode.asStateFlow()

    // Cart & Coupon
    private val _appliedCoupon = MutableStateFlow<CouponEntity?>(null)
    val appliedCoupon: StateFlow<CouponEntity?> = _appliedCoupon.asStateFlow()

    private val _couponMessage = MutableStateFlow<String?>(null)
    val couponMessage: StateFlow<String?> = _couponMessage.asStateFlow()

    private val _lastCreatedOrder = MutableStateFlow<OrderEntity?>(null)
    val lastCreatedOrder: StateFlow<OrderEntity?> = _lastCreatedOrder.asStateFlow()

    private val _isCheckingOut = MutableStateFlow(false)
    val isCheckingOut: StateFlow<Boolean> = _isCheckingOut.asStateFlow()

    // Gemini Chatbot State
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                role = "model",
                text = "👋 Welcome to **Growth Up Tech**! I am your AI Coding Mentor and Course Advisor.\n\nAsk me about:\n• Course recommendations & learning roadmaps\n• Debugging HTML, CSS, JavaScript, Node.js & React\n• Practical tips to build your tech portfolio\n\nHow can I help you today?"
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isChatLoading = MutableStateFlow(false)
    val isChatLoading: StateFlow<Boolean> = _isChatLoading.asStateFlow()

    // AI Banner Generator State (gemini-3-pro-image-preview with 1K, 2K, 4K)
    private val _aiImagePrompt = MutableStateFlow("Futuristic glowing web development laptop with holographic code and cyan emerald lasers")
    val aiImagePrompt: StateFlow<String> = _aiImagePrompt.asStateFlow()

    private val _selectedResolution = MutableStateFlow("1K") // "1K", "2K", "4K"
    val selectedResolution: StateFlow<String> = _selectedResolution.asStateFlow()

    private val _generatedImage = MutableStateFlow<GeneratedAiImage?>(null)
    val generatedImage: StateFlow<GeneratedAiImage?> = _generatedImage.asStateFlow()

    private val _isGeneratingImage = MutableStateFlow(false)
    val isGeneratingImage: StateFlow<Boolean> = _isGeneratingImage.asStateFlow()

    // Notification banner
    private val _snackMessage = MutableStateFlow<String?>(null)
    val snackMessage: StateFlow<String?> = _snackMessage.asStateFlow()

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun toggleDarkTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }

    fun toggleAdminMode() {
        _isAdminMode.value = !_isAdminMode.value
        showSnack(if (_isAdminMode.value) "Admin Dashboard Unlocked" else "Switched to Learner Mode")
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCategory(cat: String) {
        _selectedCategory.value = cat
    }

    fun viewCourseDetails(course: CourseEntity) {
        _selectedCourse.value = course
        _currentScreen.value = Screen.CourseDetail
    }

    fun openCoursePlayer(course: CourseEntity, initialLesson: Int = 0) {
        _selectedCourse.value = course
        _activeLessonIndex.value = initialLesson
        _currentScreen.value = Screen.CoursePlayer
    }

    fun setActiveLesson(index: Int) {
        _activeLessonIndex.value = index
    }

    fun markCurrentLessonCompleted() {
        val course = _selectedCourse.value ?: return
        val currentIdx = _activeLessonIndex.value
        viewModelScope.launch {
            repository.markLessonCompleted(
                courseId = course.id,
                lessonIndex = currentIdx,
                currentCompleted = course.completedLessons,
                totalLessons = course.lessonsCount
            )
            // Refresh selected course
            val updated = repository.allCourses
            showSnack("Lesson completed! Progress updated.")
        }
    }

    fun viewProductDetails(product: ProductEntity) {
        _selectedProduct.value = product
        _currentScreen.value = Screen.ProductDetail
    }

    fun viewBlog(blog: BlogEntity) {
        _selectedBlog.value = blog
        _currentScreen.value = Screen.BlogDetail
    }

    fun addToCart(
        itemId: Long,
        itemType: String,
        title: String,
        price: Double,
        formatOrDuration: String
    ) {
        viewModelScope.launch {
            repository.addToCart(itemId, itemType, title, price, formatOrDuration)
            showSnack("Added '$title' to Cart!")
        }
    }

    fun updateCartItemQuantity(id: Long, newQuantity: Int) {
        viewModelScope.launch {
            repository.updateCartQuantity(id, newQuantity)
        }
    }

    fun removeCartItem(id: Long) {
        viewModelScope.launch {
            repository.removeFromCart(id)
            showSnack("Item removed from cart.")
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }

    fun applyCoupon(code: String, subtotal: Double) {
        val cleanCode = code.trim().uppercase()
        val found = allCoupons.value.firstOrNull { it.code.uppercase() == cleanCode }
        if (found == null) {
            _couponMessage.value = "Invalid coupon code: $cleanCode"
            _appliedCoupon.value = null
            return
        }
        if (subtotal < found.minPurchase) {
            _couponMessage.value = "Minimum order amount for $cleanCode is ₹${found.minPurchase.toInt()}"
            _appliedCoupon.value = null
            return
        }
        _appliedCoupon.value = found
        _couponMessage.value = "Coupon applied! ${found.description}"
        showSnack("Coupon $cleanCode applied!")
    }

    fun removeCoupon() {
        _appliedCoupon.value = null
        _couponMessage.value = null
    }

    fun executeCheckout(
        items: List<CartItemEntity>,
        subtotal: Double,
        discount: Double,
        tax: Double,
        total: Double,
        paymentMethod: String,
        onComplete: (OrderEntity) -> Unit
    ) {
        if (items.isEmpty()) return
        _isCheckingOut.value = true
        val email = userProfile.value?.email ?: "learner@growthup.tech"

        viewModelScope.launch {
            // Realistic simulated verification delay
            kotlinx.coroutines.delay(1200)
            val order = repository.completeCheckout(
                cartItems = items,
                subtotal = subtotal,
                discount = discount,
                tax = tax,
                total = total,
                paymentMethod = paymentMethod,
                customerEmail = email
            )
            _lastCreatedOrder.value = order
            _appliedCoupon.value = null
            _couponMessage.value = null
            _isCheckingOut.value = false
            showSnack("Payment Verified! Order #${order.orderId} Confirmed.")
            onComplete(order)
        }
    }

    fun submitServiceInquiry(
        serviceName: String,
        clientName: String,
        clientEmail: String,
        budget: String,
        timeline: String,
        details: String
    ) {
        viewModelScope.launch {
            repository.submitServiceInquiry(
                serviceName, clientName, clientEmail, budget, timeline, details
            )
            showSnack("Service inquiry submitted! Our tech team will email your quote.")
        }
    }

    fun updateUser(name: String, headline: String, bio: String) {
        val current = userProfile.value ?: return
        viewModelScope.launch {
            repository.updateUserProfile(
                current.copy(fullName = name, headline = headline, bio = bio)
            )
            showSnack("Profile details updated!")
        }
    }

    // Gemini Chatbot
    fun sendChatMessage(text: String, isComplex: Boolean = false) {
        if (text.isBlank()) return
        val userMsg = ChatMessage(role = "user", text = text)
        val updatedHistory = _chatMessages.value + userMsg
        _chatMessages.value = updatedHistory
        _isChatLoading.value = true

        viewModelScope.launch {
            val replyText = repository.sendChatMessage(
                history = updatedHistory,
                message = text,
                isComplex = isComplex
            )
            _chatMessages.value = updatedHistory + ChatMessage(role = "model", text = replyText)
            _isChatLoading.value = false
        }
    }

    // Gemini AI Image Generator (gemini-3-pro-image-preview with 1K, 2K, 4K)
    fun setAiImagePrompt(prompt: String) {
        _aiImagePrompt.value = prompt
    }

    fun setSelectedResolution(res: String) {
        _selectedResolution.value = res
    }

    fun generateCourseBanner() {
        val prompt = _aiImagePrompt.value
        val res = _selectedResolution.value
        _isGeneratingImage.value = true
        _generatedImage.value = null

        viewModelScope.launch {
            val result = repository.generateAiImage(prompt, res)
            _generatedImage.value = result
            _isGeneratingImage.value = false
            if (result.errorMessage != null) {
                showSnack(result.errorMessage)
            } else {
                showSnack("Generated $res graphic successfully!")
            }
        }
    }

    // Admin Operations
    fun saveCourse(course: CourseEntity) {
        viewModelScope.launch {
            repository.saveCourse(course)
            showSnack("Course saved successfully!")
        }
    }

    fun deleteCourse(id: Long) {
        viewModelScope.launch {
            repository.deleteCourse(id)
            showSnack("Course deleted from catalog.")
        }
    }

    fun saveProduct(product: ProductEntity) {
        viewModelScope.launch {
            repository.saveProduct(product)
            showSnack("Digital product updated!")
        }
    }

    fun deleteProduct(id: Long) {
        viewModelScope.launch {
            repository.deleteProduct(id)
            showSnack("Product deleted from catalog.")
        }
    }

    fun saveCoupon(code: String, discountPct: Int, minPurchase: Double, expiry: String, desc: String) {
        viewModelScope.launch {
            repository.saveCoupon(
                CouponEntity(
                    code = code.uppercase(),
                    discountPercentage = discountPct,
                    minPurchase = minPurchase,
                    expiryDate = expiry,
                    description = desc
                )
            )
            showSnack("Coupon $code created!")
        }
    }

    fun deleteCoupon(code: String) {
        viewModelScope.launch {
            repository.deleteCoupon(code)
            showSnack("Coupon deleted.")
        }
    }

    fun showSnack(msg: String) {
        _snackMessage.value = msg
    }

    fun clearSnack() {
        _snackMessage.value = null
    }
}
