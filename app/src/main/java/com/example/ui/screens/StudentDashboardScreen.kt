package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.sp
import com.example.data.model.SubmissionEntity
import com.example.data.model.TaskEntity
import com.example.data.model.TaskStatus
import com.example.data.model.UserEntity
import com.example.ui.AppScreen
import com.example.ui.components.DifficultyBadge
import com.example.ui.components.SkillChip
import com.example.ui.components.StatusBadge
import com.example.ui.theme.DivnexAmber
import com.example.ui.theme.DivnexCyan
import com.example.ui.theme.DivnexEmerald
import com.example.ui.theme.DivnexIndigo
import com.example.ui.theme.DivnexRose

@Composable
fun StudentDashboardScreen(
  currentUser: UserEntity?,
  tasks: List<TaskEntity>,
  submissions: List<SubmissionEntity>,
  selectedCategory: String,
  searchQuery: String,
  onCategorySelected: (String) -> Unit,
  onSearchQueryChanged: (String) -> Unit,
  onOpenTaskDetail: (TaskEntity) -> Unit,
  onOpenSubmitDeliverables: (TaskEntity) -> Unit,
  onNavigateToPassport: () -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableIntStateOf(0) }
  val tabs = listOf("Browse Tasks", "My Active Tasks", "Under Review")

  val myStudentId = currentUser?.id ?: "student_alex"

  // Filter tasks based on active tab
  val displayedTasks = when (selectedTab) {
    0 -> tasks.filter { it.status == TaskStatus.PUBLISHED }
    1 -> tasks.filter {
      (it.status == TaskStatus.ASSIGNED || it.status == TaskStatus.REVISION_REQUESTED) &&
          (it.assignedStudentId == myStudentId)
    }
    2 -> tasks.filter {
      it.status == TaskStatus.SUBMITTED && (it.assignedStudentId == myStudentId)
    }
    else -> tasks
  }

  val categories = listOf("All", "Android", "Backend", "Frontend", "UI/UX", "AI/Data")

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Header Profile & Passport Banner
    item {
      Spacer(Modifier.height(4.dp))
      StudentHeaderCard(
        user = currentUser,
        onNavigateToPassport = onNavigateToPassport
      )
    }

    // Tabs
    item {
      TabRow(
        selectedTabIndex = selectedTab,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.primary,
        modifier = Modifier.testTag("student_dashboard_tabs")
      ) {
        tabs.forEachIndexed { index, title ->
          Tab(
            selected = selectedTab == index,
            onClick = { selectedTab = index },
            text = {
              Text(
                text = title,
                style = MaterialTheme.typography.labelLarge.copy(
                  fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                )
              )
            }
          )
        }
      }
    }

    // Search and Category Chips (Visible mainly on Browse tab)
    if (selectedTab == 0) {
      item {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChanged,
            placeholder = { Text("Search by skill, title, or company...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("student_task_search_input"),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
          )

          LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            items(categories) { cat ->
              FilterChip(
                selected = selectedCategory.equals(cat, ignoreCase = true),
                onClick = { onCategorySelected(cat) },
                label = { Text(cat) },
                modifier = Modifier.testTag("filter_chip_$cat")
              )
            }
          }
        }
      }
    }

    // Task count label
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = when (selectedTab) {
            0 -> "Available Opportunities (${displayedTasks.size})"
            1 -> "Active Assignments (${displayedTasks.size})"
            2 -> "Pending Review (${displayedTasks.size})"
            else -> "Tasks"
          },
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onSurface
        )
      }
    }

    // Task Items
    if (displayedTasks.isEmpty()) {
      item {
        EmptyTasksPlaceholder(
          tabIndex = selectedTab,
          onResetFilter = {
            onCategorySelected("All")
            onSearchQueryChanged("")
          }
        )
      }
    } else {
      items(displayedTasks) { task ->
        val matchingSubmission = submissions.firstOrNull { it.taskId == task.id }
        StudentTaskCard(
          task = task,
          submission = matchingSubmission,
          onOpenDetail = { onOpenTaskDetail(task) },
          onSubmitDeliverables = { onOpenSubmitDeliverables(task) }
        )
      }
    }

    item {
      Spacer(Modifier.height(20.dp))
    }
  }
}

