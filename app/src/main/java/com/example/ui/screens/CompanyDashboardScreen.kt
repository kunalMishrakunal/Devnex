package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.ApplicationEntity
import com.example.data.model.SubmissionEntity
import com.example.data.model.TaskEntity
import com.example.data.model.TaskStatus
import com.example.data.model.UserEntity
import com.example.ui.components.DifficultyBadge
import com.example.ui.components.MetricCard
import com.example.ui.components.StatusBadge
import com.example.ui.theme.DivnexAmber
import com.example.ui.theme.DivnexCyan
import com.example.ui.theme.DivnexEmerald
import com.example.ui.theme.DivnexIndigo
import com.example.ui.theme.DivnexRose

@Composable
fun CompanyDashboardScreen(
  currentUser: UserEntity?,
  tasks: List<TaskEntity>,
  applications: List<ApplicationEntity>,
  submissions: List<SubmissionEntity>,
  onOpenCreateTask: () -> Unit,
  onOpenReviewSubmission: (TaskEntity, SubmissionEntity) -> Unit,
  onAcceptApplication: (appId: String, taskId: String, studentId: String, studentName: String) -> Unit,
  onRejectApplication: (appId: String) -> Unit,
  onOpenTaskDetail: (TaskEntity) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableIntStateOf(0) }
  val tabs = listOf("Submissions Awaiting Review", "Applicants", "Our Listings")

  val companyId = currentUser?.id ?: "company_technova"
  val companyTasks = tasks.filter { it.companyId == companyId }

  val pendingSubmissions = submissions.filter { sub ->
    val task = tasks.firstOrNull { it.id == sub.taskId }
    task?.companyId == companyId && (sub.status == TaskStatus.SUBMITTED || task.status == TaskStatus.SUBMITTED)
  }

  val pendingApplications = applications.filter { it.status == "PENDING" }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Company Header Card
    item {
      Spacer(Modifier.height(4.dp))
      CompanyHeader(
        user = currentUser,
        onOpenCreateTask = onOpenCreateTask
      )
    }

    // KPI Metrics
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        MetricCard(
          title = "Active Listings",
          value = companyTasks.size.toString(),
          subtitle = "Total created",
          icon = Icons.Default.Business,
          accentColor = DivnexIndigo,
          modifier = Modifier.weight(1f)
        )
        MetricCard(
          title = "To Review",
          value = pendingSubmissions.size.toString(),
          subtitle = "Deliverables ready",
          icon = Icons.Default.RateReview,
          accentColor = DivnexAmber,
          modifier = Modifier.weight(1f)
        )
        MetricCard(
          title = "Applicants",
          value = pendingApplications.size.toString(),
          subtitle = "Awaiting decision",
          icon = Icons.Default.HourglassTop,
          accentColor = DivnexCyan,
          modifier = Modifier.weight(1f)
        )
      }
    }

    // Tabs
    item {
      TabRow(
        selectedTabIndex = selectedTab,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.primary,
        modifier = Modifier.testTag("company_dashboard_tabs")
      ) {
        tabs.forEachIndexed { index, title ->
          Tab(
            selected = selectedTab == index,
            onClick = { selectedTab = index },
            text = {
              Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                )
              )
            }
          )
        }
      }
    }

    // Tab 0: Submissions to Review
    if (selectedTab == 0) {
      if (pendingSubmissions.isEmpty()) {
        item {
          EmptyCompanyState(
            title = "All Submissions Evaluated",
            message = "There are no pending submissions awaiting review right now."
          )
        }
      } else {
        items(pendingSubmissions) { submission ->
          val task = tasks.firstOrNull { it.id == submission.taskId }
          if (task != null) {
            SubmissionReviewCard(
              task = task,
              submission = submission,
              onEvaluate = { onOpenReviewSubmission(task, submission) }
            )
          }
        }
      }
    }

    // Tab 1: Applicants
    if (selectedTab == 1) {
      if (pendingApplications.isEmpty()) {
        item {
          EmptyCompanyState(
            title = "No Pending Applicants",
            message = "New student applications will appear here for your review and approval."
          )
        }
      } else {
        items(pendingApplications) { application ->
          ApplicantCard(
            application = application,
            onAccept = {
              onAcceptApplication(
                application.id,
                application.taskId,
                application.studentId,
                application.studentName
              )
            },
            onReject = { onRejectApplication(application.id) }
          )
        }
      }
    }

    // Tab 2: Company Listings
    if (selectedTab == 2) {
      if (companyTasks.isEmpty()) {
        item {
          EmptyCompanyState(
            title = "No Tasks Posted Yet",
            message = "Create your first practical micro-task to attract verified fresher talent."
          )
        }
      } else {
        items(companyTasks) { task ->
          CompanyTaskListingCard(
            task = task,
            onClick = { onOpenTaskDetail(task) }
          )
        }
      }
    }

    item {
      Spacer(Modifier.height(24.dp))
    }
  }
}

