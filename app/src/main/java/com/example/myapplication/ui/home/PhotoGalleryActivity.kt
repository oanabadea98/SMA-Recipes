package com.example.myapplication.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.R
import android.content.Intent
import android.app.Activity
import android.graphics.Bitmap
import android.provider.MediaStore
import android.content.ContentResolver
import android.webkit.MimeTypeMap
import android.app.ProgressDialog
import android.net.Uri
import android.widget.*
import com.google.firebase.storage.StorageReference
import com.example.myapplication.Helpers.FirebaseHelper
import com.google.firebase.storage.UploadTask
import kotlin.Throws
import com.google.android.gms.tasks.OnCompleteListener
import com.example.myapplication.Models.ListPhotoModel
import com.google.android.gms.tasks.OnFailureListener
import java.io.IOException

class PhotoGalleryActivity : AppCompatActivity() {
    private var textView: TextView? = null
    private var imageView: ImageView? = null
    private var nameUpload: EditText? = null
    private var timeUpload: EditText? = null
    private var typeUpload: EditText? = null
    private var ingredientsUpload: EditText? = null
    private var preparationUpload: EditText? = null
    private var btnChoose: Button? = null
    private var btnUpload: Button? = null
    private var filePath: Uri? = null
    private val PICK_IMAGE_REQUEST = 71

    // Folder path for Firebase Storage.
    var Storage_Path = "Images/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_gallery)
        initializeViews()
        setOnClickListeners()
    }

    fun initializeViews() {
        textView = findViewById(R.id.text_view_upload)
        imageView = findViewById(R.id.img_View)
        nameUpload = findViewById(R.id.name_et_upload)
        timeUpload = findViewById(R.id.time_et_upload)
        typeUpload = findViewById(R.id.type_et_upload)
        ingredientsUpload = findViewById(R.id.ingredients_et_upload)
        preparationUpload = findViewById(R.id.preparation_et_upload)
        btnChoose = findViewById(R.id.btn_photo_choose)
        btnUpload = findViewById(R.id.btn_photo_upload)

        //btnShow = findViewById(R.id.btn_photo_show);
    }

    fun setOnClickListeners() {
        btnChoose!!.setOnClickListener { chooseImage() }
        btnUpload!!.setOnClickListener { uploadImage() }

        /*btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PhotoGalleryActivity.this, ShowGalleryActivity.class));
            }
        });*/
    }

    private fun chooseImage() {
        val intent = Intent() //creates an image chooser dialog(allows user to browse through the device gallery)
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST) //receive the result (selected image)
    }

    //for diplaying the image _ getting image path ->  onActivityResult
    /* onActivityResult receives a request code, result code, and the data. In this method, you will check to see if the request code equals PICK_IMAGE_REQUEST, with the result code equal to RESULT_OK and the data available. If all this is true, you want to display the selected image in the ImageView.*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) //displaying the image
        {
            filePath = data.data //path of the choosen image
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                imageView!!.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    // Creating Method to get the selected image file Extension from File Path URI.
    fun GetFileExtension(uri: Uri?): String? {
        val contentResolver = contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri!!))
    }

    //uploading to firebase
    private fun uploadImage() {
        val name = nameUpload!!.text.toString()
        val time = timeUpload!!.text.toString()
        val type = typeUpload!!.text.toString()
        val ingredients = ingredientsUpload!!.text.toString()
        val prep = preparationUpload!!.text.toString()
        if (nameUpload!!.text.toString().isEmpty() || timeUpload!!.text.toString().isEmpty() || typeUpload!!.text.toString().isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }
        if (filePath != null) {
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading...")
            progressDialog.show()


            //second storage ref
            val storageReference2 = FirebaseHelper.imageStorage.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(filePath))
            val uploadTask = storageReference2.putFile(filePath!!)
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception!!
                }
                storageReference2.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progressDialog.dismiss()
                    val downloadUri = task.result
                    val mUri = downloadUri.toString()
                    val listPhotoModel = ListPhotoModel(mUri, name, time, type, ingredients, prep)
                    val ImageUploadId = FirebaseHelper.recipeDatabase.push().key
                    FirebaseHelper.recipeDatabase.child(ImageUploadId!!).setValue(listPhotoModel)
                } else {
                    Toast.makeText(this@PhotoGalleryActivity, "Failed upload", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { }
        }
    }
}