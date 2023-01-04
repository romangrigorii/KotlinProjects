package com.example.sevenminuteworkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.sevenminuteworkout.databinding.ActivityEndScreenBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class endScreen : AppCompatActivity() {

    private var binding : ActivityEndScreenBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEndScreenBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolBarFinish) // link up the toolbar with a variable
        if (supportActionBar==null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.toolBarFinish?.setNavigationOnClickListener{
            onBackPressed() // performs the back actions that can be performed with the button on the action bar of the phone
        }
        binding?.finishBut?.setOnClickListener {// binding now replaces findByID code
            finish()
        }
        val dao = (application as WorkOutApp).db.historyDao() // this sets up the Dao
        addDateToDatBase(dao)

    }

    private fun addDateToDatBase(historyDao: HistoryDao){
        // create the date as an id
        val c = Calendar.getInstance()
        val dateTime = c.time
        // Log.e("Date: ", "" + dateTime)
        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
        val date = sdf.format(dateTime)
        // Log.e("Date: ", "" + date)
        // add the exercise with the date as an unique id
        lifecycleScope.launch { // this launches the coroutine
            historyDao.insert(HistoryEntity(date))
        }
    }
}