package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.BuildConfig
import com.example.data.local.AppDatabase
import com.example.data.model.SavedQuote
import com.example.data.remote.RetrofitClient
import com.example.data.repository.QuoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface ApiStatus {
    object Idle : ApiStatus
    object Loading : ApiStatus
    data class Success(val quote: String) : ApiStatus
    data class Error(val message: String) : ApiStatus
}

class GreetingViewModel(
    application: Application,
    private val repository: QuoteRepository
) : AndroidViewModel(application) {

    private val _nameInput = MutableStateFlow("")
    val nameInput: StateFlow<String> = _nameInput.asStateFlow()

    private val _selectedMood = MutableStateFlow("Peaceful")
    val selectedMood: StateFlow<String> = _selectedMood.asStateFlow()

    private val _apiStatus = MutableStateFlow<ApiStatus>(ApiStatus.Idle)
    val apiStatus: StateFlow<ApiStatus> = _apiStatus.asStateFlow()

    private val _generatedQuote = MutableStateFlow("")
    val generatedQuote: StateFlow<String> = _generatedQuote.asStateFlow()

    val savedQuotes: StateFlow<List<SavedQuote>> = repository.savedQuotes
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onNameChange(newName: String) {
        _nameInput.value = newName
    }

    fun onMoodChange(newMood: String) {
        _selectedMood.value = newMood
    }

    fun generateGreeting() {
        val name = _nameInput.value.trim()
        val displayName = if (name.isEmpty()) "Seeker" else name
        val mood = _selectedMood.value

        viewModelScope.launch {
            _apiStatus.value = ApiStatus.Loading
            
            // Fetch API key from BuildConfig. If it's empty, use fallback.
            val apiKey = BuildConfig.GEMINI_API_KEY
            
            if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
                // If no real API key is supplied, use a beautiful local fallback after a tiny delay
                kotlinx.coroutines.delay(1000)
                val fallback = getLocalFallbackQuote(displayName, mood)
                _generatedQuote.value = fallback
                _apiStatus.value = ApiStatus.Success(fallback)
                return@launch
            }

            val result = repository.generateAiGreeting(displayName, mood, apiKey)
            if (result.startsWith("Failed") || result.startsWith("Error")) {
                // Graceful fallback on API failure
                val fallback = getLocalFallbackQuote(displayName, mood) + " (Offline Mode)"
                _generatedQuote.value = fallback
                _apiStatus.value = ApiStatus.Success(fallback)
            } else {
                _generatedQuote.value = result
                _apiStatus.value = ApiStatus.Success(result)
            }
        }
    }

    fun saveCurrentQuote() {
        val text = _generatedQuote.value
        if (text.isEmpty()) return

        val name = _nameInput.value.trim()
        val displayName = if (name.isEmpty()) "Seeker" else name
        val mood = _selectedMood.value

        viewModelScope.launch {
            repository.saveQuote(
                SavedQuote(
                    name = displayName,
                    mood = mood,
                    text = text
                )
            )
        }
    }

    fun deleteQuote(id: Int) {
        viewModelScope.launch {
            repository.deleteQuoteById(id)
        }
    }

    private fun getLocalFallbackQuote(name: String, mood: String): String {
        val wisdom = when (mood) {
            "Peaceful" -> "May your mind find quiet in a busy world. Calm is not the absence of the storm, but the peace inside of you."
            "Creative" -> "The universe is full of infinite possibilities. Let your imagination break standard rules and manifest your dreams."
            "Reflective" -> "Look within, for there lies a universe of quiet truths and timeless wisdom waiting to be uncovered."
            "Energetic" -> "Unleash the vibrant energy inside your soul. Today is a brilliant canvas for your passion and purpose!"
            else -> "Every new sunrise is a gift of hope and endless new possibilities."
        }
        return "Hello $name. $wisdom"
    }

    // Factory to construct the ViewModel with dependencies
    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val database = AppDatabase.getDatabase(application)
            val repository = QuoteRepository(database.quoteDao(), RetrofitClient.service)
            return GreetingViewModel(application, repository) as T
        }
    }
}
