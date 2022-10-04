package com.example.testfarma_app

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.testfarma_app.databinding.UserInfoLayoutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_appt.*

class UserDataInfo:  AppCompatActivity() {
    private lateinit var binding : UserInfoLayoutBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var imageUri: Uri
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UserInfoLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().getReference("UserData")

        menu.setOnClickListener {
            val intent = Intent(this, MenuApp::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnGuardarDatos.setOnClickListener {

            showProgressBar()
            val nombre = binding.nomPaciente.text.toString()
            val email = binding.emailUserInfo.text.toString()
            val rfc = binding.rfc.text.toString()
            val estado = binding.estado.text.toString()
            val codigoPostal = binding.codPost.text.toString()
            val colonia = binding.colonia.text.toString()
            val calle = binding.calle.text.toString()
            val numero = binding.numero.text.toString()

            val userData = UserData(nombre,email,rfc, estado, codigoPostal, colonia, calle, numero)
            if(uid!= null){
                databaseReference.child(uid).setValue(userData).addOnCompleteListener {
                    if (it.isSuccessful){
                        uploadProfilePic()
                    }else{
                        hideProgressBar()
                        Toast.makeText(this@UserDataInfo, "Failed to update profile", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun uploadProfilePic() {
        imageUri=Uri.parse("androud.resource://$packageName/${R.drawable.user_icon}")
        storageReference = FirebaseStorage.getInstance().getReference("User/"+auth.currentUser?.uid)
        storageReference.putFile(imageUri).addOnSuccessListener {
            hideProgressBar()
            Toast.makeText(this@UserDataInfo, "Profile succesfuly updated", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            hideProgressBar()
            Toast.makeText(this@UserDataInfo, "Failed to upload the image", Toast.LENGTH_SHORT).show()
        }
    }
    private fun showProgressBar(){
        dialog = Dialog(this@UserDataInfo)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_wait)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }
    private fun hideProgressBar(){
        dialog.dismiss()
    }
}