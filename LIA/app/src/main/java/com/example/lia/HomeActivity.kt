package com.example.lia

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.lia.databinding.ActivityHomeBinding
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private var binding : ActivityHomeBinding? = null
    var testid = -1
    private var mRewardedAd: RewardedAd? = null // ad lines
    private var TAG = "HomeActivity" // ad lines
    lateinit var classDao : ClassDao
    var email : String = "JonSnow@Winterfell.vm"
    var name : String = "Jon Snow"
    var answers : String = ""
    var questions : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        classDao = (application as ClassApp).db.classDao()
        lifecycleScope.launch {
            classDao.fetchAllClasses().collect() {
                for (classEntity in it) {
                    classList[classEntity.id].subscription = 1
                    classList[classEntity.id].answers = classEntity.answers_correct
                }
            }
        }
        val adapter = HomeAdapter(
            classList,
            { id ->
                //Toast.makeText(this, classList[id].subscription.toString() + " " + id.toString(),Toast.LENGTH_SHORT).show()
                if (com.example.lia.classList[id].subscription == 1) Toast.makeText(
                    this,
                    "Thank you for upgrading!",
                    Toast.LENGTH_SHORT
                ).show() else {
                    customDialogFunction(null, id)
                }
            },
            { id ->
                if (com.example.lia.classList[id].subscription == 0) Toast.makeText(
                    this,
                    "This is a premium feature",
                    Toast.LENGTH_SHORT
                ).show() else {
//                        Toast.makeText(
//                        this,
//                        "This feature is on its way!",
//                        Toast.LENGTH_SHORT).show()
                    beginAnalysis(id)
                    //Toast.makeText(this, classList[id].answers,Toast.LENGTH_SHORT).show()
                }
            },
            { id -> beginTest(id) },
        )
        binding?.taskRv?.adapter = adapter

        // ad stuff

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

        // firebase authentication stuff
        auth = FirebaseAuth.getInstance()
        email = intent.getStringExtra("email").toString()
        name = intent.getStringExtra("name").toString()
        binding?.tvName?.text = "Welcome " + name
        binding?.tvSignOut?.setOnClickListener{
            auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun beginAnalysis(id: Int) {
        val intent = Intent(this, Analytics::class.java)
        intent.putExtra("id", id.toString())
        intent.putExtra("name", name)
        intent.putExtra("email", email)
        intent.putExtra("answers", classList[id].answers)
        startActivity(intent)
        //finish()
    }

    fun handleAd(id: Int){
        if (mRewardedAd != null) {
            mRewardedAd?.show(this, OnUserEarnedRewardListener() {
                fun onUserEarnedReward(rewardItem: RewardItem) {
                    var rewardAmount = rewardItem.amount
                    var rewardType = rewardItem.type
                    Log.d(TAG, "User earned the reward.")
                }
                startActivity(getIntent())
                classList[id].subscription = 1
                addRecord(classDao, classList[id].topic, id)
            })
        } else {
            Log.d(TAG, "The rewarded ad wasn't ready yet.")
        }
    }

    private fun beginTest(id: Int) {
        initialize(classList[id].topic, "en")
        initialize2(classList[id].topic, "en")
        if (classList[id].subscription == 1){
            testOptimize()
        } else {
            allQuestions = if( Qlimit>allQuestions.size) allQuestions else subArrayList(allQuestions,0,
                Qlimit)
        }
        val intent = Intent(this, Test::class.java)
        intent.putExtra("id",id)
        intent.putExtra("name",name)
        startActivity(intent)
        finish()
    }

    private fun subArrayList(allQuestions: ArrayList<Questions>, n1: Int, n2: Int): ArrayList<Questions> {
        var out = ArrayList<Questions>()
        for (i in n1..n2){
            out.add(allQuestions[i])
        }
        return out
    }

    private fun testOptimize(){
        allQuestions.shuffled()
    }

    private fun customDialogFunction(intent: Intent?, id : Int ) {
        val customDialog = Dialog(this)
        customDialog.setContentView(R.layout.upgrade_dialog_main)
        customDialog.findViewById<TextView>(R.id.tv_upgrade).setOnClickListener(){
            //Toast.makeText(this,"clicked Upgrade",Toast.LENGTH_LONG).show()
            customDialog.dismiss()
            if (intent != null) {
                startActivity(intent)
                finish()
            }
        }
        customDialog.findViewById<TextView>(R.id.tv_exit).setOnClickListener(){
            //Toast.makeText(this,"clicked Cancel",Toast.LENGTH_LONG).show()
            customDialog.dismiss()
            if (intent != null) {
                startActivity(intent)
                finish()
            }
        }
        customDialog.findViewById<TextView>(R.id.tv_watch).setOnClickListener(){
            //Toast.makeText(this,"clicked Watch Videos",Toast.LENGTH_LONG).show()
            customDialog.dismiss()
            handleAd(id)
        }
        customDialog.show()
        customDialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT)
    }

    fun addRecord(classDao : ClassDao, class_name : String, id: Int){ // we add the subscription value
        lifecycleScope.launch {
            classDao.insert(ClassEntity(name=class_name, classid = id, subscription = 1))
        }
    }

    onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            QuestionsReset()
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}