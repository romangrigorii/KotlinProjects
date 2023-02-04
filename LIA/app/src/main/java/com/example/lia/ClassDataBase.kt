package com.example.lia

import android.content.Context
import androidx.room.*


@Database(entities = [ClassEntity::class] , version = 1, exportSchema = false)
abstract class ClassDataBase : RoomDatabase() {
    abstract fun classDao() : ClassDao
    companion object {
        @Volatile // this basically means that the variable, when modified, is visible to other threads that it was modified
        private var INSTANCE : ClassDataBase? = null
        fun getInstance(context : Context) : ClassDataBase {
            synchronized(this){ // this will return an instance of our database
                var instance = INSTANCE
                if (instance==null){ // if there is non data instance we build one
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ClassDataBase::class.java,
                        "class_database"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}