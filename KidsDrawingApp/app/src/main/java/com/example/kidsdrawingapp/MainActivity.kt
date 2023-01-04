package com.example.kidsdrawingapp

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity(){

    private var drawingView : DrawingView? = null
    private var selectBrush : ImageButton? = null
    private var mImageButtonCurrentPaint: ImageButton? = null
    private var selectImage : ImageButton? = null
    private var undo : ImageButton? = null
    private var redo : ImageButton? = null
    private var savebut : ImageButton? = null
    private var customProgressDialog : Dialog? = null

    private fun showProgressDialog(){ // shows the progress dialog
        customProgressDialog = Dialog(this@MainActivity)
        customProgressDialog?.setContentView(R.layout.dialog_custom_progress)
        customProgressDialog?.show()
    }

    private fun cancelProgressDialog(){ // will dismiss the progress dialog
        if (customProgressDialog != null) {
            customProgressDialog?.dismiss()
            customProgressDialog = null
        }
    }

    private fun shareImage(result: String){
        MediaScannerConnection.scanFile(this, arrayOf(result), null){
            path, uri ->
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
            shareIntent.type = "image/png"
            startActivity(Intent.createChooser(shareIntent, "Share"))
        }
    }

    private val openGalleryLauncher : ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result -> // the result is the return from StartActivityForResult() function
        if (result.resultCode == RESULT_OK && result.data != null) {
            val imageBackground : ImageView = findViewById(R.id.iv_background)
            imageBackground.setImageURI(result.data?.data)
        }
    }

    // the code below sets up permission for camera
    private val cameraResultLauncher : ActivityResultLauncher<String> =
        registerForActivityResult( // just this line of code will ask for permission
            ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Toast.makeText(this, "permission granted", Toast.LENGTH_LONG)
            } else {
                android.widget.Toast.makeText(this, "permission denied", Toast.LENGTH_LONG)
            }
        }

    // the code below sets up permission for camera and location
    private val cameraAndLocationResultLauncher : ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()) {
            permissions ->
            permissions.entries.forEach{
                val permissionName = it.key
                val isGranted = it.value
                if (isGranted){
                    if (permissionName == Manifest.permission.ACCESS_FINE_LOCATION){
                        Toast.makeText(this, "Permission granted for location", Toast.LENGTH_LONG)
                    } else {
                        Toast.makeText(this, "Permission granted for camera", Toast.LENGTH_LONG)
                    }
                } else {
                    if (permissionName == Manifest.permission.ACCESS_FINE_LOCATION){
                        Toast.makeText(this, "Permission denied for location", Toast.LENGTH_LONG)
                    } else {
                        Toast.makeText(this, "Permission denied for camera", Toast.LENGTH_LONG)
                    }
                }
            }
        }

    private val requestPermission: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()) {
                permissions->
                permissions.entries.forEach{
                    val permissionName = it.key
                    val isGranted = it.value
                    if (isGranted){
                        Toast.makeText(this@MainActivity, "Storage permission granted", Toast.LENGTH_LONG).show()
                        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        openGalleryLauncher.launch(pickIntent)
                    } else {
                        if (permissionName==Manifest.permission.READ_EXTERNAL_STORAGE){
                            Toast.makeText(this, "Permission has been denied", Toast.LENGTH_LONG).show()
                        }
                    }
                }
        }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val linearLayoutPaintColors = findViewById<LinearLayout>(R.id.ll_paint_colors) // this contains all the image buttons
        mImageButtonCurrentPaint = linearLayoutPaintColors[1] as ImageButton // will take item at position 1 of the list and treat it like an Image Button
        mImageButtonCurrentPaint!!.setImageDrawable( // we set the palette
            ContextCompat.getDrawable(this, R.drawable.palette_pressed)
        )

        drawingView = findViewById(R.id.drawing_view) // we find the view in activity main
        drawingView?.setSizeForBrush(5.0F)
        selectBrush = findViewById(R.id.ib_brush)
        selectBrush?.setOnClickListener{
            showBrushSizeChooserDialog()
        }
        selectImage = findViewById(R.id.ib_img)
        selectImage?.setOnClickListener {
            selectStoragePermission()
        }
        undo = findViewById(R.id.ib_undo)
        undo?.setOnClickListener{
            drawingView!!.onClickUndo()
        }
        redo = findViewById(R.id.ib_redo)
        redo?.setOnClickListener{
            drawingView!!.onClickRedo()
        }
        savebut = findViewById(R.id.ib_save)
        savebut?.setOnClickListener{
            showProgressDialog()
            if (isReadStorageAllowed()){
                lifecycleScope.launch(){
                    val flDrawingView:FrameLayout = findViewById(R.id.fl_drawing_view_container)
                    saveBitmapFile(getBitMapFromView(flDrawingView))
                }
            }
        }
    }

    private fun isReadStorageAllowed() : Boolean {
        val result = ContextCompat.checkSelfPermission(this,
        Manifest.permission.READ_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun selectStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale( // returns true of the user has requested permission previously
                this, Manifest.permission.READ_EXTERNAL_STORAGE)
        ){
            showRationaleDialog("Kids Drawing App", "Kids Drawing app need access to your storage")
        } else {
            requestPermission.launch(arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )) // TODO - add writing external storage permission
        }
    }

    private fun showRationaleDialog(title : String, message : String){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title).setMessage(message).setPositiveButton("Cancel"){
            dialog, _ -> dialog.dismiss()
        }
        builder.create().show()
    }

    private fun showBrushSizeChooserDialog(){
        val brushDialog = Dialog(this)
        brushDialog.setContentView(R.layout.dialog_brush_size)
        brushDialog.setTitle("Brush size : ")
        val smallBtn: ImageButton = brushDialog.findViewById(R.id.ib_small_brush)
        val mediumBtn: ImageButton = brushDialog.findViewById(R.id.ib_medium_brush)
        val largeBtn: ImageButton = brushDialog.findViewById(R.id.ib_large_brush)
        smallBtn.setOnClickListener{
            drawingView?.setSizeForBrush(5.0F)
            brushDialog.dismiss()
        }
        mediumBtn.setOnClickListener{
            drawingView?.setSizeForBrush(10.0F)
            brushDialog.dismiss()
        }
        largeBtn.setOnClickListener{
            drawingView?.setSizeForBrush(20.0F)
            brushDialog.dismiss()
        }
        brushDialog.show()
    }

    fun paintClicked(view: View){
        if (mImageButtonCurrentPaint != view){ // only do something if we are clicking a paint that is not current selected
            val imageButton = view as ImageButton // extracts the image button element out of the view
            val colorTag = imageButton.tag.toString() // grabs the tag which is the color
            drawingView?.setColor(colorTag)
            imageButton.setImageDrawable( // we set the palette of the new pressed button
                ContextCompat.getDrawable(this, R.drawable.palette_pressed)
            )
            mImageButtonCurrentPaint?.setImageDrawable( // setting the old button to normal palette
                ContextCompat.getDrawable(this, R.drawable.palette_normal)
            )
            mImageButtonCurrentPaint = view // setting the new button
        }
    }

    private fun getBitMapFromView(view : View) : Bitmap {
        val returnedBitmap = Bitmap.createBitmap(view.width,view.height,Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background
        if (bgDrawable != null){
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)
        return returnedBitmap
    }

    private suspend fun saveBitmapFile(mBitmap : Bitmap?) : String{
        var result = ""
        withContext(Dispatchers.IO){
            if (mBitmap != null){
                try {
                    val bytes = ByteArrayOutputStream()
                    mBitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes)
                    val file = File(externalCacheDir?.absoluteFile.toString() + File.separator + "KidsDrawingApp_" + System.currentTimeMillis()/1000 + ".png")
                    val fo = FileOutputStream(file)
                    fo.write(bytes.toByteArray())
                    fo.close()
                    result = file.absolutePath

                    runOnUiThread{
                        cancelProgressDialog() // will run this task on the main thread which means its instanteneusly cancellable
                        if(result.isNotEmpty()){
                            Toast.makeText(this@MainActivity, "File saved successfully at: $result", Toast.LENGTH_SHORT).show()
                            shareImage(result)
                        } else {
                            Toast.makeText(this@MainActivity, "Somwthing went wrong", Toast.LENGTH_SHORT)
                        }
                    }
                } catch(e : Exception){
                    result = ""
                    e.printStackTrace()
                }
            }
        }
        return result
    }
}