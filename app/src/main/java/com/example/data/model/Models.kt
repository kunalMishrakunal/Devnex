package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class UserRole {
  STUDENT,
  COMPANY,
  ADMIN
}

enum class TaskStatus(val displayName: String) {
  DRAFT("Draft"),
  PUBLISHED("Open for Application"),
  APPLIED("Applied"),
  ASSIGNED("In Progress"),
  SUBMITTED("Submitted for Review"),
  REVISION_REQUESTED("Revision Requested"),
  APPROVED("Verified & Approved"),
  REJECTED("Rejected")
}

@Entity(tableName = "users")
data class UserEntity(
  @PrimaryKey val id: String,
  val name: String,
  val email: String,
  val role: UserRole,
  val avatarUrl: String = "",
  val headline: String,
  val bio: String,
  val skills: String = "", // Comma-separated
  val companyName: String = "",
  val companyWebsite: String = "",
  val isVerified: Boolean = true
)

@Entity(tableName = "tasks")
data class TaskEntity(
  @PrimaryKey val id: String,
  val companyId: String,
  val companyName: String,
  val title: String,
  val summary: String,
  val description: String,
  val deliverablesCriteria: String,
  val category: String, // Android, Backend, Frontend, UI/UX, AI/Data
  val difficulty: String, // Beginner, Intermediate, Advanced
  val skillsRequired: String, // Comma-separated
  val estimatedHours: Int,
  val stipendOrReward: String,
  val deadlineDays: Int,
  val status: TaskStatus = TaskStatus.PUBLISHED,
  val assignedStudentId: String? = null,
  val assignedStudentName: String? = null,
  val createdAtEpochMs: Long = System.currentTimeMillis()
)

@Entity(tableName = "applications")
data class ApplicationEntity(
  @PrimaryKey val id: String,
  val taskId: String,
  val taskTitle: String,
  val studentId: String,
  val studentName: String,
  val studentHeadline: String,
  val coverNote: String,
  val portfolioUrl: String,
  val status: String = "PENDING", // PENDING, ACCEPTED, REJECTED
  val appliedAtEpochMs: Long = System.currentTimeMillis()
)

@Entity(tableName = "submissions")
data class SubmissionEntity(
  @PrimaryKey val id: String,
  val taskId: String,
  val taskTitle: String,
  val studentId: String,
  val studentName: String,
  val githubUrl: String,
  val liveDemoUrl: String,
  val notes: String,
  val status: TaskStatus = TaskStatus.SUBMITTED,
  val revisionFeedback: String? = null,
  val submittedAtEpochMs: Long = System.currentTimeMillis(),
  val reviewedAtEpochMs: Long? = null
)

@Entity(tableName = "reviews")
data class ReviewEntity(
  @PrimaryKey val id: String,
  val taskId: String,
  val submissionId: String,
  val companyId: String,
  val companyName: String,
  val studentId: String,
  val studentName: String,
  val taskTitle: String,
  val codeQualityRating: Int, // 1-5
  val timelinessRating: Int, // 1-5
  val communicationRating: Int, // 1-5
  val overallRating: Double,
  val writtenFeedback: String,
  val verificationHash: String,
  val verifiedTimestampEpochMs: Long = System.currentTimeMillis()
)

@Entity(tableName = "disputes")
data class DisputeEntity(
  @PrimaryKey val id: String,
  val taskId: String,
  val taskTitle: String,
  val raisedByRole: UserRole,
  val raisedById: String,
  val raisedByName: String,
  val reason: String,
  val status: String = "OPEN", // OPEN, RESOLVED
  val adminResolutionNotes: String? = null,
  val createdAtEpochMs: Long = System.currentTimeMillis()
)
