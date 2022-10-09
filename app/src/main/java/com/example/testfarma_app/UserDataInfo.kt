package com.example.testfarma_app

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.testfarma_app.databinding.UserInfoLayoutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_appt.menu
import kotlinx.android.synthetic.main.user_info_layout.*
import java.util.regex.Pattern

class UserDataInfo:  AppCompatActivity() {
    private lateinit var binding: UserInfoLayoutBinding

    private lateinit var auth: FirebaseAuth
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
            if (it.exists()) {
                val nombreEspacio = it.child("nombre").value
                val emailEspacio = it.child("email").value
                binding.textNomUser.text = nombreEspacio.toString()
                binding.textEmail.text = emailEspacio.toString()

            } else {
                Toast.makeText(this, "No existe", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Falló", Toast.LENGTH_SHORT).show()
        }



        binding.btnGuardarDatos.setOnClickListener {

            if (validarTelefono() && validarFechNa() && validarEst() && validarPeso()) {
                val nombre = binding.textNomUser.text.toString()
                val email = binding.textEmail.text.toString()
                val telefono = binding.editTelefono.text.toString()
                val fechaNac = binding.editFechaNac.text.toString()
                val estatura = binding.editEstatura.text.toString()
                val peso = binding.editPeso.text.toString()

                val userData = UserData(nombre, email, telefono, fechaNac, estatura, peso)
                if (uid != null) {
                    databaseReference.child(uid).setValue(userData).addOnSuccessListener {
                        Toast.makeText(this, "Registro de perfil exitoso", Toast.LENGTH_SHORT)
                            .show()

                    }.addOnFailureListener {

                        Toast.makeText(
                            this@UserDataInfo,
                            "Fallo el registro de perfil",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            } else {
                showAlert()
            }
        }
    }
    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Llene todos los campos para guardar su información")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun validarTelefono () : Boolean{
        val espacioTel = findViewById<EditText>(R.id.editTelefono)
        val telefono = espacioTel.text.toString()
        val telRegex = Pattern.compile(
            "^[+]?[0-9]{10,13}\$"

        )
        return if (telefono.isEmpty()) {
            editTelefono.error = "El campo del teléfono no puede estar vacío"
            false
        }else if (!telRegex.matcher(telefono).matches()) {
            editTelefono.error = "Revise el número de teléfono que sea válido no exceda los 13 dígitos"
            false
        } else {
            editTelefono.error = null
            true
        }
    }

    private fun validarFechNa () : Boolean{
        val espacioFech = findViewById<EditText>(R.id.editFechaNac)
        val fechaNa = espacioFech.text.toString()
        val fechRegex = Pattern.compile(
            "^\\d{4}[\\-\\/\\s]?((((0[13578])|(1[02]))[\\-\\/\\s]?(([0-2][0-9])|(3[01])))|(((0[469])|(11))[\\-\\/\\s]?(([0-2][0-9])|(30)))|(02[\\-\\/\\s]?[0-2][0-9]))\$"

        )
        return if (fechaNa.isEmpty()) {
            editFechaNac.error = "El campo de la fecha de nacimiento no puede estar vacío"
            false
        }else if (!fechRegex.matcher(fechaNa).matches()) {
            editFechaNac.error = "Revise que la fecha de nacimiento sea válida Ej. aaaa/mm/dd"
            false
        } else {
            editFechaNac.error = null
            true
        }
    }


    private fun validarEst () : Boolean{
        val espacioEst = findViewById<EditText>(R.id.editEstatura)
        val estatura = espacioEst.text.toString()
        return if (estatura.isEmpty()) {
            editEstatura.error = "El campo de la estatura no puede estar vacío"
            false
        }else if (estatura.toFloat() < .70 || estatura.toFloat() > 2.51) {
            editEstatura.error = "Revise que haya ingresado una estatura válida"
            false
        } else {
            editEstatura.error = null
            true
        }
    }

    private fun validarPeso () : Boolean{
        val espacioPeso = findViewById<EditText>(R.id.editPeso)
        val peso = espacioPeso.text.toString()
        return if (peso.isEmpty()) {
            editEstatura.error = "El campo de la estatura no puede estar vacío"
            false
        }else if (peso.toFloat() < 25 || peso.toFloat() > 440) {
            editPeso.error = "Revise que haya ingresado un peso válido"
            false
        } else {
            editPeso.error = null
            true
        }
    }

}
