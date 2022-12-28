package com.example.sevenminuteworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sevenminuteworkout.databinding.ActivityExerciseBinding
import com.example.sevenminuteworkout.databinding.DialogCustomBackConfirmationBinding
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
    private var exerciseAdapter : ExerciseStatusAdapter? = null

    private val restTimerDur : Long = 10
    private val exerciseTimerDur : Long = 30

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolBarExercise) // link up the toolbar with a variable
        tts = TextToSpeech(this@ExerciseActivity, this) // sets up text to speech

        if (supportActionBar!=null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        exerciseList = constants.defaultExerciseList()
        binding?.toolBarExercise?.setNavigationOnClickListener{
            onBackPressed() // performs the back actions that can be performed with the button on the action bar of the phone
        }

        setupRestView()
        setupExerciseStatusRecyclerView()

    }

    override fun onBackPressed() {
        customDialogForBackButton()
    }
    private fun customDialogForBackButton() {
        val customDialog = Dialog(this)
        val dialogBinding = DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.btnYES.setOnClickListener {
            this@ExerciseActivity.finish()
            customDialog.dismiss()
            onBackPressed()
        }
        dialogBinding.btnNO.setOnClickListener {
            customDialog.dismiss()
        }
        customDialog.show()
    }

    private fun setupExerciseStatusRecyclerView(){
        binding?.tvExerciseStatus?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!) // assign an adapter to recycler view
        binding?.tvExerciseStatus?.adapter = exerciseAdapter
    }

    private fun setProgressBar(timeOnClock: Long, message: String, type: Int) {
        binding?.progressBar?.progress = actProgress ?: 0
        binding?.progressBar?.max = (timeOnClock/100).toInt() ?: 10
        restTimer = object : CountDownTimer(timeOnClock, 100) {
            override fun onTick(p0: Long) {
                actProgress++
                binding?.progressBar?.progress = ((timeOnClock/100).toInt() - actProgress)
                binding?.tvTimer?.text = ((((timeOnClock/100).toInt() - actProgress) / 10).toInt()).toString()
            }
            override fun onFinish() {
                Toast.makeText(this@ExerciseActivity, message, Toast.LENGTH_LONG).show()
                when(type){
                    0 -> {
                        exerciseList!![currentExercise-1].setIsSelected(true)
                        exerciseAdapter!!.notifyDataSetChanged()
                        setupExerciseView("Nothing")

                    }
                    1 -> {
                        exerciseList!![currentExercise-2].setIsSelected(false)
                        exerciseList!![currentExercise-2].setIsCompleted(true)
                        exerciseAdapter!!.notifyDataSetChanged()
                        setupRestView()
                    }
                }
                if (currentExercise == exerciseList?.size){
                    finish()
                    val intent = Intent(this@ExerciseActivity, endScreen::class.java)
                    startActivity(intent)
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
        setProgressBar(restTimerDur*1000, "Begin exercise:",0) // the message is for onFinish and type for next type
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
        setProgressBar(exerciseTimerDur*1000, "You may rest now", 1)
    }

    override fun onDestroy() {
        super.onDestroy()
        restTimer?.cancel()
        actProgress = 0
        tts?.stop()
        tts?.shutdown()
        binding = null
        player!!.stop()
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