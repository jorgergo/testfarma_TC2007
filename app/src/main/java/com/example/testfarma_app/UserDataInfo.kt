package com.example.testfarma_app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.testfarma_app.databinding.UserInfoLayoutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserDataInfo:  AppCompatActivity() {
    private lateinit var binding : UserInfoLayoutBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UserInfoLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().getReference("UserData")
        binding.btnGuardarDatos.setOnClickListener {
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
                        Toast.makeText(this@UserDataInfo, "Failed to update profile", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun uploadProfilePic() {
        TODO("Not yet implemented")
    }
}