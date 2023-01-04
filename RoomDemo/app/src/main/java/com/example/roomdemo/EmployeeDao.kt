package com.example.roomdemo

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// Dao is an interface which defines the functions that will query the database

@Dao
interface EmployeeDao {
    @Insert
    suspend fun insert (employeeEntity: EmployeeEntity)

    @Update
    suspend fun update (employeeEntity: EmployeeEntity)

    @Delete
    suspend fun delete (employeeEntity: EmployeeEntity)

    @Query("SELECT * FROM `employee-table`")
    fun fetchAllEmployees() : Flow<List<EmployeeEntity>>

    @Query("SELECT * FROM `employee-table` where id = :id")
    fun fetchEmployeeById(id:Int) : Flow<EmployeeEntity>

}