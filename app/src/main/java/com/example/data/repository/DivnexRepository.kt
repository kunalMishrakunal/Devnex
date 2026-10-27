package com.example.data.repository

import com.example.data.local.DivnexDao
import com.example.data.model.ApplicationEntity
import com.example.data.model.DisputeEntity
import com.example.data.model.ReviewEntity
import com.example.data.model.SubmissionEntity
import com.example.data.model.TaskEntity
import com.example.data.model.TaskStatus
import com.example.data.model.UserEntity
import com.example.data.model.UserRole
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID

class DivnexRepository(private val dao: DivnexDao) {

  val allTasks: Flow<List<TaskEntity>> = dao.getAllTasks()
  val allUsers: Flow<List<UserEntity>> = dao.getAllUsers()
  val allApplications: Flow<List<ApplicationEntity>> = dao.getAllApplications()
  val allSubmissions: Flow<List<SubmissionEntity>> = dao.getAllSubmissions()
  val allReviews: Flow<List<ReviewEntity>> = dao.getAllReviews()
  val allDisputes: Flow<List<DisputeEntity>> = dao.getAllDisputes()

  fun getTasksForCompany(companyId: String): Flow<List<TaskEntity>> =
    dao.getTasksForCompany(companyId)

  fun getTasksForStudent(studentId: String): Flow<List<TaskEntity>> =
    dao.getTasksForStudent(studentId)

  fun getReviewsForStudent(studentId: String): Flow<List<ReviewEntity>> =
    dao.getReviewsForStudent(studentId)

  suspend fun getUser(userId: String): UserEntity? = dao.getUserByIdOnce(userId)

  suspend fun getTask(taskId: String): TaskEntity? = dao.getTaskByIdOnce(taskId)

  suspend fun getSubmissionForTask(taskId: String): SubmissionEntity? =
    dao.getSubmissionForTaskOnce(taskId)

