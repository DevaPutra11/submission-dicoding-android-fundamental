package com.deva.bangkit_submission1.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.deva.bangkit_submission1.data.local.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var instance: UserDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): UserDatabase {
            if (instance == null) {
                synchronized(UserDatabase::class.java) {
                    instance = Room.databaseBuilder(context.applicationContext,
                        UserDatabase::class.java, "user_database")
                        .build()
                }
            }
            return instance as UserDatabase
        }
    }
}