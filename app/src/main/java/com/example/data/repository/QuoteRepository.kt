package com.example.data.repository

import com.example.data.local.QuoteDao
import com.example.data.model.SavedQuote
import com.example.data.remote.Content
import com.example.data.remote.GeminiApiService
import com.example.data.remote.GenerateContentRequest
import com.example.data.remote.Part
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class QuoteRepository(
    private val quoteDao: QuoteDao,
    private val geminiApiService: com.example.data.remote.GeminiApiService
) {
    val savedQuotes: Flow<List<SavedQuote>> = quoteDao.getAllQuotes()

    suspend fun saveQuote(quote: SavedQuote) = withContext(Dispatchers.IO) {
        quoteDao.insertQuote(quote)
    }

    suspend fun deleteQuoteById(id: Int) = withContext(Dispatchers.IO) {
        quoteDao.deleteQuoteById(id)
    }

    suspend fun generateAiGreeting(name: String, mood: String, apiKey: String): String = withContext(Dispatchers.IO) {
        if (apiKey.isEmpty()) {
            return@withContext "API Key is missing. Please configure it in the AI Studio Secrets panel."
        }

        val prompt = """
            Generate a personalized greeting and short wisdom quote for $name whose current vibe is '$mood'.
            The response should start with a warm greeting to $name, followed by a beautiful, inspiring, and calming sentence tailored to the '$mood' vibe.
            Keep it under 3 sentences total, deeply poetic, elegant, and warm. Avoid generic cliches.
        """.trimIndent()

        val request = GenerateContentRequest(
            contents = listOf(
                Content(
                    parts = listOf(
                        Part(text = prompt)
                    )
                )
            )
        )

        try {
            val response = geminiApiService.generateContent(apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text?.trim()
                ?: "Could not generate quote. Please try again."
        } catch (e: Exception) {
            e.printStackTrace()
            "Failed to reach Gemini: ${e.localizedMessage ?: "Unknown Error"}"
        }
    }
}
