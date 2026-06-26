const express = require('express');
const cors = require('cors');
require('dotenv').config();
const connectDB = require('./db');
const mongoose = require('mongoose');

const app = express();
const PORT = process.env.PORT || 5000;

// Enable CORS and JSON parsing
app.use(cors());
app.use(express.json({ limit: '50mb' }));

// Connect to MongoDB Atlas
connectDB();

// Define Mongoose Schemas & Models
const UserSchema = new mongoose.Schema({
    username: { type: String, required: true, unique: true },
    passwordHash: { type: String, required: true },
    role: { type: String, required: true }
}, { timestamps: true });

const BatchSchema = new mongoose.Schema({
    androidId: { type: Number, required: true, unique: true },
    batchYearRange: String,
    academicYear: String,
    deeksharambhVersion: String,
    startDate: String,
    endDate: String,
    hodName: String,
    principalName: String,
    className: String,
    insights: [String],
    totalStudents: Number,
    tamilMax: Number,
    englishMax: Number,
    mathsMax: Number,
    coreMax: Number,
    totalMax: Number,
    bestWishesFrom: String
}, { timestamps: true });

const StudentSchema = new mongoose.Schema({
    androidId: { type: Number, required: true, unique: true },
    batchId: Number,
    sNo: Number,
    name: String,
    mathsStream: String
}, { timestamps: true });

const AttendanceSchema = new mongoose.Schema({
    androidId: { type: Number, required: true, unique: true },
    batchId: Number,
    studentId: Number,
    date: String,
    status: String
}, { timestamps: true });

const QuestionSchema = new mongoose.Schema({
    androidId: { type: Number, required: true, unique: true },
    batchId: Number,
    subject: String,
    mathsStream: String,
    questionText: String,
    optionA: String,
    optionB: String,
    optionC: String,
    optionD: String,
    correctAnswer: String
}, { timestamps: true });

const ResultSchema = new mongoose.Schema({
    androidId: { type: Number, required: true, unique: true },
    batchId: Number,
    studentId: Number,
    tamil: String,
    english: String,
    maths: String,
    core: String,
    total: Number,
    percentage: Number,
    isAbsent: Boolean
}, { timestamps: true });

const SyllabusSchema = new mongoose.Schema({
    androidId: { type: Number, required: true, unique: true },
    batchId: Number,
    subjectName: String,
    departmentName: String,
    hours: Number,
    mathsStream: String,
    objectives: [String],
    units: [{
        unitNumber: String,
        description: String
    }],
    referenceBooks: [String],
    staffIncharge: String,
    subjectExpertName: String,
    subjectExpertDesignation: String,
    subjectExpertInstitution: String,
    hodName: String
}, { timestamps: true });

const ScheduleSlotSchema = new mongoose.Schema({
    androidId: { type: Number, required: true, unique: true },
    batchId: Number,
    dayOrder: String,
    date: String,
    period1: String,
    period2: String,
    period3: String,
    period4: String,
    period5: String,
    period6: String
}, { timestamps: true });

const AbbreviationSchema = new mongoose.Schema({
    androidId: { type: Number, required: true, unique: true },
    batchId: Number,
    sNo: Number,
    abbreviation: String,
    particulars: String,
    facultyName: String,
    noOfHours: Number
}, { timestamps: true });

const PhotoSchema = new mongoose.Schema({
    androidId: { type: Number, required: true, unique: true },
    batchId: Number,
    localUri: String,
    caption: String,
    photoDate: String,
    gpsOverlayText: String
}, { timestamps: true });

const SipReportSchema = new mongoose.Schema({
    androidId: { type: Number, required: true, unique: true },
    batchId: Number,
    academicYear: String,
    startDate: String,
    endDate: String,
    narrative: String,
    objectives: [String]
}, { timestamps: true });

