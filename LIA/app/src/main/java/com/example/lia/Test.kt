package com.example.lia

import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.lia.databinding.ActivityTestBinding
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
//import com.google.android.gms.ads.*
//import com.google.android.gms.ads.rewarded.RewardItem
//import com.google.android.gms.ads.rewarded.RewardedAd
//import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import java.util.*
import kotlin.collections.ArrayList

class Test : AppCompatActivity() {

    private var binding : ActivityTestBinding? = null
    private var commit = ArrayList<Int>(Collections.nCopies(4, 0))
    private var maxCommitSet = false
    var actProgress : Int = 0
    var maxDur : Long = 90L
    private var aTimer: CountDownTimer? = null
    var q : Int = 0
    private var TAG = "HomeActivity" // ad lines
    private var question : String = ""
    private var answered : Int = 0
    lateinit var classDao : ClassDao
    var id : Int = 0
    lateinit var entity : ClassEntity
    var subscribed : Int = 0
    private var mRewardedAd: RewardedAd? = null
    var name : String = "Jon Snow"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        id = intent.getIntExtra("id",0)
        classDao = (application as ClassApp).db.classDao()
        lifecycleScope.launch {
            classDao.fetchClassById(id).collect { it ->
                if (it != null) {
                    entity = it
                    subscribed = 1
                }
            }
        }
        setSupportActionBar(binding?.toolBar) // link up the toolbar with a variable
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding?.toolBar?.setNavigationOnClickListener {
            onBackPressed() // performs the back actions that can be performed with the button on the action bar of the phone
        }
        binding?.toolBar?.title = classList[id!!].topic + " test"
        setQuestion(q)
        binding?.clA1?.setOnClickListener{
            answered = 1
            if (!maxCommitSet) {
                resetAnswers(1)
            }
            if (commit[0] == 0) {
                binding?.clA1?.background = getDrawable(R.drawable.answer_background_selected)
                commit[0] = 1
            }
            else if (commit[0] == 1){
                binding?.clA1?.background =
                    getDrawable(R.drawable.answer_background_incorrect)
                setCorrect()
                maxCommits()
                }
            }

        binding?.clA2?.setOnClickListener{
            answered = 2
            if (!maxCommitSet) {
                resetAnswers(2)
            }
            if (commit[1] == 0) {
                binding?.clA2?.background = getDrawable(R.drawable.answer_background_selected)
                commit[1] = 1
            }
            else if (commit[1] == 1){
                binding?.clA2?.background =
                    getDrawable(R.drawable.answer_background_incorrect)
                setCorrect()
                maxCommits()
            }
        }

        binding?.clA3?.setOnClickListener{
            answered = 3
            if (!maxCommitSet) {
                resetAnswers(3)
            }
            if (commit[2] == 0) {
                binding?.clA3?.background = getDrawable(R.drawable.answer_background_selected)
                commit[2] = 1
            }
            else if(commit[2] == 1){
                binding?.clA3?.background =
                    getDrawable(R.drawable.answer_background_incorrect)
                setCorrect()
                maxCommits()
            }
        }

        binding?.clA4?.setOnClickListener{
            answered = 4
            if (!maxCommitSet) {
                resetAnswers(4)
            }
            if (commit[3] == 0) {
                binding?.clA4?.background = getDrawable(R.drawable.answer_background_selected)
                commit[3] = 1
            }
            else if (commit[3] == 1){
                //if (classList[id!!].subscription == 1) {
                binding?.clA4?.background =
                    getDrawable(R.drawable.answer_background_incorrect)
                setCorrect()
                maxCommits()
            }
        }

        binding?.butNextQuestion?.setOnClickListener{
            q++
            if (q<allQuestions.size && (classList[id].subscription==1 || (classList[id].subscription==0 && q<Qlimit))) {
                setQuestion(q)
            } else {
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("name",name)
                QuestionsReset()
                if (q >= Qlimit) {
                    customDialogFunction(intent, id)
                    // onBackPressed()
                }
                else if (q==allQuestions.size) {
                    startActivity(intent)
                }
            }
        }

