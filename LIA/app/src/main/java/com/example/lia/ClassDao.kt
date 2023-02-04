package com.example.lia

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ClassDao {
    @Insert
    suspend fun insert (classEntity: ClassEntity)

    @Update
    suspend fun update (classEntity: ClassEntity)

    @Delete
    suspend fun delete (classEntity: ClassEntity)

    @Query("SELECT * FROM `class-table`")
    fun fetchAllClasses() : Flow<List<ClassEntity>>

    @Query("SELECT * FROM `class-table` where id= :id")
    fun fetchClassById(id:Int) : Flow<ClassEntity>
}