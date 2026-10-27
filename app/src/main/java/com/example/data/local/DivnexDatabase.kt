package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.model.ApplicationEntity
import com.example.data.model.DisputeEntity
import com.example.data.model.ReviewEntity
import com.example.data.model.SubmissionEntity
import com.example.data.model.TaskEntity
import com.example.data.model.UserEntity

@Database(
  entities = [
    UserEntity::class,
    TaskEntity::class,
    ApplicationEntity::class,
    SubmissionEntity::class,
    ReviewEntity::class,
    DisputeEntity::class
  ],
  version = 1,
  exportSchema = false
)
@TypeConverters(Converters::class)
abstract class DivnexDatabase : RoomDatabase() {
  abstract fun divnexDao(): DivnexDao

  companion object {
    @Volatile
    private var INSTANCE: DivnexDatabase? = null

    fun getDatabase(context: Context): DivnexDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          DivnexDatabase::class.java,
          "divnex_database"
        ).build()
        INSTANCE = instance
        instance
      }
    }
  }
}
