package com.example.sevenminuteworkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import com.example.sevenminuteworkout.databinding.ActivityEndScreenBinding

class endScreen : AppCompatActivity() {

    private var binding : ActivityEndScreenBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEndScreenBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolBarFinish) // link up the toolbar with a variable
        if (supportActionBar!=null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.toolBarFinish?.setNavigationOnClickListener{
            onBackPressed() // performs the back actions that can be performed with the button on the action bar of the phone
        }
        binding?.finishBut?.setOnClickListener {// binding now replaces findByID code
            finish()
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
        }
    }
}