package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_quotes")
data class SavedQuote(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val mood: String,
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)