  // Seed Data Initializer
  suspend fun seedInitialDataIfNeeded() {
    val existingUsers = dao.getAllUsers().firstOrNull() ?: emptyList()
    if (existingUsers.isNotEmpty()) return

    val users = listOf(
      UserEntity(
        id = "student_alex",
        name = "Alex Chen",
        email = "alex.chen@student.dev",
        role = UserRole.STUDENT,
        headline = "Mobile & Systems Fresher | CS Graduate",
        bio = "Passionate about Kotlin, Jetpack Compose, and clean architecture. Eager to solve real production engineering challenges and build an authenticated track record.",
        skills = "Kotlin, Jetpack Compose, Room DB, Coroutines, Git, REST APIs",
        isVerified = true
      ),
      UserEntity(
        id = "company_technova",
        name = "Devin Vance",
        email = "devin@technova.io",
        role = UserRole.COMPANY,
        headline = "Staff Mobile Architect @ TechNova Solutions",
        bio = "TechNova builds enterprise cloud and edge mobile solutions. We evaluate engineering candidates based on real code and verified micro-projects rather than algorithmic puzzles.",
        companyName = "TechNova Solutions",
        companyWebsite = "https://technova.dev",
        isVerified = true
      ),
      UserEntity(
        id = "company_cloudscale",
        name = "Maya Lin",
        email = "maya@cloudscale.net",
        role = UserRole.COMPANY,
        headline = "Engineering VP @ CloudScale Systems",
        bio = "CloudScale engineers high-performance distributed systems. We sponsor open micro-tasks to mentor and hire emerging software talent.",
        companyName = "CloudScale Systems",
        companyWebsite = "https://cloudscale.net",
        isVerified = true
      ),
      UserEntity(
        id = "admin_divnex",
        name = "Divnex Trust & Safety Officer",
        email = "compliance@divnex.org",
        role = UserRole.ADMIN,
        headline = "Platform Integrity & Verification Auditor",
        bio = "Overseeing verification proof-of-work protocols, preventing fraudulent claims, and auditing dispute outcomes across student and company micro-engagements.",
        isVerified = true
      )
    )
    dao.insertUsers(users)

    val tasks = listOf(
      TaskEntity(
        id = "task_01",
        companyId = "company_technova",
        companyName = "TechNova Solutions",
        title = "Jetpack Compose Micro-Chart Component",
        summary = "Build a lightweight, customizable line and sparkline chart using Compose Canvas with animations.",
        description = "TechNova requires an ultra-lightweight sparkline chart component for our mobile telemetry dashboard. Avoid heavy third-party dependencies; implement directly using DrawScope with smooth spring animations, accessibility labels, and dynamic touch tooltips.",
        deliverablesCriteria = "1. Functional Compose Composable\n2. GitHub repository with sample preview\n3. Zero external rendering libraries\n4. Verified touch target and TalkBack content descriptions",
        category = "Android",
        difficulty = "Intermediate",
        skillsRequired = "Kotlin, Jetpack Compose, Canvas API, Accessibility",
        estimatedHours = 6,
        stipendOrReward = "$150 + Verified Passport Badge",
        deadlineDays = 4,
        status = TaskStatus.APPROVED,
        assignedStudentId = "student_alex",
        assignedStudentName = "Alex Chen"
      ),
      TaskEntity(
        id = "task_02",
        companyId = "company_technova",
        companyName = "TechNova Solutions",
        title = "Offline Room Cache Synchronizer",
        summary = "Implement an optimistic update cache using Room and Kotlin StateFlow with backpressure safety.",
        description = "Create an offline-first data layer module that queues local mutations, syncs when network connectivity returns, and resolves potential conflicts gracefully with deterministic timestamps.",
        deliverablesCriteria = "1. Room Database with DAO and Repository\n2. Network status monitoring listener\n3. Clean architecture separation\n4. GitHub repo with video or live test walkthrough",
        category = "Android",
        difficulty = "Advanced",
        skillsRequired = "Kotlin, Room DB, Coroutines, StateFlow, SQLite",
        estimatedHours = 8,
        stipendOrReward = "$220 + Verified Passport Badge",
        deadlineDays = 5,
        status = TaskStatus.ASSIGNED,
        assignedStudentId = "student_alex",
        assignedStudentName = "Alex Chen"
      ),
      TaskEntity(
        id = "task_03",
        companyId = "company_cloudscale",
        companyName = "CloudScale Systems",
        title = "OAuth2 Deep Link Redirection Handler",
        summary = "Design an Android Intent filter and secure token storage flow using Jetpack Security Crypto.",
        description = "Implement an end-to-end OAuth callback redirect flow that handles custom scheme deep links, stores authorization tokens inside Android EncryptedSharedPreferences, and validates state parameters to prevent CSRF.",
        deliverablesCriteria = "1. AndroidManifest intent-filter declaration\n2. Secure token persistence module\n3. Unit test coverage for state token validation\n4. GitHub repository link",
        category = "Android",
        difficulty = "Intermediate",
        skillsRequired = "Kotlin, Android Intents, Security Crypto, OAuth2",
        estimatedHours = 5,
        stipendOrReward = "$140 + Verified Passport Badge",
        deadlineDays = 3,
        status = TaskStatus.PUBLISHED
      ),
      TaskEntity(
        id = "task_04",
        companyId = "company_technova",
        companyName = "TechNova Solutions",
        title = "Accessible Design System Token Audit",
        summary = "Audit and refactor color contrast and touch targets in an existing M3 component catalogue.",
        description = "Evaluate 10 core Compose components against WCAG AAA color contrast guidelines, ensuring 48dp minimum touch targets and clean semantic TalkBack labels.",
        deliverablesCriteria = "1. Refactored theme color token mappings\n2. Compose Preview matrix with talkback assertions\n3. Summary markdown audit report",
        category = "UI/UX",
        difficulty = "Beginner",
        skillsRequired = "Design Systems, WCAG, Material 3, Jetpack Compose",
        estimatedHours = 4,
        stipendOrReward = "$100 + Verified Passport Badge",
        deadlineDays = 2,
        status = TaskStatus.PUBLISHED
      ),
      TaskEntity(
        id = "task_05",
        companyId = "company_cloudscale",
        companyName = "CloudScale Systems",
        title = "REST API Sliding Window Rate Limiter",
        summary = "Build a thread-safe in-memory rate limiting interceptor using Ktor or OkHttp.",
        description = "Construct an HTTP client interceptor that enforces sliding window rate limits per host, queues burst requests, and emits log telemetry for observability.",
        deliverablesCriteria = "1. OkHttp / Ktor Interceptor implementation\n2. Coroutine Mutex for thread safety\n3. Unit tests simulating burst traffic\n4. GitHub repo link & live test script",
        category = "Backend",
        difficulty = "Advanced",
        skillsRequired = "Kotlin, Coroutines, OkHttp, Ktor, Concurrency",
        estimatedHours = 7,
        stipendOrReward = "$180 + Verified Passport Badge",
        deadlineDays = 4,
        status = TaskStatus.SUBMITTED,
        assignedStudentId = "student_alex",
        assignedStudentName = "Alex Chen"
      )
    )
    dao.insertTasks(tasks)

    // Submissions
    val submissions = listOf(
      SubmissionEntity(
        id = "sub_01",
        taskId = "task_01",
        taskTitle = "Jetpack Compose Micro-Chart Component",
        studentId = "student_alex",
        studentName = "Alex Chen",
        githubUrl = "https://github.com/alexchen/compose-microchart",
        liveDemoUrl = "https://alexchen.dev/demos/chart-preview",
        notes = "Implemented custom DrawScope line rendering with cubic bezier smoothing, spring enter animations, and full TalkBack semantics support.",
        status = TaskStatus.APPROVED,
        submittedAtEpochMs = System.currentTimeMillis() - 86400000L * 3,
        reviewedAtEpochMs = System.currentTimeMillis() - 86400000L * 2
      ),
      SubmissionEntity(
        id = "sub_05",
        taskId = "task_05",
        taskTitle = "REST API Sliding Window Rate Limiter",
        studentId = "student_alex",
        studentName = "Alex Chen",
        githubUrl = "https://github.com/alexchen/sliding-rate-limiter",
        liveDemoUrl = "https://alexchen.dev/demos/limiter-test",
        notes = "Built sliding window counter utilizing atomic integer counters and Kotlin Coroutine Mutex locks. Verified under 100 concurrent coroutine invocations.",
        status = TaskStatus.SUBMITTED,
        submittedAtEpochMs = System.currentTimeMillis() - 3600000L * 4
      )
    )
    dao.insertSubmissions(submissions)

    // Review Record for task_01 -> Populates student's Digital Experience Passport
    val reviews = listOf(
      ReviewEntity(
        id = "rev_01",
        taskId = "task_01",
        submissionId = "sub_01",
        companyId = "company_technova",
        companyName = "TechNova Solutions",
        studentId = "student_alex",
        studentName = "Alex Chen",
        taskTitle = "Jetpack Compose Micro-Chart Component",
        codeQualityRating = 5,
        timelinessRating = 5,
        communicationRating = 4,
        overallRating = 4.8,
        writtenFeedback = "Alex demonstrated exceptional command of low-level Compose Canvas APIs. The bezier curve rendering is remarkably smooth and the code is structured cleanly into reusable modules. Exactly what we need in production.",
        verificationHash = "DVNX-2026-8A9F-7C3E-PASS",
        verifiedTimestampEpochMs = System.currentTimeMillis() - 86400000L * 2
      )
    )
    dao.insertReviews(reviews)

    // Dispute sample
    val disputes = listOf(
      DisputeEntity(
        id = "disp_01",
        taskId = "task_01",
        taskTitle = "Jetpack Compose Micro-Chart Component",
        raisedByRole = UserRole.STUDENT,
        raisedById = "student_alex",
        raisedByName = "Alex Chen",
        reason = "Initial requirement clarification was delayed by 24h, requested deadline extension of 1 day.",
        status = "RESOLVED",
        adminResolutionNotes = "Audited communications log; 24-hour extension granted and accepted by TechNova lead. Verification completed successfully.",
        createdAtEpochMs = System.currentTimeMillis() - 86400000L * 4
      )
    )
    dao.insertDisputes(disputes)
  }

