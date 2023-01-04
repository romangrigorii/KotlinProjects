package com.example.coroutines3

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    var customProgressDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnExecute: Button = findViewById(R.id.btn_execute)
        btnExecute.setOnClickListener {
            showProgressDialog() // show the progress before we launch the program
            lifecycleScope.launch {
                execute2("Task executed successfully")
            }
        }
    }

    private fun execute1(input: String) {
        for (i in 1..1000) {
            Log.e("delay", "msg $i")
        }
        Toast.makeText(this, input, Toast.LENGTH_LONG).show()
    }

    private suspend fun execute2(input: String) {
        withContext(Dispatchers.IO) { // this moves the operation into a different thread until it completes
            for (i in 1..100000) {
                Log.e("delay", "msg $i")
            }
            runOnUiThread {
                cancelProgressDialog() // can cancel on the UI thread
                Toast.makeText(this@MainActivity, input, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showProgressDialog(){
        customProgressDialog = Dialog(this@MainActivity)
        customProgressDialog?.setContentView(R.layout.dialog_custom_progress)
        customProgressDialog?.show()
    }

    private fun cancelProgressDialog(){
        if (customProgressDialog != null) {
            customProgressDialog?.dismiss()
            customProgressDialog = null
        }
    }
}