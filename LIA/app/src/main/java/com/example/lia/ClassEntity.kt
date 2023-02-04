package com.example.lia

import androidx.room.*
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson


@Entity(tableName = "class-table") // @Entity means that the next object is an Entity in Room
data class ClassEntity(
    @PrimaryKey(autoGenerate = true)
    var classid : Int = 0,
    //@ColumnInfo(name = "name")
    var name : String = "",
    var id : Int = 0,
    @ColumnInfo(name = "subscription")
    var subscription : Int = 0,
    @ColumnInfo(name = "answers_correct")
    var answers_correct : String = "1",
    @ColumnInfo(name = "questions_total")
    var questions_total : String = "1",
)
