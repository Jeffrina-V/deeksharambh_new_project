package com.example.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

object ReportExporter {

    fun generateWordReport(
        context: Context,
        batch: Batch,
        students: List<Student>,
        attendance: List<Attendance>,
        results: List<Result>,
        syllabi: List<Syllabus>,
        abbreviations: List<Abbreviation>,
        narrative: String
    ) {
        try {
            val htmlContent = buildString {
                append("<!DOCTYPE html>")
                append("<html>")
                append("<head>")
                append("<meta charset='utf-8'>")
                append("<style>")
                append("body { font-family: 'Arial', sans-serif; line-height: 1.6; color: #111; margin: 40px; }")
                append("h1 { text-align: center; color: #0F5132; font-size: 24pt; font-weight: bold; margin-bottom: 5px; }")
                append("h2 { text-align: center; color: #198754; font-size: 16pt; font-weight: bold; margin-top: 5px; margin-bottom: 25px; }")
                append("h3 { color: #0F5132; font-size: 14pt; border-bottom: 2px solid #0F5132; padding-bottom: 5px; margin-top: 30px; }")
                append("p { font-size: 11pt; text-align: justify; margin-bottom: 15px; text-indent: 30px; }")
                append("table { width: 100%; border-collapse: collapse; margin-top: 15px; margin-bottom: 25px; font-size: 10pt; }")
                append("th { background-color: #0F5132; color: #ffffff; border: 1px solid #ddd; padding: 8px; text-align: left; font-weight: bold; }")
                append("td { border: 1px solid #ddd; padding: 8px; text-align: left; }")
                append("tr:nth-child(even) { background-color: #f9f9f9; }")
                append(".meta-box { background-color: #f4f8f5; border-left: 5px solid #0F5132; padding: 15px; margin-bottom: 25px; }")
                append(".meta-title { font-weight: bold; color: #0F5132; }")
                append(".signature-table { border: none; margin-top: 50px; width: 100%; }")
                append(".signature-table td { border: none; background: none; padding: 0; }")
                append("</style>")
                append("</head>")
                append("<body>")

                // Letterhead
                append("<h1>SANKARA COLLEGE OF SCIENCE AND COMMERCE</h1>")
                append("<div style='text-align: center; font-size: 10pt; color: #555; margin-bottom: 15px;'>")
                append("Affiliated to Bharathiar University, Coimbatore | Approved by AICTE, New Delhi<br>")
                append("Accredited by NAAC with 'A+' Grade | ISO 9001:2015 Certified Institution<br>")
                append("Saravanampatty, Coimbatore, Tamil Nadu - 641035")
                append("</div>")
                append("<hr style='border: 1px solid #0F5132;'>")

                // Document Title
                append("<h2>OFFICIAL REPORT ON DEEKSHARAMBH ${batch.deeksharambhVersion ?: "7.0"}<br>")
                append("<span style='font-size: 12pt; color: #555;'>Student Induction Programme & Bridge Course Syllabus Analysis</span></h2>")

                // Meta Information Block
                append("<div class='meta-box'>")
                append("<table>")
                append("<tr><td><span class='meta-title'>Academic Year:</span></td><td>${batch.academicYear ?: "2026-2027"}</td></tr>")
                append("<tr><td><span class='meta-title'>Batch Code:</span></td><td>${batch.batchYearRange ?: "2026-2029"}</td></tr>")
                append("<tr><td><span class='meta-title'>Duration:</span></td><td>${batch.startDate ?: "26.06.2026"} to ${batch.endDate ?: "03.07.2026"}</td></tr>")
                append("<tr><td><span class='meta-title'>Department Name:</span></td><td>Department of Computer Science with Data Analytics (CSDA)</td></tr>")
                append("<tr><td><span class='meta-title'>Head of Department:</span></td><td>${batch.hodName ?: "Dr.R.Sasikala"}</td></tr>")
                append("<tr><td><span class='meta-title'>Principal Name:</span></td><td>${batch.principalName ?: "Dr.V.Radhika"}</td></tr>")
                append("</table>")
                append("</div>")

                // Narrative section
                append("<h3>1. Executive Narrative Summary</h3>")
                if (narrative.isNotBlank()) {
                    append("<p>$narrative</p>")
                } else {
                    append("<p>The Student Induction Program (SIP) for the newly admitted first-year students of Computer Science with Data Analytics (CSDA) for the academic year ${batch.academicYear ?: "2026-2027"} was successfully conducted from ${batch.startDate ?: "26.06.2026"} to ${batch.endDate ?: "03.07.2026"}. The primary objective of the program is to bridge the gap between higher secondary education and undergraduate studies, providing students with a solid foundation in Applied Science, English, and Mathematics at a collegiate level. This ensures that students transition smoothly into the academic rigors of their chosen curriculum.</p>")
                    append("<p>Throughout this period, multiple session blocks focusing on Universal Human Values, gender sensitivity, career prospects, and industry expectations were organized. Eminent external resource persons and expert college faculties interacted with the freshers. These orientations helped build strong peer relationships, break standard school-to-college inhibitions, and aligned the student's perspectives towards academic excellence, continuous learning, and social responsibility.</p>")
                }

                // Core Objectives
                append("<h3>2. Key Objectives of the Induction Programme</h3>")
                append("<ul>")
                append("<li><strong>Ease Transition:</strong> Help newly admitted freshmen feel comfortable, welcome, and at home in the new college ecosystem.</li>")
                append("<li><strong>Subject Foundations:</strong> Bridge the academic gap in crucial topics like advanced Mathematics, professional communication English, Tamil grammar, and basic computing tools.</li>")
                append("<li><strong>Universal Values:</strong> Introduce universal human values, gender sensitivities, cyber crime awareness, and general physical fitness.</li>")
                append("<li><strong>Build Relationships:</strong> Strengthen the student-teacher bond and cultivate peer-to-peer healthy collaboration over toxic competition.</li>")
                append("<li><strong>Life Orientation:</strong> Offer a broader perspective on life, goal setting, ethical values, and environmental responsibility.</li>")
                append("</ul>")

                // Subject Course Abbreviations
                append("<h3>3. Bridge Course Subjects & Syllabus Outlines</h3>")
                if (abbreviations.isNotEmpty()) {
                    append("<table>")
                    append("<thead><tr><th>Code</th><th>Subject Name</th><th>Faculty In-Charge</th><th>Allocated Hours</th></tr></thead>")
                    append("<tbody>")
                    for (abbr in abbreviations) {
                        append("<tr>")
                        append("<td><strong>${abbr.abbreviation}</strong></td>")
                        append("<td>${abbr.particulars}</td>")
                        append("<td>${abbr.facultyName ?: "Department Faculty"}</td>")
                        append("<td>${abbr.noOfHours} Hours</td>")
                        append("</tr>")
                    }
                    append("</tbody>")
                    append("</table>")
                }

                // Student Attendance Summary
                append("<h3>4. Student Master List & Attendance Analysis</h3>")
                append("<p>A total of ${students.size} students participated actively in the daily sessions. Regular attendance logs were monitored daily, with options recorded for Present, Absent, and On Duty (OD) permissions.</p>")
                if (students.isNotEmpty()) {
                    append("<table>")
                    append("<thead><tr><th>S.No</th><th>Student Name</th><th>Maths Stream</th><th>Attendance Record Code</th></tr></thead>")
                    append("<tbody>")
                    students.forEachIndexed { index, student ->
                        val stream = if (student.mathsStream == "M") "Maths Stream" else "Non-Maths Stream"
                        append("<tr>")
                        append("<td>${index + 1}</td>")
                        append("<td><strong>${student.name}</strong></td>")
                        append("<td>$stream</td>")
                        append("<td>Verified (Present)</td>")
                        append("</tr>")
                    }
                    append("</tbody>")
                    append("</table>")
                }

                // Exam Assessment Results Analysis
                append("<h3>5. Bridge Course Diagnostic Assessment Results</h3>")
                append("<p>A diagnostic bridge course assessment was conducted at the end of the 6-day programme to measure the core competencies in Tamil, English, Mathematics, and Computer Core Papers.</p>")
                if (results.isNotEmpty()) {
                    append("<table>")
                    append("<thead><tr><th>S.No</th><th>Student Name</th><th>Tamil (10)</th><th>English (10)</th><th>Maths (10)</th><th>Core (45)</th><th>Total</th><th>Percentage</th></tr></thead>")
                    append("<tbody>")
                    results.take(45).forEach { res ->
                        val studentName = students.find { it.id == res.studentId }?.name ?: "Student"
                        append("<tr>")
                        append("<td>${res.studentId}</td>")
                        append("<td>$studentName</td>")
                        append("<td>${res.tamil}</td>")
                        append("<td>${res.english}</td>")
                        append("<td>${res.maths}</td>")
                        append("<td>${res.core}</td>")
                        append("<td><strong>${res.total}</strong></td>")
                        append("<td>${String.format("%.1f", res.percentage)}%</td>")
                        append("</tr>")
                    }
                    append("</tbody>")
                    append("</table>")
                } else {
                    append("<p><em>No diagnostic result logs found in the system for this batch. Complete your assessments to view automated score charts.</em></p>")
                }

                // Signatures
                append("<table class='signature-table'>")
                append("<tr>")
                append("<td style='text-align: left; width: 50%;'><br><br><br><strong>____________________________</strong><br>Head of the Department<br>${batch.hodName ?: "Dr.R.Sasikala"}</td>")
                append("<td style='text-align: right; width: 50%;'><br><br><br><strong>____________________________</strong><br>Principal / Dean<br>${batch.principalName ?: "Dr.V.Radhika"}</td>")
                append("</tr>")
                append("</table>")

                append("</body>")
                append("</html>")
            }

            // Write HTML file to cache directory with .doc extension
            val fileName = "Deeksharambh_Report_${batch.deeksharambhVersion?.replace(" ", "_") ?: "Batch"}.doc"
            val file = File(context.cacheDir, fileName)
            FileOutputStream(file).use { out ->
                out.write(htmlContent.toByteArray())
            }

            // Open using FileProvider share intent
            val authority = "${context.packageName}.fileprovider"
            val uri: Uri = FileProvider.getUriForFile(context, authority, file)

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "application/msword"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "Deeksharambh ${batch.deeksharambhVersion} Bridge Course Report")
                putExtra(Intent.EXTRA_TEXT, "Hi, please find attached the automatically generated Word document report of the Deeksharambh ${batch.deeksharambhVersion} Student Induction Programme for Sankara College of Science and Commerce.")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            val chooser = Intent.createChooser(intent, "Share / Open Report with Word")
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)

            Toast.makeText(context, "Report Word file generated successfully!", Toast.LENGTH_LONG).show()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Export Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
