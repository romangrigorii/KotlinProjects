package com.example.loginauth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loginauth.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInOptions.DEFAULT_SIGN_IN
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import java.util.*


class MainActivity : AppCompatActivity(), View.OnClickListener {


    lateinit var mGoogleSignInClient: GoogleSignInClient
    val Req_Code: Int = 123
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
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, Req_Code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Req_Code) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                UpdateUI(account)
            }
        } catch (e: ApiException) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    // this is where we update the UI after Google signin takes place
    private fun UpdateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("email",account.email.toString())
                intent.putExtra("name",account.displayName.toString())
                startActivity(intent)
                //finish()
            }
            else {
                Toast.makeText(this, "Sign in was unsuccessful",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
            startActivity(
                Intent(
                    this, HomeActivity
                    ::class.java
                )
            )
            //finish()
        }
    }

}

class DashboardActivity {

}

class SavedPreference {

}

class GoogleSignInOptons {

}