package com.example.testfarma_app

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_resultado.*
import java.text.SimpleDateFormat
import java.util.*


class resultado : AppCompatActivity() {

    private lateinit var dbref : DatabaseReference
    private lateinit var resRecyclerView : RecyclerView
    private lateinit var histRecyclerView : RecyclerView
    private lateinit var archArrayList: ArrayList<arch_resultado>
    private lateinit var archArrayListRes: ArrayList<arch_resultado>
    val resultadosRef =Firebase.storage.reference.child("Resultados")
    val storageRef = Firebase.storage.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado)

        resRecyclerView = findViewById(R.id.recycler_view_res)
        resRecyclerView.layoutManager = GridLayoutManager(this, 2)
        resRecyclerView.setHasFixedSize(true)

        histRecyclerView = findViewById(R.id.recycler_View_Hist)
        histRecyclerView.layoutManager = GridLayoutManager(this, 2)
        histRecyclerView.setHasFixedSize(true)

        archArrayListRes = arrayListOf<arch_resultado>()
        archArrayList = arrayListOf<arch_resultado>()
        getArchdata()
    }


    fun download(f_name: String,t_files: String) {
        val storageReference = FirebaseStorage.getInstance().reference
        val ref = storageReference.child("Resultados/$f_name.$t_files")
        ref.downloadUrl.addOnSuccessListener { uri ->
            val url = uri.toString()
            Toast.makeText(this@resultado, "Descargando: "+f_name+t_files, Toast.LENGTH_SHORT).show()
            downloadFiles(this@resultado, f_name, t_files, Environment.DIRECTORY_DOWNLOADS, url)
        }.addOnFailureListener { }
    }

    fun downloadFiles(
        context: Context,
        fileName: String,
        fileExtension: String,
        destinationDirectory: String?,
        url: String?
    ) {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val uri = Uri.parse(url)
        val request = DownloadManager.Request(uri)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalFilesDir(
            context,
            destinationDirectory,
            fileName + fileExtension
        )
        downloadManager.enqueue(request)
    }

    private fun getArchdata() {
        dbref = FirebaseDatabase.getInstance().getReference("Resultados/u1")
        dbref.addValueEventListener(object : ValueEventListener{


            override fun onDataChange(snapshot: DataSnapshot) {
                val sdf = SimpleDateFormat("dd-MM-yyyy")
                var fechaBase = "18-01-2010"
                var fechaArch = ""
                var fechaRec: Date = sdf.parse(fechaBase) as Date
                if (snapshot.exists()){
                    for(userSnapshot in snapshot.children){
                        val user = userSnapshot.getValue(arch_resultado::class.java)
                        archArrayList.add(user!!)
                        //var fechas = archArrayList.date.toString
                        fechaArch = user.date.toString()
                        var fechaArchivo: Date = sdf.parse(fechaArch) as Date

                        var fechaRec: Date = sdf.parse(fechaBase) as Date
                        var cmp = fechaRec.compareTo(fechaArchivo)
                        Log.i("Estatus", "fecha $fechaArch");
                        Log.i("Estatus", "cmp $cmp");

                        if (cmp < 0){
                            fechaBase = fechaArch
                        }
                    }
                    Log.i("Estatus", "-----Fecha F $fechaBase");
                    for(userSnapshot in snapshot.children) {
                        val user2 = userSnapshot.getValue(arch_resultado::class.java)
                        var fechaArch = user2?.date.toString()
                        var fechaArchivo: Date = sdf.parse(fechaArch) as Date

                        var fechaRec: Date = sdf.parse(fechaBase) as Date
                        var cmp2 = fechaRec.compareTo(fechaArchivo)
                        Log.i("Estatus", "Fecha F $fechaBase");
                        Log.i("Estatus", "fecha $fechaArch");
                        Log.i("Estatus", "cmp $cmp2");

                        if (cmp2 == 0){
                            archArrayListRes.add(user2!!)
                        }
                    }
                    var adapter =resultadosAdapter(archArrayList)
                    var adapterRes =resultadosAdapter(archArrayListRes)
                    resRecyclerView.adapter = adapterRes
                    histRecyclerView.adapter = adapter

                    adapter.setOnClickListener(object : resultadosAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            //Toast.makeText(this@resultado, "clicked item no. "+ archArrayList.get(position).f_name.toString(),Toast.LENGTH_SHORT).show()

                            var nombreFile = archArrayList.get(position).f_name.toString()
                            //Toast.makeText(this@resultado, "clicked item : "+ nombreFile,Toast.LENGTH_SHORT).show()
                            var strStringName = nombreFile.split(".").toTypedArray()
                            //Toast.makeText(this@resultado, "nombre archivo: "+nombreDarchivo+tipoDarchivo, Toast.LENGTH_SHORT).show()
                            //downloadFiles(nombreDarchivo,tipoDarchivo)
                            var nomFile = strStringName[0]
                            var tFile = strStringName[1]
                            download(nomFile, tFile)
                        }
                    })

                    adapterRes.setOnClickListener(object : resultadosAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            //Toast.makeText(this@resultado, "clicked item no. "+ archArrayList.get(position).f_name.toString(),Toast.LENGTH_SHORT).show()

                            var nombreFile2 = archArrayListRes.get(position).f_name.toString()
                            //Toast.makeText(this@resultado, "clicked item : "+ nombreFile,Toast.LENGTH_SHORT).show()
                            var strStringName2 = nombreFile2.split(".").toTypedArray()
                            //Toast.makeText(this@resultado, "nombre archivo: "+nombreDarchivo+tipoDarchivo, Toast.LENGTH_SHORT).show()
                            //downloadFiles(nombreDarchivo,tipoDarchivo)
                            var nomFile2 = strStringName2[0]
                            var tFile2 = strStringName2[1]
                            download(nomFile2, tFile2)
                        }
                    })
                }
            }


            override fun onCancelled(error: DatabaseError) {
                Log.i("Estatus", "Error de conexion");
            }
        }
        )
    }


}