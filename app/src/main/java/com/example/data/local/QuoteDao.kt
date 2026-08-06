package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.SavedQuote
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {
    @Query("SELECT * FROM saved_quotes ORDER BY timestamp DESC")
    fun getAllQuotes(): Flow<List<SavedQuote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(quote: SavedQuote)

    @Delete
    suspend fun deleteQuote(quote: SavedQuote)

    @Query("DELETE FROM saved_quotes WHERE id = :id")
    suspend fun deleteQuoteById(id: Int)
}
