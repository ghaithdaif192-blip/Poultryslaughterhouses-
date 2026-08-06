package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "poultry_batches")
data class PoultryBatch(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val supplierName: String,
    val truckNumber: String,
    val driverName: String,
    val cagesCount: Int,
    val birdsCount: Int,
    val weightKg: Double,
    val arrivalTime: Long = System.currentTimeMillis(),
    val inspectionResult: String, // "Passed" or "Rejected"
    val currentStage: String = "Reception", // "Reception", "Slaughter", "Cleaning", "Chilling", "Freezing", "Packing", "Inventory", "Shipping"
    val qrCodeData: String = "BATCH-${System.currentTimeMillis()}",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "qc_entries")
data class QCEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val batchId: Int,
    val temperature: Double,
    val waterPh: Double,
    val rejectionCount: Int,
    val rejectionReason: String,
    val inspectorName: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "production_lines")
data class ProductionLine(
    @PrimaryKey val id: Int, // 1, 2, 3
    val lineName: String,
    val isActive: Boolean,
    val speedBirdsPerMin: Int,
    val operatorName: String,
    val downtimeMinutes: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "cold_storage_locations")
data class ColdStorageLocation(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val roomNumber: String, // e.g. "Freezer Room A", "Cold Room B"
    val roomType: String, // "Chilling Room", "Blast Freezer", "Frozen Storage"
    val temperature: Double, // e.g. -18.5, 2.4
    val capacityCartons: Int,
    val currentCartons: Int,
    val timestamp: Long = System.currentTimeMillis()
)