// Register Models
const User = mongoose.model('User', UserSchema);
const Batch = mongoose.model('Batch', BatchSchema);
const Student = mongoose.model('Student', StudentSchema);
const Attendance = mongoose.model('Attendance', AttendanceSchema);
const Question = mongoose.model('Question', QuestionSchema);
const Result = mongoose.model('Result', ResultSchema);
const Syllabus = mongoose.model('Syllabus', SyllabusSchema);
const ScheduleSlot = mongoose.model('ScheduleSlot', ScheduleSlotSchema);
const Abbreviation = mongoose.model('Abbreviation', AbbreviationSchema);
const Photo = mongoose.model('Photo', PhotoSchema);
const SipReport = mongoose.model('SipReport', SipReportSchema);

// REST API Endpoints
// Bulk Synchronization Endpoint
app.post('/api/sync', async (req, res) => {
    try {
        const {
            users,
            batches,
            students,
            attendance,
            questions,
            results,
            syllabi,
            scheduleSlots,
            abbreviations,
            photos,
            sipReports
        } = req.body;

        console.log(`Received bulk sync request from Android app. Processing...`);

        // Helper to perform upsert operation on MongoDB
        const syncCollection = async (items, Model) => {
            if (!items || !Array.isArray(items)) return { synced: 0 };
            let count = 0;
            for (const item of items) {
                // Determine unique filter (username for users, androidId for others)
                let filter = {};
                if (Model === User) {
                    filter = { username: item.username };
                } else {
                    filter = { androidId: item.id || item.androidId };
                    // Map local Room ID to 'androidId'
                    item.androidId = item.id || item.androidId;
                    delete item.id; // Avoid Mongoose ID conflicts
                }

                await Model.findOneAndUpdate(filter, item, { upsert: true, new: true });
                count++;
            }
            return { synced: count };
        };

        const resultsSummary = {
            users: await syncCollection(users, User),
            batches: await syncCollection(batches, Batch),
            students: await syncCollection(students, Student),
            attendance: await syncCollection(attendance, Attendance),
            questions: await syncCollection(questions, Question),
            results: await syncCollection(results, Result),
            syllabi: await syncCollection(syllabi, Syllabus),
            scheduleSlots: await syncCollection(scheduleSlots, ScheduleSlot),
            abbreviations: await syncCollection(abbreviations, Abbreviation),
            photos: await syncCollection(photos, Photo),
            sipReports: await syncCollection(sipReports, SipReport),
        };

        console.log('Synchronization complete! Summary:', resultsSummary);
        res.status(200).json({
            success: true,
            message: 'All application data successfully synced to MongoDB Atlas!',
            summary: resultsSummary
        });
    } catch (error) {
        console.error('Synchronization failed:', error);
        res.status(500).json({
            success: false,
            message: `Internal server sync error: ${error.message}`
        });
    }
});

// GET status endpoint
app.get('/api/status', (req, res) => {
    res.json({
        status: "online",
        database: mongoose.connection.readyState === 1 ? "connected" : "disconnected",
        timestamp: new Date()
    });
});

