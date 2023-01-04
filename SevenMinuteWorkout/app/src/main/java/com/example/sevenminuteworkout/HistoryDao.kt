package com.example.sevenminuteworkout

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// Dao is an interface which defines the functions that will query the database

@Dao
interface HistoryDao {
    @Insert // this is only used to insert data into the database
    suspend fun insert (employeeEntity: HistoryEntity)

    @Query("SELECT * FROM `history-table`") // this will retrieve all of the data from the database
    fun fetchAllDates():Flow<List<HistoryEntity>>
}