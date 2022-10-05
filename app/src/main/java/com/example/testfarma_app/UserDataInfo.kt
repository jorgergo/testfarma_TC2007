package com.example.testfarma_app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.testfarma_app.databinding.UserInfoLayoutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_appt.*

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
        databaseReference = FirebaseDatabase.getInstance().getReference("User")

        menu.setOnClickListener {
            val intent = Intent(this, MenuApp::class.java)
            startActivity(intent)
            finish()
        }

        databaseReference.child(uid.toString()).get().addOnSuccessListener {
            if (it.exists()){
                val nombreEspacio = it.child("nombre").value
                val emailEspacio = it.child("email").value
                Toast.makeText(this, "Se logró",Toast.LENGTH_SHORT).show()
                binding.textNomUser.text = nombreEspacio.toString()
                binding.textEmail.text = emailEspacio.toString()

            } else {
                Toast.makeText(this, "No existe",Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener{
            Toast.makeText(this, "Falló",Toast.LENGTH_SHORT).show()
        }

        binding.btnGuardarDatos.setOnClickListener {

            val nombre = binding.textNomUser.text.toString()
            val email = binding.textEmail.text.toString()
            val telefono = binding.telefono.text.toString()
            val fechaNac = binding.fechaNac.text.toString()
            val estatura = binding.estatura.text.toString()
            val peso = binding.peso.text.toString()

            val userData = UserData(nombre, email, telefono, fechaNac, estatura, peso)
            if(uid != null) {
                databaseReference.child(uid).setValue(userData).addOnSuccessListener {
                    Toast.makeText(this, "Registro de perfil exitoso", Toast.LENGTH_SHORT).show()

                }.addOnFailureListener {

                    Toast.makeText(
                        this@UserDataInfo,
                        "Fallo el registro de perfil",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun showData() {
        databaseReference = FirebaseDatabase.getInstance().getReference("User")
        val uid = auth.currentUser?.uid

    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Error al cargar el nombre")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}