  // Task Creation
  suspend fun createTask(
    companyId: String,
    companyName: String,
    title: String,
    summary: String,
    description: String,
    deliverablesCriteria: String,
    category: String,
    difficulty: String,
    skillsRequired: String,
    estimatedHours: Int,
    stipendOrReward: String,
    deadlineDays: Int
  ) {
    val newTask = TaskEntity(
      id = "task_" + UUID.randomUUID().toString().take(8),
      companyId = companyId,
      companyName = companyName,
      title = title,
      summary = summary,
      description = description,
      deliverablesCriteria = deliverablesCriteria,
      category = category,
      difficulty = difficulty,
      skillsRequired = skillsRequired,
      estimatedHours = estimatedHours,
      stipendOrReward = stipendOrReward,
      deadlineDays = deadlineDays,
      status = TaskStatus.PUBLISHED
    )
    dao.insertTask(newTask)
  }

  // Student Applies for Task
  suspend fun applyForTask(
    taskId: String,
    taskTitle: String,
    studentId: String,
    studentName: String,
    studentHeadline: String,
    coverNote: String,
    portfolioUrl: String
  ) {
    val application = ApplicationEntity(
      id = "app_" + UUID.randomUUID().toString().take(8),
      taskId = taskId,
      taskTitle = taskTitle,
      studentId = studentId,
      studentName = studentName,
      studentHeadline = studentHeadline,
      coverNote = coverNote,
      portfolioUrl = portfolioUrl,
      status = "PENDING"
    )
    dao.insertApplication(application)
  }

