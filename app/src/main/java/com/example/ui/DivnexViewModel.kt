package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.DivnexDatabase
import com.example.data.model.ApplicationEntity
import com.example.data.model.DisputeEntity
import com.example.data.model.ReviewEntity
import com.example.data.model.SubmissionEntity
import com.example.data.model.TaskEntity
import com.example.data.model.UserEntity
import com.example.data.model.UserRole
import com.example.data.repository.DivnexRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppScreen {
  LANDING,
  STUDENT_DASHBOARD,
  COMPANY_DASHBOARD,
  ADMIN_DASHBOARD,
  PASSPORT
}

data class BackendReadinessStatus(
  val roomDatabaseActive: Boolean = true,
  val firebaseConfigured: Boolean = false,
  val authProvider: String = "Local Role Session Engine",
  val storageProvider: String = "External Evidence URLs (GitHub / Live Deployments)",
  val securityModel: String = "Strict Lifecycle State Machine & Tamper-Evident Hashing"
)

class DivnexViewModel(application: Application) : AndroidViewModel(application) {

  private val repository: DivnexRepository

  init {
    val db = DivnexDatabase.getDatabase(application)
    repository = DivnexRepository(db.divnexDao())
    viewModelScope.launch {
      repository.seedInitialDataIfNeeded()
    }
  }

  // Active Screen Navigation
  private val _currentScreen = MutableStateFlow(AppScreen.LANDING)
  val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

  // Active Persona / Role
  private val _currentRole = MutableStateFlow(UserRole.STUDENT)
  val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

  // Search & Filter
  private val _selectedCategory = MutableStateFlow("All")
  val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  // Toast / Notification Message
  private val _toastMessage = MutableStateFlow<String?>(null)
  val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

  // Modal / Dialog States
  private val _detailTask = MutableStateFlow<TaskEntity?>(null)
  val detailTask: StateFlow<TaskEntity?> = _detailTask.asStateFlow()

  private val _applyTask = MutableStateFlow<TaskEntity?>(null)
  val applyTask: StateFlow<TaskEntity?> = _applyTask.asStateFlow()

  private val _submitTask = MutableStateFlow<TaskEntity?>(null)
  val submitTask: StateFlow<TaskEntity?> = _submitTask.asStateFlow()

  private val _reviewingSubmission = MutableStateFlow<Pair<TaskEntity, SubmissionEntity>?>(null)
  val reviewingSubmission: StateFlow<Pair<TaskEntity, SubmissionEntity>?> = _reviewingSubmission.asStateFlow()

  private val _isCreateTaskDialogOpen = MutableStateFlow(false)
  val isCreateTaskDialogOpen: StateFlow<Boolean> = _isCreateTaskDialogOpen.asStateFlow()

  private val _isBackendDiagnosticsOpen = MutableStateFlow(false)
  val isBackendDiagnosticsOpen: StateFlow<Boolean> = _isBackendDiagnosticsOpen.asStateFlow()

