package com.example.testfarma_app

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.testfarma_app.databinding.ActivityApptBinding

class Appointment: AppCompatActivity(), AdapterView.OnItemClickListener {
    private lateinit var binding: ActivityApptBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApptBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sucursal = resources.getStringArray(R.array.sucursal_ar)
        val adapter = ArrayAdapter(
            this,
            R.layout.list_sucursal,
            sucursal
        )
        with(binding.sucursalSelect) {
            setAdapter(adapter)
            onItemClickListener = this@Appointment
        }

        val hora= resources.getStringArray(R.array.time_ar)
        val adapter1 = ArrayAdapter(
            this,
            R.layout.list_hora,
            hora

        )
        with(binding.timeSelect) {
            setAdapter(adapter1)
            onItemClickListener = this@Appointment
        }

        val estudio= resources.getStringArray(R.array.estudio_ar)
        val adapter2 = ArrayAdapter(
            this,
            R.layout.list_estudios,
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

    }


    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }
}