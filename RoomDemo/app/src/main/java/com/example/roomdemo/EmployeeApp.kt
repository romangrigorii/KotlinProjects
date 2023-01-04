package com.example.roomdemo

import android.app.Application

class EmployeeApp : Application() {
    val db by lazy {
        EmployeeDataBase.getInstance(this)
    }
}