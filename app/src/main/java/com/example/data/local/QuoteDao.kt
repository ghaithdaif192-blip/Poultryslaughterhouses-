package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ColdStorageLocation
import com.example.data.model.PoultryBatch
import com.example.data.model.ProductionLine
import com.example.data.model.QCEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {
    // Poultry Batches
    @Query("SELECT * FROM poultry_batches ORDER BY timestamp DESC")
    fun getAllBatches(): Flow<List<PoultryBatch>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBatch(batch: PoultryBatch): Long

    @Update
    suspend fun updateBatch(batch: PoultryBatch)

    @Query("DELETE FROM poultry_batches WHERE id = :id")
    suspend fun deleteBatchById(id: Int)

    // QC Entries
    @Query("SELECT * FROM qc_entries ORDER BY timestamp DESC")
    fun getAllQCEntries(): Flow<List<QCEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQCEntry(entry: QCEntry): Long

    // Production Lines
    @Query("SELECT * FROM production_lines ORDER BY id ASC")
    fun getAllProductionLines(): Flow<List<ProductionLine>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductionLine(line: ProductionLine)

    // Cold Storage Locations
    @Query("SELECT * FROM cold_storage_locations ORDER BY roomNumber ASC")
    fun getAllColdStorageLocations(): Flow<List<ColdStorageLocation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertColdStorage(location: ColdStorageLocation)

    @Update
    suspend fun updateColdStorage(location: ColdStorageLocation)
}
