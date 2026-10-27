package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.model.TaskStatus
import com.example.data.model.UserRole

class Converters {
  @TypeConverter
  fun fromUserRole(role: UserRole?): String? = role?.name

  @TypeConverter
  fun toUserRole(value: String?): UserRole? = value?.let { enumValueOf<UserRole>(it) }

  @TypeConverter
  fun fromTaskStatus(status: TaskStatus?): String? = status?.name

  @TypeConverter
  fun toTaskStatus(value: String?): TaskStatus? = value?.let { enumValueOf<TaskStatus>(it) }
}