@Composable
private fun StudentHeaderCard(
  user: UserEntity?,
  onNavigateToPassport: () -> Unit
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
            text = "Welcome back, ${user?.name ?: "Alex"}",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = user?.headline ?: "Mobile & Systems Fresher",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Surface(
          shape = RoundedCornerShape(12.dp),
          color = DivnexIndigo.copy(alpha = 0.12f),
          modifier = Modifier.padding(start = 8.dp)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Icon(Icons.Default.AssignmentTurnedIn, contentDescription = null, tint = DivnexIndigo, modifier = Modifier.size(14.dp))
            Spacer(Modifier.width(4.dp))
            Text("Fresher Tier", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = DivnexIndigo)
          }
        }
      }

      Spacer(Modifier.height(14.dp))

      // Experience Passport CTA Bar
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = DivnexCyan.copy(alpha = 0.1f),
        border = androidx.compose.foundation.BorderStroke(1.dp, DivnexCyan.copy(alpha = 0.35f)),
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onNavigateToPassport() }
          .testTag("open_passport_from_student_hub")
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = DivnexCyan, modifier = Modifier.size(24.dp))
            Spacer(Modifier.width(10.dp))
            Column {
              Text(
                text = "Digital Experience Passport",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = DivnexCyan
              )
              Text(
                text = "Tamper-evident verification records & skill badges",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
          Icon(Icons.Default.Check, contentDescription = null, tint = DivnexCyan, modifier = Modifier.size(18.dp))
        }
      }
    }
  }
}

@Composable
private fun StudentTaskCard(
  task: TaskEntity,
  submission: SubmissionEntity?,
  onOpenDetail: () -> Unit,
  onSubmitDeliverables: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("student_task_card_${task.id}"),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = task.companyName,
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.primary
        )
        StatusBadge(status = task.status)
      }

      Spacer(Modifier.height(8.dp))
      Text(
        text = task.title,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(Modifier.height(4.dp))
      Text(
        text = task.summary,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      // Revision note callout if status is REVISION_REQUESTED
      if (task.status == TaskStatus.REVISION_REQUESTED && submission?.revisionFeedback != null) {
        Spacer(Modifier.height(8.dp))
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = DivnexRose.copy(alpha = 0.1f),
          border = androidx.compose.foundation.BorderStroke(1.dp, DivnexRose.copy(alpha = 0.4f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.Top) {
            Icon(Icons.Default.Warning, contentDescription = null, tint = DivnexRose, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(8.dp))
            Column {
              Text("Revision Required by Company:", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = DivnexRose)
              Text(submission.revisionFeedback, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
            }
          }
        }
      }

      Spacer(Modifier.height(10.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        task.skillsRequired.split(",").take(3).forEach { skill ->
          SkillChip(skill = skill.trim())
        }
      }

      Spacer(Modifier.height(12.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(text = "Effort & Reward", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text(
            text = "${task.estimatedHours}h • ${task.stipendOrReward}",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = DivnexEmerald
          )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedButton(
            onClick = onOpenDetail,
            modifier = Modifier.testTag("task_details_button_${task.id}")
          ) {
            Text("Details")
          }

          if (task.status == TaskStatus.ASSIGNED || task.status == TaskStatus.REVISION_REQUESTED) {
            Button(
              onClick = onSubmitDeliverables,
              modifier = Modifier.testTag("submit_deliverable_card_btn_${task.id}")
            ) {
              Text(if (task.status == TaskStatus.REVISION_REQUESTED) "Re-Submit" else "Submit Work")
            }
          }
        }
      }
    }
  }
}

@Composable
private fun EmptyTasksPlaceholder(
  tabIndex: Int,
  onResetFilter: () -> Unit
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
      Icon(
        imageVector = Icons.Default.Code,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.size(36.dp)
      )
      Spacer(Modifier.height(10.dp))
      Text(
        text = when (tabIndex) {
          0 -> "No open tasks match your search or filter."
          1 -> "You have no active in-progress tasks right now."
          2 -> "No submissions currently pending company review."
          else -> "No tasks found."
        },
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
      if (tabIndex == 0) {
        Spacer(Modifier.height(8.dp))
        Button(onClick = onResetFilter) {
          Text("Clear Filter")
        }
      }
    }
  }
}
