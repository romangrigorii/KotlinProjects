package com.example.roomdemo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Besides Entity and Dao we need the Database - which will actually store all the data

@Database(entities = [EmployeeEntity::class] , version = 1)
abstract class EmployeeDataBase : RoomDatabase() {
    abstract fun employeeDao() : EmployeeDao
    companion object {
        @Volatile
        private var INSTANCE : EmployeeDataBase? = null
        fun getInstance(context : Context) : EmployeeDataBase {
            synchronized(this){ // this will return an instance of our database
                var instance = INSTANCE
                if (instance==null){ // if there is non data instance we build one
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        EmployeeDataBase::class.java,
                        "employee_database"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}