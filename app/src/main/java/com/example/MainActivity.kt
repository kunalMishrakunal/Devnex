package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.UserRole
import com.example.ui.AppScreen
import com.example.ui.DivnexViewModel
import com.example.ui.components.BackendDiagnosticsDialog
import com.example.ui.components.DivnexTopBar
import com.example.ui.dialogs.ApplyTaskDialog
import com.example.ui.dialogs.CreateTaskDialog
import com.example.ui.dialogs.ReviewSubmissionDialog
import com.example.ui.dialogs.SubmitDeliverablesDialog
import com.example.ui.dialogs.TaskDetailDialog
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.CompanyDashboardScreen
import com.example.ui.screens.LandingScreen
import com.example.ui.screens.PassportScreen
import com.example.ui.screens.StudentDashboardScreen
import com.example.ui.theme.DivnexTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      DivnexTheme {
        DivnexApp()
      }
    }
  }
}

@Composable
fun DivnexApp(viewModel: DivnexViewModel = viewModel()) {
  val context = LocalContext.current
  val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
  val currentRole by viewModel.currentRole.collectAsStateWithLifecycle()
  val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()

  val allTasks by viewModel.allTasks.collectAsStateWithLifecycle()
  val filteredTasks by viewModel.filteredTasks.collectAsStateWithLifecycle()
  val allUsers by viewModel.allUsers.collectAsStateWithLifecycle()
  val allApplications by viewModel.allApplications.collectAsStateWithLifecycle()
  val allSubmissions by viewModel.allSubmissions.collectAsStateWithLifecycle()
  val allReviews by viewModel.allReviews.collectAsStateWithLifecycle()
  val allDisputes by viewModel.allDisputes.collectAsStateWithLifecycle()

  val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
  val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

  // Dialog States
  val detailTask by viewModel.detailTask.collectAsStateWithLifecycle()
  val applyTask by viewModel.applyTask.collectAsStateWithLifecycle()
  val submitTask by viewModel.submitTask.collectAsStateWithLifecycle()
  val reviewingSubmission by viewModel.reviewingSubmission.collectAsStateWithLifecycle()
  val isCreateTaskDialogOpen by viewModel.isCreateTaskDialogOpen.collectAsStateWithLifecycle()
  val isBackendDiagnosticsOpen by viewModel.isBackendDiagnosticsOpen.collectAsStateWithLifecycle()

  val toastMessage by viewModel.toastMessage.collectAsStateWithLifecycle()
  val snackbarHostState = remember { SnackbarHostState() }

  // Handle toast notifications
  LaunchedEffect(toastMessage) {
    toastMessage?.let { msg ->
      Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
      viewModel.clearToast()
    }
  }

  // Handle Android Predictive / Hardware Back
  if (currentScreen != AppScreen.LANDING) {
    BackHandler {
      viewModel.navigateTo(AppScreen.LANDING)
    }
  }

  Scaffold(
    contentWindowInsets = WindowInsets.safeDrawing,
    snackbarHost = { SnackbarHost(snackbarHostState) },
    topBar = {
      DivnexTopBar(
        currentScreen = currentScreen,
        currentRole = currentRole,
        currentUser = currentUser,
        onNavigate = { viewModel.navigateTo(it) },
        onSwitchRole = { viewModel.switchRole(it) },
        onOpenDiagnostics = { viewModel.setBackendDiagnosticsOpen(true) }
      )
    },
    bottomBar = {
      NavigationBar(
        modifier = Modifier.testTag("divnex_bottom_navigation"),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
      ) {
        NavigationBarItem(
          selected = currentScreen == AppScreen.LANDING,
          onClick = { viewModel.navigateTo(AppScreen.LANDING) },
          icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
          label = { Text("Home") },
          modifier = Modifier.testTag("nav_item_home")
        )
        NavigationBarItem(
          selected = currentScreen == AppScreen.STUDENT_DASHBOARD,
          onClick = {
            viewModel.switchRole(UserRole.STUDENT)
            viewModel.navigateTo(AppScreen.STUDENT_DASHBOARD)
          },
          icon = { Icon(Icons.Default.School, contentDescription = "Student Hub") },
          label = { Text("Student") },
          modifier = Modifier.testTag("nav_item_student")
        )
        NavigationBarItem(
          selected = currentScreen == AppScreen.COMPANY_DASHBOARD,
          onClick = {
            viewModel.switchRole(UserRole.COMPANY)
            viewModel.navigateTo(AppScreen.COMPANY_DASHBOARD)
          },
          icon = { Icon(Icons.Default.Business, contentDescription = "Company Hub") },
          label = { Text("Company") },
          modifier = Modifier.testTag("nav_item_company")
        )
        NavigationBarItem(
          selected = currentScreen == AppScreen.PASSPORT,
          onClick = { viewModel.navigateTo(AppScreen.PASSPORT) },
          icon = { Icon(Icons.Default.WorkspacePremium, contentDescription = "Passport") },
          label = { Text("Passport") },
          modifier = Modifier.testTag("nav_item_passport")
        )
        NavigationBarItem(
          selected = currentScreen == AppScreen.ADMIN_DASHBOARD,
          onClick = {
            viewModel.switchRole(UserRole.ADMIN)
            viewModel.navigateTo(AppScreen.ADMIN_DASHBOARD)
          },
          icon = { Icon(Icons.Default.Security, contentDescription = "Admin") },
          label = { Text("Admin") },
          modifier = Modifier.testTag("nav_item_admin")
        )
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding),
      contentAlignment = Alignment.TopCenter
    ) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .widthIn(max = 680.dp)
      ) {
        when (currentScreen) {
          AppScreen.LANDING -> {
            LandingScreen(
              tasks = allTasks,
              onNavigate = { viewModel.navigateTo(it) },
              onSwitchRole = { viewModel.switchRole(it) },
              onOpenTaskDetail = { viewModel.openTaskDetail(it) }
            )
          }

          AppScreen.STUDENT_DASHBOARD -> {
            StudentDashboardScreen(
              currentUser = currentUser,
              tasks = filteredTasks,
              submissions = allSubmissions,
              selectedCategory = selectedCategory,
              searchQuery = searchQuery,
              onCategorySelected = { viewModel.setCategoryFilter(it) },
              onSearchQueryChanged = { viewModel.setSearchQuery(it) },
              onOpenTaskDetail = { viewModel.openTaskDetail(it) },
              onOpenSubmitDeliverables = { viewModel.openSubmitDialog(it) },
              onNavigateToPassport = { viewModel.navigateTo(AppScreen.PASSPORT) }
            )
          }

          AppScreen.COMPANY_DASHBOARD -> {
            CompanyDashboardScreen(
              currentUser = currentUser,
              tasks = allTasks,
              applications = allApplications,
              submissions = allSubmissions,
              onOpenCreateTask = { viewModel.setCreateTaskDialogOpen(true) },
              onOpenReviewSubmission = { task, submission ->
                viewModel.openReviewDialog(task, submission)
              },
              onAcceptApplication = { appId, taskId, studentId, studentName ->
                viewModel.acceptApplication(appId, taskId, studentId, studentName)
              },
              onRejectApplication = { appId ->
                viewModel.rejectApplication(appId)
              },
              onOpenTaskDetail = { viewModel.openTaskDetail(it) }
            )
          }

          AppScreen.PASSPORT -> {
            val studentUser = allUsers.firstOrNull { it.role == UserRole.STUDENT } ?: currentUser
            PassportScreen(
              user = studentUser,
              reviews = allReviews,
              submissions = allSubmissions,
              onCopyPassportLink = {
                val clip = android.content.ClipData.newPlainText("Divnex Passport", "https://divnex.io/passport/alex-chen")
                val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                clipboard.setPrimaryClip(clip)
                Toast.makeText(context, "Passport link copied to clipboard!", Toast.LENGTH_SHORT).show()
              }
            )
          }

          AppScreen.ADMIN_DASHBOARD -> {
            AdminDashboardScreen(
              users = allUsers,
              tasks = allTasks,
              reviews = allReviews,
              disputes = allDisputes,
              backendStatus = viewModel.backendStatus,
              onResolveDispute = { disputeId, notes ->
                viewModel.resolveDispute(disputeId, notes)
              }
            )
          }
        }
      }
    }
  }

  // Task Details Modal
  if (detailTask != null) {
    TaskDetailDialog(
      task = detailTask!!,
      currentRole = currentRole,
      onDismiss = { viewModel.openTaskDetail(null) },
      onApply = { viewModel.openApplyDialog(it) },
      onSubmit = {
        viewModel.openTaskDetail(null)
        viewModel.openSubmitDialog(it)
      }
    )
  }

  // Apply Modal
  if (applyTask != null) {
    ApplyTaskDialog(
      task = applyTask!!,
      onDismiss = { viewModel.closeApplyDialog() },
      onSubmitApplication = { taskId, coverNote, portfolioUrl ->
        viewModel.submitApplication(taskId, coverNote, portfolioUrl)
      }
    )
  }

  // Submit Deliverable Modal
  if (submitTask != null) {
    SubmitDeliverablesDialog(
      task = submitTask!!,
      onDismiss = { viewModel.closeSubmitDialog() },
      onSubmit = { taskId, githubUrl, liveDemoUrl, notes ->
        viewModel.submitDeliverable(taskId, githubUrl, liveDemoUrl, notes)
      }
    )
  }

  // Review Submission Modal
  if (reviewingSubmission != null) {
    val (task, sub) = reviewingSubmission!!
    ReviewSubmissionDialog(
      task = task,
      submission = sub,
      onDismiss = { viewModel.closeReviewDialog() },
      onRequestRevision = { subId, taskId, notes ->
        viewModel.requestRevision(subId, taskId, notes)
      },
      onApproveAndVerify = { subId, taskId, cq, tl, cm, feedback ->
        viewModel.approveAndVerify(subId, taskId, cq, tl, cm, feedback)
      }
    )
  }

  // Create Task Modal
  if (isCreateTaskDialogOpen) {
    CreateTaskDialog(
      onDismiss = { viewModel.setCreateTaskDialogOpen(false) },
      onCreateTask = { title, summary, desc, criteria, cat, diff, skills, hrs, stipend, days ->
        viewModel.createMicroTask(
          title = title,
          summary = summary,
          description = desc,
          deliverablesCriteria = criteria,
          category = cat,
          difficulty = diff,
          skillsRequired = skills,
          estimatedHours = hrs,
          stipendOrReward = stipend,
          deadlineDays = days
        )
      }
    )
  }

  // Backend Diagnostics Modal
  if (isBackendDiagnosticsOpen) {
    BackendDiagnosticsDialog(
      status = viewModel.backendStatus,
      onDismiss = { viewModel.setBackendDiagnosticsOpen(false) }
    )
  }
}
