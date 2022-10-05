package com.example.testfarma_app

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.PatternsCompat
import com.example.testfarma_app.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_reg.*
import java.util.regex.Pattern

class SignUp : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    val database = Firebase.database
    val ref = database.getReference("User")
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg)

        cancelarReg.setOnClickListener {
            val intent = Intent(this, MainActivity_NotSignedIn::class.java)
            startActivity(intent)
            finish()
        }
        setup()

        Politicas_priv.setOnClickListener {
            val intent = Intent(this, Poliitcasprivacidad::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setup() {

        auth =  Firebase.auth
        signUpBtn.setOnClickListener {


            if (!validarEmail() && !validarContra()) {
                showAlert()
            } else {
                val espacioEmail = findViewById<EditText>(R.id.editTextEmail)
                val espacioNombre = findViewById<EditText>(R.id.editTextName)

                val email = espacioEmail.text.toString()
                val nombre = espacioNombre.text.toString()

                if (email.isNotEmpty() && editTextPassword.text.isNotEmpty() &&
                    nombre.isNotEmpty()) {

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        email,
                        editTextPassword.text.toString()
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val user = auth.currentUser
                            user?.let {
                                regUsuario(user.uid)
                            }

                            showHome()
                            showData(it.result?.user?.email ?: "", nombre)

                        } else {
                            showAlert()
                        }
                    }
                }
            }
        }
    }

    private fun validarEmail() : Boolean {
        val espacioEmail = findViewById<EditText>(R.id.editTextEmail)
        val email = espacioEmail.text.toString()
        return if (email.isEmpty()) {
            editTextEmail.error = "El campo del correo no puede estar vacío"
            false
        } else if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.error = "Escriba una dirección de correo válida"
            false
        } else {
            editTextEmail.error = null
            true
        }
    }

    private fun validarContra() : Boolean {
        val espacioContra = findViewById<EditText>(R.id.editTextPassword)
        val contra = espacioContra.text.toString()
        val passwordRegex = Pattern.compile(
            "^" +
                    "(?=.*[0-9])" +
                    "(?=.*[a-z])" +
                    "(?=.*[A-Z])" +
                    "(?=.*[@#$%^&+=])" +
                    "(?=\\S+$)" +
                    ".{4,}" +
                    "$"

        )
        return if (contra.isEmpty()) {
            editTextPassword.error = "El campo del correo no puede estar vacío"
            false
        }else if (!passwordRegex.matcher(contra).matches()) {
            editTextPassword.error = "Revise la contraseña que contenga todo lo que se pide"
            false
        } else {
            editTextPassword.error = null
            true
        }
    }

    private fun regUsuario(uid: String){
        val espacioEmail = findViewById<EditText>(R.id.editTextEmail)
        val espacioNombre = findViewById<EditText>(R.id.editTextName)

        val email = espacioEmail.text.toString()
        val nombre = espacioNombre.text.toString()

        val usuario = User(uid,
            email,
            nombre
        )
        ref.child(uid).setValue(usuario).addOnCompleteListener{
            Toast.makeText(this, "Se ha iniciado sesión", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{err ->
            Toast.makeText(this, "No se ha iniciado sesión ${err.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("El correo electrónico ya está registrado")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome() {
        val intent = Intent(this, acercanos::class.java).apply {
        }
        startActivity(intent)
        finish()
    }

    private fun showData(email: String, nombre: String) {

        val intent = Intent(this, UserDataInfo::class.java).apply {
            putExtra("email", email)
            putExtra("nombre", nombre)
        }
    }
}


