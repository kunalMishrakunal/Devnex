package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TaskEntity
import com.example.data.model.UserRole
import com.example.ui.AppScreen
import com.example.ui.components.DifficultyBadge
import com.example.ui.components.SkillChip
import com.example.ui.components.StatusBadge
import com.example.ui.theme.DivnexAmber
import com.example.ui.theme.DivnexCyan
import com.example.ui.theme.DivnexEmerald
import com.example.ui.theme.DivnexIndigo

@Composable
fun LandingScreen(
  tasks: List<TaskEntity>,
  onNavigate: (AppScreen) -> Unit,
  onSwitchRole: (UserRole) -> Unit,
  onOpenTaskDetail: (TaskEntity) -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Hero Banner
    item {
      Spacer(modifier = Modifier.height(8.dp))
      HeroSection(
        onExploreStudent = {
          onSwitchRole(UserRole.STUDENT)
          onNavigate(AppScreen.STUDENT_DASHBOARD)
        },
        onExploreCompany = {
          onSwitchRole(UserRole.COMPANY)
          onNavigate(AppScreen.COMPANY_DASHBOARD)
        },
        onViewPassport = {
          onNavigate(AppScreen.PASSPORT)
        }
      )
    }

    // Platform Live Metrics
    item {
      LiveMetricsSection()
    }

    // How Divnex Works (4-Step Lifecycle)
    item {
      HowItWorksSection()
    }

    // Role Benefits Cards
    item {
      BenefitsSection(
        onStudentClick = {
          onSwitchRole(UserRole.STUDENT)
          onNavigate(AppScreen.STUDENT_DASHBOARD)
        },
        onCompanyClick = {
          onSwitchRole(UserRole.COMPANY)
          onNavigate(AppScreen.COMPANY_DASHBOARD)
        }
      )
    }

    // Featured Micro-Tasks Header
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Featured Micro-Tasks",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "Real practical projects currently open for application",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
        OutlinedButton(
          onClick = {
            onSwitchRole(UserRole.STUDENT)
            onNavigate(AppScreen.STUDENT_DASHBOARD)
          },
          modifier = Modifier.testTag("view_all_tasks_landing_btn")
        ) {
          Text("View All")
          Spacer(Modifier.width(4.dp))
          Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
        }
      }
    }

    // Featured Tasks List
    items(tasks.take(3)) { task ->
      FeaturedTaskCard(
        task = task,
        onClick = { onOpenTaskDetail(task) }
      )
    }

    // Bottom spacing
    item {
      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

@Composable
private fun HeroSection(
  onExploreStudent: () -> Unit,
  onExploreCompany: () -> Unit,
  onViewPassport: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("landing_hero_section"),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          brush = Brush.verticalGradient(
            colors = listOf(
              DivnexIndigo.copy(alpha = 0.12f),
              Color.Transparent
            )
          )
        )
        .padding(20.dp)
    ) {
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Tagline chip
        Surface(
          shape = RoundedCornerShape(20.dp),
          color = DivnexCyan.copy(alpha = 0.15f),
          border = androidx.compose.foundation.BorderStroke(1.dp, DivnexCyan.copy(alpha = 0.4f))
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
          ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = DivnexCyan, modifier = Modifier.size(14.dp))
            Spacer(Modifier.width(6.dp))
            Text(
              text = "VERIFIED WORKPLACE PROOF-OF-WORK",
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
              color = DivnexCyan
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = "Micro-Experience.\nMaximum Proof.",
          style = MaterialTheme.typography.headlineLarge.copy(
            fontWeight = FontWeight.Black,
            lineHeight = 36.sp,
            textAlign = TextAlign.Center
          ),
          color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = "Divnex connects freshers with tech companies for real-world micro-tasks. Complete hands-on deliverables, receive vetted company reviews, and build an authenticated Digital Experience Passport.",
          style = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center),
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(18.dp))

        // CTA Buttons
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Button(
            onClick = onExploreStudent,
            modifier = Modifier
              .weight(1f)
              .testTag("cta_student_portal"),
            colors = ButtonDefaults.buttonColors(containerColor = DivnexIndigo)
          ) {
            Icon(Icons.Default.School, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(6.dp))
            Text("I'm a Fresher")
          }

          Button(
            onClick = onExploreCompany,
            modifier = Modifier
              .weight(1f)
              .testTag("cta_company_portal"),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
          ) {
            Icon(Icons.Default.Business, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(6.dp))
            Text("I'm a Company", color = MaterialTheme.colorScheme.onPrimaryContainer)
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
          onClick = onViewPassport,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("cta_sample_passport"),
          border = androidx.compose.foundation.BorderStroke(1.dp, DivnexCyan.copy(alpha = 0.5f))
        ) {
          Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = DivnexCyan, modifier = Modifier.size(18.dp))
          Spacer(Modifier.width(8.dp))
          Text("Inspect Sample Digital Experience Passport", color = DivnexCyan)
        }
      }
    }
  }
}