  // Company Accepts Application & Assigns Task
  suspend fun acceptApplication(applicationId: String, taskId: String, studentId: String, studentName: String) {
    dao.updateApplicationStatus(applicationId, "ACCEPTED")
    dao.assignTaskToStudent(taskId, studentId, studentName, TaskStatus.ASSIGNED)
  }

  // Company Rejects Application
  suspend fun rejectApplication(applicationId: String) {
    dao.updateApplicationStatus(applicationId, "REJECTED")
  }

  // Student Submits Deliverable
  suspend fun submitDeliverable(
    taskId: String,
    taskTitle: String,
    studentId: String,
    studentName: String,
    githubUrl: String,
    liveDemoUrl: String,
    notes: String
  ) {
    val submission = SubmissionEntity(
      id = "sub_" + UUID.randomUUID().toString().take(8),
      taskId = taskId,
      taskTitle = taskTitle,
      studentId = studentId,
      studentName = studentName,
      githubUrl = githubUrl,
      liveDemoUrl = liveDemoUrl,
      notes = notes,
      status = TaskStatus.SUBMITTED,
      submittedAtEpochMs = System.currentTimeMillis()
    )
    dao.insertSubmission(submission)
    dao.updateTaskStatus(taskId, TaskStatus.SUBMITTED)
  }

  // Company Requests Revision
  suspend fun requestRevision(
    submissionId: String,
    taskId: String,
    revisionNotes: String
  ) {
    val existing = dao.getSubmissionForTaskOnce(taskId)
    if (existing != null) {
      val updated = existing.copy(
        status = TaskStatus.REVISION_REQUESTED,
        revisionFeedback = revisionNotes,
        reviewedAtEpochMs = System.currentTimeMillis()
      )
      dao.updateSubmission(updated)
    }
    dao.updateTaskStatus(taskId, TaskStatus.REVISION_REQUESTED)
  }

  // Company Approves & Mints Verified Passport Record
  suspend fun approveAndVerifySubmission(
    submissionId: String,
    taskId: String,
    companyId: String,
    companyName: String,
    studentId: String,
    studentName: String,
    taskTitle: String,
    codeQualityRating: Int,
    timelinessRating: Int,
    communicationRating: Int,
    writtenFeedback: String
  ) {
    val overallRating = ((codeQualityRating + timelinessRating + communicationRating) / 3.0 * 10).toInt() / 10.0
    val verificationHash = "DVNX-" + UUID.randomUUID().toString().take(8).uppercase() + "-PASS"

    val review = ReviewEntity(
      id = "rev_" + UUID.randomUUID().toString().take(8),
      taskId = taskId,
      submissionId = submissionId,
      companyId = companyId,
      companyName = companyName,
      studentId = studentId,
      studentName = studentName,
      taskTitle = taskTitle,
      codeQualityRating = codeQualityRating,
      timelinessRating = timelinessRating,
      communicationRating = communicationRating,
      overallRating = overallRating,
      writtenFeedback = writtenFeedback,
      verificationHash = verificationHash,
      verifiedTimestampEpochMs = System.currentTimeMillis()
    )

    dao.insertReview(review)

    val existingSub = dao.getSubmissionForTaskOnce(taskId)
    if (existingSub != null) {
      dao.updateSubmission(existingSub.copy(status = TaskStatus.APPROVED, reviewedAtEpochMs = System.currentTimeMillis()))
    }
    dao.updateTaskStatus(taskId, TaskStatus.APPROVED)
  }

  // Dispute submission
  suspend fun raiseDispute(
    taskId: String,
    taskTitle: String,
    raisedByRole: UserRole,
    raisedById: String,
    raisedByName: String,
    reason: String
  ) {
    val dispute = DisputeEntity(
      id = "disp_" + UUID.randomUUID().toString().take(8),
      taskId = taskId,
      taskTitle = taskTitle,
      raisedByRole = raisedByRole,
      raisedById = raisedById,
      raisedByName = raisedByName,
      reason = reason,
      status = "OPEN"
    )
    dao.insertDispute(dispute)
  }

  // Admin resolves dispute
  suspend fun resolveDispute(disputeId: String, notes: String) {
    dao.resolveDispute(disputeId, "RESOLVED", notes)
  }
}
