package com.example.data

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class Repository(private val appDao: AppDao) {

    // Users
    suspend fun getAllUsers(): List<User> = appDao.getAllUsers()
    suspend fun getUserByUsername(username: String): User? = appDao.getUserByUsername(username)
    suspend fun insertUser(user: User) = appDao.insertUser(user)

    // Batches
    val allBatches: Flow<List<Batch>> = appDao.getAllBatches()
    suspend fun getBatchById(id: Int): Batch? = appDao.getBatchById(id)
    suspend fun insertBatch(batch: Batch): Long = appDao.insertBatch(batch)
    suspend fun updateBatch(batch: Batch) = appDao.updateBatch(batch)
    suspend fun deleteBatch(batch: Batch) = appDao.deleteBatch(batch)

    // Students
    fun getStudentsForBatch(batchId: Int): Flow<List<Student>> = appDao.getStudentsForBatch(batchId)
    suspend fun insertStudent(student: Student): Long = appDao.insertStudent(student)
    suspend fun updateStudent(student: Student) = appDao.updateStudent(student)
    suspend fun deleteStudent(student: Student) = appDao.deleteStudent(student)

    // Syllabi
    fun getSyllabiForBatch(batchId: Int): Flow<List<Syllabus>> = appDao.getSyllabiForBatch(batchId)
    suspend fun insertSyllabus(syllabus: Syllabus) = appDao.insertSyllabus(syllabus)

    // Schedule
    fun getScheduleForBatch(batchId: Int): Flow<List<ScheduleSlot>> = appDao.getScheduleForBatch(batchId)
    suspend fun insertScheduleSlot(slot: ScheduleSlot) = appDao.insertScheduleSlot(slot)

    // Abbreviations
    fun getAbbreviationsForBatch(batchId: Int): Flow<List<Abbreviation>> = appDao.getAbbreviationsForBatch(batchId)
    suspend fun insertAbbreviation(abbr: Abbreviation) = appDao.insertAbbreviation(abbr)

    // Attendance
    fun getAttendanceForBatch(batchId: Int): Flow<List<Attendance>> = appDao.getAttendanceForBatch(batchId)
    suspend fun insertAttendance(attendance: Attendance) = appDao.insertAttendance(attendance)
    suspend fun insertAttendanceList(list: List<Attendance>) = appDao.insertAttendanceList(list)

    // Questions
    fun getQuestionsForBatch(batchId: Int): Flow<List<Question>> = appDao.getQuestionsForBatch(batchId)
    suspend fun insertQuestion(question: Question) = appDao.insertQuestion(question)

    // Results
    fun getResultsForBatch(batchId: Int): Flow<List<Result>> = appDao.getResultsForBatch(batchId)
    suspend fun insertResult(result: Result) = appDao.insertResult(result)

    // Photos
    fun getPhotosForBatch(batchId: Int): Flow<List<Photo>> = appDao.getPhotosForBatch(batchId)
    suspend fun insertPhoto(photo: Photo) = appDao.insertPhoto(photo)

    // SIP Reports
    fun getSipReportForBatch(batchId: Int): Flow<SipReport?> = appDao.getSipReportForBatch(batchId)
    suspend fun insertSipReport(report: SipReport) = appDao.insertSipReport(report)

    // Seeding Method
    suspend fun seedDatabaseIfNeeded() {
        val batches = allBatches.first()
        if (batches.isNotEmpty()) return // Already seeded

        // 1. Seed Users
        appDao.insertUser(User(username = "admin", passwordHash = "admin123", role = "Admin"))
        appDao.insertUser(User(username = "faculty", passwordHash = "faculty123", role = "Faculty"))
        appDao.insertUser(User(username = "viewer", passwordHash = "viewer123", role = "Viewer"))

        // 2. Seed Batch 1 (2024-2027) - Deeksharambh 5.0
        val batch1 = Batch(
            batchYearRange = "2024-2027",
            academicYear = "2024-2025",
            deeksharambhVersion = "5.0",
            startDate = "02.07.2024",
            endDate = "09.07.2024",
            hodName = "Dr. M. Lingaraj",
            principalName = "Dr. V. Radhika",
            className = "I B.Sc. CSDA",
            insights = listOf(
                "Motivational Talks",
                "Gender Sensitivity Programmes",
                "Placement & Life Skill Orientation",
                "Clubs & Committees Orientation",
                "Physical Education",
                "Fun Events",
                "SWAYAM-NPTEL-MOOCS Orientation",
                "Annual Plan",
                "Short & Long-Term Goal Setting",
                "Dissemination of POs & COs"
            ),
            totalStudents = 47,
            tamilMax = 10,
            englishMax = 10,
            mathsMax = 10,
            coreMax = 45,
            totalMax = 75,
            bestWishesFrom = "Shri.T.P.Ramachandran & Dr.V.Radhika"
        )
        val b1Id = appDao.insertBatch(batch1).toInt()

        // Seed 47 students for Batch 1
        val b1StudentsList = listOf(
            "SUKESH.A R" to "NM",
            "SIVA.S" to "M",
            "ABISHEK.S" to "NM",
            "ASWINI.S" to "NM",
            "PRIYANKA.S" to "M",
            "NISHAANTH.D" to "M",
            "DHANABAL.L" to "NM",
            "JEFFRINA.V" to "M",
            "DINESH KARTHIK.S" to "NM",
            "GOKUL.K" to "M",
            "SACHIN .S" to "NM",
            "PAVITHRA.M" to "M",
            "LAVANYA KUMAR.S" to "M",
            "HARIHARAN.S" to "M",
            "RITHIKA.R" to "M",
            "SRIVARTHINI.V" to "M",
            "VENGATESHWARAN.N" to "M",
            "MURUGAPRIYA.I" to "M",
            "ANGELIN GIFTY.I" to "M",
            "SATHIYA.V" to "M",
            "JINOJ.M" to "M",
            "MIRUDHULASINI.R" to "M",
            "DHANYA.D" to "NM",
            "ARTHI.M" to "M",
            "ABINESH.M" to "NM",
            "DEVI PRIYA.M" to "M",
            "HEMALATHA.M" to "M",
            "ROHITH.P" to "M",
            "THENMOZHI.M" to "M",
            "MAKIZH.N" to "M",
            "NIRANJAN.A" to "M",
            "PRAVEEN. N H" to "NM",
            "DHARUNESH.S" to "NM",
            "KANGAR BHARATHESWARAN.M" to "NM",
            "PRABU.S" to "M",
            "DHANUSH.S" to "M",
            "STEPHEN THAYANITHI.J" to "M",
            "KASTHURI KAVIYA.S" to "NM",
            "KAMALESH.P" to "M",
            "TAMILARASAN.S" to "M",
            "VISHAL.B" to "M",
            "KOWSALYA.M" to "NM",
            "GOKULAKRISHNAN.K" to "NM",
            "DHARANI.S" to "M",
            "PRAVEEN RAJA.G" to "NM",
            "JAYASURYA.R" to "M",
            "HEMAMALINI.R" to "M"
        )

        val b1StudentIds = mutableMapOf<String, Int>()
        b1StudentsList.forEachIndexed { idx, pair ->
            val studId = appDao.insertStudent(
                Student(batchId = b1Id, sNo = idx + 1, name = pair.first, mathsStream = pair.second)
            ).toInt()
            b1StudentIds[pair.first] = studId
        }

        // Seed Results for Batch 1 (45 rows of marks)
        val b1Results = listOf(
            Result(batchId = b1Id, studentId = b1StudentIds["ABINESH.M"] ?: 0, tamil = "7", english = "3", maths = "9", core = "23", total = 42, percentage = 56.0, isAbsent = false),
            Result(batchId = b1Id, studentId = b1StudentIds["ANGELIN GIFTY.I"] ?: 0, tamil = "8", english = "6", maths = "9", core = "31", total = 54, percentage = 72.0, isAbsent = false),
            Result(batchId = b1Id, studentId = b1StudentIds["ARTHI.M"] ?: 0, tamil = "9", english = "3", maths = "4", core = "12", total = 28, percentage = 37.3, isAbsent = false),
            Result(batchId = b1Id, studentId = b1StudentIds["ASWINI.S"] ?: 0, tamil = "AB", english = "4", maths = "6", core = "18", total = 28, percentage = 43.1, isAbsent = false),
            Result(batchId = b1Id, studentId = b1StudentIds["DEVI PRIYA.M"] ?: 0, tamil = "6", english = "5", maths = "7", core = "13", total = 31, percentage = 41.3, isAbsent = false),
            Result(batchId = b1Id, studentId = b1StudentIds["DHANABAL.L"] ?: 0, tamil = "6", english = "4", maths = "5", core = "9", total = 24, percentage = 32.0, isAbsent = false),
            Result(batchId = b1Id, studentId = b1StudentIds["DHANUSH.S"] ?: 0, tamil = "AB", english = "4", maths = "7", core = "18", total = 29, percentage = 44.6, isAbsent = false),
            Result(batchId = b1Id, studentId = b1StudentIds["DHANYA.D"] ?: 0, tamil = "9", english = "5", maths = "6", core = "29", total = 49, percentage = 65.3, isAbsent = false),
            Result(batchId = b1Id, studentId = b1StudentIds["DHARANI.S"] ?: 0, tamil = "7", english = "5", maths = "10", core = "27", total = 49, percentage = 65.3, isAbsent = false),
            Result(batchId = b1Id, studentId = b1StudentIds["DINESH KARTHIK.S"] ?: 0, tamil = "9", english = "4", maths = "7", core = "22", total = 42, percentage = 56.0, isAbsent = false),
            Result(batchId = b1Id, studentId = b1StudentIds["GOKUL.K"] ?: 0, tamil = "10", english = "5", maths = "9", core = "27", total = 51, percentage = 68.0, isAbsent = false),
            Result(batchId = b1Id, studentId = b1StudentIds["GOKULAKRISHNAN.K"] ?: 0, tamil = "9", english = "5", maths = "6", core = "12", total = 30, percentage = 40.0, isAbsent = false),
            Result(batchId = b1Id, studentId = b1StudentIds["HARIHARAN.S"] ?: 0, tamil = "6", english = "6", maths = "8", core = "24", total = 47, percentage = 62.7, isAbsent = false),
            Result(batchId = b1Id, studentId = b1StudentIds["HEMALATHA.M"] ?: 0, tamil = "AB", english = "AB", maths = "3", core = "13", total = 16, percentage = 29.1, isAbsent = false),
            Result(batchId = b1Id, studentId = b1StudentIds["HEMAMALINI.R"] ?: 0, tamil = "6", english = "3", maths = "6", core = "28", total = 43, percentage = 57.3, isAbsent = false),
            Result(batchId = b1Id, studentId = b1StudentIds["JAYASURYA.R"] ?: 0, tamil = "5", english = "4", maths = "7", core = "27", total = 43, percentage = 57.3, isAbsent = false),
            Result(batchId = b1Id, studentId = b1StudentIds["JEFFRINA.V"] ?: 0, tamil = "9", english = "5", maths = "9", core = "35", total = 58, percentage = 77.3, isAbsent = false),
            Result(batchId = b1Id, studentId = b1StudentIds["JINOJ.M"] ?: 0, tamil = "7", english = "5", maths = "5", core = "27", total = 44, percentage = 58.7, isAbsent = false),
            Result(batchId = b1Id, studentId = b1StudentIds["KAMALESH.P"] ?: 0, tamil = "7", english = "3", maths = "4", core = "22", total = 36, percentage = 48.0, isAbsent = false),
            Result(batchId = b1Id, studentId = b1StudentIds["KANGAR BHARATHESWARAN.M"] ?: 0, tamil = "6", english = "5", maths = "10", core = "19", total = 40, percentage = 53.3, isAbsent = false),
            Result(batchId = b1Id, studentId = b1StudentIds["KASTHURI KAVIYA.S"] ?: 0, tamil = "8", english = "6", maths = "8", core = "27", total = 49, percentage = 65.3, isAbsent = false),
            Result(batchId = b1Id, studentId = b1StudentIds["KOWSALYA.M"] ?: 0, tamil = "7", english = "2", maths = "5", core = "14", total = 28, percentage = 37.3, isAbsent = false),
            Result(batchId = b1Id, studentId = b1StudentIds["LAVANYA KUMAR.S"] ?: 0, tamil = "8", english = "3", maths = "9", core = "26", total = 46, percentage = 61.3, isAbsent = false),
            Result(batchId = b1Id, studentId = b1StudentIds["MAKIZH.N"] ?: 0, tamil = "9", english = "6", maths = "10", core = "33", total = 58, percentage = 77.3, isAbsent = false),
            Result(batchId = b1Id, studentId = b1StudentIds["MIRUDHULASINI.R"] ?: 0, tamil = "8", english = "6", maths = "10", core = "36", total = 60, percentage = 80.0, isAbsent = false),
            Result(batchId = b1Id, studentId = b1StudentIds["MURUGAPRIYA.I"] ?: 0, tamil = "8", english = "6", maths = "8", core = "30", total = 52, percentage = 69.3, isAbsent = false),
            Result(batchId = b1Id, studentId = b1StudentIds["NIRANJAN.A"] ?: 0, tamil = "9", english = "4", maths = "8", core = "28", total = 49, percentage = 65.3, isAbsent = false),
            Result(batchId = b1Id, studentId = b1StudentIds["NISHAANTH.D"] ?: 0, tamil = "5", english = "6", maths = "5", core = "23", total = 39, percentage = 52.0, isAbsent = false),
            Result(batchId = b1Id, studentId = b1StudentIds["PAVITHRA.M"] ?: 0, tamil = "8", english = "5", maths = "6", core = "24", total = 43, percentage = 57.3, isAbsent = false),
            Result(batchId = b1Id, studentId = b1StudentIds["PRABU.S"] ?: 0, tamil = "AB", english = "AB", maths = "AB", core = "AB", total = 0, percentage = 0.0, isAbsent = true),
            Result(batchId = b1Id, studentId = b1StudentIds["PRAVEEN. N H"] ?: 0, tamil = "2", english = "3", maths = "5", core = "15", total = 25, percentage = 33.3, isAbsent = false),
            Result(batchId = b1Id, studentId = b1StudentIds["PRAVEEN RAJA.G"] ?: 0, tamil = "7", english = "3", maths = "6", core = "27", total = 43, percentage = 57.3, isAbsent = false),
            Result(batchId = b1Id, studentId = b1StudentIds["PRIYANKA.S"] ?: 0, tamil = "7", english = "6", maths = "10", core = "31", total = 54, percentage = 72.0, isAbsent = false),
            Result(batchId = b1Id, studentId = b1StudentIds["RITHIKA.R"] ?: 0, tamil = "10", english = "4", maths = "7", core = "27", total = 48, percentage = 64.0, isAbsent = false),
            Result(batchId = b1Id, studentId = b1StudentIds["ROHITH.P"] ?: 0, tamil = "8", english = "3", maths = "7", core = "18", total = 36, percentage = 48.0, isAbsent = false),
            Result(batchId = b1Id, studentId = b1StudentIds["SACHIN .S"] ?: 0, tamil = "9", english = "6", maths = "8", core = "21", total = 44, percentage = 58.7, isAbsent = false),
            Result(batchId = b1Id, studentId = b1StudentIds["SATHIYA.V"] ?: 0, tamil = "9", english = "5", maths = "4", core = "18", total = 36, percentage = 48.0, isAbsent = false),
            Result(batchId = b1Id, studentId = b1StudentIds["SIVA.S"] ?: 0, tamil = "AB", english = "5", maths = "8", core = "29", total = 42, percentage = 64.6, isAbsent = false),
            Result(batchId = b1Id, studentId = b1StudentIds["SRIVARTHINI.V"] ?: 0, tamil = "10", english = "4", maths = "8", core = "25", total = 47, percentage = 62.7, isAbsent = false),
            Result(batchId = b1Id, studentId = b1StudentIds["STEPHEN THAYANITHI.J"] ?: 0, tamil = "10", english = "4", maths = "10", core = "14", total = 38, percentage = 50.7, isAbsent = false),
            Result(batchId = b1Id, studentId = b1StudentIds["SUKESH.A R"] ?: 0, tamil = "AB", english = "AB", maths = "9", core = "27", total = 36, percentage = 65.5, isAbsent = false),
            Result(batchId = b1Id, studentId = b1StudentIds["TAMILARASAN.S"] ?: 0, tamil = "6", english = "0", maths = "5", core = "14", total = 25, percentage = 33.3, isAbsent = false),
            Result(batchId = b1Id, studentId = b1StudentIds["THENMOZHI.M"] ?: 0, tamil = "8", english = "3", maths = "6", core = "18", total = 35, percentage = 46.7, isAbsent = false),
            Result(batchId = b1Id, studentId = b1StudentIds["VENGATESHWARAN.N"] ?: 0, tamil = "9", english = "4", maths = "6", core = "18", total = 37, percentage = 49.3, isAbsent = false)
        )
        appDao.insertResultsList(b1Results)

        // Seed Syllabi for Batch 1
        val syllabusTamil1 = Syllabus(
            batchId = b1Id,
            subjectName = "Tamil-I",
            departmentName = "Department of Tamil",
            hours = 3,
            mathsStream = "All",
            objectives = listOf("தமிழ் மொழியின் சிறப்புகளை அறிந்து அதன் மீது ஆர்வத்தைத் தூண்டுதல்."),
            units = listOf(
                SyllabusUnit("அலகு I", "தமிழ் மொழியின் பெருமைகள், சிறப்புகள்"),
                SyllabusUnit("அலகு II", "இலக்கியங்கள் அறிமுகம்"),
                SyllabusUnit("அலகு III", "தமிழ் இலக்கிய வகைகள்"),
                SyllabusUnit("அலகு IV", "கணினித் தமிழ்"),
                SyllabusUnit("அலகு V", "ஊடகத்தமிழ்")
            ),
            referenceBooks = listOf(
                "தமிழ் இலக்கிய வரலாறு - மு.வரதராசனார்",
                "ஊடகவியல் - முனைவர் துரை.மணிகண்டன்"
            ),
            staffIncharge = "Dr. M. Lingaraj",
            subjectExpertName = "Dr. S. Poongodi",
            subjectExpertDesignation = "Associate Professor & HOD PG & Research Dept of Tamil",
            subjectExpertInstitution = "Sankara College of Science and Commerce",
            hodName = "Dr. M. Lingaraj"
        )
        appDao.insertSyllabus(syllabusTamil1)

        val syllabusEnglish1 = Syllabus(
            batchId = b1Id,
            subjectName = "English Non-Major",
            departmentName = "Department of English",
            hours = 3,
            mathsStream = "All",
            objectives = listOf(
                "The role of communication in business related professions",
                "To improve analytical skills",
                "To enhance listening, writing and speaking skills"
            ),
            units = listOf(
                SyllabusUnit("Unit I", "Active listening and speaking skills - Pod Cast, Self-introduction and Impromptu speech"),
                SyllabusUnit("Unit II", "Writing Skills - Movie Review, Book Review"),
                SyllabusUnit("Unit III", "Creating Blogs"),
                SyllabusUnit("Unit IV", "Grammar: Parts of Speech"),
                SyllabusUnit("Unit V", "Composition: Letter of Application and Bonafide")
            ),
            referenceBooks = listOf(
                "Parts of Speech: Basics of English Grammar by Kuldeep Yadav",
                "WordPress for Beginners 2022 by Dr. Andy Williams",
                "Develop Self-Confidence, Improve Public Speaking by Dale Carnegie",
                "A Handbook for Letter Writing by S C Gupta"
            ),
            staffIncharge = "Captain E. Justin Ruben",
            subjectExpertName = "Captain E. Justin Ruben",
            subjectExpertDesignation = "Associate NCC Officer & Assistant Professor of English",
            subjectExpertInstitution = "Coimbatore Institute of Technology",
            hodName = "Dr. M. Lingaraj"
        )
        appDao.insertSyllabus(syllabusEnglish1)

        val syllabusMathsStreamM = Syllabus(
            batchId = b1Id,
            subjectName = "Mathematics (Maths)",
            departmentName = "Department of Mathematics",
            hours = 3,
            mathsStream = "M",
            objectives = listOf(
                "To understand the fundamental concepts of mathematical and statistical techniques",
                "To aware basic ideas to apply for mathematical modelling"
            ),
            units = listOf(
                SyllabusUnit("Unit I", "Matrices: Definition - Types of Matrices - Operations: Addition, Subtraction and Multiplication of Matrices"),
                SyllabusUnit("Unit II", "Introduction to Variables, Constants and Functions - Kind of Equations - Solving Quadratic and Cubic Equations"),
                SyllabusUnit("Unit III", "Statistics - Introduction and Meaning - Data Classification - Diagrammatic and Graphical Representation of data"),
                SyllabusUnit("Unit IV", "Measure of Central Tendency - Introduction and Definition"),
                SyllabusUnit("Unit V", "Measure of Dispersion - Introduction and Definition")
            ),
            referenceBooks = listOf("\"Business Mathematics & Statistics\" - P. A. Navanitham"),
            staffIncharge = "Ms. G. Poongothai",
            subjectExpertName = "Dr. E. Prakash M.Sc., Ph.D.",
            subjectExpertDesignation = "Assistant Professor",
            subjectExpertInstitution = "Kongunadu Arts and Science College",
            hodName = "Dr. M. Lingaraj"
        )
        appDao.insertSyllabus(syllabusMathsStreamM)

        val syllabusMathsStreamNM = Syllabus(
            batchId = b1Id,
            subjectName = "Mathematics (Non-Maths)",
            departmentName = "Department of Mathematics",
            hours = 3,
            mathsStream = "NM",
            objectives = listOf(
                "To understand the fundamental concepts of mathematical and statistical techniques",
                "To aware basic ideas to apply for mathematical modelling"
            ),
            units = listOf(
                SyllabusUnit("Unit I", "Introduction to Number System and its Operations - BODMAS Rule - LCM, GCD & HCF"),
                SyllabusUnit("Unit II", "Matrices: Definition - Types of Matrices - Operations: Addition, Subtraction and Multiplication of Matrices"),
                SyllabusUnit("Unit III", "Introduction to Variables, Constants and Functions - Kind of Equations - Solving Quadratic and Cubic Equations"),
                SyllabusUnit("Unit IV", "Statistics - Introduction & Meaning - Types of Data - Diagrammatic and Graphical Representation of data"),
                SyllabusUnit("Unit V", "Measure of Central Tendency - Introduction and Definition")
            ),
            referenceBooks = listOf("\"Business Mathematics & Statistics\" - P. A. Navanitham"),
            staffIncharge = "Mr. S. Bharath",
            subjectExpertName = "Dr. E. Prakash M.Sc., Ph.D.",
            subjectExpertDesignation = "Assistant Professor",
            subjectExpertInstitution = "Kongunadu Arts and Science College",
            hodName = "Dr. M. Lingaraj"
        )
        appDao.insertSyllabus(syllabusMathsStreamNM)

        // Seed Schedule abbreviations for Batch 1
        val b1Abbrs = listOf(
            Abbreviation(batchId = b1Id, sNo = 1, abbreviation = "CT", particulars = "Campus Tour, Institutional Rules and Regulations", facultyName = "Head of the Department", noOfHours = 1),
            Abbreviation(batchId = b1Id, sNo = 2, abbreviation = "FD", particulars = "Familiarization with Department, Career Prospects offered by the Department, workshops, ICT facilities and other facilities", facultyName = "Concerned Class Tutors", noOfHours = 1),
            Abbreviation(batchId = b1Id, sNo = 3, abbreviation = "SSA", particulars = "Student Support Activities / Familiarization of Various Clubs", facultyName = "Bernard Edward - VP", noOfHours = 1),
            Abbreviation(batchId = b1Id, sNo = 4, abbreviation = "PMF", particulars = "Physical & Mental Fitness", facultyName = "Physical Director", noOfHours = 1),
            Abbreviation(batchId = b1Id, sNo = 5, abbreviation = "AI", particulars = "Alumni Interaction with fresher's", facultyName = "Department Alumni", noOfHours = 1),
            Abbreviation(batchId = b1Id, sNo = 6, abbreviation = "TMD", particulars = "Training Module Description", facultyName = "Ms.Chitralekha.S", noOfHours = 1),
            Abbreviation(batchId = b1Id, sNo = 7, abbreviation = "CE", particulars = "Corporate Expectations and How to Prepare Yourself in Three years at College", facultyName = "Trainer - Placement", noOfHours = 1),
            Abbreviation(batchId = b1Id, sNo = 8, abbreviation = "Tamil", particulars = "Tamil Department - Syllabus", facultyName = "From Tamil Department", noOfHours = 3),
            Abbreviation(batchId = b1Id, sNo = 9, abbreviation = "English", particulars = "English Department - Syllabus", facultyName = "From English Department", noOfHours = 3),
            Abbreviation(batchId = b1Id, sNo = 10, abbreviation = "Mathematics", particulars = "Maths Department - Syllabus", facultyName = "From Maths Department", noOfHours = 3),
            Abbreviation(batchId = b1Id, sNo = 11, abbreviation = "GSP", particulars = "Gender Sensitivity Programme / Awareness on Cyber Crime", facultyName = "Expert", noOfHours = 1),
            Abbreviation(batchId = b1Id, sNo = 12, abbreviation = "Core Course", particulars = "Concerned Department Subjects", facultyName = "Concerned Department", noOfHours = 15),
            Abbreviation(batchId = b1Id, sNo = 13, abbreviation = "BCA", particulars = "Bridge Course Assessment", facultyName = "Concerned Department", noOfHours = 2)
        )
        b1Abbrs.forEach { appDao.insertAbbreviation(it) }

        // Seed Schedule slots for Batch 1 (6 Day orders)
        val slotsB1 = listOf(
            ScheduleSlot(batchId = b1Id, dayOrder = "I", date = "02.07.2024", period1 = "FD", period2 = "CT", period3 = "ENG(X)", period4 = "TAMIL", period5 = "NP", period6 = "MS"),
            ScheduleSlot(batchId = b1Id, dayOrder = "II", date = "03.07.2024", period1 = "BJ", period2 = "SSA", period3 = "TAMIL", period4 = "B](OL)", period5 = "MATHS", period6 = "B](OL)"),
            ScheduleSlot(batchId = b1Id, dayOrder = "III", date = "04.07.2024", period1 = "ENG(X)", period2 = "ML", period3 = "TAMIL", period4 = "SS", period5 = "GSP", period6 = "MATHS"),
            ScheduleSlot(batchId = b1Id, dayOrder = "IV", date = "05.07.2024", period1 = "MS", period2 = "AI", period3 = "TMD", period4 = "JC", period5 = "CE", period6 = "MS"),
            ScheduleSlot(batchId = b1Id, dayOrder = "V", date = "08.07.2024", period1 = "MS", period2 = "MATHS", period3 = "NP", period4 = "JC", period5 = "PMF", period6 = "MS"),
            ScheduleSlot(batchId = b1Id, dayOrder = "VI", date = "09.07.2024", period1 = "C LAB(MS)", period2 = "JC", period3 = "ENG(X)", period4 = "Bridge Course Assessment", period5 = "", period6 = "")
        )
        slotsB1.forEach { appDao.insertScheduleSlot(it) }

        // Seed SIP Report for Batch 1
        val reportB1 = SipReport(
            batchId = b1Id,
            academicYear = "2024-2025",
            startDate = "02.07.2024",
            endDate = "09.07.2024",
            narrative = "The Student Induction Program (SIP) for newly admitted first-year CSDA students for AY 2024-2025 was conducted from 02.07.2024 to 09.07.2024. The objective was to bridge the gap between school and college education.",
            objectives = listOf(
                "Help students feel at ease in the new academic environment",
                "Encourage exploration of academic interests and institutional activities",
                "Cultivate collaboration over competition, nurturing a drive for excellence",
                "Strengthen the student-teacher bond",
                "Offer a broader perspective on life, values, and responsibility"
            )
        )
        appDao.insertSipReport(reportB1)

        // Seed questions for Batch 1
        val questionsB1 = listOf(
            Question(batchId = b1Id, subject = "Tamil", mathsStream = "All", questionText = "தமிழ் மொழி செம்மொழி என்று அறிவிக்கப்பட்ட ஆண்டு எது?", optionA = "2004", optionB = "2005", optionC = "2006", optionD = "2008", correctAnswer = "A"),
            Question(batchId = b1Id, subject = "English", mathsStream = "All", questionText = "Which of the following is NOT typically included in a self-introduction?", optionA = "Name", optionB = "Hobbies", optionC = "Favorite food", optionD = "Educational background", correctAnswer = "C"),
            Question(batchId = b1Id, subject = "Maths", mathsStream = "NM", questionText = "Find the value of the expression using BODMAS rule: 8/2*(2+2)", optionA = "4", optionB = "8", optionC = "16", optionD = "20", correctAnswer = "C"),
            Question(batchId = b1Id, subject = "Maths", mathsStream = "M", questionText = "If f(x) = x + 10, what is the value of f(20)?", optionA = "10", optionB = "15", optionC = "20", optionD = "30", correctAnswer = "D")
        )
        questionsB1.forEach { appDao.insertQuestion(it) }

        // Seed Photos for Batch 1
        val photoB1_1 = Photo(
            batchId = b1Id,
            localUri = "classroom_1",
            caption = "Bridge Course Inauguration",
            photoDate = "02.07.2024",
            gpsOverlayText = "Saravanampatty, Coimbatore, Tamil Nadu, India\nLat 11.085266°, Long 76.984707°\n02/07/24 10:41 AM GMT +05:30"
        )
        val photoB1_2 = Photo(
            batchId = b1Id,
            localUri = "classroom_2",
            caption = "Special Lecture on AI & ML",
            photoDate = "04.07.2024",
            gpsOverlayText = "Saravanampatty, Coimbatore, Tamil Nadu, India\nLat 11.085273°, Long 76.984711°\n04/07/24 01:20 PM GMT +05:30"
        )
        appDao.insertPhoto(photoB1_1)
        appDao.insertPhoto(photoB1_2)


        // 3. Seed Batch 2 (2025-2028) - Deeksharambh 6.0
        val batch2 = Batch(
            batchYearRange = "2025-2028",
            academicYear = "2025-2026",
            deeksharambhVersion = "6.0",
            startDate = "26.06.2025",
            endDate = "03.07.2025",
            hodName = "Dr. R. Sasikala",
            principalName = "Dr. V. Radhika",
            className = "I B.Sc. CSDA",
            insights = listOf(
                "Motivational Talks",
                "Gender Sensitivity Programmes",
                "Placement & Life Skill Orientation",
                "Clubs & Committees Orientation",
                "Physical Education",
                "Fun Events",
                "SWAYAM-NPTEL-MOOCS Orientation",
                "Annual Plan",
                "Short & Long-Term Goal Setting",
                "Dissemination of POs & COs"
            ),
            totalStudents = 43,
            tamilMax = 15,
            englishMax = 15,
            mathsMax = 15,
            coreMax = 55,
            totalMax = 100,
            bestWishesFrom = "Shri.T.P.Ramachandran & Dr.V.Radhika"
        )
        val b2Id = appDao.insertBatch(batch2).toInt()

        // Seed students for Batch 2 (43 students)
        val b2StudentsList = listOf(
            "Saarah Azizah K.M" to "M",
            "Mahadharshini V" to "M",
            "Kaavya P" to "M",
            "Karthikaa P" to "M",
            "Abarna C" to "M",
            "Subiskha . P" to "M",
            "Neha Sai .S" to "M",
            "Varshini M" to "M",
            "Abhinaya M" to "M",
            "Shandhanalakshmi C" to "M",
            "Gowri P" to "M",
            "Janaka Nandhini M" to "M",
            "Indhumathi N" to "M",
            "Dharaneesh P.B" to "NM",
            "Madhan Prasanth A" to "NM",
            "Sanjay Aravind .R" to "NM",
            "Keshav balaji K" to "M",
            "Sharmila M" to "M",
            "Surya G" to "M",
            "Vijay C" to "M",
            "Kalaiyarasan A" to "M",
            "Arthi B" to "M",
            "Kaviya Lakshmi M" to "M",
            "Ramanujam P" to "M",
            "Shameer S" to "M",
            "Prakash S" to "NM",
            "Kiruthik C" to "NM",
            "Ajay M" to "M",
            "Illakiya R" to "M",
            "Sandhiya S" to "M",
            "Keerthana V" to "M",
            "Gokulraj P" to "M",
            "Sunil Kumar M" to "M",
            "Naveen V" to "M",
            "Dhanaseelan R M" to "NM",
            "Bharathraj R" to "NM",
            "Pooja" to "NM",
            "Niroshini R" to "NM",
            "Sri ruthra" to "NM",
            "Sasiram S" to "NM",
            "Harani" to "NM",
            "Thirumurugam.M" to "NM",
            "Sathendrareddy" to "NM"
        )

        val b2StudentIds = mutableMapOf<String, Int>()
        b2StudentsList.forEachIndexed { idx, pair ->
            val studId = appDao.insertStudent(
                Student(batchId = b2Id, sNo = idx + 1, name = pair.first, mathsStream = pair.second)
            ).toInt()
            b2StudentIds[pair.first] = studId
        }

        // Seed Results for Batch 2 (43 rows of marks)
        val b2Results = listOf(
            Result(batchId = b2Id, studentId = b2StudentIds["Saarah Azizah K.M"] ?: 0, tamil = "11", english = "12", maths = "12", core = "36", total = 71, percentage = 71.0, isAbsent = false),
            Result(batchId = b2Id, studentId = b2StudentIds["Mahadharshini V"] ?: 0, tamil = "AB", english = "AB", maths = "AB", core = "AB", total = 0, percentage = 0.0, isAbsent = true),
            Result(batchId = b2Id, studentId = b2StudentIds["Kaavya P"] ?: 0, tamil = "11", english = "13", maths = "15", core = "39", total = 78, percentage = 78.0, isAbsent = false),
            Result(batchId = b2Id, studentId = b2StudentIds["Karthikaa P"] ?: 0, tamil = "12", english = "14", maths = "14", core = "45", total = 85, percentage = 85.0, isAbsent = false),
            Result(batchId = b2Id, studentId = b2StudentIds["Abarna C"] ?: 0, tamil = "13", english = "13", maths = "14", core = "30", total = 70, percentage = 70.0, isAbsent = false),
            Result(batchId = b2Id, studentId = b2StudentIds["Subiskha . P"] ?: 0, tamil = "13", english = "15", maths = "13", core = "38", total = 79, percentage = 79.0, isAbsent = false),
            Result(batchId = b2Id, studentId = b2StudentIds["Neha Sai .S"] ?: 0, tamil = "12", english = "8", maths = "9", core = "20", total = 49, percentage = 49.0, isAbsent = false),
            Result(batchId = b2Id, studentId = b2StudentIds["Varshini M"] ?: 0, tamil = "12", english = "14", maths = "15", core = "48", total = 89, percentage = 89.0, isAbsent = false),
            Result(batchId = b2Id, studentId = b2StudentIds["Abhinaya M"] ?: 0, tamil = "13", english = "12", maths = "14", core = "31", total = 70, percentage = 70.0, isAbsent = false),
            Result(batchId = b2Id, studentId = b2StudentIds["Shandhanalakshmi C"] ?: 0, tamil = "13", english = "15", maths = "14", core = "52", total = 94, percentage = 94.0, isAbsent = false),
            Result(batchId = b2Id, studentId = b2StudentIds["Gowri P"] ?: 0, tamil = "13", english = "13", maths = "15", core = "43", total = 84, percentage = 84.0, isAbsent = false),
            Result(batchId = b2Id, studentId = b2StudentIds["Janaka Nandhini M"] ?: 0, tamil = "10", english = "15", maths = "15", core = "49", total = 89, percentage = 89.0, isAbsent = false),
            Result(batchId = b2Id, studentId = b2StudentIds["Indhumathi N"] ?: 0, tamil = "12", english = "15", maths = "14", core = "48", total = 89, percentage = 89.0, isAbsent = false),
            Result(batchId = b2Id, studentId = b2StudentIds["Dharaneesh P.B"] ?: 0, tamil = "12", english = "12", maths = "13", core = "30", total = 67, percentage = 67.0, isAbsent = false),
            Result(batchId = b2Id, studentId = b2StudentIds["Madhan Prasanth A"] ?: 0, tamil = "AB", english = "AB", maths = "AB", core = "AB", total = 0, percentage = 0.0, isAbsent = true),
            Result(batchId = b2Id, studentId = b2StudentIds["Sanjay Aravind .R"] ?: 0, tamil = "AB", english = "AB", maths = "AB", core = "AB", total = 0, percentage = 0.0, isAbsent = true),
            Result(batchId = b2Id, studentId = b2StudentIds["Keshav balaji K"] ?: 0, tamil = "13", english = "15", maths = "15", core = "46", total = 89, percentage = 89.0, isAbsent = false),
            Result(batchId = b2Id, studentId = b2StudentIds["Sharmila M"] ?: 0, tamil = "13", english = "15", maths = "14", core = "51", total = 93, percentage = 93.0, isAbsent = false),
            Result(batchId = b2Id, studentId = b2StudentIds["Surya G"] ?: 0, tamil = "12", english = "10", maths = "7", core = "17", total = 46, percentage = 46.0, isAbsent = false),
            Result(batchId = b2Id, studentId = b2StudentIds["Vijay C"] ?: 0, tamil = "AB", english = "AB", maths = "AB", core = "AB", total = 0, percentage = 0.0, isAbsent = true),
            Result(batchId = b2Id, studentId = b2StudentIds["Kalaiyarasan A"] ?: 0, tamil = "11", english = "11", maths = "12", core = "39", total = 73, percentage = 73.0, isAbsent = false),
            Result(batchId = b2Id, studentId = b2StudentIds["Arthi B"] ?: 0, tamil = "6", english = "15", maths = "15", core = "48", total = 84, percentage = 84.0, isAbsent = false),
            Result(batchId = b2Id, studentId = b2StudentIds["Kaviya Lakshmi M"] ?: 0, tamil = "11", english = "14", maths = "15", core = "47", total = 87, percentage = 87.0, isAbsent = false),
            Result(batchId = b2Id, studentId = b2StudentIds["Ramanujam P"] ?: 0, tamil = "12", english = "7", maths = "12", core = "20", total = 51, percentage = 51.0, isAbsent = false),
            Result(batchId = b2Id, studentId = b2StudentIds["Shameer S"] ?: 0, tamil = "AB", english = "AB", maths = "AB", core = "AB", total = 0, percentage = 0.0, isAbsent = true),
            Result(batchId = b2Id, studentId = b2StudentIds["Prakash S"] ?: 0, tamil = "5", english = "7", maths = "5", core = "23", total = 40, percentage = 40.0, isAbsent = false),
            Result(batchId = b2Id, studentId = b2StudentIds["Kiruthik C"] ?: 0, tamil = "13", english = "12", maths = "12", core = "25", total = 62, percentage = 62.0, isAbsent = false),
            Result(batchId = b2Id, studentId = b2StudentIds["Ajay M"] ?: 0, tamil = "10", english = "10", maths = "13", core = "41", total = 74, percentage = 74.0, isAbsent = false),
            Result(batchId = b2Id, studentId = b2StudentIds["Illakiya R"] ?: 0, tamil = "13", english = "13", maths = "13", core = "50", total = 89, percentage = 89.0, isAbsent = false),
            Result(batchId = b2Id, studentId = b2StudentIds["Sandhiya S"] ?: 0, tamil = "13", english = "12", maths = "10", core = "33", total = 68, percentage = 68.0, isAbsent = false),
            Result(batchId = b2Id, studentId = b2StudentIds["Keerthana V"] ?: 0, tamil = "12", english = "14", maths = "14", core = "38", total = 78, percentage = 78.0, isAbsent = false),
            Result(batchId = b2Id, studentId = b2StudentIds["Gokulraj P"] ?: 0, tamil = "10", english = "13", maths = "12", core = "30", total = 65, percentage = 65.0, isAbsent = false),
            Result(batchId = b2Id, studentId = b2StudentIds["Sunil Kumar M"] ?: 0, tamil = "13", english = "10", maths = "11", core = "27", total = 61, percentage = 61.0, isAbsent = false),
            Result(batchId = b2Id, studentId = b2StudentIds["Naveen V"] ?: 0, tamil = "10", english = "15", maths = "15", core = "36", total = 76, percentage = 76.0, isAbsent = false),
            Result(batchId = b2Id, studentId = b2StudentIds["Dhanaseelan R M"] ?: 0, tamil = "13", english = "12", maths = "11", core = "44", total = 80, percentage = 80.0, isAbsent = false),
            Result(batchId = b2Id, studentId = b2StudentIds["Bharathraj R"] ?: 0, tamil = "11", english = "15", maths = "14", core = "44", total = 84, percentage = 84.0, isAbsent = false),
            Result(batchId = b2Id, studentId = b2StudentIds["Pooja"] ?: 0, tamil = "12", english = "13", maths = "14", core = "33", total = 72, percentage = 72.0, isAbsent = false),
            Result(batchId = b2Id, studentId = b2StudentIds["Niroshini R"] ?: 0, tamil = "12", english = "15", maths = "15", core = "36", total = 78, percentage = 78.0, isAbsent = false),
            Result(batchId = b2Id, studentId = b2StudentIds["Sri ruthra"] ?: 0, tamil = "13", english = "13", maths = "12", core = "45", total = 83, percentage = 83.0, isAbsent = false),
            Result(batchId = b2Id, studentId = b2StudentIds["Sasiram S"] ?: 0, tamil = "10", english = "12", maths = "15", core = "44", total = 81, percentage = 81.0, isAbsent = false),
            Result(batchId = b2Id, studentId = b2StudentIds["Harani"] ?: 0, tamil = "13", english = "14", maths = "13", core = "27", total = 67, percentage = 67.0, isAbsent = false),
            Result(batchId = b2Id, studentId = b2StudentIds["Thirumurugam.M"] ?: 0, tamil = "10", english = "12", maths = "11", core = "23", total = 56, percentage = 56.0, isAbsent = false),
            Result(batchId = b2Id, studentId = b2StudentIds["Sathendrareddy"] ?: 0, tamil = "11", english = "15", maths = "14", core = "47", total = 87, percentage = 87.0, isAbsent = false)
        )
        appDao.insertResultsList(b2Results)

        // Seed Syllabi for Batch 2
        val syllabusTamil2 = Syllabus(
            batchId = b2Id,
            subjectName = "Tamil-I",
            departmentName = "Department of Tamil",
            hours = 3,
            mathsStream = "All",
            objectives = listOf("தமிழ் மொழியின் மீது ஆர்வத்தையும், வாசிப்புத்திறனையும் மேம்படுத்துதல்."),
            units = listOf(
                SyllabusUnit("அலகு 1", "தமிழ் மொழி அறிமுகம்"),
                SyllabusUnit("அலகு 2", "இலக்கிய வரலாறு"),
                SyllabusUnit("அலகு 3", "இக்கால இலக்கியங்கள்"),
                SyllabusUnit("அலகு 4", "இணையத்தமிழ்"),
                SyllabusUnit("அலகு 5", "செயற்கை நுண்ணறிவு")
            ),
            referenceBooks = listOf(
                "தமிழ் இலக்கிய வரலாறு - மு.வரதராசனார்",
                "இணைய செயற்கை நுண்ணறிவுத் தகவல்கள் - வலைத்தளம்"
            ),
            staffIncharge = "Dr. R. Sasikala",
            subjectExpertName = "Dr. S. Poongodi",
            subjectExpertDesignation = "Associate Professor & HOD PG & Research Dept of Tamil",
            subjectExpertInstitution = "Sankara College of Science and Commerce",
            hodName = "Dr. R. Sasikala"
        )
        appDao.insertSyllabus(syllabusTamil2)

        val syllabusEnglish2 = Syllabus(
            batchId = b2Id,
            subjectName = "English Non-Major",
            departmentName = "Department of English",
            hours = 3,
            mathsStream = "All",
            objectives = listOf(
                "The role of communication in business related professions",
                "To improve analytical skills",
                "To enhance listening, writing and speaking skills"
            ),
            units = listOf(
                SyllabusUnit("Unit 1", "Active listening and speaking skills - Pod Cast, Self-introduction and Impromptu speech"),
                SyllabusUnit("Unit 2", "Writing Skills - View on AI"),
                SyllabusUnit("Unit 3", "Narrating an Epic story"),
                SyllabusUnit("Unit 4", "Grammar: Over view - Parts of Speech"),
                SyllabusUnit("Unit 5", "Composition: Covering letter and Bonafide")
            ),
            referenceBooks = listOf(
                "Parts of Speech: Basics of English Grammar by Kuldeep Yadav",
                "Introduction to Indian Philosophy - S. Radhakrishnan",
                "An AI-Powered writing assistant: Grammarly",
                "A Handbook for Letter Writing by S C Gupta"
            ),
            staffIncharge = "Captain E. Justin Ruben",
            subjectExpertName = "Captain E. Justin Ruben",
            subjectExpertDesignation = "Associate NCC Officer & Assistant Professor of English",
            subjectExpertInstitution = "Coimbatore Institute of Technology",
            hodName = "Dr. R. Sasikala"
        )
        appDao.insertSyllabus(syllabusEnglish2)

        val syllabusMathsM2 = Syllabus(
            batchId = b2Id,
            subjectName = "Mathematics (Maths)",
            departmentName = "Department of Mathematics",
            hours = 3,
            mathsStream = "M",
            objectives = listOf(
                "To understand the fundamental concepts of mathematical and statistical techniques",
                "To aware basic ideas to apply for mathematical modelling"
            ),
            units = listOf(
                SyllabusUnit("Unit I", "Matrices: Definition - Types of Matrices - Operations: Addition, Subtraction and Multiplication of Matrices"),
                SyllabusUnit("Unit II", "Introduction to Variables, Constants and Functions - Kind of Equations - Solving Quadratic and Cubic Equations"),
                SyllabusUnit("Unit III", "Statistics - Introduction and Meaning - Data Classification - Diagrammatic and Graphical Representation of data"),
                SyllabusUnit("Unit IV", "Measure of Central Tendency - Introduction and Definition"),
                SyllabusUnit("Unit V", "Measure of Dispersion - Introduction and Definition")
            ),
            referenceBooks = listOf("\"Business Mathematics & Statistics\" - P. A. Navanitham"),
            staffIncharge = "Ms. G. Poongothai",
            subjectExpertName = "Dr. E. Prakash M.Sc., Ph.D.",
            subjectExpertDesignation = "Assistant Professor",
            subjectExpertInstitution = "Kongunadu Arts and Science College",
            hodName = "Dr. R. Sasikala"
        )
        appDao.insertSyllabus(syllabusMathsM2)

        val syllabusMathsNM2 = Syllabus(
            batchId = b2Id,
            subjectName = "Mathematics (Non-Maths)",
            departmentName = "Department of Mathematics",
            hours = 3,
            mathsStream = "NM",
            objectives = listOf(
                "To understand the fundamental concepts of mathematical and statistical techniques",
                "To aware basic ideas to apply for mathematical modelling"
            ),
            units = listOf(
                SyllabusUnit("Unit I", "Introduction to Number System and its Operations - BODMAS Rule - LCM, GCD & HCF"),
                SyllabusUnit("Unit II", "Matrices: Definition - Types of Matrices - Operations: Addition, Subtraction and Multiplication of Matrices"),
                SyllabusUnit("Unit III", "Introduction to Variables, Constants and Functions - Kind of Equations - Solving Quadratic and Cubic Equations"),
                SyllabusUnit("Unit IV", "Statistics - Introduction & Meaning - Types of Data - Diagrammatic and Graphical Representation of data"),
                SyllabusUnit("Unit V", "Measure of Central Tendency - Introduction and Definition")
            ),
            referenceBooks = listOf("\"Business Mathematics & Statistics\" - P. A. Navanitham"),
            staffIncharge = "Mr. S. Bharath",
            subjectExpertName = "Dr. E. Prakash M.Sc., Ph.D.",
            subjectExpertDesignation = "Assistant Professor",
            subjectExpertInstitution = "Kongunadu Arts and Science College",
            hodName = "Dr. R. Sasikala"
        )
        appDao.insertSyllabus(syllabusMathsNM2)

        // Seed Schedule abbreviations for Batch 2
        val b2Abbrs = listOf(
            Abbreviation(batchId = b2Id, sNo = 1, abbreviation = "CT", particulars = "Campus Tour, Institutional Rules and Regulations", facultyName = "Head of the Department", noOfHours = 1),
            Abbreviation(batchId = b2Id, sNo = 2, abbreviation = "FD", particulars = "Familiarization with Department, Career Prospects offered by the Department, workshops, ICT facilities and other facilities", facultyName = "Concerned Class Tutors", noOfHours = 1),
            Abbreviation(batchId = b2Id, sNo = 3, abbreviation = "SSA", particulars = "Student Support Activities / Familiarization of Various Clubs", facultyName = "Bernard Edward - VP", noOfHours = 1),
            Abbreviation(batchId = b2Id, sNo = 4, abbreviation = "PMF", particulars = "Physical & Mental Fitness", facultyName = "Physical Director", noOfHours = 1),
            Abbreviation(batchId = b2Id, sNo = 5, abbreviation = "AI", particulars = "Alumni Interaction with fresher's", facultyName = "Alumni for the Department", noOfHours = 2),
            Abbreviation(batchId = b2Id, sNo = 6, abbreviation = "SDP", particulars = "How to Prepare Yourself in Three years at College - Skill Development Programme", facultyName = "Trainer - Placement", noOfHours = 3),
            Abbreviation(batchId = b2Id, sNo = 7, abbreviation = "LL", particulars = "Library Learning Tools", facultyName = "Librarian", noOfHours = 1),
            Abbreviation(batchId = b2Id, sNo = 8, abbreviation = "Tamil", particulars = "General Tamil", facultyName = "Tamil Department", noOfHours = 3),
            Abbreviation(batchId = b2Id, sNo = 9, abbreviation = "English", particulars = "Communicative English", facultyName = "English Department", noOfHours = 3),
            Abbreviation(batchId = b2Id, sNo = 10, abbreviation = "Mathematics", particulars = "Maths Department - Syllabus", facultyName = "Maths Department", noOfHours = 3),
            Abbreviation(batchId = b2Id, sNo = 11, abbreviation = "GSP", particulars = "Gender Sensitivity Programme", facultyName = "Expert", noOfHours = 2),
            Abbreviation(batchId = b2Id, sNo = 12, abbreviation = "Discipline", particulars = "Concerned Department Courses", facultyName = "Concerned Department", noOfHours = 12),
            Abbreviation(batchId = b2Id, sNo = 13, abbreviation = "BCA", particulars = "Bridge Course Assessment", facultyName = "Concerned Department", noOfHours = 3)
        )
        b2Abbrs.forEach { appDao.insertAbbreviation(it) }

        // Seed Schedule slots for Batch 2 (6 Day orders)
        val slotsB2 = listOf(
            ScheduleSlot(batchId = b2Id, dayOrder = "II", date = "26-06-2025", period1 = "CT", period2 = "FD", period3 = "ENG", period4 = "PRG(SW)", period5 = "N/W(VG)", period6 = "TAMIL"),
            ScheduleSlot(batchId = b2Id, dayOrder = "III", date = "27-06-2025", period1 = "ICT(SVK)", period2 = "SSA", period3 = "ICT(SVK)", period4 = "PRG(SW)", period5 = "ENG", period6 = "PMF"),
            ScheduleSlot(batchId = b2Id, dayOrder = "IV", date = "30-06-2025", period1 = "SDP", period2 = "SDP", period3 = "SDP", period4 = "MATH", period5 = "MATH", period6 = "LL"),
            ScheduleSlot(batchId = b2Id, dayOrder = "V", date = "01-07-2025", period1 = "GSP", period2 = "GSP", period3 = "TAMIL", period4 = "PRG(SW)", period5 = "MATH", period6 = "N/W(VG)"),
            ScheduleSlot(batchId = b2Id, dayOrder = "VI", date = "02-07-2025", period1 = "AI(SW)", period2 = "ALUMNI TALK", period3 = "ENG", period4 = "ENG", period5 = "BD", period6 = "ICT(SVK)"),
            ScheduleSlot(batchId = b2Id, dayOrder = "I", date = "03-07-2025", period1 = "TAMIL", period2 = "MATH", period3 = "N/W(VG)", period4 = "BD", period5 = "BD", period6 = "Bridge Course Assessment")
        )
        slotsB2.forEach { appDao.insertScheduleSlot(it) }

        // Seed SIP Report for Batch 2
        val reportB2 = SipReport(
            batchId = b2Id,
            academicYear = "2025-2026",
            startDate = "26.06.2025",
            endDate = "03.07.2025",
            narrative = "The Student Induction Program (SIP) for newly admitted first-year CSDA students for AY 2025-2026 was conducted from 26.06.2025 to 03.07.2025. It helped build strong student-teacher relationships and transition smoothly.",
            objectives = listOf(
                "Help students feel at ease in the new academic environment",
                "Encourage exploration of academic interests and institutional activities",
                "Cultivate collaboration over competition, nurturing a drive for excellence",
                "Strengthen the student-teacher bond",
                "Offer a broader perspective on life, values, and responsibility"
            )
        )
        appDao.insertSipReport(reportB2)

        // Seed questions for Batch 2
        val questionsB2 = listOf(
            Question(batchId = b2Id, subject = "Tamil", mathsStream = "All", questionText = "தமிழ்த்தாய் வாழ்த்தை எழுதியவர் யார்?", optionA = "பாரதியார்", optionB = "பாரதிதாசன்", optionC = "மனோன்மணீயம் சுந்தரனார்", optionD = "கண்ணதாசன்", correctAnswer = "C"),
            Question(batchId = b2Id, subject = "English", mathsStream = "All", questionText = "Which part of speech is the word 'quickly'?", optionA = "Noun", optionB = "Adjective", optionC = "Adverb", optionD = "Verb", correctAnswer = "C"),
            Question(batchId = b2Id, subject = "Maths", mathsStream = "NM", questionText = "What does 'O' mean in the BODMAS rule?", optionA = "Of", optionB = "Order", optionC = "Odd", optionD = "Operation", correctAnswer = "B"),
            Question(batchId = b2Id, subject = "Maths", mathsStream = "M", questionText = "What is the square root of variance?", optionA = "Standard deviation", optionB = "Mean deviation", optionC = "Quartile deviation", optionD = "Range", correctAnswer = "A")
        )
        questionsB2.forEach { appDao.insertQuestion(it) }

        // Seed Photos for Batch 2
        val photoB2_1 = Photo(
            batchId = b2Id,
            localUri = "classroom_3",
            caption = "Student Interaction Session",
            photoDate = "26.06.2025",
            gpsOverlayText = "Saravanampatty, Coimbatore, Tamil Nadu, India\nLat 11.084996°, Long 76.984207°\n26/06/25 09:51 AM GMT +05:30"
        )
        val photoB2_2 = Photo(
            batchId = b2Id,
            localUri = "classroom_4",
            caption = "Alumni Guest Lecture",
            photoDate = "02.07.2025",
            gpsOverlayText = "Saravanampatty, Coimbatore, Tamil Nadu, India\nLat 11.085007°, Long 76.984277°\n02/07/25 10:49 AM GMT +05:30"
        )
        appDao.insertPhoto(photoB2_1)
        appDao.insertPhoto(photoB2_2)

        // 4. Seed Batch 3 (2026-2029) - Deeksharambh 7.0
        val batch3 = Batch(
            batchYearRange = "2026-2029",
            academicYear = "2026-2027",
            deeksharambhVersion = "7.0",
            startDate = "26.06.2026",
            endDate = "03.07.2026",
            hodName = "Dr. R. Sasikala",
            principalName = "Dr. V. Radhika",
            className = "I B.Sc. CSDA",
            insights = listOf(
                "Motivational Talks",
                "Gender Sensitivity Programmes",
                "Placement & Life Skill Orientation",
                "Clubs & Committees Orientation",
                "Physical Education",
                "Fun Events",
                "SWAYAM-NPTEL-MOOCS Orientation",
                "Annual Plan",
                "Short & Long-Term Goal Setting",
                "Dissemination of POs & COs"
            ),
            totalStudents = 43,
            tamilMax = 10,
            englishMax = 10,
            mathsMax = 10,
            coreMax = 45,
            totalMax = 75,
            bestWishesFrom = "Shri.T.P.Ramachandran & Dr.V.Radhika"
        )
        val b3Id = appDao.insertBatch(batch3).toInt()

        // Seed 43 students for Batch 3 (Deeksharambh 7.0)
        val b3StudentsList = listOf(
            "Saarah Azizah K.M" to "M",
            "Mahadharshini V" to "M",
            "Kaavya P" to "M",
            "Karthikaa P" to "M",
            "Abarna C" to "M",
            "Subiskha . P" to "M",
            "Neha Sai .S" to "M",
            "Varshini M" to "M",
            "Abhinaya M" to "M",
            "Shandhanalakshmi C" to "M",
            "Gowri P" to "M",
            "Janaka Nandhini M" to "M",
            "Indhumathi N" to "M",
            "Dharaneesh P.B" to "M",
            "Madhan Prasanth A" to "M",
            "Sanjay Aravind .R" to "M",
            "Keshav balaji K" to "M",
            "Sharmila M" to "M",
            "Surya G" to "M",
            "Vijay C" to "M",
            "Kalaiyarasan A" to "M",
            "Arthi B" to "M",
            "Kaviya Lakshmi M" to "M",
            "Ramanujam P" to "M",
            "Shameer S" to "M",
            "Prakash S" to "NM",
            "Kiruthik C" to "NM",
            "Ajay M" to "M",
            "Illakiya R" to "M",
            "Sandhiya S" to "M",
            "Keerthana V" to "M",
            "Gokulraj P" to "M",
            "Sunil Kumar M" to "M",
            "Naveen V" to "M",
            "Dhanaseelan R M" to "NM",
            "Bharathraj R" to "NM",
            "Pooja" to "NM",
            "Niroshini R" to "NM",
            "Sri ruthra" to "NM",
            "Sasiram S" to "NM",
            "Harani" to "NM",
            "Thirumurugam.M" to "NM",
            "Sathendrareddy" to "NM"
        )

        val b3StudentIds = mutableMapOf<String, Int>()
        b3StudentsList.forEachIndexed { idx, pair ->
            val studId = appDao.insertStudent(
                Student(batchId = b3Id, sNo = idx + 1, name = pair.first, mathsStream = pair.second)
            ).toInt()
            b3StudentIds[pair.first] = studId
        }

        // Seed abbreviations, syllabus, and schedules for Batch 3 (same as batch 2)
        val b3Abbrs = listOf(
            Abbreviation(batchId = b3Id, sNo = 1, abbreviation = "CT", particulars = "Campus Tour, Institutional Rules and Regulations", facultyName = "Head of the Department", noOfHours = 1),
            Abbreviation(batchId = b3Id, sNo = 2, abbreviation = "FD", particulars = "Familiarization with Department, Career Prospects offered by the Department, workshops, ICT facilities and other facilities", facultyName = "Concerned Class Tutors", noOfHours = 1),
            Abbreviation(batchId = b3Id, sNo = 3, abbreviation = "SSA", particulars = "Student Support Activities / Familiarization of Various Clubs", facultyName = "Bernard Edward - VP", noOfHours = 1),
            Abbreviation(batchId = b3Id, sNo = 4, abbreviation = "PMF", particulars = "Physical & Mental Fitness", facultyName = "Physical Director", noOfHours = 1),
            Abbreviation(batchId = b3Id, sNo = 5, abbreviation = "AI", particulars = "Alumni Interaction with fresher's", facultyName = "Alumni for the Department", noOfHours = 2),
            Abbreviation(batchId = b3Id, sNo = 6, abbreviation = "SDP", particulars = "How to Prepare Yourself in Three years at College - Skill Development Programme", facultyName = "Trainer - Placement", noOfHours = 3),
            Abbreviation(batchId = b3Id, sNo = 7, abbreviation = "LL", particulars = "Library Learning Tools", facultyName = "Librarian", noOfHours = 1),
            Abbreviation(batchId = b3Id, sNo = 8, abbreviation = "Tamil", particulars = "General Tamil", facultyName = "Tamil Department", noOfHours = 3),
            Abbreviation(batchId = b3Id, sNo = 9, abbreviation = "English", particulars = "Communicative English", facultyName = "English Department", noOfHours = 3),
            Abbreviation(batchId = b3Id, sNo = 10, abbreviation = "Mathematics", particulars = "Maths Department - Syllabus", facultyName = "Maths Department", noOfHours = 3),
            Abbreviation(batchId = b3Id, sNo = 11, abbreviation = "GSP", particulars = "Gender Sensitivity Programme", facultyName = "Expert", noOfHours = 2),
            Abbreviation(batchId = b3Id, sNo = 12, abbreviation = "Discipline", particulars = "Concerned Department Courses", facultyName = "Concerned Department", noOfHours = 12),
            Abbreviation(batchId = b3Id, sNo = 13, abbreviation = "BCA", particulars = "Bridge Course Assessment", facultyName = "Concerned Department", noOfHours = 3)
        )
        b3Abbrs.forEach { appDao.insertAbbreviation(it) }

        val slotsB3 = listOf(
            ScheduleSlot(batchId = b3Id, dayOrder = "II", date = "26-06-2026", period1 = "CT", period2 = "FD", period3 = "ENG", period4 = "PRG(SW)", period5 = "N/W(VG)", period6 = "TAMIL"),
            ScheduleSlot(batchId = b3Id, dayOrder = "III", date = "27-06-2026", period1 = "ICT(SVK)", period2 = "SSA", period3 = "ICT(SVK)", period4 = "PRG(SW)", period5 = "ENG", period6 = "PMF"),
            ScheduleSlot(batchId = b3Id, dayOrder = "IV", date = "30-06-2026", period1 = "SDP", period2 = "SDP", period3 = "SDP", period4 = "MATH", period5 = "MATH", period6 = "LL"),
            ScheduleSlot(batchId = b3Id, dayOrder = "V", date = "01-07-2026", period1 = "GSP", period2 = "GSP", period3 = "TAMIL", period4 = "PRG(SW)", period5 = "MATH", period6 = "N/W(VG)"),
            ScheduleSlot(batchId = b3Id, dayOrder = "VI", date = "02-07-2026", period1 = "AI(SW)", period2 = "ALUMNI TALK", period3 = "ENG", period4 = "ENG", period5 = "BD", period6 = "ICT(SVK)"),
            ScheduleSlot(batchId = b3Id, dayOrder = "I", date = "03-07-2026", period1 = "TAMIL", period2 = "MATH", period3 = "N/W(VG)", period4 = "BD", period5 = "BD", period6 = "Bridge Course Assessment")
        )
        slotsB3.forEach { appDao.insertScheduleSlot(it) }

        // Seed SIP Report for Batch 3
        val reportB3 = SipReport(
            batchId = b3Id,
            academicYear = "2026-2027",
            startDate = "26.06.2026",
            endDate = "03.07.2026",
            narrative = "The Student Induction Program (SIP) for newly admitted first-year CSDA students for AY 2026-2027 was conducted from 26.06.2026 to 03.07.2026. It helped build strong student-teacher relationships and transition smoothly.",
            objectives = listOf(
                "Help students feel at ease in the new academic environment",
                "Encourage exploration of academic interests and institutional activities",
                "Cultivate collaboration over competition, nurturing a drive for excellence",
                "Strengthen the student-teacher bond",
                "Offer a broader perspective on life, values, and responsibility"
            )
        )
        appDao.insertSipReport(reportB3)

        // Seed questions for Batch 3
        val questionsB3 = listOf(
            Question(batchId = b3Id, subject = "Tamil", mathsStream = "All", questionText = "தமிழ்த்தாய் வாழ்த்தை எழுதியவர் யார்?", optionA = "பாரதியார்", optionB = "பாரதிதாசன்", optionC = "மனோன்மணீயம் சுந்தரனார்", optionD = "கண்ணதாசன்", correctAnswer = "C"),
            Question(batchId = b3Id, subject = "English", mathsStream = "All", questionText = "Which part of speech is the word 'quickly'?", optionA = "Noun", optionB = "Adjective", optionC = "Adverb", optionD = "Verb", correctAnswer = "C"),
            Question(batchId = b3Id, subject = "Maths", mathsStream = "NM", questionText = "What does 'O' mean in the BODMAS rule?", optionA = "Of", optionB = "Order", optionC = "Odd", optionD = "Operation", correctAnswer = "B"),
            Question(batchId = b3Id, subject = "Maths", mathsStream = "M", questionText = "What is the square root of variance?", optionA = "Standard deviation", optionB = "Mean deviation", optionC = "Quartile deviation", optionD = "Range", correctAnswer = "A")
        )
        questionsB3.forEach { appDao.insertQuestion(it) }

        // Seed Photos for Batch 3
        val photoB3_1 = Photo(
            batchId = b3Id,
            localUri = "classroom_3",
            caption = "Bridge Course Welcome Session",
            photoDate = "26.06.2026",
            gpsOverlayText = "Saravanampatty, Coimbatore, Tamil Nadu, India\nLat 11.084996°, Long 76.984207°\n26/06/26 09:51 AM GMT +05:30"
        )
        val photoB3_2 = Photo(
            batchId = b3Id,
            localUri = "classroom_4",
            caption = "Deeksharambh 7.0 Guest Lecture",
            photoDate = "02.07.2026",
            gpsOverlayText = "Saravanampatty, Coimbatore, Tamil Nadu, India\nLat 11.085007°, Long 76.984277°\n02/07/26 10:49 AM GMT +05:30"
        )
        appDao.insertPhoto(photoB3_1)
        appDao.insertPhoto(photoB3_2)
    }
}
