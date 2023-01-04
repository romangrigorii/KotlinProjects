package com.example.roomdemo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Here we set up how data will be ordered within the columns
@Entity(tableName = "employee-table") // @Entity means that the next object is an Entity in Room
data class EmployeeEntity(
    @PrimaryKey(autoGenerate = true) // in this case id is the unique identifier
    val id : Int = 0,
    val name : String = "",
    @ColumnInfo(name = "email-id") // column info will hold the email
    val email : String = "",
)