// HTML Web Dashboard
app.get('/', async (req, res) => {
    try {
        const stats = {
            users: await User.countDocuments(),
            batches: await Batch.countDocuments(),
            students: await Student.countDocuments(),
            attendance: await Attendance.countDocuments(),
            questions: await Question.countDocuments(),
            results: await Result.countDocuments(),
        };

        const userList = await User.find().limit(10);
        const batchList = await Batch.find().limit(5);
        const studentList = await Student.find().limit(10);
        const recentAttendance = await Attendance.find().limit(10);

        res.send(`
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Deeksharambh Cloud Console</title>
    <!-- Tailwind CSS -->
    <script src="https://cdn.tailwindcss.com"></script>
    <!-- FontAwesome Icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@300;400;500;600;700&display=swap');
        body { font-family: 'Plus Jakarta Sans', sans-serif; }
    </style>
</head>
<body class="bg-slate-50 text-slate-800 min-h-screen">
    <!-- Navbar -->
    <nav class="bg-emerald-800 text-white shadow-md">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div class="flex items-center justify-between h-16">
                <div class="flex items-center space-x-3">
                    <div class="bg-white p-2 rounded-lg text-emerald-800">
                        <i class="fa-solid fa-graduation-cap text-xl"></i>
                    </div>
                    <div>
                        <span class="font-bold text-lg block tracking-wide">DEEKSHARAMBH</span>
                        <span class="text-xs text-emerald-100 block">MongoDB Atlas Cloud Console</span>
                    </div>
                </div>
                <div class="flex items-center space-x-4">
                    <span class="bg-emerald-700 px-3 py-1 rounded-full text-xs font-semibold flex items-center space-x-2">
                        <span class="w-2.5 h-2.5 rounded-full bg-green-400 inline-block animate-pulse"></span>
                        <span>Atlas Connected</span>
                    </span>
                </div>
            </div>
        </div>
    </nav>

    <!-- Content -->
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <!-- Banner -->
        <div class="bg-gradient-to-r from-emerald-700 to-teal-800 rounded-2xl text-white p-6 md:p-8 shadow-xl mb-8 relative overflow-hidden">
            <div class="relative z-10 max-w-2xl">
                <span class="bg-teal-600 text-teal-100 text-xs font-bold px-3 py-1 rounded-full tracking-wider uppercase">Active Integration</span>
                <h1 class="text-3xl md:text-4xl font-extrabold mt-3 tracking-tight">Your Real-time Android App Sync Status</h1>
                <p class="text-emerald-50 mt-2 text-sm md:text-base leading-relaxed">
                    This Node.js backend connects directly to your MongoDB Atlas cluster. When Android users tap "Sync to Cloud" inside the app, the local SQLite database instantly mirrors onto your Atlas cloud collections.
                </p>
            </div>
            <div class="absolute right-6 bottom-0 top-0 items-center justify-center hidden md:flex opacity-20">
                <i class="fa-solid fa-cloud-arrow-up text-9xl"></i>
            </div>
        </div>

        <!-- Metrics Grid -->
        <div class="grid grid-cols-2 md:grid-cols-6 gap-4 mb-8">
            <div class="bg-white p-4 rounded-xl border border-slate-100 shadow-sm flex flex-col justify-between">
                <div class="text-slate-400 text-xs font-semibold uppercase tracking-wider">Batches</div>
                <div class="flex items-baseline space-x-2 mt-2">
                    <span class="text-2xl font-bold text-slate-800">${stats.batches}</span>
                    <i class="fa-solid fa-folder-open text-emerald-600 text-sm"></i>
                </div>
            </div>
            <div class="bg-white p-4 rounded-xl border border-slate-100 shadow-sm flex flex-col justify-between">
                <div class="text-slate-400 text-xs font-semibold uppercase tracking-wider">Students</div>
                <div class="flex items-baseline space-x-2 mt-2">
                    <span class="text-2xl font-bold text-slate-800">${stats.students}</span>
                    <i class="fa-solid fa-user-graduate text-blue-600 text-sm"></i>
                </div>
            </div>
            <div class="bg-white p-4 rounded-xl border border-slate-100 shadow-sm flex flex-col justify-between">
                <div class="text-slate-400 text-xs font-semibold uppercase tracking-wider">Attendance</div>
                <div class="flex items-baseline space-x-2 mt-2">
                    <span class="text-2xl font-bold text-slate-800">${stats.attendance}</span>
                    <i class="fa-solid fa-clipboard-user text-amber-600 text-sm"></i>
                </div>
            </div>
            <div class="bg-white p-4 rounded-xl border border-slate-100 shadow-sm flex flex-col justify-between">
                <div class="text-slate-400 text-xs font-semibold uppercase tracking-wider">Exam Results</div>
                <div class="flex items-baseline space-x-2 mt-2">
                    <span class="text-2xl font-bold text-slate-800">${stats.results}</span>
                    <i class="fa-solid fa-chart-line text-purple-600 text-sm"></i>
                </div>
            </div>
            <div class="bg-white p-4 rounded-xl border border-slate-100 shadow-sm flex flex-col justify-between">
                <div class="text-slate-400 text-xs font-semibold uppercase tracking-wider">Quiz Qns</div>
                <div class="flex items-baseline space-x-2 mt-2">
                    <span class="text-2xl font-bold text-slate-800">${stats.questions}</span>
                    <i class="fa-solid fa-circle-question text-cyan-600 text-sm"></i>
                </div>
            </div>
            <div class="bg-white p-4 rounded-xl border border-slate-100 shadow-sm flex flex-col justify-between">
                <div class="text-slate-400 text-xs font-semibold uppercase tracking-wider">Auth Users</div>
                <div class="flex items-baseline space-x-2 mt-2">
                    <span class="text-2xl font-bold text-slate-800">${stats.users}</span>
                    <i class="fa-solid fa-users text-rose-600 text-sm"></i>
                </div>
            </div>
        </div>

        <!-- Main Workspace -->
        <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
            <!-- Left: Config details -->
            <div class="bg-white p-6 rounded-xl border border-slate-100 shadow-sm h-fit">
                <h2 class="font-bold text-lg text-slate-800 mb-4 flex items-center space-x-2">
                    <i class="fa-solid fa-server text-emerald-700"></i>
                    <span>Server Configuration</span>
                </h2>
                
                <div class="space-y-4 text-sm">
                    <div>
                        <span class="block text-xs font-semibold text-slate-400 uppercase tracking-wider">MongoDB Atlas URI</span>
                        <code class="block bg-slate-50 p-2.5 rounded border border-slate-100 text-xs text-slate-600 break-all select-all mt-1">
                            mongodb+srv://deeksharambh:7NobfrzanL4eiGQs@cluster0.cp6tdts.mongodb.net/?appName=Cluster0
                        </code>
                    </div>
                    <div>
                        <span class="block text-xs font-semibold text-slate-400 uppercase tracking-wider">Active Database</span>
                        <span class="block mt-1 font-semibold text-slate-700"><i class="fa-solid fa-database text-teal-600 mr-1.5"></i>deeksharambh</span>
                    </div>
                    <div>
                        <span class="block text-xs font-semibold text-slate-400 uppercase tracking-wider">Platform Status</span>
                        <span class="inline-flex items-center mt-1 text-emerald-800 bg-emerald-50 text-xs px-2.5 py-1 rounded-full font-bold">
                            <span class="w-1.5 h-1.5 rounded-full bg-emerald-500 mr-1.5 inline-block"></span>
                            REST API Live
                        </span>
                    </div>
                    <div class="pt-4 border-t border-slate-100">
                        <h3 class="font-bold text-xs text-slate-400 uppercase tracking-wider mb-2">Android Login Credentials</h3>
                        <div class="bg-emerald-50 p-3 rounded-lg border border-emerald-100 text-xs text-emerald-900 space-y-2">
                            <div><strong class="font-semibold text-emerald-800">Admin Account:</strong> admin / admin123</div>
                            <div><strong class="font-semibold text-emerald-800">Faculty Account:</strong> faculty / faculty123</div>
                            <div><strong class="font-semibold text-emerald-800">Viewer Account:</strong> viewer / viewer123</div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Right: Data Inspector -->
            <div class="lg:col-span-2 space-y-8">
                <!-- Sync Summary -->
                <div class="bg-white p-6 rounded-xl border border-slate-100 shadow-sm">
                    <h2 class="font-bold text-lg text-slate-800 mb-4 flex items-center space-x-2">
                        <i class="fa-solid fa-users-gear text-emerald-700"></i>
                        <span>Users Collections in Atlas (Total: ${stats.users})</span>
                    </h2>
                    
                    <div class="overflow-x-auto">
                        <table class="min-w-full divide-y divide-slate-100 text-sm">
                            <thead class="bg-slate-50">
                                <tr>
                                    <th class="px-4 py-2.5 text-left text-xs font-semibold text-slate-400 uppercase tracking-wider">Username</th>
                                    <th class="px-4 py-2.5 text-left text-xs font-semibold text-slate-400 uppercase tracking-wider">Role</th>
                                    <th class="px-4 py-2.5 text-left text-xs font-semibold text-slate-400 uppercase tracking-wider">Password Hash</th>
                                    <th class="px-4 py-2.5 text-left text-xs font-semibold text-slate-400 uppercase tracking-wider">Created</th>
                                </tr>
                            </thead>
                            <tbody class="divide-y divide-slate-100">
                                ${userList.map(u => `
                                    <tr>
                                        <td class="px-4 py-3 font-semibold text-slate-800">${u.username}</td>
                                        <td class="px-4 py-3">
                                            <span class="px-2 py-0.5 rounded text-xs font-semibold ${
                                                u.role === 'Admin' ? 'bg-rose-50 text-rose-700 border border-rose-100' :
                                                u.role === 'Faculty' ? 'bg-amber-50 text-amber-700 border border-amber-100' :
                                                'bg-slate-100 text-slate-700'
                                            }">${u.role}</span>
                                        </td>
                                        <td class="px-4 py-3 font-mono text-xs text-slate-400">${u.passwordHash}</td>
                                        <td class="px-4 py-3 text-slate-500 text-xs">${new Date(u.createdAt).toLocaleDateString()}</td>
                                    </tr>
                                `).join('') || `<tr><td colspan="4" class="text-center py-4 text-slate-400 text-xs">No synced users yet. Tap sync in Android!</td></tr>`}
                            </tbody>
                        </table>
                    </div>
                </div>

                <!-- Sync Students -->
                <div class="bg-white p-6 rounded-xl border border-slate-100 shadow-sm">
                    <h2 class="font-bold text-lg text-slate-800 mb-4 flex items-center space-x-2">
                        <i class="fa-solid fa-graduation-cap text-emerald-700"></i>
                        <span>Recent Synced Students (Total: ${stats.students})</span>
                    </h2>
                    
                    <div class="overflow-x-auto">
                        <table class="min-w-full divide-y divide-slate-100 text-sm">
                            <thead class="bg-slate-50">
                                <tr>
                                    <th class="px-4 py-2.5 text-left text-xs font-semibold text-slate-400 uppercase tracking-wider">S.No</th>
                                    <th class="px-4 py-2.5 text-left text-xs font-semibold text-slate-400 uppercase tracking-wider">Student Name</th>
                                    <th class="px-4 py-2.5 text-left text-xs font-semibold text-slate-400 uppercase tracking-wider">Maths Stream</th>
                                    <th class="px-4 py-2.5 text-left text-xs font-semibold text-slate-400 uppercase tracking-wider">Atlas Object ID</th>
                                </tr>
                            </thead>
                            <tbody class="divide-y divide-slate-100">
                                ${studentList.map(s => `
                                    <tr>
                                        <td class="px-4 py-3 text-slate-500 font-mono text-xs">${s.sNo}</td>
                                        <td class="px-4 py-3 font-bold text-slate-800">${s.name}</td>
                                        <td class="px-4 py-3">
                                            <span class="px-2 py-0.5 rounded text-xs font-bold ${
                                                s.mathsStream === 'M' ? 'bg-emerald-50 text-emerald-800' : 'bg-orange-50 text-orange-800'
                                            }">${s.mathsStream === 'M' ? 'Maths' : 'Non-Maths'}</span>
                                        </td>
                                        <td class="px-4 py-3 font-mono text-xs text-slate-400">${s._id}</td>
                                    </tr>
                                `).join('') || `<tr><td colspan="4" class="text-center py-4 text-slate-400 text-xs">No students synced. Please execute synchronization from the mobile app.</td></tr>`}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </main>
</body>
</html>
        `);
    } catch (error) {
        res.status(500).send(`Server Dashboard Error: ${error.message}`);
    }
});

// Start the server
app.listen(PORT, () => {
    console.log(`Backend Express server is running on port ${PORT}`);
    console.log(`Connecting to database...`);
});
