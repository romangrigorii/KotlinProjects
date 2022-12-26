package com.example.sevenminuteworkout

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isInvisible
import com.example.sevenminuteworkout.databinding.ActivityExerciseBinding
import java.net.URI
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var tts : TextToSpeech? = null
    private var binding : ActivityExerciseBinding? = null
    private var restTimer: CountDownTimer? = null
    private var actProgress = 0
    private var exerciseList : ArrayList<ExerciseModel>? = null
    private var currentExercise : Int = 1
    private var player: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolBarExercise)
        tts = TextToSpeech(this@ExerciseActivity, this)

        if (supportActionBar!=null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        exerciseList = constants.defaultExerciseList()
        binding?.toolBarExercise?.setNavigationOnClickListener{
            onBackPressed()
        }

//        binding?.Button?.setOnClickListener{ view ->
//            if(binding?.editScreen?.text!!.isEmpty()){
//                Toast.makeText(this@ExerciseActivity, "Enter a text to speak", Toast.LENGTH_SHORT).show()
//            } else {
//                speakOut(binding?.editScreen?.text.toString())
//            }
//        }

        setupRestView()
    }

    private fun setProgressBar(timeOnClock : Long, message : String, type : Int) {
        binding?.progressBar?.progress = actProgress
        binding?.progressBar?.max = (timeOnClock/100).toInt()
        restTimer = object : CountDownTimer(timeOnClock, 100) {
            override fun onTick(p0: Long) {
                actProgress++
                binding?.progressBar?.progress = ((timeOnClock/100).toInt() - actProgress)
                binding?.tvTimer?.text = ((((timeOnClock/100).toInt() - actProgress) / 10).toInt()).toString()
            }
            override fun onFinish() {
                Toast.makeText(this@ExerciseActivity, message, Toast.LENGTH_LONG).show()
                when(type){
                    0 -> setupExerciseView("Nothing")
                    1 -> setupRestView()
                }
            }
        }.start()
    }

    private fun setupRestView(){

        try {
            val soundURI = Uri.parse("android.resource://com.example.sevenminuteworkout/" + R.raw.iphone_text)
            player = MediaPlayer.create(applicationContext, soundURI)
            player?.isLooping = false
            player?.start()
        } catch (e: Exception){
            e.printStackTrace()
        }

        if (restTimer != null){
            restTimer?.cancel()
            actProgress = 0
            binding?.tvImage?.visibility = View.INVISIBLE
            binding?.tvExercise?.text = "Rest"
            binding?.tvTitle?.text = "Up next: ${exerciseList?.get(currentExercise)?.getName()}"
            speakOut("Take some rest. Up next is ${exerciseList?.get(currentExercise)?.getName()}")
        }
        setProgressBar(10000, "Begin exercise:",0) // the message is for onFinish and type for next type
    }

    private fun setupExerciseView(exerciseTitle : String){
        if (restTimer != null){
            restTimer?.cancel()
            actProgress = 0
            binding?.tvExercise?.text = exerciseList?.get(currentExercise)?.getName()
            binding?.tvTitle?.text = ""
            binding?.tvImage?.setImageResource(exerciseList!![currentExercise].getImage())
            binding?.tvImage?.visibility = View.VISIBLE
            speakOut("Begin ${exerciseList!![currentExercise].getName()}")
            currentExercise++
        }
        setProgressBar(30000, "You may rest now", 1)
    }

    override fun onDestroy() {
        super.onDestroy()
        restTimer?.cancel()
        actProgress = 0
        tts?.stop()
        tts?.shutdown()
        binding = null
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS){
            val result = tts!!.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result ==TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS", "The language is not supported")
            }
        } else Log.e("TTS", "Initialization Error")
    }

    private fun speakOut(text: String){
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }


}