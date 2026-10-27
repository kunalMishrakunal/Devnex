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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ReviewEntity
import com.example.data.model.SubmissionEntity
import com.example.data.model.UserEntity
import com.example.ui.components.SkillChip
import com.example.ui.theme.DivnexAmber
import com.example.ui.theme.DivnexCyan
import com.example.ui.theme.DivnexEmerald
import com.example.ui.theme.DivnexIndigo

@Composable
fun PassportScreen(
  user: UserEntity?,
  reviews: List<ReviewEntity>,
  submissions: List<SubmissionEntity>,
  onCopyPassportLink: () -> Unit,
  modifier: Modifier = Modifier
) {
  var isPublicProfileShared by remember { mutableStateOf(true) }

  // Student reviews
  val studentReviews = if (user != null) {
    reviews.filter { it.studentId == user.id }
  } else {
    reviews
  }

  val totalVerifiedHours = studentReviews.size * 6 // 6 hours average per micro-task
  val averageScore = if (studentReviews.isNotEmpty()) {
    (studentReviews.map { it.overallRating }.average() * 10).toInt() / 10.0
  } else {
    4.8
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    item {
      Spacer(Modifier.height(4.dp))
      Text(
        text = "Digital Experience Passport",
        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black),
        color = MaterialTheme.colorScheme.onSurface
      )
      Text(
        text = "Verifiable proof-of-work, validated deliverables, and cryptographic endorsements.",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }

    // High-Trust Passport Credential Card
    item {
      PassportCredentialCard(
        user = user,
        verifiedCount = studentReviews.size,
        avgScore = averageScore,
        totalHours = totalVerifiedHours
      )
    }

    // Share & Privacy Toggle Bar
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
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
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Authorized Public Portfolio",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = if (isPublicProfileShared) "Public profile URL is live & verifiable by employers." else "Portfolio is private.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
            Switch(
              checked = isPublicProfileShared,
              onCheckedChange = { isPublicProfileShared = it },
              modifier = Modifier.testTag("privacy_share_toggle")
            )
          }

          if (isPublicProfileShared) {
            Spacer(Modifier.height(10.dp))
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = "https://divnex.io/passport/alex-chen",
                  style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                  color = DivnexCyan
                )
                OutlinedButton(
                  onClick = onCopyPassportLink,
                  modifier = Modifier.testTag("copy_passport_link_btn")
                ) {
                  Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                  Spacer(Modifier.width(4.dp))
                  Text("Copy Link")
                }
              }
            }
          }
        }
      }
    }

    // Verified Records Section Header
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Verified Practical Records (${studentReviews.size})",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onSurface
        )
      }
    }

    // List of Verified Work Records
    if (studentReviews.isEmpty()) {
      item {
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
            Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = DivnexCyan, modifier = Modifier.size(36.dp))
            Spacer(Modifier.height(8.dp))
            Text("No Verified Records Yet", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
            Text(
              "Complete an active micro-task deliverable and receive company sign-off to mint your first verified passport credential.",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              textAlign = TextAlign.Center
            )
          }
        }
      }
    } else {
      items(studentReviews) { review ->
        val sub = submissions.firstOrNull { it.id == review.submissionId }
        VerifiedRecordItemCard(review = review, submission = sub)
      }
    }

    item {
      Spacer(Modifier.height(24.dp))
    }
  }
}