  // Raw Database Streams
  val allTasks: StateFlow<List<TaskEntity>> = repository.allTasks
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allUsers: StateFlow<List<UserEntity>> = repository.allUsers
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allApplications: StateFlow<List<ApplicationEntity>> = repository.allApplications
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allSubmissions: StateFlow<List<SubmissionEntity>> = repository.allSubmissions
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allReviews: StateFlow<List<ReviewEntity>> = repository.allReviews
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allDisputes: StateFlow<List<DisputeEntity>> = repository.allDisputes
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Current User Entity derivation
  val currentUser: StateFlow<UserEntity?> = combine(allUsers, currentRole) { users, role ->
    users.firstOrNull { it.role == role }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

  // Filtered Tasks for Discovery
  val filteredTasks: StateFlow<List<TaskEntity>> = combine(
    allTasks,
    _selectedCategory,
    _searchQuery
  ) { tasks, category, query ->
    tasks.filter { task ->
      val matchesCategory = (category == "All") || task.category.equals(category, ignoreCase = true)
      val matchesQuery = query.isBlank() ||
          task.title.contains(query, ignoreCase = true) ||
          task.summary.contains(query, ignoreCase = true) ||
          task.skillsRequired.contains(query, ignoreCase = true) ||
          task.companyName.contains(query, ignoreCase = true)
      matchesCategory && matchesQuery
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Backend Diagnostic status
  val backendStatus = BackendReadinessStatus()

  // Navigation & Persona Controls
  fun navigateTo(screen: AppScreen) {
    _currentScreen.value = screen
  }

  fun switchRole(role: UserRole) {
    _currentRole.value = role
    when (role) {
      UserRole.STUDENT -> _currentScreen.value = AppScreen.STUDENT_DASHBOARD
      UserRole.COMPANY -> _currentScreen.value = AppScreen.COMPANY_DASHBOARD
      UserRole.ADMIN -> _currentScreen.value = AppScreen.ADMIN_DASHBOARD
    }
    showToast("Switched active persona to ${role.name}")
  }

  fun setCategoryFilter(category: String) {
    _selectedCategory.value = category
  }

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun openTaskDetail(task: TaskEntity?) {
    _detailTask.value = task
  }

  fun openApplyDialog(task: TaskEntity?) {
    _detailTask.value = null
    _applyTask.value = task
  }

  fun closeApplyDialog() {
    _applyTask.value = null
  }

  fun openSubmitDialog(task: TaskEntity?) {
    _submitTask.value = task
  }

  fun closeSubmitDialog() {
    _submitTask.value = null
  }

  fun openReviewDialog(task: TaskEntity, submission: SubmissionEntity) {
    _reviewingSubmission.value = Pair(task, submission)
  }

  fun closeReviewDialog() {
    _reviewingSubmission.value = null
  }

  fun setCreateTaskDialogOpen(open: Boolean) {
    _isCreateTaskDialogOpen.value = open
  }

  fun setBackendDiagnosticsOpen(open: Boolean) {
    _isBackendDiagnosticsOpen.value = open
  }

  fun clearToast() {
    _toastMessage.value = null
  }

  private fun showToast(msg: String) {
    _toastMessage.value = msg
  }

  // Business Action: Student Apply
  fun submitApplication(taskId: String, coverNote: String, portfolioUrl: String) {
    val user = currentUser.value ?: return
    val task = allTasks.value.firstOrNull { it.id == taskId } ?: return

    viewModelScope.launch {
      repository.applyForTask(
        taskId = taskId,
        taskTitle = task.title,
        studentId = user.id,
        studentName = user.name,
        studentHeadline = user.headline,
        coverNote = coverNote,
        portfolioUrl = portfolioUrl
      )
      closeApplyDialog()
      showToast("Application submitted successfully to ${task.companyName}!")
    }
  }

  // Business Action: Company Accept Applicant
  fun acceptApplication(appId: String, taskId: String, studentId: String, studentName: String) {
    viewModelScope.launch {
      repository.acceptApplication(appId, taskId, studentId, studentName)
      showToast("Application accepted! Task is now assigned to $studentName.")
    }
  }

  // Business Action: Company Reject Applicant
  fun rejectApplication(appId: String) {
    viewModelScope.launch {
      repository.rejectApplication(appId)
      showToast("Application declined.")
    }
  }

  // Business Action: Student Submit Deliverable
  fun submitDeliverable(taskId: String, githubUrl: String, liveDemoUrl: String, notes: String) {
    val user = currentUser.value ?: return
    val task = allTasks.value.firstOrNull { it.id == taskId } ?: return

    if (!githubUrl.startsWith("http://") && !githubUrl.startsWith("https://")) {
      showToast("Please enter a valid GitHub repository URL starting with https://")
      return
    }

    viewModelScope.launch {
      repository.submitDeliverable(
        taskId = taskId,
        taskTitle = task.title,
        studentId = user.id,
        studentName = user.name,
        githubUrl = githubUrl,
        liveDemoUrl = liveDemoUrl,
        notes = notes
      )
      closeSubmitDialog()
      showToast("Deliverables submitted! Pending company review.")
    }
  }

  // Business Action: Company Request Revision
  fun requestRevision(submissionId: String, taskId: String, revisionNotes: String) {
    if (revisionNotes.isBlank()) {
      showToast("Please specify revision requirements.")
      return
    }

    viewModelScope.launch {
      repository.requestRevision(submissionId, taskId, revisionNotes)
      closeReviewDialog()
      showToast("Revision requested. Student notified with feedback.")
    }
  }

  // Business Action: Company Approve & Mint Verification
  fun approveAndVerify(
    submissionId: String,
    taskId: String,
    codeQuality: Int,
    timeliness: Int,
    communication: Int,
    writtenFeedback: String
  ) {
    val task = allTasks.value.firstOrNull { it.id == taskId } ?: return
    val sub = allSubmissions.value.firstOrNull { it.id == submissionId } ?: return

    if (writtenFeedback.isBlank()) {
      showToast("Please provide constructive written feedback.")
      return
    }

    viewModelScope.launch {
      repository.approveAndVerifySubmission(
        submissionId = submissionId,
        taskId = taskId,
        companyId = task.companyId,
        companyName = task.companyName,
        studentId = sub.studentId,
        studentName = sub.studentName,
        taskTitle = task.title,
        codeQualityRating = codeQuality,
        timelinessRating = timeliness,
        communicationRating = communication,
        writtenFeedback = writtenFeedback
      )
      closeReviewDialog()
      showToast("Submission verified! Digital Experience Passport record minted.")
    }
  }

  // Business Action: Company Create Task
  fun createMicroTask(
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
    val user = currentUser.value ?: return
    val companyName = if (user.companyName.isNotBlank()) user.companyName else user.name

    viewModelScope.launch {
      repository.createTask(
        companyId = user.id,
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
        deadlineDays = deadlineDays
      )
      setCreateTaskDialogOpen(false)
      showToast("Micro-task \"$title\" published successfully!")
    }
  }

  // Business Action: Dispute Resolution
  fun resolveDispute(disputeId: String, resolutionNotes: String) {
    viewModelScope.launch {
      repository.resolveDispute(disputeId, resolutionNotes)
      showToast("Dispute resolved by Admin.")
    }
  }
}
