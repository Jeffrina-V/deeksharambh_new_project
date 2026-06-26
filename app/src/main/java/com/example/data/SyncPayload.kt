package com.example.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SyncPayload(
    val users: List<User>,
    val batches: List<Batch>,
    val students: List<Student>,
    val attendance: List<Attendance>,
    val questions: List<Question>,
    val results: List<Result>,
    val syllabi: List<Syllabus>,
    val scheduleSlots: List<ScheduleSlot>,
    val abbreviations: List<Abbreviation>,
    val photos: List<Photo>,
    val sipReports: List<SipReport>
)

@JsonClass(generateAdapter = true)
data class SyncSummaryItem(
    val synced: Int
)

@JsonClass(generateAdapter = true)
data class SyncResponse(
    val success: Boolean,
    val message: String,
    val summary: Map<String, SyncSummaryItem>?
)
