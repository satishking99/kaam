package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [WorkerEntity::class, WorkRequestEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class KaamWalaDatabase : RoomDatabase() {
    abstract fun workerDao(): WorkerDao
    abstract fun workRequestDao(): WorkRequestDao

    companion object {
        @Volatile
        private var INSTANCE: KaamWalaDatabase? = null

        fun getDatabase(context: Context): KaamWalaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KaamWalaDatabase::class.java,
                    "kaamwala_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
