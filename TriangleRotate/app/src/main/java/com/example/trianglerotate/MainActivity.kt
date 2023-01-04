package com.example.trianglerotate

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // manifest
        cameraResultLauncher.launch(Manifest.permission.CAMERA)
        // intent
        openGalleryLauncher.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
    }

    // this part of the code is simply to practice getting app permissions

    private val cameraResultLauncher : ActivityResultLauncher<String> =
        registerForActivityResult( // just this line of code will ask for permission
            ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Toast.makeText(this, "permission granted", Toast.LENGTH_LONG)
            } else {
                android.widget.Toast.makeText(this, "permission denied", Toast.LENGTH_LONG)
            }
        }

    private val openGalleryLauncher : ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result -> // the result is the return from StartActivityForResult() function
        if (result.resultCode == RESULT_OK && result.data != null) {
            val imageBackground : ImageView = findViewById(R.id.iv_background)
            imageBackground.setImageURI(result.data?.data)
        }
    }

}