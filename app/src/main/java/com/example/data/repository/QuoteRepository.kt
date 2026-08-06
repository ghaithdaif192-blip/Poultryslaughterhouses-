package com.example.data.repository

import com.example.data.local.QuoteDao
import com.example.data.model.ColdStorageLocation
import com.example.data.model.PoultryBatch
import com.example.data.model.ProductionLine
import com.example.data.model.QCEntry
import com.example.data.remote.Content
import com.example.data.remote.GeminiApiService
import com.example.data.remote.GenerateContentRequest
import com.example.data.remote.Part
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class QuoteRepository(
    private val quoteDao: QuoteDao,
    private val geminiApiService: GeminiApiService
) {
    val allBatches: Flow<List<PoultryBatch>> = quoteDao.getAllBatches()
    val allQCEntries: Flow<List<QCEntry>> = quoteDao.getAllQCEntries()
    val allProductionLines: Flow<List<ProductionLine>> = quoteDao.getAllProductionLines()
    val allColdStorageLocations: Flow<List<ColdStorageLocation>> = quoteDao.getAllColdStorageLocations()

    suspend fun insertBatch(batch: PoultryBatch): Long = withContext(Dispatchers.IO) {
        quoteDao.insertBatch(batch)
    }

    suspend fun updateBatch(batch: PoultryBatch) = withContext(Dispatchers.IO) {
        quoteDao.updateBatch(batch)
    }

    suspend fun deleteBatchById(id: Int) = withContext(Dispatchers.IO) {
        quoteDao.deleteBatchById(id)
    }

    suspend fun insertQCEntry(entry: QCEntry): Long = withContext(Dispatchers.IO) {
        quoteDao.insertQCEntry(entry)
    }

    suspend fun insertProductionLine(line: ProductionLine) = withContext(Dispatchers.IO) {
        quoteDao.insertProductionLine(line)
    }

    suspend fun insertColdStorage(location: ColdStorageLocation) = withContext(Dispatchers.IO) {
        quoteDao.insertColdStorage(location)
    }

    suspend fun updateColdStorage(location: ColdStorageLocation) = withContext(Dispatchers.IO) {
        quoteDao.updateColdStorage(location)
    }

    // AI Analytical Report Generator powered by Gemini
    suspend fun generateAiReportSummary(
        batches: List<PoultryBatch>,
        qcEntries: List<QCEntry>,
        apiKey: String,
        isArabic: Boolean
    ): String = withContext(Dispatchers.IO) {
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext if (isArabic) {
                "مفتاح API غير متوفر. يرجى تهيئته في لوحة Secrets في AI Studio لتفعيل التحليل بالذكاء الاصطناعي."
            } else {
                "API Key is missing. Please configure it in the AI Studio Secrets panel to enable AI Analytics."
            }
        }

        // Prepare a concise snapshot of the factory state for the AI
        val totalBirds = batches.sumOf { it.birdsCount }
        val totalWeight = batches.sumOf { it.weightKg }
        val rejectedBatchesCount = batches.count { it.inspectionResult == "Rejected" }
        val averageTemp = if (qcEntries.isNotEmpty()) qcEntries.map { it.temperature }.average() else 0.0
        val totalQcRejections = qcEntries.sumOf { it.rejectionCount }

        val prompt = if (isArabic) {
            """
                أنت خبير ذكاء اصطناعي ومستشار صناعي متخصص في تشغيل مسالخ ومصانع تعبئة وتبريد وتجميد الدواجن (Poultry Processing).
                بناءً على تقرير المصنع الحالي، قدم تحليلاً تشغيلياً دقيقاً وموجزاً جداً (أقل من 4 جمل) مع توصية ذكية لتحسين الكفاءة:
                - إجمالي الطيور المستلمة: $totalBirds طير
                - إجمالي الوزن: $totalWeight كجم
                - شحنات تم رفضها عند الاستلام: $rejectedBatchesCount شحنة
                - متوسط درجة حرارة فحص الجودة المبرد: ${String.format("%.1f", averageTemp)}°م
                - إجمالي الدجاج المرفوض أثناء الفحص الفني والبيطري: $totalQcRejections طير

                اجعل أسلوبك إمبراطورياً، دقيقاً، تشغيلياً، وخالياً من الحشو المبتذل. ركز على كفاءة خطوط التشغيل وسلامة الغذاء.
            """.trimIndent()
        } else {
            """
                You are an elite Industrial AI Consultant specializing in Poultry Processing Plant Operations (Slaughter, Chilling, Freezing, and Packing ERP systems).
                Based on the following factory snapshot, provide a sharp, executive-level diagnostic summary and a single actionable efficiency recommendation (under 4 sentences):
                - Total Birds Received: $totalBirds
                - Total Weight: ${String.format("%.1f", totalWeight)} kg
                - Batches Rejected at Reception: $rejectedBatchesCount
                - Average Quality Control Temperature: ${String.format("%.1f", averageTemp)}°C
                - Total QC Condemnations/Rejections: $totalQcRejections

                Keep the tone highly professional, precise, and operational. Focus on cold chain integrity, throughput, and food safety compliance.
            """.trimIndent()
        }

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
                ?: (if (isArabic) "فشل إنشاء التقرير. حاول مرة أخرى." else "Could not generate report. Please try again.")
        } catch (e: Exception) {
            e.printStackTrace()
            "AI Engine Error: ${e.localizedMessage ?: "Connection Timeout"}"
        }
    }
}
