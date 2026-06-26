package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val passwordHash: String,
    val role: String // Admin, Faculty, Viewer
)

@Entity(tableName = "batches")
data class Batch(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val batchYearRange: String, // e.g., 2024-2027
    val academicYear: String, // e.g., 2024-2025
    val deeksharambhVersion: String, // e.g., 5.0
    val startDate: String,
    val endDate: String,
    val hodName: String,
    val principalName: String,
    val className: String,
    val insights: List<String>, // list of insights for cover/brochure page
    val totalStudents: Int,
    val tamilMax: Int,
    val englishMax: Int,
    val mathsMax: Int,
    val coreMax: Int,
    val totalMax: Int,
    val bestWishesFrom: String = "Shri.T.P.Ramachandran & Dr.V.Radhika"
)

@Entity(tableName = "students")
data class Student(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val batchId: Int,
    val sNo: Int,
    val name: String,
    val mathsStream: String // M (Maths) or NM (Non-Maths)
)

data class SyllabusUnit(
    val unitNumber: String,
    val description: String
)

@Entity(tableName = "syllabi")
data class Syllabus(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val batchId: Int,
    val subjectName: String,
    val departmentName: String,
    val hours: Int,
    val mathsStream: String, // M, NM, or All (for non-maths subjects)
    val objectives: List<String>,
    val units: List<SyllabusUnit>,
    val referenceBooks: List<String>,
    val staffIncharge: String,
    val subjectExpertName: String,
    val subjectExpertDesignation: String,
    val subjectExpertInstitution: String,
    val hodName: String
)

@Entity(tableName = "schedule_slots")
data class ScheduleSlot(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val batchId: Int,
    val dayOrder: String, // e.g., I, II, III
    val date: String, // e.g., 02.07.2024
    val period1: String, // subject or activity abbreviation
    val period2: String,
    val period3: String,
    val period4: String,
    val period5: String,
    val period6: String
)

@Entity(tableName = "abbreviations")
data class Abbreviation(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val batchId: Int,
    val sNo: Int,
    val abbreviation: String,
    val particulars: String,
    val facultyName: String,
    val noOfHours: Int
)

@Entity(tableName = "attendance")
data class Attendance(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val batchId: Int,
    val studentId: Int,
    val date: String,
    val status: String // P or A
)

@Entity(tableName = "questions")
data class Question(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val batchId: Int,
    val subject: String, // Tamil, English, Maths, Core
    val mathsStream: String, // M, NM, or All
    val questionText: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctAnswer: String // A, B, C, D
)

@Entity(tableName = "results")
data class Result(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val batchId: Int,
    val studentId: Int,
    val tamil: String, // mark as string (e.g. "9" or "AB")
    val english: String,
    val maths: String,
    val core: String,
    val total: Int,
    val percentage: Double,
    val isAbsent: Boolean
)

@Entity(tableName = "photos")
data class Photo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val batchId: Int,
    val localUri: String, // local placeholder or mock image resource ID/name
    val caption: String,
    val photoDate: String,
    val gpsOverlayText: String
)

@Entity(tableName = "sip_reports")
data class SipReport(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val batchId: Int,
    val academicYear: String,
    val startDate: String,
    val endDate: String,
    val narrative: String,
    val objectives: List<String>
)