@Composable
private fun LiveMetricsSection() {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    MetricItem(
      value = "500+",
      label = "Verified Tasks",
      color = DivnexIndigo,
      modifier = Modifier.weight(1f)
    )
    MetricItem(
      value = "120+",
      label = "Tech Partners",
      color = DivnexCyan,
      modifier = Modifier.weight(1f)
    )
    MetricItem(
      value = "100%",
      label = "Evidence Vetted",
      color = DivnexEmerald,
      modifier = Modifier.weight(1f)
    )
  }
}

@Composable
private fun MetricItem(
  value: String,
  label: String,
  color: Color,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier,
    shape = RoundedCornerShape(12.dp),
    color = MaterialTheme.colorScheme.surface,
    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
  ) {
    Column(
      modifier = Modifier.padding(12.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = value,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
        color = color
      )
      Text(
        text = label,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}

@Composable
private fun HowItWorksSection() {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Text(
        text = "How Divnex Works",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onSurface
      )
      Text(
        text = "A continuous 4-stage proof-of-work pipeline",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
      Spacer(Modifier.height(14.dp))

      StepRow(stepNumber = "1", title = "Post Micro-Tasks", description = "Companies define practical, bite-sized tasks (2-8 hours) with clear acceptance criteria.")
      StepRow(stepNumber = "2", title = "Apply & Code", description = "Students apply with relevant skill tags, build real software, and submit verifiable GitHub & live links.")
      StepRow(stepNumber = "3", title = "Rubric Review", description = "Engineering leads evaluate code quality, architecture, and timeliness with written feedback.")
      StepRow(stepNumber = "4", title = "Mint Digital Passport", description = "Approved work mints a tamper-evident digital certificate that proves real-world capability.", isLast = true)
    }
  }
}

@Composable
private fun StepRow(
  stepNumber: String,
  title: String,
  description: String,
  isLast: Boolean = false
) {
  Row(modifier = Modifier.fillMaxWidth()) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Box(
        modifier = Modifier
          .size(28.dp)
          .background(DivnexIndigo, CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Text(text = stepNumber, color = Color.White, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
      }
      if (!isLast) {
        Box(
          modifier = Modifier
            .width(2.dp)
            .height(28.dp)
            .background(DivnexIndigo.copy(alpha = 0.3f))
        )
      }
    }
    Spacer(Modifier.width(12.dp))
    Column(modifier = Modifier.padding(bottom = if (isLast) 0.dp else 12.dp)) {
      Text(text = title, style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.onSurface)
      Text(text = description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
  }
}

@Composable
private fun BenefitsSection(
  onStudentClick: () -> Unit,
  onCompanyClick: () -> Unit
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    // Student Card
    Card(
      modifier = Modifier
        .weight(1f)
        .clickable { onStudentClick() },
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
      border = androidx.compose.foundation.BorderStroke(1.dp, DivnexIndigo.copy(alpha = 0.3f))
    ) {
      Column(modifier = Modifier.padding(12.dp)) {
        Icon(Icons.Default.School, contentDescription = null, tint = DivnexIndigo, modifier = Modifier.size(22.dp))
        Spacer(Modifier.height(6.dp))
        Text("For Freshers", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
        Spacer(Modifier.height(4.dp))
        Text(
          "• Real portfolio evidence\n• No whiteboard trivia\n• Employer endorsements",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    // Company Card
    Card(
      modifier = Modifier
        .weight(1f)
        .clickable { onCompanyClick() },
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
      border = androidx.compose.foundation.BorderStroke(1.dp, DivnexCyan.copy(alpha = 0.3f))
    ) {
      Column(modifier = Modifier.padding(12.dp)) {
        Icon(Icons.Default.Business, contentDescription = null, tint = DivnexCyan, modifier = Modifier.size(22.dp))
        Spacer(Modifier.height(6.dp))
        Text("For Companies", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
        Spacer(Modifier.height(4.dp))
        Text(
          "• Evaluate actual code\n• Scoped low-risk tasks\n• Pre-vetted talent pool",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
  }
}

@Composable
private fun FeaturedTaskCard(
  task: TaskEntity,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .testTag("featured_task_card_${task.id}"),
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
        Text(
          text = task.companyName,
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
          color = MaterialTheme.colorScheme.primary
        )
        StatusBadge(status = task.status)
      }
      Spacer(Modifier.height(6.dp))
      Text(
        text = task.title,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(Modifier.height(4.dp))
      Text(
        text = task.summary,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = 2
      )
      Spacer(Modifier.height(10.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        DifficultyBadge(difficulty = task.difficulty)
        Text(
          text = task.stipendOrReward,
          style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
          color = DivnexEmerald
        )
      }
    }
  }
}
