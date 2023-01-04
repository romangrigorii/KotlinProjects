package com.example.countdown

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import com.example.countdown.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding : ActivityMainBinding? = null // this binds the activity to xls
    private var countDownTimer: CountDownTimer? = null
    private var timerDuration : Long = 60000
    private var pauseOffset: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)

        binding?.tvtext?.text = "${(timerDuration/1000).toString()}"
        binding?.startbut?.setOnClickListener{startTimer(pauseOffset)} // pass the time elapsed
        binding?.pausebut?.setOnClickListener{pauseTimer()}
        binding?.resetbut?.setOnClickListener{resetTimer()}

    }

    private fun resetTimer() {
        if (countDownTimer != null){
            countDownTimer!!.cancel()
            binding?.tvtext?.text = "${(timerDuration/1000).toString()}"
            countDownTimer = null
            pauseOffset = 0L
            Toast.makeText(this@MainActivity, "reset",Toast.LENGTH_SHORT).show()
        } else Toast.makeText(this@MainActivity, "no time to reset",Toast.LENGTH_SHORT).show()

    }

    private fun pauseTimer(){
        if (countDownTimer != null){
            countDownTimer!!.cancel()
            Toast.makeText(this@MainActivity, "timer paused",Toast.LENGTH_SHORT).show()
        } else Toast.makeText(this@MainActivity, "no timer to pause",Toast.LENGTH_SHORT).show()
    }

    private fun startTimer(pauseOffsetL: Long) {
        countDownTimer = object : CountDownTimer(timerDuration - pauseOffsetL, 1000){
            override fun onTick(millisUntilFinished : Long){ // onTick is what is called every countdown interval
                pauseOffset = timerDuration - millisUntilFinished
                binding?.tvtext?.text = (millisUntilFinished/1000).toString()
            }
            override fun onFinish(){
                Toast.makeText(this@MainActivity, "Timer is finished", Toast.LENGTH_LONG).show()
            }
        }.start()
        Toast.makeText(this@MainActivity, "started",Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}