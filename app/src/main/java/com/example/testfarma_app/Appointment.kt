package com.example.testfarma_app

import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.testfarma_app.databinding.ActivityApptBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_appt.*


class Appointment: AppCompatActivity(), AdapterView.OnItemClickListener {
    private lateinit var binding: ActivityApptBinding
    private lateinit var database: DatabaseReference

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        binding = ActivityApptBinding.inflate(layoutInflater)
        setContentView(binding.root)

        menu.setOnClickListener {
            val intent = Intent(this, MenuApp::class.java)
            startActivity(intent)
            finish()
        }
        perfil.setOnClickListener{
            val intent = Intent(this, UserDataInfo::class.java)
            startActivity(intent)
            finish()
        }

        val sucursal = resources.getStringArray(R.array.sucursal_ar)
        val adapter = ArrayAdapter(
            this,
            R.layout.list_cita,
            sucursal
        )
        with(binding.sucursalSelect) {
            setAdapter(adapter)
            onItemClickListener = this@Appointment
        }

        val hora= resources.getStringArray(R.array.time_ar)
        val adapter1 = ArrayAdapter(
            this,
            R.layout.list_cita,
            hora

        )
        with(binding.timeSelect) {
            setAdapter(adapter1)
            onItemClickListener = this@Appointment
        }

        val estudio= resources.getStringArray(R.array.estudio_ar)
        val adapter2 = ArrayAdapter(
            this,
            R.layout.list_cita,
            estudio

        )
        with(binding.estudioSelect) {
            setAdapter(adapter2)
            onItemClickListener = this@Appointment
        }

        val tv= findViewById<TextView>(R.id.date_selec)
        val cal= Calendar.getInstance()
        val myYear=cal.get(Calendar.YEAR)
        val myMonth=cal.get(Calendar.MONTH)
        val myDay=cal.get(Calendar.DAY_OF_MONTH)

        tv.setOnClickListener{
            val datePickerDialog= DatePickerDialog(this,DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                tv.text=" " + dayOfMonth + "/" + (month) + "/" + year
            },myYear,myMonth,myDay)
            datePickerDialog.show()
        }

        binding.saveAppt.setOnClickListener {
            if ( date_selec.text.isNotEmpty() && time_select.text.isNotEmpty() && estudio_select.text.isNotEmpty() && sucursal_select.text.isNotEmpty()) {
                val userid = binding.idUsin.text.toString()
                val fecha = binding.dateSelec.text.toString()
                val hora = binding.timeSelect.text.toString()
                val estudio = binding.estudioSelect.text.toString()
                val domicilio = binding.homeUsin.text.toString()
                val sucursal = binding.sucursalSelect.text.toString()
                database = FirebaseDatabase.getInstance().getReference("Appt")
                val Appt = Appt(userid, fecha, hora, estudio, domicilio, sucursal)
                database.child(userid).setValue(Appt).addOnSuccessListener {
                    binding.dateSelec.text.clear()
                    binding.timeSelect.text.clear()
                    binding.estudioSelect.text.clear()
                    binding.homeUsin.text.clear()
                    binding.sucursalSelect.text.clear()
                    Toast.makeText(this, "Registro Exitoso", Toast.LENGTH_SHORT).show()
                }
            }else {
                Toast.makeText(this, "Información Incompleta", Toast.LENGTH_SHORT).show()
            }
        }

        btn_sucursalpop.setOnClickListener {
            var dialog = PopUpFragment()

            dialog.show(supportFragmentManager, "PopUpFragment")
        }


    }


    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }
}
