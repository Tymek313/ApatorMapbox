package com.example.apatormapbox.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(), version = 1)
abstract class MainDatabase : RoomDatabase() {

    companion object {
        private lateinit var DATABASE: MainDatabase

        fun getDatabase(context: Context): MainDatabase {
            if (!::DATABASE.isInitialized) {
                synchronized(MainDatabase::class.java) {
                    DATABASE = Room.databaseBuilder(context, MainDatabase::class.java, "MainDatabase")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }

            return DATABASE
        }
    }
}