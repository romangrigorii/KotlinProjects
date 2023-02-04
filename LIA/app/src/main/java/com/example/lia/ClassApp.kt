package com.example.lia

import android.app.Application

class ClassApp : Application() {
    val db by lazy {
        ClassDataBase.getInstance(this) // this will create an instance of our database with which we can interact
    }
}