package com.example.lia

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.lia.databinding.ActivityMainBinding
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity(), View.OnClickListener {
    // sign in
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    private var binding : ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.butLogin?.setOnClickListener(this)
        binding?.tvCreateAccount?.setOnClickListener(this)
        binding?.tvForgotPassword?.setOnClickListener(this)
        binding?.butGoogle?.setOnClickListener(this)
        binding?.butFacebook?.setOnClickListener(this)
        binding?.butTwitter?.setOnClickListener(this)

        //*** AD MOB INIT S
        MobileAds.initialize(this){}
        //*** AD MOB INIT E

        // *** GOOGLE LOG IN S
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onClick(view: View?) {
        when(view){
            binding?.butLogin -> {
                Toast.makeText(this, "Feature not yet implemented", Toast.LENGTH_SHORT).show()
            }
            binding?.tvCreateAccount -> {
                Toast.makeText(this, "Feature not yet implemented", Toast.LENGTH_SHORT).show()
            }
            binding?.tvForgotPassword -> {
                Toast.makeText(this, "Feature not yet implemented", Toast.LENGTH_SHORT).show()
            }
            binding?.butGoogle -> {
                signInGoogle()
            }
            binding?.butFacebook -> {
                Toast.makeText(this, "Feature not yet implemented", Toast.LENGTH_SHORT).show()
            }
            binding?.butGoogle -> {
                Toast.makeText(this, "Feature not yet implemented", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signInGoogle() {
        //// *** GOOGLE LOG IN S
          val signInIntent: Intent = mGoogleSignInClient.signInIntent
          launcher.launch(signInIntent)
        //// *** GOOGLE LOG IN E
//        val intent = Intent(this, HomeActivity::class.java)
//        startActivity(intent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result -> if (result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResult(task)
        }
    }

    // *** GOOGLE LOG IN S
    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        if (completedTask.isSuccessful){
            val account: GoogleSignInAccount? = completedTask.result
            if (account != null) {
                UpdateUI(account)
            }
        } else {
            Toast.makeText(this, completedTask.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }
     private fun UpdateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("email",account.email.toString())
                intent.putExtra("name",account.displayName.toString())
                startActivity(intent)
                //Toast.makeText(this,account.email.toString(),Toast.LENGTH_SHORT).show()
                finish()
            }
            else {
                Toast.makeText(this, "Sign in was unsuccessful",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null

    }
//    override fun onStart() {
//        super.onStart()
//        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
////            startActivity(
////                Intent(
////                    this, HomeActivity
////                    ::class.java
////                )
////            )
//            val intent = Intent(this, HomeActivity::class.java)
//            intent.putExtra("email", GoogleSignIn.getLastSignedInAccount(this)?.email.toString())
//            intent.putExtra("name",GoogleSignIn.getLastSignedInAccount(this)?.displayName.toString())
//            startActivity(intent)
//            finish()
//        }
//    }
    // *** GOOGLE LOG IN E

}

class DashboardActivity {

}

class SavedPreference {

}

class GoogleSignInOptons {
//
}