        var adRequest = AdRequest.Builder().build()
        RewardedAd.load(this,"ca-app-pub-3940256099942544/5224354917", adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                adError?.toString()?.let { Log.d(TAG, it) }
                mRewardedAd = null
            }
            override fun onAdLoaded(rewardedAd: RewardedAd) {
                Log.d(TAG, "Ad was loaded.")
                mRewardedAd = rewardedAd
            }
        })

        mRewardedAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdClicked() {
                // Called when a click is recorded for an ad.
                Log.d(TAG, "Ad was clicked.")
            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                Log.d(TAG, "Ad dismissed fullscreen content.")
                mRewardedAd = null
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                // Called when ad fails to show.
                Log.e(TAG, "Ad failed to show fullscreen content.")
                mRewardedAd = null
            }

            override fun onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d(TAG, "Ad recorded an impression.")
            }

            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad showed fullscreen content.")
            }
        }
        name = intent.getStringExtra("name").toString()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        QuestionsReset()
        intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("name",name)
        startActivity(intent)
        finish()
    }

    fun maxCommits(){
        commit[0] = 2
        commit[1] = 2
        commit[2] = 2
        commit[3] = 2
        binding?.butNextQuestion?.visibility = View.VISIBLE
        maxCommitSet = true
    }

    fun resetCommits(v : Int){
        for (i in 1..4)
            if (i != v) commit[i-1] = 0
    }

    fun setQuestion(q : Int){
        answered = 0
        binding?.tvQtitle?.text = "Question " + q.toString()
        binding?.tvQtext?.text =
            allQuestions[q].question.substring(0, allQuestions[q].question.indexOf("\n"))
        binding?.tvQtextCode?.text = allQuestions[q].question.substring(
            allQuestions[q].question.indexOf("\n") + 1,
            allQuestions[q].question.length
        )
        binding?.tvA1text?.text = allQuestions[q].optionOne
        binding?.tvA2text?.text = allQuestions[q].optionTwo
        binding?.tvA3text?.text = allQuestions[q].optionThree
        binding?.tvA4text?.text = allQuestions[q].optionFour
        binding?.clA1?.background = getDrawable(R.drawable.answer_background)
        binding?.clA2?.background = getDrawable(R.drawable.answer_background)
        binding?.clA3?.background = getDrawable(R.drawable.answer_background)
        binding?.clA4?.background = getDrawable(R.drawable.answer_background)
        resetCommits(-1)
        maxCommitSet = false
        setProgressBar((maxDur*1000).toLong())
        actProgress = 0
        binding?.butNextQuestion?.visibility = View.INVISIBLE
    }

    private fun customDialogFunction(intent: Intent, id : Int) {
        val customDialog = Dialog(this)
        customDialog.setContentView(R.layout.upgrade_dialog)
        customDialog.findViewById<TextView>(R.id.tv_upgrade).setOnClickListener(){
            //Toast.makeText(this,"clicked Upgrade",Toast.LENGTH_LONG).show()
            customDialog.dismiss()
            startActivity(intent)
            finish()
        }
        customDialog.findViewById<TextView>(R.id.tv_exit).setOnClickListener(){
            //Toast.makeText(this,"clicked Cancel",Toast.LENGTH_LONG).show()
            customDialog.dismiss()
            startActivity(intent)
            finish()
        }
        customDialog.findViewById<TextView>(R.id.tv_watch).setOnClickListener(){
            //Toast.makeText(this,"clicked Watch Videos",Toast.LENGTH_LONG).show()
            customDialog.dismiss()
            handleAd(intent, id)
        }
        customDialog.show()
        customDialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }

    fun handleAd(intent: Intent, id : Int){
        if (mRewardedAd != null) {
            mRewardedAd?.show(this, OnUserEarnedRewardListener() {
                fun onUserEarnedReward(rewardItem: RewardItem) {
                    var rewardAmount = rewardItem.amount
                    var rewardType = rewardItem.type
                    Log.d(TAG, "User earned the reward.")
                }
                Toast.makeText(this, classList[id].topic, Toast.LENGTH_SHORT).show()
                classList[id].subscription = 1
                addRecord(classDao, classList[id].topic, id)
                startActivity(intent)
                finish()
            })
        } else {
            Log.d(TAG, "The rewarded ad wasn't ready yet.")
        }
    }

    private fun setProgressBar(timeOnClock: Long) {
        if (aTimer!=null){
            aTimer?.cancel()
        }
        binding?.progressBar?.progress = actProgress ?: 0
        binding?.progressBar?.max = (timeOnClock/100).toInt()
        aTimer = object : CountDownTimer(timeOnClock, 100) {
            override fun onTick(p0: Long) {
                binding?.progressBar?.progress = (timeOnClock/100).toInt() - ++actProgress
            }
            override fun onFinish() {
                maxCommits()
                Toast.makeText(this@Test, "You're out of time",Toast.LENGTH_SHORT).show()
                //setCorrect()
            }
        }.start()
    }

    fun setCorrect(){
        question = " " + allQuestions[q].id.toString()
        if (allQuestions[q].correctAnswer == 1) {
            binding?.clA1?.background = getDrawable(R.drawable.answer_background_correct)
            if (answered == 1) updateRecord(" 1", question) else updateRecord(" 0", question)
        }
        if (allQuestions[q].correctAnswer == 2) {
            binding?.clA2?.background = getDrawable(R.drawable.answer_background_correct)
            if (answered == 2) updateRecord(" 1", question) else updateRecord(" 0", question)
        }
        if (allQuestions[q].correctAnswer == 3) {
            binding?.clA3?.background = getDrawable(R.drawable.answer_background_correct)
            if (answered == 3) updateRecord(" 1", question) else updateRecord(" 0", question)
        }
        if (allQuestions[q].correctAnswer == 4) {
            binding?.clA4?.background = getDrawable(R.drawable.answer_background_correct)
            if (answered == 4) updateRecord(" 1", question) else updateRecord(" 0", question)
        }
        //Toast.makeText(this,question + entity.name,Toast.LENGTH_SHORT).show()
    }

    fun resetAnswers(allbut : Int){
        for (i in 0..3)
            if (i != (allbut-1)) commit[i] = 0
        if (allbut != 1) binding?.clA1?.background = getDrawable(R.drawable.answer_background)
        if (allbut != 2) binding?.clA2?.background = getDrawable(R.drawable.answer_background)
        if (allbut != 3) binding?.clA3?.background = getDrawable(R.drawable.answer_background)
        if (allbut != 4) binding?.clA4?.background = getDrawable(R.drawable.answer_background)
    }

    override fun onDestroy() {
        super.onDestroy()
        aTimer?.cancel()
        actProgress = 0
        binding = null
    }

    fun updateRecord(answers: String, question: String){
        if (subscribed==1){
            lifecycleScope.launch { // we populate the fields with the text that wa saved
                    classDao.update(ClassEntity(classid = entity.classid, id = entity.id, name = entity.name, subscription = 1, answers_correct = entity.answers_correct + answers, questions_total = entity.questions_total + question))
                }
            }
        }

    fun addRecord(classDao : ClassDao, class_name : String, id: Int){ // we add the subscription value
        lifecycleScope.launch {
            classDao.insert(ClassEntity(name=class_name, id = id, subscription = 1))
        }
    }
}