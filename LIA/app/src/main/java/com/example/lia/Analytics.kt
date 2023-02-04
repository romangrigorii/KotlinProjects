package com.example.lia

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.security.identity.CredentialDataResult.Entries
import android.util.Log
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.lia.databinding.ActivityAnalyticsBinding
import com.example.lia.databinding.ActivityHomeBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*

class Analytics : AppCompatActivity() {
    private var binding: ActivityAnalyticsBinding? = null
    //lateinit var classDao: ClassDao
    var email = ""
    var name = ""
    var id = 0
    var questions: String = "2"
    var answers: String = "2"
    var questionsArr: ArrayList<Float> = ArrayList<Float>()
    var answersArr: ArrayList<Float> = ArrayList<Float>()
    var answersArrx : ArrayList<Float> = ArrayList<Float>()
    var answersArry : ArrayList<Float> = ArrayList<Float>()
    var answersArrLPy : ArrayList<Float> = ArrayList<Float>()
    var entriesPoints = ArrayList<Entry>()
    var entriesLine = ArrayList<Entry>()
    var a = floatArrayOf(1.0F, 1.8227F, (-0.8372).toFloat()).toCollection(ArrayList())
    var b = floatArrayOf(0.00362F, 0.00724F, 0.003622F).toCollection(ArrayList())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalyticsBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        var classDao = (application as ClassApp).db.classDao()
        email = intent.getStringExtra("email").toString()
        name = intent.getStringExtra("name").toString()
        answers = intent.getStringExtra("answers").toString().substring(2)
        id = intent.getStringExtra("id")?.toInt()!!
        Toast.makeText(this@Analytics,answers,Toast.LENGTH_SHORT).show()
        PlotChart()
    }
    fun ParseString(input : String) : ArrayList<Float> {
        var values = ArrayList<Float>()
        for (it in input.split(" ")) {
            values.add(it.toFloat())
        }
        return values
    }

    fun ALFtoALE(inputx : ArrayList<Float>, inputy : ArrayList<Float>): ArrayList<Entry> {
        var entries: ArrayList<Entry> = ArrayList<Entry>()
        for (i in 0..inputy.size-1) {
            inputx.add((i+1).toFloat())
            entries.add(Entry(inputx[i],inputy[i]))
        }
        return entries
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    fun UpSample(vector: ArrayList<Float>, n: Int){
        for (i in 0..n*vector.size-1){
            answersArrx.add(i.toFloat()/n)
            answersArry.add(vector[i/n]*15)
            answersArrLPy.add(0f)
        }
    }

    fun LowPass(vector : ArrayList<Float>, n : Int, a: ArrayList<Float>, b:ArrayList<Float>){
        for (i in 2..n*vector.size-1){
            answersArrLPy[i] = +a[1]*answersArrLPy[i-1] +a[2]*answersArrLPy[i-2] + b[0]*answersArry[i] + b[1]*answersArry[i-1] + b[2]*answersArry[i-2]
        }
    }

    fun PlotChart(){
        answersArr = ParseString(answers)
        UpSample(answersArr,5)
        LowPass(answersArr, 5, a,b)
        entriesPoints = ALFtoALE(ArrayList(), answersArr)
        entriesLine = ALFtoALE(answersArrx,answersArrLPy)
        val v2 = LineDataSet(entriesLine, "My Type")
        v2.setDrawValues(false)
        v2.setDrawCircles(false)
        v2.lineWidth = 3f
        binding?.lineChart?.data = LineData(v2) // ScatterData(vl)
        binding?.lineChart?.axisRight?.isEnabled = false
        binding?.lineChart?.xAxis?.axisMaximum = if (answersArr.size>17) answersArr.size.toFloat()+3f else 20f
        binding?.lineChart?.xAxis?.axisMinimum = if (answersArr.size>17) answersArr.size.toFloat()-17f else 0f
        binding?.lineChart?.xAxis?.textSize = 12f
        binding?.lineChart?.xAxis?.position = XAxis.XAxisPosition.BOTTOM
        binding?.lineChart?.xAxis?.setDrawLabels(true)
        binding?.lineChart?.description?.text = ""
        binding?.lineChart?.description?.textSize = 15f
        binding?.lineChart?.legend?.setEnabled(false)
        binding?.lineChart?.axisLeft?.axisMaximum = if (answersArrLPy.max()<15) answersArrLPy.max()+1 else 16f
        binding?.lineChart?.axisLeft?.axisMinimum = -.5f
        binding?.lineChart?.axisLeft?.textSize = 12f
        binding?.lineChart?.setTouchEnabled(true)
        binding?.lineChart?.setPinchZoom(true)
        binding?.lineChart?.setTouchEnabled(true)
        binding?.lineChart?.setPinchZoom(true)
        binding?.lineChart?.animateX(1800, Easing.EaseInExpo)
    }

    @SuppressLint("ResourceAsColor")
    fun FillScores(){
        var numcorrect = answersArr.sum()
        var numcorrect15 = answersArr.subList(if(answersArr.size<15) 0 else answersArr.size-15, if(answersArr.size>15) answersArr.size else 15).sum()
        var siz =  answersArr.size
        var siz15 = if (siz>15) 15 else siz
        binding?.TVscore?.text = numcorrect.toString() + "/" + siz.toString() + " " + if (siz>0) String.format("%.2f",(numcorrect/siz).toString()) + "%" else ""
        binding?.TVscore15?.text = numcorrect15.toString() + "/" + siz15.toString() + " " + if (siz15>0) String.format("%.2f",(numcorrect15/siz15).toString()) + "%" else ""

        if (siz<30){
            binding?.TVstatus1?.setTextColor(R.color.col4)
        } else {
            when(siz){
                in 0..10 -> binding?.TVstatus1?.setTextColor(R.color.col4)
                in 11..13 -> binding?.TVstatus2?.setTextColor(R.color.col4)
                in 14..15 -> binding?.TVstatus2?.setTextColor(R.color.col4)
            }
        }
    }
}


