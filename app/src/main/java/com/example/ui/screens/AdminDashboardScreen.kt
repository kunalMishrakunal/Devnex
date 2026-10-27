package com.example.ui.screens

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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.DisputeEntity
import com.example.data.model.ReviewEntity
import com.example.data.model.TaskEntity
import com.example.data.model.UserEntity
import com.example.ui.BackendReadinessStatus
import com.example.ui.components.MetricCard
import com.example.ui.components.StatusBadge
import com.example.ui.theme.DivnexAmber
import com.example.ui.theme.DivnexCyan
import com.example.ui.theme.DivnexEmerald
import com.example.ui.theme.DivnexIndigo
import com.example.ui.theme.DivnexRose

@Composable
fun AdminDashboardScreen(
  users: List<UserEntity>,
  tasks: List<TaskEntity>,
  reviews: List<ReviewEntity>,
  disputes: List<DisputeEntity>,
  backendStatus: BackendReadinessStatus,
  onResolveDispute: (disputeId: String, resolutionNotes: String) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedDisputeToResolve by remember { mutableStateOf<DisputeEntity?>(null) }
  var resolutionNotes by remember { mutableStateOf("") }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    item {
      Spacer(Modifier.height(4.dp))
      Text(
        text = "Admin Governance & Audit",
        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black),
        color = MaterialTheme.colorScheme.onSurface
      )
      Text(
        text = "Monitor ecosystem integrity, resolve disputes, and audit verification credentials.",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }

    // Platform Metrics
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        MetricCard(
          title = "Registered Users",
          value = users.size.toString(),
          subtitle = "Students & Cos",
          icon = Icons.Default.VerifiedUser,
          accentColor = DivnexIndigo,
          modifier = Modifier.weight(1f)
        )
        MetricCard(
          title = "Total Tasks",
          value = tasks.size.toString(),
          subtitle = "Across all stages",
          icon = Icons.Default.Storage,
          accentColor = DivnexCyan,
          modifier = Modifier.weight(1f)
        )
        MetricCard(
          title = "Verified Mints",
          value = reviews.size.toString(),
          subtitle = "Passport badges",
          icon = Icons.Default.CheckCircle,
          accentColor = DivnexEmerald,
          modifier = Modifier.weight(1f)
        )
      }
    }

    // Backend Readiness Card
    item {
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
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Security, contentDescription = null, tint = DivnexCyan)
              Spacer(Modifier.width(8.dp))
              Text("Backend Infrastructure Audit", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            }
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = DivnexEmerald.copy(alpha = 0.15f)
            ) {
              Text(
                text = "ONLINE",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = DivnexEmerald,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
              )
            }
          }

          Spacer(Modifier.height(8.dp))
          Text(
            text = "• Persistence Engine: Active Local Room Database (divnex_database)\n• Security Model: Enforced state transitions (Submissions locked until Company review)\n• Cloud Remote Sync: Ready for Firebase integration without code rewrites",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }

    // Disputes Section
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Dispute Mediation & Appeals (${disputes.size})",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onSurface
        )
      }
    }

    if (disputes.isEmpty()) {
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(Icons.Default.Gavel, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(6.dp))
            Text("No active platform disputes", style = MaterialTheme.typography.bodyMedium)
          }
        }
      }
    } else {
      items(disputes) { dispute ->
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (dispute.status == "OPEN") DivnexAmber.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
          )
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Task: ${dispute.taskTitle}",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
              )
              Surface(
                shape = RoundedCornerShape(6.dp),
                color = if (dispute.status == "OPEN") DivnexAmber.copy(alpha = 0.15f) else DivnexEmerald.copy(alpha = 0.15f)
              ) {
                Text(
                  text = dispute.status,
                  style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                  color = if (dispute.status == "OPEN") DivnexAmber else DivnexEmerald,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }

            Spacer(Modifier.height(6.dp))
            Text(
              text = "Raised by: ${dispute.raisedByName} (${dispute.raisedByRole.name})",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.primary
            )
            Text(
              text = "Reason: ${dispute.reason}",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (dispute.adminResolutionNotes != null) {
              Spacer(Modifier.height(6.dp))
              Surface(
                shape = RoundedCornerShape(6.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
              ) {
                Text(
                  text = "Resolution: ${dispute.adminResolutionNotes}",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurface,
                  modifier = Modifier.padding(8.dp)
                )
              }
            } else if (dispute.status == "OPEN") {
              Spacer(Modifier.height(10.dp))
              Button(
                onClick = {
                  selectedDisputeToResolve = dispute
                  resolutionNotes = "Audited evidence and verified compliance. Outcome signed off by Admin."
                },
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("resolve_dispute_btn_${dispute.id}"),
                colors = ButtonDefaults.buttonColors(containerColor = DivnexIndigo)
              ) {
                Text("Resolve Dispute")
              }
            }
          }
        }
      }
    }

    // Task Verification Audit Feed
    item {
      Spacer(Modifier.height(6.dp))
      Text(
        text = "Audit Log: Recent Task Lifecycles",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onSurface
      )
    }

    items(tasks) { task ->
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(text = task.title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold))
            Text(text = "${task.companyName} • ${task.category}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
          StatusBadge(status = task.status)
        }
      }
    }

    item {
      Spacer(Modifier.height(24.dp))
    }
  }

  // Resolve Dispute Dialog
  if (selectedDisputeToResolve != null) {
    AlertDialog(
      onDismissRequest = { selectedDisputeToResolve = null },
      title = { Text("Resolve Dispute") },
      text = {
        Column {
          Text("Provide official moderation decision for: ${selectedDisputeToResolve!!.taskTitle}", style = MaterialTheme.typography.bodySmall)
          Spacer(Modifier.height(10.dp))
          OutlinedTextField(
            value = resolutionNotes,
            onValueChange = { resolutionNotes = it },
            label = { Text("Resolution Notes") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            onResolveDispute(selectedDisputeToResolve!!.id, resolutionNotes)
            selectedDisputeToResolve = null
          }
        ) {
          Text("Confirm Decision")
        }
      },
      dismissButton = {
        OutlinedButton(onClick = { selectedDisputeToResolve = null }) {
          Text("Cancel")
        }
      }
    )
  }
}
