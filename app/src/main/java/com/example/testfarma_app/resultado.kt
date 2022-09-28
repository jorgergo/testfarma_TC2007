package com.example.testfarma_app

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.lang.Exception

class resultado : AppCompatActivity() {

    private lateinit var dbref : DatabaseReference
    private lateinit var resRecyclerView : RecyclerView
    private lateinit var archArrayList: ArrayList<arch_resultado>
    val resultadosRef =Firebase.storage.reference.child("Resultados")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado)

        resRecyclerView = findViewById(R.id.recycler_view_res)
        resRecyclerView.layoutManager = GridLayoutManager(this, 2)
        resRecyclerView.setHasFixedSize(true)

        archArrayList = arrayListOf<arch_resultado>()
        getArchdata()

    }

    private fun downloadFiles(f_name: String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val strVacio = ""
            val localFile = File.createTempFile(f_name,strVacio)
            resultadosRef.getFile(localFile).addOnSuccessListener {
                Toast.makeText(this@resultado, "Archivo descargado",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this@resultado,"Algo salio mal",Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(this@resultado,e.message,Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getArchdata() {
        dbref = FirebaseDatabase.getInstance().getReference("Resultados/u1")
        dbref.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for(userSnapshot in snapshot.children){
                        val user = userSnapshot.getValue(arch_resultado::class.java)
                        archArrayList.add(user!!)
                    }
                    var adapter =resultadosAdapter(archArrayList)
                    resRecyclerView.adapter = adapter
                    adapter.setOnClickListener(object : resultadosAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            //Toast.makeText(this@resultado, "clicked item no. "+ archArrayList.get(position).f_name.toString(),Toast.LENGTH_SHORT).show()
                            //add downloader
                            var nombreFile = archArrayList.get(position).f_name.toString()
                            //Toast.makeText(this@resultado, "clicked item : "+ nombreFile,Toast.LENGTH_SHORT).show()
                            downloadFiles(nombreFile)



                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Estatus", "No jalo el archdata");
            }
        }
        )
    }


}