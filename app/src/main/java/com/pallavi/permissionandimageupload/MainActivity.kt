package com.pallavi.permissionandimageupload

import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.firebase.storage.FirebaseStorage
import com.pallavi.permissionandimageupload.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var storageRef = FirebaseStorage.getInstance()
    private lateinit var uri : Uri
    private lateinit var binding: ActivityMainBinding
    var getImageContracts = registerForActivityResult(ActivityResultContracts.RequestPermission())
    {
        if(it)
        {
            Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show()
        }
        else
        {
            Toast.makeText(this, "permission not granted", Toast.LENGTH_SHORT).show()
        }
    }
    private var pickImage = registerForActivityResult(ActivityResultContracts.GetContent())
    {
        println("in uri $it")
        if (it!=null)
        {
            uri=it
        }
        binding.image.setImageURI(it)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnGet.setOnClickListener {
            getImageContracts.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        binding.btnGet.setOnClickListener{
            if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)
            pickImage.launch("image/*")
            else{
                getImageContracts.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
        binding.btnUpload.setOnClickListener {
            if (this::uri.isInitialized)
            {
             storageRef.getReference("image").putFile(uri).addOnSuccessListener {
                 Toast.makeText(this, "Successfully", Toast.LENGTH_SHORT).show()
             }
                 .addOnFailureListener{
                     println("in failure $it")
                     Toast.makeText(this, "in failure $it", Toast.LENGTH_SHORT).show()
                 }
            }
            else
            {
                Toast.makeText(this, "sorry", Toast.LENGTH_SHORT).show()
            }
        }
    }
}