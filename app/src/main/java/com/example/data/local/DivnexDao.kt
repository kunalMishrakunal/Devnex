package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ApplicationEntity
import com.example.data.model.DisputeEntity
import com.example.data.model.ReviewEntity
import com.example.data.model.SubmissionEntity
import com.example.data.model.TaskEntity
import com.example.data.model.TaskStatus
import com.example.data.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DivnexDao {

  // Users
  @Query("SELECT * FROM users")
  fun getAllUsers(): Flow<List<UserEntity>>

  @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
  fun getUserById(userId: String): Flow<UserEntity?>

  @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
  suspend fun getUserByIdOnce(userId: String): UserEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertUsers(users: List<UserEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertUser(user: UserEntity)

  // Tasks
  @Query("SELECT * FROM tasks ORDER BY createdAtEpochMs DESC")
  fun getAllTasks(): Flow<List<TaskEntity>>

  @Query("SELECT * FROM tasks WHERE id = :taskId LIMIT 1")
  fun getTaskById(taskId: String): Flow<TaskEntity?>

  @Query("SELECT * FROM tasks WHERE id = :taskId LIMIT 1")
  suspend fun getTaskByIdOnce(taskId: String): TaskEntity?

  @Query("SELECT * FROM tasks WHERE companyId = :companyId ORDER BY createdAtEpochMs DESC")
  fun getTasksForCompany(companyId: String): Flow<List<TaskEntity>>

  @Query("SELECT * FROM tasks WHERE assignedStudentId = :studentId ORDER BY createdAtEpochMs DESC")
  fun getTasksForStudent(studentId: String): Flow<List<TaskEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertTask(task: TaskEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertTasks(tasks: List<TaskEntity>)

  @Update
  suspend fun updateTask(task: TaskEntity)

  @Query("UPDATE tasks SET status = :status WHERE id = :taskId")
  suspend fun updateTaskStatus(taskId: String, status: TaskStatus)

  @Query("UPDATE tasks SET assignedStudentId = :studentId, assignedStudentName = :studentName, status = :status WHERE id = :taskId")
  suspend fun assignTaskToStudent(taskId: String, studentId: String, studentName: String, status: TaskStatus = TaskStatus.ASSIGNED)

  // Applications
  @Query("SELECT * FROM applications ORDER BY appliedAtEpochMs DESC")
  fun getAllApplications(): Flow<List<ApplicationEntity>>

  @Query("SELECT * FROM applications WHERE taskId = :taskId")
  fun getApplicationsForTask(taskId: String): Flow<List<ApplicationEntity>>

  @Query("SELECT * FROM applications WHERE studentId = :studentId")
  fun getApplicationsForStudent(studentId: String): Flow<List<ApplicationEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertApplication(app: ApplicationEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertApplications(apps: List<ApplicationEntity>)

  @Query("UPDATE applications SET status = :status WHERE id = :appId")
  suspend fun updateApplicationStatus(appId: String, status: String)

  // Submissions
  @Query("SELECT * FROM submissions ORDER BY submittedAtEpochMs DESC")
  fun getAllSubmissions(): Flow<List<SubmissionEntity>>

  @Query("SELECT * FROM submissions WHERE taskId = :taskId LIMIT 1")
  fun getSubmissionForTask(taskId: String): Flow<SubmissionEntity?>

  @Query("SELECT * FROM submissions WHERE taskId = :taskId LIMIT 1")
  suspend fun getSubmissionForTaskOnce(taskId: String): SubmissionEntity?

  @Query("SELECT * FROM submissions WHERE studentId = :studentId ORDER BY submittedAtEpochMs DESC")
  fun getSubmissionsForStudent(studentId: String): Flow<List<SubmissionEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertSubmission(submission: SubmissionEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertSubmissions(submissions: List<SubmissionEntity>)

  @Update
  suspend fun updateSubmission(submission: SubmissionEntity)

  // Reviews (Verified Passport Records)
  @Query("SELECT * FROM reviews ORDER BY verifiedTimestampEpochMs DESC")
  fun getAllReviews(): Flow<List<ReviewEntity>>

  @Query("SELECT * FROM reviews WHERE studentId = :studentId ORDER BY verifiedTimestampEpochMs DESC")
  fun getReviewsForStudent(studentId: String): Flow<List<ReviewEntity>>

  @Query("SELECT * FROM reviews WHERE companyId = :companyId ORDER BY verifiedTimestampEpochMs DESC")
  fun getReviewsFromCompany(companyId: String): Flow<List<ReviewEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertReview(review: ReviewEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertReviews(reviews: List<ReviewEntity>)

  // Disputes
  @Query("SELECT * FROM disputes ORDER BY createdAtEpochMs DESC")
  fun getAllDisputes(): Flow<List<DisputeEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertDispute(dispute: DisputeEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertDisputes(disputes: List<DisputeEntity>)

  @Query("UPDATE disputes SET status = :status, adminResolutionNotes = :resolutionNotes WHERE id = :disputeId")
  suspend fun resolveDispute(disputeId: String, status: String, resolutionNotes: String)
}
