package com.example.ui.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.SubmissionEntity
import com.example.data.model.TaskEntity
import com.example.data.model.TaskStatus
import com.example.data.model.UserRole
import com.example.ui.components.DifficultyBadge
import com.example.ui.components.SkillChip
import com.example.ui.components.StatusBadge
import com.example.ui.theme.DivnexAmber
import com.example.ui.theme.DivnexEmerald
import com.example.ui.theme.DivnexIndigo
import com.example.ui.theme.DivnexRose

@Composable
fun TaskDetailDialog(
  task: TaskEntity,
  currentRole: UserRole,
  onDismiss: () -> Unit,
  onApply: (TaskEntity) -> Unit,
  onSubmit: (TaskEntity) -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Column {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          DifficultyBadge(difficulty = task.difficulty)
          StatusBadge(status = task.status)
        }
        Spacer(Modifier.height(8.dp))
        Text(
          text = task.title,
          style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        Text(
          text = "Posted by ${task.companyName}",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.primary
        )
      }
    },
    text = {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState())
      ) {
        Text(
          text = "Overview",
          style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(4.dp))
        Text(
          text = task.description,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(12.dp))
        Text(
          text = "Deliverables & Evaluation Criteria",
          style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(4.dp))
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = task.deliverablesCriteria,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(10.dp)
          )
        }

        Spacer(Modifier.height(12.dp))
        Text(
          text = "Required Skills",
          style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(6.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          task.skillsRequired.split(",").forEach { skill ->
            SkillChip(skill = skill.trim())
          }
        }

        Spacer(Modifier.height(14.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Column {
            Text("Estimated Effort", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("${task.estimatedHours} Hours", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
          }
          Column {
            Text("Stipend / Value", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(task.stipendOrReward, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = DivnexEmerald)
          }
          Column {
            Text("Deadline", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("${task.deadlineDays} Days", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
          }
        }
      }
    },
    confirmButton = {
      if (currentRole == UserRole.STUDENT) {
        when (task.status) {
          TaskStatus.PUBLISHED -> {
            Button(
              onClick = { onApply(task) },
              modifier = Modifier.testTag("apply_task_button")
            ) {
              Text("Apply for Micro-Task")
            }
          }
          TaskStatus.ASSIGNED, TaskStatus.REVISION_REQUESTED -> {
            Button(
              onClick = { onSubmit(task) },
              modifier = Modifier.testTag("submit_deliverables_button")
            ) {
              Text("Submit Deliverables")
            }
          }
          else -> {
            TextButton(onClick = onDismiss) {
              Text("Close")
            }
          }
        }
      } else {
        TextButton(onClick = onDismiss) {
          Text("Close")
        }
      }
    },
    dismissButton = {
      if (currentRole == UserRole.STUDENT && (task.status == TaskStatus.PUBLISHED || task.status == TaskStatus.ASSIGNED)) {
        OutlinedButton(onClick = onDismiss) {
          Text("Back")
        }
      }
    }
  )
}

@Composable
fun ApplyTaskDialog(
  task: TaskEntity,
  onDismiss: () -> Unit,
  onSubmitApplication: (taskId: String, coverNote: String, portfolioUrl: String) -> Unit
) {
  var coverNote by remember {
    mutableStateOf("I have hands-on experience in ${task.skillsRequired.split(",").firstOrNull() ?: "this stack"} and can complete this within ${task.estimatedHours} hours adhering to the acceptance criteria.")
  }
  var portfolioUrl by remember { mutableStateOf("https://github.com/alexchen") }
  var errorText by remember { mutableStateOf<String?>(null) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text("Apply: ${task.title}", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
    },
    text = {
      Column(modifier = Modifier.fillMaxWidth()) {
        Text(
          text = "Submit your application to ${task.companyName}. Companies review your previous verified passport records and application note.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
          value = coverNote,
          onValueChange = { coverNote = it },
          label = { Text("Why are you a good fit?") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("application_cover_note_input"),
          minLines = 3
        )

        Spacer(Modifier.height(10.dp))
        OutlinedTextField(
          value = portfolioUrl,
          onValueChange = { portfolioUrl = it },
          label = { Text("Portfolio or GitHub Profile Link") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("application_portfolio_input"),
          singleLine = true
        )

        if (errorText != null) {
          Spacer(Modifier.height(6.dp))
          Text(text = errorText!!, color = DivnexRose, style = MaterialTheme.typography.labelSmall)
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (coverNote.isBlank()) {
            errorText = "Please write a brief note explaining your fit."
          } else if (portfolioUrl.isBlank() || !portfolioUrl.startsWith("http")) {
            errorText = "Please provide a valid URL starting with http:// or https://"
          } else {
            onSubmitApplication(task.id, coverNote, portfolioUrl)
          }
        },
        modifier = Modifier.testTag("submit_application_btn")
      ) {
        Text("Submit Application")
      }
    },
    dismissButton = {
      OutlinedButton(onClick = onDismiss) {
        Text("Cancel")
      }
    }
  )
}

@Composable
fun SubmitDeliverablesDialog(
  task: TaskEntity,
  onDismiss: () -> Unit,
  onSubmit: (taskId: String, githubUrl: String, liveDemoUrl: String, notes: String) -> Unit
) {
  var githubUrl by remember { mutableStateOf("https://github.com/alexchen/divnex-task-solution") }
  var liveDemoUrl by remember { mutableStateOf("https://divnex-demo.alexchen.dev") }
  var notes by remember {
    mutableStateOf("Implemented full acceptance criteria. Key architecture details: clean MVVM separation, TalkBack accessibility tags, and unit tests.")
  }
  var errorMessage by remember { mutableStateOf<String?>(null) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text("Submit Deliverables", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
    },
    text = {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState())
      ) {
        Text(
          text = "Task: ${task.title}",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.primary
        )
        Text(
          text = "Verification requires verifiable source code and demonstration evidence.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
          value = githubUrl,
          onValueChange = { githubUrl = it },
          label = { Text("GitHub Repository URL (Required)") },
          leadingIcon = { Icon(Icons.Default.Code, contentDescription = null) },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("submission_github_url_input"),
          singleLine = true
        )

        Spacer(Modifier.height(10.dp))
        OutlinedTextField(
          value = liveDemoUrl,
          onValueChange = { liveDemoUrl = it },
          label = { Text("Live Demo / Video / Preview URL (Optional)") },
          leadingIcon = { Icon(Icons.Default.Link, contentDescription = null) },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("submission_live_demo_url_input"),
          singleLine = true
        )

        Spacer(Modifier.height(10.dp))
        OutlinedTextField(
          value = notes,
          onValueChange = { notes = it },
          label = { Text("Architecture & Reflection Notes") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("submission_notes_input"),
          minLines = 3
        )

        if (errorMessage != null) {
          Spacer(Modifier.height(8.dp))
          Text(text = errorMessage!!, color = DivnexRose, style = MaterialTheme.typography.labelSmall)
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (githubUrl.isBlank() || (!githubUrl.startsWith("http://") && !githubUrl.startsWith("https://"))) {
            errorMessage = "Please enter a valid GitHub repository URL starting with https://"
          } else if (notes.isBlank()) {
            errorMessage = "Please include architecture or reflection notes."
          } else {
            onSubmit(task.id, githubUrl, liveDemoUrl, notes)
          }
        },
        modifier = Modifier.testTag("confirm_submit_deliverables_btn")
      ) {
        Text("Submit for Review")
      }
    },
    dismissButton = {
      OutlinedButton(onClick = onDismiss) {
        Text("Cancel")
      }
    }
  )
}

@Composable
fun ReviewSubmissionDialog(
  task: TaskEntity,
  submission: SubmissionEntity,
  onDismiss: () -> Unit,
  onRequestRevision: (submissionId: String, taskId: String, notes: String) -> Unit,
  onApproveAndVerify: (submissionId: String, taskId: String, codeQuality: Int, timeliness: Int, communication: Int, feedback: String) -> Unit
) {
  var codeQualityRating by remember { mutableIntStateOf(5) }
  var timelinessRating by remember { mutableIntStateOf(5) }
  var communicationRating by remember { mutableIntStateOf(4) }
  var feedbackText by remember {
    mutableStateOf("High quality deliverable. Clean architectural modularization, well-commented code, and full compliance with our acceptance criteria.")
  }
  var revisionText by remember { mutableStateOf("") }
  var showRevisionField by remember { mutableStateOf(false) }
  var errorMessage by remember { mutableStateOf<String?>(null) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = "Evaluate Submission: ${submission.studentName}",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
      )
    },
    text = {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState())
      ) {
        Text(
          text = "Deliverable Evidence",
          style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(4.dp))
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(10.dp)) {
            Text("GitHub: ${submission.githubUrl}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
            if (submission.liveDemoUrl.isNotBlank()) {
              Text("Live Demo: ${submission.liveDemoUrl}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
            }
            Spacer(Modifier.height(4.dp))
            Text("Notes: ${submission.notes}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }

        Spacer(Modifier.height(14.dp))
        Text(
          text = "Evaluation Rubric (1 to 5 Stars)",
          style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(6.dp))

        RatingRow(label = "Code Quality & Clean Architecture", rating = codeQualityRating, onRatingChange = { codeQualityRating = it })
        RatingRow(label = "Timeliness & Deadline Adherence", rating = timelinessRating, onRatingChange = { timelinessRating = it })
        RatingRow(label = "Communication & Evidence Clarity", rating = communicationRating, onRatingChange = { communicationRating = it })

        Spacer(Modifier.height(10.dp))
        if (!showRevisionField) {
          OutlinedTextField(
            value = feedbackText,
            onValueChange = { feedbackText = it },
            label = { Text("Written Verification Feedback & Endorsement") },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("review_written_feedback_input"),
            minLines = 3
          )
        } else {
          OutlinedTextField(
            value = revisionText,
            onValueChange = { revisionText = it },
            label = { Text("Specify Necessary Revisions") },
            placeholder = { Text("e.g. Please add error handling for offline state in the repository.") },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("revision_notes_input"),
            minLines = 3
          )
        }

        if (errorMessage != null) {
          Spacer(Modifier.height(6.dp))
          Text(text = errorMessage!!, color = DivnexRose, style = MaterialTheme.typography.labelSmall)
        }
      }
    },
    confirmButton = {
      if (!showRevisionField) {
        Button(
          onClick = {
            if (feedbackText.isBlank()) {
              errorMessage = "Please provide written feedback for the verified passport."
            } else {
              onApproveAndVerify(
                submission.id,
                task.id,
                codeQualityRating,
                timelinessRating,
                communicationRating,
                feedbackText
              )
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = DivnexEmerald),
          modifier = Modifier.testTag("approve_and_verify_btn")
        ) {
          Text("Approve & Mint Passport")
        }
      } else {
        Button(
          onClick = {
            if (revisionText.isBlank()) {
              errorMessage = "Please enter specific revision notes."
            } else {
              onRequestRevision(submission.id, task.id, revisionText)
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = DivnexAmber),
          modifier = Modifier.testTag("confirm_request_revision_btn")
        ) {
          Text("Send Revision Request")
        }
      }
    },
    dismissButton = {
      if (!showRevisionField) {
        OutlinedButton(onClick = { showRevisionField = true }) {
          Text("Request Revision")
        }
      } else {
        TextButton(onClick = { showRevisionField = false }) {
          Text("Back to Approval")
        }
      }
    }
  )
}

@Composable
private fun RatingRow(
  label: String,
  rating: Int,
  onRatingChange: (Int) -> Unit
) {
  Column(modifier = Modifier.padding(vertical = 4.dp)) {
    Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    Row(verticalAlignment = Alignment.CenterVertically) {
      for (i in 1..5) {
        IconButton(
          onClick = { onRatingChange(i) },
          modifier = Modifier.size(32.dp)
        ) {
          Icon(
            imageVector = if (i <= rating) Icons.Default.Star else Icons.Default.StarOutline,
            contentDescription = "$i stars",
            tint = if (i <= rating) DivnexAmber else MaterialTheme.colorScheme.outline
          )
        }
      }
      Spacer(Modifier.width(8.dp))
      Text(
        text = "$rating / 5",
        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onSurface
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskDialog(
  onDismiss: () -> Unit,
  onCreateTask: (
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
  ) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var summary by remember { mutableStateOf("") }
  var description by remember { mutableStateOf("") }
  var deliverablesCriteria by remember {
    mutableStateOf("1. Clean modular Kotlin/Compose code\n2. GitHub repository link\n3. TalkBack accessibility test\n4. Readme with setup instructions")
  }
  var category by remember { mutableStateOf("Android") }
  var difficulty by remember { mutableStateOf("Intermediate") }
  var skillsRequired by remember { mutableStateOf("Kotlin, Jetpack Compose, Coroutines") }
  var estimatedHours by remember { mutableStateOf("6") }
  var stipendOrReward by remember { mutableStateOf("$150 + Verified Passport Badge") }
  var deadlineDays by remember { mutableStateOf("4") }
  var errorMessage by remember { mutableStateOf<String?>(null) }

  var categoryExpanded by remember { mutableStateOf(false) }
  val categories = listOf("Android", "Backend", "Frontend", "UI/UX", "AI/Data")

  var difficultyExpanded by remember { mutableStateOf(false) }
  val difficulties = listOf("Beginner", "Intermediate", "Advanced")

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text("Create Micro-Task", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
    },
    text = {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Task Title") },
          placeholder = { Text("e.g. Build an Offline Cache Synchronizer") },
          modifier = Modifier.fillMaxWidth().testTag("create_task_title_input"),
          singleLine = true
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          // Category selector
          ExposedDropdownMenuBox(
            expanded = categoryExpanded,
            onExpandedChange = { categoryExpanded = it },
            modifier = Modifier.weight(1f)
          ) {
            OutlinedTextField(
              value = category,
              onValueChange = {},
              readOnly = true,
              label = { Text("Category") },
              trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
              modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
              expanded = categoryExpanded,
              onDismissRequest = { categoryExpanded = false }
            ) {
              categories.forEach { cat ->
                DropdownMenuItem(
                  text = { Text(cat) },
                  onClick = {
                    category = cat
                    categoryExpanded = false
                  }
                )
              }
            }
          }

          // Difficulty selector
          ExposedDropdownMenuBox(
            expanded = difficultyExpanded,
            onExpandedChange = { difficultyExpanded = it },
            modifier = Modifier.weight(1f)
          ) {
            OutlinedTextField(
              value = difficulty,
              onValueChange = {},
              readOnly = true,
              label = { Text("Difficulty") },
              trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = difficultyExpanded) },
              modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
              expanded = difficultyExpanded,
              onDismissRequest = { difficultyExpanded = false }
            ) {
              difficulties.forEach { diff ->
                DropdownMenuItem(
                  text = { Text(diff) },
                  onClick = {
                    difficulty = diff
                    difficultyExpanded = false
                  }
                )
              }
            }
          }
        }

        OutlinedTextField(
          value = summary,
          onValueChange = { summary = it },
          label = { Text("Short Summary (1 sentence)") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        OutlinedTextField(
          value = description,
          onValueChange = { description = it },
          label = { Text("Detailed Scope & Requirements") },
          modifier = Modifier.fillMaxWidth(),
          minLines = 3
        )

        OutlinedTextField(
          value = deliverablesCriteria,
          onValueChange = { deliverablesCriteria = it },
          label = { Text("Acceptance Checklist") },
          modifier = Modifier.fillMaxWidth(),
          minLines = 2
        )

        OutlinedTextField(
          value = skillsRequired,
          onValueChange = { skillsRequired = it },
          label = { Text("Required Skills (comma separated)") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = estimatedHours,
            onValueChange = { estimatedHours = it },
            label = { Text("Hours") },
            modifier = Modifier.weight(1f),
            singleLine = true
          )
          OutlinedTextField(
            value = deadlineDays,
            onValueChange = { deadlineDays = it },
            label = { Text("Deadline (Days)") },
            modifier = Modifier.weight(1f),
            singleLine = true
          )
        }

        OutlinedTextField(
          value = stipendOrReward,
          onValueChange = { stipendOrReward = it },
          label = { Text("Stipend / Credential Reward") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        if (errorMessage != null) {
          Text(text = errorMessage!!, color = DivnexRose, style = MaterialTheme.typography.labelSmall)
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (title.isBlank() || description.isBlank() || skillsRequired.isBlank()) {
            errorMessage = "Please fill in title, description, and required skills."
          } else {
            val hours = estimatedHours.toIntOrNull() ?: 5
            val days = deadlineDays.toIntOrNull() ?: 3
            onCreateTask(
              title,
              summary.ifBlank { title },
              description,
              deliverablesCriteria,
              category,
              difficulty,
              skillsRequired,
              hours,
              stipendOrReward.ifBlank { "Verified Passport Credential" },
              days
            )
          }
        },
        modifier = Modifier.testTag("publish_micro_task_btn")
      ) {
        Text("Publish Task")
      }
    },
    dismissButton = {
      OutlinedButton(onClick = onDismiss) {
        Text("Cancel")
      }
    }
  )
}
