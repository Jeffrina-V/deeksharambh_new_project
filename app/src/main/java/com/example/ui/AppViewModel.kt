package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    val repository = Repository(db.appDao())

    // UI state for authentication
    var currentUser = MutableStateFlow<User?>(null)
    val loginError = MutableStateFlow<String?>(null)

    // Current selected batch
    val selectedBatchId = MutableStateFlow<Int?>(null)

    // Flow for all batches
    val allBatches: StateFlow<List<Batch>> = repository.allBatches
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // UI state for active batch
    val selectedBatch: StateFlow<Batch?> = selectedBatchId
        .flatMapLatest { id ->
            if (id == null) flowOf<Batch?>(null)
            else flow { emit(repository.getBatchById(id)) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Flow for students in the selected batch
    val students: StateFlow<List<Student>> = selectedBatchId
        .flatMapLatest { id ->
            if (id == null) flowOf(emptyList())
            else repository.getStudentsForBatch(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Flow for syllabi in the selected batch
    val syllabi: StateFlow<List<Syllabus>> = selectedBatchId
        .flatMapLatest { id ->
            if (id == null) flowOf(emptyList())
            else repository.getSyllabiForBatch(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Flow for schedule in selected batch
    val schedule: StateFlow<List<ScheduleSlot>> = selectedBatchId
        .flatMapLatest { id ->
            if (id == null) flowOf(emptyList())
            else repository.getScheduleForBatch(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Flow for abbreviations in selected batch
    val abbreviations: StateFlow<List<Abbreviation>> = selectedBatchId
        .flatMapLatest { id ->
            if (id == null) flowOf(emptyList())
            else repository.getAbbreviationsForBatch(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Flow for attendance in selected batch
    val attendance: StateFlow<List<Attendance>> = selectedBatchId
        .flatMapLatest { id ->
            if (id == null) flowOf(emptyList())
            else repository.getAttendanceForBatch(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Flow for assessment questions
    val questions: StateFlow<List<Question>> = selectedBatchId
        .flatMapLatest { id ->
            if (id == null) flowOf(emptyList())
            else repository.getQuestionsForBatch(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Flow for assessment results
    val results: StateFlow<List<Result>> = selectedBatchId
        .flatMapLatest { id ->
            if (id == null) flowOf(emptyList())
            else repository.getResultsForBatch(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Flow for photo gallery
    val photos: StateFlow<List<Photo>> = selectedBatchId
        .flatMapLatest { id ->
            if (id == null) flowOf(emptyList())
            else repository.getPhotosForBatch(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Flow for narrative report
    val sipReport: StateFlow<SipReport?> = selectedBatchId
        .flatMapLatest { id ->
            if (id == null) flowOf(null)
            else repository.getSipReportForBatch(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        // Seed the database with all details from 2024 and 2025 batches on start
        viewModelScope.launch {
            repository.seedDatabaseIfNeeded()
            // Set first batch as active default if available
            val list = repository.allBatches.first()
            if (list.isNotEmpty()) {
                selectedBatchId.value = list.first().id
            }
        }
    }

    // AUTH ACTIONS
    fun login(username: String, passwordHash: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val user = repository.getUserByUsername(username)
            if (user != null && user.passwordHash == passwordHash) {
                currentUser.value = user
                loginError.value = null
                onResult(true)
            } else {
                loginError.value = "Invalid Username or Password!"
                onResult(false)
            }
        }
    }

    fun logout() {
        currentUser.value = null
    }

    // SYNC ACTIONS
    val syncLoading = MutableStateFlow(false)
    val syncSuccess = MutableStateFlow<String?>(null)
    val syncError = MutableStateFlow<String?>(null)

    fun syncToCloud(baseUrl: String) {
        viewModelScope.launch {
            syncLoading.value = true
            syncSuccess.value = null
            syncError.value = null
            try {
                val usersList = repository.getAllUsers()
                val batchesList = repository.allBatches.first()
                
                val allStudents = mutableListOf<Student>()
                val allAttendance = mutableListOf<Attendance>()
                val allQuestions = mutableListOf<Question>()
                val allResults = mutableListOf<Result>()
                val allSyllabi = mutableListOf<Syllabus>()
                val allScheduleSlots = mutableListOf<ScheduleSlot>()
                val allAbbreviations = mutableListOf<Abbreviation>()
                val allPhotos = mutableListOf<Photo>()
                val allSipReports = mutableListOf<SipReport>()

                for (batch in batchesList) {
                    val batchId = batch.id
                    allStudents.addAll(repository.getStudentsForBatch(batchId).first())
                    allAttendance.addAll(repository.getAttendanceForBatch(batchId).first())
                    allQuestions.addAll(repository.getQuestionsForBatch(batchId).first())
                    allResults.addAll(repository.getResultsForBatch(batchId).first())
                    allSyllabi.addAll(repository.getSyllabiForBatch(batchId).first())
                    allScheduleSlots.addAll(repository.getScheduleForBatch(batchId).first())
                    allAbbreviations.addAll(repository.getAbbreviationsForBatch(batchId).first())
                    allPhotos.addAll(repository.getPhotosForBatch(batchId).first())
                    repository.getSipReportForBatch(batchId).first()?.let {
                        allSipReports.add(it)
                    }
                }

                val payload = SyncPayload(
                    users = usersList,
                    batches = batchesList,
                    students = allStudents,
                    attendance = allAttendance,
                    questions = allQuestions,
                    results = allResults,
                    syllabi = allSyllabi,
                    scheduleSlots = allScheduleSlots,
                    abbreviations = allAbbreviations,
                    photos = allPhotos,
                    sipReports = allSipReports
                )

                val service = SyncService.create(baseUrl)
                val response = service.syncData(payload)

                if (response.success) {
                    syncSuccess.value = response.message
                } else {
                    syncError.value = response.message
                }
            } catch (e: Exception) {
                syncError.value = "Failed to sync: ${e.localizedMessage ?: e.message}"
            } finally {
                syncLoading.value = false
            }
        }
    }

    // BATCH SETUP
    fun createBatch(
        batchYearRange: String,
        academicYear: String,
        deeksharambhVersion: String,
        startDate: String,
        endDate: String,
        hodName: String,
        principalName: String,
        className: String,
        tamilMax: Int,
        englishMax: Int,
        mathsMax: Int,
        coreMax: Int,
        totalMax: Int,
        insights: List<String>
    ) {
        viewModelScope.launch {
            val newBatch = Batch(
                batchYearRange = batchYearRange,
                academicYear = academicYear,
                deeksharambhVersion = deeksharambhVersion,
                startDate = startDate,
                endDate = endDate,
                hodName = hodName,
                principalName = principalName,
                className = className,
                insights = insights,
                totalStudents = 0,
                tamilMax = tamilMax,
                englishMax = englishMax,
                mathsMax = mathsMax,
                coreMax = coreMax,
                totalMax = totalMax
            )
            val newId = repository.insertBatch(newBatch).toInt()
            selectedBatchId.value = newId

            // Generate a default empty SIP report for this new batch
            val defaultReport = SipReport(
                batchId = newId,
                academicYear = academicYear,
                startDate = startDate,
                endDate = endDate,
                narrative = "The Student Induction Program for newly admitted first-year students of Department of CSDA, Sankara College of Science and Commerce was successfully conducted.",
                objectives = listOf(
                    "Help students feel at ease in the new academic environment",
                    "Encourage exploration of academic interests",
                    "Strengthen the student-teacher bond"
                )
            )
            repository.insertSipReport(defaultReport)
        }
    }

    // STUDENT ACTIONS
    fun addStudent(name: String, stream: String) {
        val bId = selectedBatchId.value ?: return
        viewModelScope.launch {
            val currentStudents = students.value
            val nextSNo = if (currentStudents.isEmpty()) 1 else currentStudents.maxOf { it.sNo } + 1
            repository.insertStudent(Student(batchId = bId, sNo = nextSNo, name = name, mathsStream = stream))
            updateBatchStudentCount(bId)
        }
    }

    fun updateStudent(student: Student) {
        viewModelScope.launch {
            repository.updateStudent(student)
        }
    }

    fun deleteStudent(student: Student) {
        val bId = selectedBatchId.value ?: return
        viewModelScope.launch {
            repository.deleteStudent(student)
            updateBatchStudentCount(bId)
        }
    }

    private suspend fun updateBatchStudentCount(batchId: Int) {
        val batch = repository.getBatchById(batchId)
        val list = repository.getStudentsForBatch(batchId).first()
        if (batch != null) {
            repository.updateBatch(batch.copy(totalStudents = list.size))
        }
    }

    // SYLLABUS ACTIONS
    fun addSyllabus(
        subjectName: String,
        departmentName: String,
        hours: Int,
        stream: String,
        objectives: List<String>,
        units: List<SyllabusUnit>,
        referenceBooks: List<String>,
        staffIncharge: String,
        expertName: String,
        expertDesignation: String,
        expertInstitution: String,
        hodName: String
    ) {
        val bId = selectedBatchId.value ?: return
        viewModelScope.launch {
            val syllabus = Syllabus(
                batchId = bId,
                subjectName = subjectName,
                departmentName = departmentName,
                hours = hours,
                mathsStream = stream,
                objectives = objectives,
                units = units,
                referenceBooks = referenceBooks,
                staffIncharge = staffIncharge,
                subjectExpertName = expertName,
                subjectExpertDesignation = expertDesignation,
                subjectExpertInstitution = expertInstitution,
                hodName = hodName
            )
            repository.insertSyllabus(syllabus)
        }
    }

    // SCHEDULE ACTIONS
    fun saveSchedule(slots: List<ScheduleSlot>) {
        viewModelScope.launch {
            slots.forEach { repository.insertScheduleSlot(it) }
        }
    }

    fun saveAbbreviations(abbrs: List<Abbreviation>) {
        viewModelScope.launch {
            abbrs.forEach { repository.insertAbbreviation(it) }
        }
    }

    // ATTENDANCE ACTIONS
    fun saveAttendance(studentId: Int, date: String, status: String) {
        val bId = selectedBatchId.value ?: return
        viewModelScope.launch {
            repository.insertAttendance(Attendance(batchId = bId, studentId = studentId, date = date, status = status))
        }
    }

    fun saveBatchAttendance(list: List<Attendance>) {
        viewModelScope.launch {
            repository.insertAttendanceList(list)
        }
    }

    // ASSESSMENT ACTIONS
    fun addQuestion(
        subject: String,
        stream: String,
        text: String,
        opA: String,
        opB: String,
        opC: String,
        opD: String,
        correct: String
    ) {
        val bId = selectedBatchId.value ?: return
        viewModelScope.launch {
            val q = Question(
                batchId = bId,
                subject = subject,
                mathsStream = stream,
                questionText = text,
                optionA = opA,
                optionB = opB,
                optionC = opC,
                optionD = opD,
                correctAnswer = correct
            )
            repository.insertQuestion(q)
        }
    }

    // Submit student responses and dynamically compute/save the results!
    fun submitAssessment(student: Student, answers: Map<Int, String>) {
        val bId = selectedBatchId.value ?: return
        val batch = selectedBatch.value ?: return
        viewModelScope.launch {
            // Calculate scores per subject
            var tamilScore = 0
            var englishScore = 0
            var mathsScore = 0
            var coreScore = 0

            val currentQuestions = questions.value
            answers.forEach { (qId, selectedOpt) ->
                val q = currentQuestions.find { it.id == qId }
                if (q != null && q.correctAnswer == selectedOpt) {
                    when (q.subject.lowercase()) {
                        "tamil" -> tamilScore++
                        "english" -> englishScore++
                        "maths" -> mathsScore++
                        "core" -> coreScore++
                    }
                }
            }

            // Map standard correct answers scaling to configured max marks (simulate realistic weight or use correct count)
            // Let's scale score proportionately: if 1/1 correct -> max score, or simply add as score points.
            // Since our pre-loaded results have marks out of tamilMax, englishMax, etc., let's set the scores.
            // To make it simple and realistic, we assign the raw score capped by max, or standard percentage.
            val tMark = (tamilScore * (batch.tamilMax / 1.0)).coerceAtMost(batch.tamilMax.toDouble()).toInt()
            val eMark = (englishScore * (batch.englishMax / 1.0)).coerceAtMost(batch.englishMax.toDouble()).toInt()
            val mMark = (mathsScore * (batch.mathsMax / 1.0)).coerceAtMost(batch.mathsMax.toDouble()).toInt()
            val cMark = (coreScore * (batch.coreMax / 1.0)).coerceAtMost(batch.coreMax.toDouble()).toInt()

            val total = tMark + eMark + mMark + cMark
            val percentage = (total.toDouble() / batch.totalMax) * 100.0

            val res = Result(
                batchId = bId,
                studentId = student.id,
                tamil = tMark.toString(),
                english = eMark.toString(),
                maths = mMark.toString(),
                core = cMark.toString(),
                total = total,
                percentage = percentage,
                isAbsent = false
            )
            repository.insertResult(res)
        }
    }

    // Manual result entry/edit by Admin or Faculty
    fun saveResult(result: Result) {
        viewModelScope.launch {
            repository.insertResult(result)
        }
    }

    // PHOTOS ACTIONS
    fun addPhoto(caption: String, date: String, gpsText: String) {
        val bId = selectedBatchId.value ?: return
        viewModelScope.launch {
            val photo = Photo(
                batchId = bId,
                localUri = "classroom_placeholder",
                caption = caption,
                photoDate = date,
                gpsOverlayText = gpsText
            )
            repository.insertPhoto(photo)
        }
    }

    // REPORT ACTIONS
    fun updateSipReport(narrative: String, objectives: List<String>) {
        val bId = selectedBatchId.value ?: return
        val currentReport = sipReport.value
        viewModelScope.launch {
            if (currentReport != null) {
                repository.insertSipReport(currentReport.copy(narrative = narrative, objectives = objectives))
            } else {
                val batch = selectedBatch.value
                val newReport = SipReport(
                    batchId = bId,
                    academicYear = batch?.academicYear ?: "",
                    startDate = batch?.startDate ?: "",
                    endDate = batch?.endDate ?: "",
                    narrative = narrative,
                    objectives = objectives
                )
                repository.insertSipReport(newReport)
            }
        }
    }

    // CHATBOT ACTIONS & STATE
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage("Hello! I am your AI Academic Tutor for Deeksharambh and Bridge Courses. Ask me any doubt about Tamil, English, Mathematics, Computer Science, or Data Analytics!", false)
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _chatLoading = MutableStateFlow(false)
    val chatLoading: StateFlow<Boolean> = _chatLoading.asStateFlow()

    fun sendChatMessage(text: String) {
        if (text.isBlank()) return
        val userMsg = ChatMessage(text, true)
        _chatMessages.value = _chatMessages.value + userMsg

        viewModelScope.launch {
            _chatLoading.value = true
            try {
                val sysInstruction = "You are an expert academic tutor for Sankara College of Science and Commerce. Specifically, you help first-year students transitioning from school to college in the Computer Science with Data Analytics (CSDA) department. Assist with doubts in Tamil (Tamil language history, modern computing, grammar), English (communication, resume, public speaking), Mathematics (Matrices, Statistics, BODMAS), and Computer Science/Data Analytics (C, Python, Big Data, Network Topologies, Operating Systems, AI & ML). Keep responses highly academic, detailed, welcoming, and clear."
                
                val service = com.example.data.GeminiService.create()
                val request = com.example.data.GeminiRequest(
                    contents = _chatMessages.value.map { msg ->
                        com.example.data.GeminiContent(
                            parts = listOf(com.example.data.GeminiPart(msg.text)),
                            role = if (msg.isUser) "user" else "model"
                        )
                    },
                    systemInstruction = com.example.data.GeminiContent(
                        parts = listOf(com.example.data.GeminiPart(sysInstruction))
                    )
                )
                
                val apiKey = com.example.BuildConfig.GEMINI_API_KEY
                val response = service.generateContent(apiKey, request)
                val aiReply = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "I'm sorry, I couldn't process that response."
                _chatMessages.value = _chatMessages.value + ChatMessage(aiReply, false)
            } catch (e: Exception) {
                _chatMessages.value = _chatMessages.value + ChatMessage("Error: Could not reach AI service. Please verify your GEMINI_API_KEY in the Secrets panel. Details: ${e.localizedMessage}", false)
            } finally {
                _chatLoading.value = false
            }
        }
    }
}

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)
