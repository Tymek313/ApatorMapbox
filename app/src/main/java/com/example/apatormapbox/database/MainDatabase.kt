package com.example.apatormapbox.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.apatormapbox.database.dao.StationDao
import com.example.apatormapbox.models.dbentities.StationBasicEntity

@Database(entities = [StationBasicEntity::class], version = 1, exportSchema = false)
abstract class MainDatabase : RoomDatabase() {

    abstract fun stationDao(): StationDao

    companion object {
        private var DATABASE: MainDatabase? = null


        fun getDatabase(context: Context): MainDatabase? {
            if (DATABASE == null) {
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