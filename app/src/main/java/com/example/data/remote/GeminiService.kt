package com.example.data.remote

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class ChatMessage(
    val role: String, // "user" or "model"
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class GeneratedAiImage(
    val bitmap: Bitmap?,
    val resolution: String,
    val prompt: String,
    val errorMessage: String? = null
)

object GeminiService {

    private const val TAG = "GeminiService"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models"

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private fun getApiKey(): String {
        return try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * Multi-turn chat with Gemini using gemini-3.5-flash or gemini-3.1-pro-preview
     */
    suspend fun sendMessage(
        history: List<ChatMessage>,
        userMessage: String,
        isComplexTask: Boolean = false
    ): String = withContext(Dispatchers.IO) {
        val apiKey = getApiKey()
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext simulateMentorResponse(userMessage)
        }

        val model = if (isComplexTask) "gemini-3.1-pro-preview" else "gemini-3.5-flash"
        val endpoint = "$BASE_URL/$model:generateContent?key=$apiKey"

        try {
            val rootJson = JSONObject()

            // System instruction
            val systemInstruction = JSONObject()
            val systemParts = JSONArray()
            systemParts.put(
                JSONObject().put(
                    "text",
                    "You are Growth Up Tech's expert AI Coding Mentor & Course Advisor. " +
                            "Help users learn coding (HTML, CSS, JavaScript, React, Node.js, Python, full-stack web dev), " +
                            "debug errors, choose the best courses/resources on Growth Up Tech, and write clean, accessible, modern code. " +
                            "Be friendly, practical, concise, and format code snippets in markdown."
                )
            )
            systemInstruction.put("parts", systemParts)
            rootJson.put("systemInstruction", systemInstruction)

            // Conversation history + new message
            val contentsArray = JSONArray()
            for (msg in history.takeLast(10)) {
                val contentObj = JSONObject()
                contentObj.put("role", msg.role)
                val parts = JSONArray()
                parts.put(JSONObject().put("text", msg.text))
                contentObj.put("parts", parts)
                contentsArray.put(contentObj)
            }

            // Current user turn
            val currentTurn = JSONObject()
            currentTurn.put("role", "user")
            val currentParts = JSONArray()
            currentParts.put(JSONObject().put("text", userMessage))
            currentTurn.put("parts", currentParts)
            contentsArray.put(currentTurn)

            rootJson.put("contents", contentsArray)

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val body = rootJson.toString().toRequestBody(mediaType)
            val request = Request.Builder().url(endpoint).post(body).build()

            client.newCall(request).execute().use { response ->
                val responseBody = response.body?.string() ?: ""
                if (!response.isSuccessful) {
                    Log.e(TAG, "Gemini API Error: ${response.code} $responseBody")
                    return@withContext simulateMentorResponse(userMessage)
                }

                val jsonResponse = JSONObject(responseBody)
                val candidates = jsonResponse.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val firstCandidate = candidates.getJSONObject(0)
                    val content = firstCandidate.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        return@withContext parts.getJSONObject(0).optString("text", "No response text")
                    }
                }
                "I'm here to help you learn and build! What coding topic would you like to explore next?"
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception calling Gemini API", e)
            simulateMentorResponse(userMessage)
        }
    }

    /**
     * Generate High-Quality Image using gemini-3-pro-image-preview
     * with affordance for 1K, 2K, 4K resolution
     */
    suspend fun generateCourseBanner(
        prompt: String,
        imageSize: String = "1K", // "1K", "2K", "4K"
        aspectRatio: String = "16:9"
    ): GeneratedAiImage = withContext(Dispatchers.IO) {
        val apiKey = getApiKey()
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext GeneratedAiImage(
                bitmap = null,
                resolution = imageSize,
                prompt = prompt,
                errorMessage = "Add your Gemini API key in Secrets to generate live AI images with gemini-3-pro-image-preview. A preview placeholder is shown."
            )
        }

        val model = "gemini-3-pro-image-preview"
        val endpoint = "$BASE_URL/$model:generateContent?key=$apiKey"

        try {
            val rootJson = JSONObject()
            val contentsArray = JSONArray()
            val turn = JSONObject()
            val parts = JSONArray()
            parts.put(JSONObject().put("text", "Create a modern, high-tech graphic for Growth Up Tech: $prompt"))
            turn.put("parts", parts)
            contentsArray.put(turn)
            rootJson.put("contents", contentsArray)

            val genConfig = JSONObject()
            val imageConfig = JSONObject()
            imageConfig.put("aspectRatio", aspectRatio)
            imageConfig.put("imageSize", imageSize)
            genConfig.put("imageConfig", imageConfig)

            val modalities = JSONArray()
            modalities.put("TEXT")
            modalities.put("IMAGE")
            genConfig.put("responseModalities", modalities)
            rootJson.put("generationConfig", genConfig)

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val body = rootJson.toString().toRequestBody(mediaType)
            val request = Request.Builder().url(endpoint).post(body).build()

            client.newCall(request).execute().use { response ->
                val responseBody = response.body?.string() ?: ""
                if (!response.isSuccessful) {
                    return@withContext GeneratedAiImage(
                        bitmap = null,
                        resolution = imageSize,
                        prompt = prompt,
                        errorMessage = "Model request failed (${response.code})."
                    )
                }

                val jsonResponse = JSONObject(responseBody)
                val candidates = jsonResponse.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val partsList = candidates.getJSONObject(0).optJSONObject("content")?.optJSONArray("parts")
                    if (partsList != null) {
                        for (i in 0 until partsList.length()) {
                            val part = partsList.getJSONObject(i)
                            val inlineData = part.optJSONObject("inlineData")
                            if (inlineData != null) {
                                val base64Data = inlineData.optString("data", "")
                                if (base64Data.isNotEmpty()) {
                                    val bytes = Base64.decode(base64Data, Base64.DEFAULT)
                                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                                    return@withContext GeneratedAiImage(
                                        bitmap = bitmap,
                                        resolution = imageSize,
                                        prompt = prompt
                                    )
                                }
                            }
                        }
                    }
                }
                GeneratedAiImage(
                    bitmap = null,
                    resolution = imageSize,
                    prompt = prompt,
                    errorMessage = "No image data returned."
                )
            }
        } catch (e: Exception) {
            GeneratedAiImage(
                bitmap = null,
                resolution = imageSize,
                prompt = prompt,
                errorMessage = e.message
            )
        }
    }

    private fun simulateMentorResponse(prompt: String): String {
        val lower = prompt.lowercase()
        return when {
            lower.contains("course") || lower.contains("learn") -> {
                "Welcome to **Growth Up Tech**! For beginners, I strongly recommend starting with **'Complete Modern Full-Stack Web Development 2026'** which takes you step-by-step through HTML5, CSS Grid, Modern JavaScript, and Node.js. If you already have basics down, check out **'Advanced JavaScript & TypeScript Mastery'**."
            }
            lower.contains("javascript") || lower.contains("js") -> {
                "Here is a golden JavaScript best practice:\n\n```javascript\n// Prefer async/await with Promise.all for parallel operations\nasync function loadDashboardData() {\n  const [courses, products] = await Promise.all([\n    fetch('/api/courses').then(r => r.json()),\n    fetch('/api/products').then(r => r.json())\n  ]);\n  return { courses, products };\n}\n```\nAlways handle potential promise rejections with `try/catch`!"
            }
            lower.contains("product") || lower.contains("template") -> {
                "Our top digital assets include the **SaaS Launchpad Web Template** (Next.js + Tailwind + billing) and the **Full-Stack Hand-Written Notes PDF**. Both include full commercial licenses and instant downloads!"
            }
            lower.contains("discount") || lower.contains("coupon") -> {
                "You can use coupon code **`GROWTH50`** for 50% off on orders above ₹499, or **`WELCOME20`** for 20% off on your first purchase!"
            }
            else -> {
                "Hello! I'm your **Growth Up Tech AI Coding Mentor**.\n\nI can help you:\n1. Debug coding problems and explain code snippets\n2. Recommend the best course for your career goals\n3. Review web development best practices (HTML, CSS, JS, Node, DBs)\n\nWhat are you working on today?"
            }
        }
    }
}
