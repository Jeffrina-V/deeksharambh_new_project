package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // Users
    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    // Batches
    @Query("SELECT * FROM batches ORDER BY id DESC")
    fun getAllBatches(): Flow<List<Batch>>

    @Query("SELECT * FROM batches WHERE id = :batchId")
    suspend fun getBatchById(batchId: Int): Batch?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBatch(batch: Batch): Long

    @Update
    suspend fun updateBatch(batch: Batch)

    @Delete
    suspend fun deleteBatch(batch: Batch)

    // Students
    @Query("SELECT * FROM students WHERE batchId = :batchId ORDER BY sNo ASC")
    fun getStudentsForBatch(batchId: Int): Flow<List<Student>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: Student): Long

    @Update
    suspend fun updateStudent(student: Student)

    @Delete
    suspend fun deleteStudent(student: Student)

    @Query("DELETE FROM students WHERE batchId = :batchId")
    suspend fun deleteStudentsForBatch(batchId: Int)

    // Syllabi
    @Query("SELECT * FROM syllabi WHERE batchId = :batchId")
    fun getSyllabiForBatch(batchId: Int): Flow<List<Syllabus>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSyllabus(syllabus: Syllabus): Long

    @Query("DELETE FROM syllabi WHERE batchId = :batchId")
    suspend fun deleteSyllabiForBatch(batchId: Int)

    // Schedule Slots
    @Query("SELECT * FROM schedule_slots WHERE batchId = :batchId ORDER BY id ASC")
    fun getScheduleForBatch(batchId: Int): Flow<List<ScheduleSlot>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScheduleSlot(slot: ScheduleSlot)

    @Query("DELETE FROM schedule_slots WHERE batchId = :batchId")
    suspend fun deleteScheduleForBatch(batchId: Int)

    // Abbreviations
    @Query("SELECT * FROM abbreviations WHERE batchId = :batchId ORDER BY sNo ASC")
    fun getAbbreviationsForBatch(batchId: Int): Flow<List<Abbreviation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAbbreviation(abbr: Abbreviation)

    @Query("DELETE FROM abbreviations WHERE batchId = :batchId")
    suspend fun deleteAbbreviationsForBatch(batchId: Int)

    // Attendance
    @Query("SELECT * FROM attendance WHERE batchId = :batchId")
    fun getAttendanceForBatch(batchId: Int): Flow<List<Attendance>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendance(attendance: Attendance)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendanceList(list: List<Attendance>)

    @Query("DELETE FROM attendance WHERE batchId = :batchId")
    suspend fun deleteAttendanceForBatch(batchId: Int)

    // Questions
    @Query("SELECT * FROM questions WHERE batchId = :batchId")
    fun getQuestionsForBatch(batchId: Int): Flow<List<Question>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: Question)

    @Query("DELETE FROM questions WHERE batchId = :batchId")
    suspend fun deleteQuestionsForBatch(batchId: Int)

    // Results
    @Query("SELECT * FROM results WHERE batchId = :batchId")
    fun getResultsForBatch(batchId: Int): Flow<List<Result>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: Result)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResultsList(list: List<Result>)

    @Query("DELETE FROM results WHERE batchId = :batchId")
    suspend fun deleteResultsForBatch(batchId: Int)

    // Photos
    @Query("SELECT * FROM photos WHERE batchId = :batchId ORDER BY id DESC")
    fun getPhotosForBatch(batchId: Int): Flow<List<Photo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photo: Photo)

    @Query("DELETE FROM photos WHERE batchId = :batchId")
    suspend fun deletePhotosForBatch(batchId: Int)

    // Sip Reports
    @Query("SELECT * FROM sip_reports WHERE batchId = :batchId LIMIT 1")
    fun getSipReportForBatch(batchId: Int): Flow<SipReport?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSipReport(report: SipReport)
}