@Composable
private fun PassportCredentialCard(
  user: UserEntity?,
  verifiedCount: Int,
  avgScore: Double,
  totalHours: Int
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("passport_credential_card"),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    border = androidx.compose.foundation.BorderStroke(1.5.dp, DivnexCyan.copy(alpha = 0.6f))
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          brush = Brush.verticalGradient(
            colors = listOf(
              DivnexIndigo.copy(alpha = 0.15f),
              DivnexCyan.copy(alpha = 0.08f),
              Color.Transparent
            )
          )
        )
        .padding(18.dp)
    ) {
      Column {
        // Top credential header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = DivnexCyan, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(6.dp))
            Text(
              text = "DIVNEX VERIFIED PASSPORT",
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black, letterSpacing = 1.sp),
              color = DivnexCyan
            )
          }

          Icon(
            imageVector = Icons.Default.QrCode,
            contentDescription = "Passport QR",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
          )
        }

        Spacer(Modifier.height(14.dp))

        // Student Info
        Text(
          text = user?.name ?: "Alex Chen",
          style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
          color = MaterialTheme.colorScheme.onSurface
        )
        Text(
          text = user?.headline ?: "Mobile & Systems Fresher | CS Graduate",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(14.dp))

        // Metric Pill row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          CredentialPill(
            label = "Rating",
            value = "★ $avgScore / 5.0",
            color = DivnexAmber,
            modifier = Modifier.weight(1f)
          )
          CredentialPill(
            label = "Verified Tasks",
            value = "$verifiedCount Projects",
            color = DivnexCyan,
            modifier = Modifier.weight(1f)
          )
          CredentialPill(
            label = "Total Effort",
            value = "$totalHours Hours",
            color = DivnexEmerald,
            modifier = Modifier.weight(1f)
          )
        }

        Spacer(Modifier.height(14.dp))

        // Demonstrated Skills
        Text(
          text = "Validated Competencies",
          style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(6.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          val skills = listOf("Kotlin", "Jetpack Compose", "Room DB", "Canvas API", "REST")
          skills.forEach { skill ->
            SkillChip(skill = skill)
          }
        }
      }
    }
  }
}

@Composable
private fun CredentialPill(
  label: String,
  value: String,
  color: Color,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier,
    shape = RoundedCornerShape(10.dp),
    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
    border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.3f))
  ) {
    Column(
      modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
      Text(text = value, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = color)
    }
  }
}

@Composable
private fun VerifiedRecordItemCard(
  review: ReviewEntity,
  submission: SubmissionEntity?
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("verified_record_${review.id}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = androidx.compose.foundation.BorderStroke(1.dp, DivnexEmerald.copy(alpha = 0.4f))
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = review.taskTitle,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "Endorsed by ${review.companyName}",
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
            color = DivnexCyan
          )
        }
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = DivnexEmerald.copy(alpha = 0.12f),
          border = androidx.compose.foundation.BorderStroke(1.dp, DivnexEmerald.copy(alpha = 0.35f))
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = DivnexEmerald, modifier = Modifier.size(12.dp))
            Spacer(Modifier.width(4.dp))
            Text("VERIFIED", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black), color = DivnexEmerald)
          }
        }
      }

      Spacer(Modifier.height(10.dp))

      // Rubric Scores Grid
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text("Code Quality: ${review.codeQualityRating}/5", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text("Timeliness: ${review.timelinessRating}/5", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text("Communication: ${review.communicationRating}/5", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
      }

      Spacer(Modifier.height(10.dp))

      // Written Employer Feedback Quote
      Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          Text(
            text = "\"${review.writtenFeedback}\"",
            style = MaterialTheme.typography.bodyMedium.copy(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic),
            color = MaterialTheme.colorScheme.onSurface
          )
        }
      }

      // Deliverable Evidence Links
      if (submission != null) {
        Spacer(Modifier.height(10.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
            modifier = Modifier.weight(1f)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(Icons.Default.Code, contentDescription = null, tint = DivnexIndigo, modifier = Modifier.size(14.dp))
              Spacer(Modifier.width(4.dp))
              Text(
                text = submission.githubUrl,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1
              )
            }
          }
          if (submission.liveDemoUrl.isNotBlank()) {
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
              modifier = Modifier.weight(1f)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(Icons.Default.Link, contentDescription = null, tint = DivnexCyan, modifier = Modifier.size(14.dp))
                Spacer(Modifier.width(4.dp))
                Text(
                  text = submission.liveDemoUrl,
                  style = MaterialTheme.typography.labelSmall,
                  color = DivnexCyan,
                  maxLines = 1
                )
              }
            }
          }
        }
      }

      Spacer(Modifier.height(12.dp))

      // Cryptographic verification fingerprint
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Fingerprint, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(14.dp))
          Spacer(Modifier.width(4.dp))
          Text(
            text = review.verificationHash,
            style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }
  }
}
