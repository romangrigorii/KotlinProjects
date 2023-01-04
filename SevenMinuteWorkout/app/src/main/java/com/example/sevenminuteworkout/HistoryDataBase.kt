package com.example.sevenminuteworkout

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HistoryEntity::class] , version = 1)
abstract class HistoryDataBase : RoomDatabase() {
    abstract fun historyDao() : HistoryDao
    companion object {
        @Volatile
        private var INSTANCE : HistoryDataBase? = null
        fun getInstance(context : Context) : HistoryDataBase {
            synchronized(this){ // this will return an instance of our database
                var instance = INSTANCE
                if (instance==null){ // if there is non data instance we build one
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        HistoryDataBase::class.java,
                        "history_database"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}