@Composable
private fun CompanyHeader(
  user: UserEntity?,
  onOpenCreateTask: () -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = user?.companyName ?: "TechNova Solutions",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "Recruiter: ${user?.name ?: "Devin Vance"} • Verified Employer Partner",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
          )
        }

        Surface(
          shape = RoundedCornerShape(12.dp),
          color = DivnexEmerald.copy(alpha = 0.12f),
          border = androidx.compose.foundation.BorderStroke(1.dp, DivnexEmerald.copy(alpha = 0.35f))
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = DivnexEmerald, modifier = Modifier.size(14.dp))
            Spacer(Modifier.width(4.dp))
            Text("Verified Org", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = DivnexEmerald)
          }
        }
      }

      Spacer(Modifier.height(14.dp))

      Button(
        onClick = onOpenCreateTask,
        modifier = Modifier
          .fillMaxWidth()
          .testTag("create_micro_task_btn"),
        colors = ButtonDefaults.buttonColors(containerColor = DivnexIndigo)
      ) {
        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(6.dp))
        Text("Post New Micro-Task")
      }
    }
  }
}

@Composable
private fun SubmissionReviewCard(
  task: TaskEntity,
  submission: SubmissionEntity,
  onEvaluate: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("submission_review_card_${submission.id}"),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = androidx.compose.foundation.BorderStroke(1.dp, DivnexAmber.copy(alpha = 0.4f))
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Candidate: ${submission.studentName}",
          style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onSurface
        )
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = DivnexAmber.copy(alpha = 0.15f)
        ) {
          Text(
            text = "Ready for Review",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = DivnexAmber,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
          )
        }
      }

      Spacer(Modifier.height(4.dp))
      Text(
        text = "Task: ${task.title}",
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
        color = MaterialTheme.colorScheme.primary
      )

      Spacer(Modifier.height(8.dp))
      Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(10.dp)) {
          Text("GitHub: ${submission.githubUrl}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
          if (submission.liveDemoUrl.isNotBlank()) {
            Text("Live Preview: ${submission.liveDemoUrl}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
          }
          Spacer(Modifier.height(4.dp))
          Text("Notes: ${submission.notes}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
      }

      Spacer(Modifier.height(12.dp))
      Button(
        onClick = onEvaluate,
        modifier = Modifier
          .fillMaxWidth()
          .testTag("evaluate_submission_btn_${submission.id}"),
        colors = ButtonDefaults.buttonColors(containerColor = DivnexIndigo)
      ) {
        Icon(Icons.Default.RateReview, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(6.dp))
        Text("Evaluate Deliverable & Score Rubric")
      }
    }
  }
}

@Composable
private fun ApplicantCard(
  application: ApplicationEntity,
  onAccept: () -> Unit,
  onReject: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("applicant_card_${application.id}"),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(text = application.studentName, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
          Text(text = application.studentHeadline, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Text("Task: ${application.taskTitle.take(20)}...", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
      }

      Spacer(Modifier.height(8.dp))
      Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(10.dp)) {
          Text("Cover Note: \"${application.coverNote}\"", style = MaterialTheme.typography.bodySmall)
          Spacer(Modifier.height(4.dp))
          Text("Portfolio: ${application.portfolioUrl}", style = MaterialTheme.typography.labelSmall, color = DivnexCyan)
        }
      }

      Spacer(Modifier.height(12.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        OutlinedButton(
          onClick = onReject,
          modifier = Modifier
            .weight(1f)
            .testTag("reject_applicant_btn_${application.id}")
        ) {
          Icon(Icons.Default.Close, contentDescription = null, tint = DivnexRose, modifier = Modifier.size(16.dp))
          Spacer(Modifier.width(4.dp))
          Text("Decline", color = DivnexRose)
        }

        Button(
          onClick = onAccept,
          modifier = Modifier
            .weight(1f)
            .testTag("accept_applicant_btn_${application.id}"),
          colors = ButtonDefaults.buttonColors(containerColor = DivnexEmerald)
        ) {
          Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(Modifier.width(4.dp))
          Text("Assign Task")
        }
      }
    }
  }
}

@Composable
private fun CompanyTaskListingCard(
  task: TaskEntity,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .testTag("company_task_listing_${task.id}"),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(text = task.category, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
        StatusBadge(status = task.status)
      }
      Spacer(Modifier.height(6.dp))
      Text(text = task.title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
      Spacer(Modifier.height(4.dp))
      Text(text = "${task.estimatedHours}h • ${task.stipendOrReward}", style = MaterialTheme.typography.bodySmall, color = DivnexEmerald)

      if (task.assignedStudentName != null) {
        Spacer(Modifier.height(6.dp))
        Text(
          text = "Assigned to: ${task.assignedStudentName}",
          style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
  }
}

@Composable
private fun EmptyCompanyState(
  title: String,
  message: String
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Icon(Icons.Default.CheckCircle, contentDescription = null, tint = DivnexEmerald, modifier = Modifier.size(36.dp))
      Spacer(Modifier.height(8.dp))
      Text(text = title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
      Spacer(Modifier.height(4.dp))
      Text(text = message, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
  }
}
