package com.example.sevenminuteworkout

import android.app.Application

class WorkOutApp : Application() {
    val db by lazy {
        HistoryDataBase.getInstance(this)
    }
}