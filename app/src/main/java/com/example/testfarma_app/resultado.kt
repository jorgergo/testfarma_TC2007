package com.example.testfarma_app

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
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
    private  lateinit var uidtxtview: TextView
    private lateinit var archArrayList: ArrayList<arch_resultado>
    private lateinit var archArrayListRes: ArrayList<arch_resultado>
    val resultadosRef =Firebase.storage.reference.child("Resultados")
    val storageRef = Firebase.storage.reference

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado)

        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid

        val userIdn = uid.toString()

        uidtxtview = findViewById(R.id.userID)

        uidtxtview.setText(userIdn)

        resRecyclerView = findViewById(R.id.recycler_view_res)
        resRecyclerView.layoutManager = GridLayoutManager(this, 2)
        resRecyclerView.setHasFixedSize(true)

        histRecyclerView = findViewById(R.id.recycler_View_Hist)
        histRecyclerView.layoutManager = GridLayoutManager(this, 2)
        histRecyclerView.setHasFixedSize(true)

        archArrayListRes = arrayListOf<arch_resultado>()
        archArrayList = arrayListOf<arch_resultado>()
        getArchdata(userIdn)

        menu.setOnClickListener {
            val intent = Intent(this, MenuApp::class.java)
            startActivity(intent)
            finish()
        }
        perfil.setOnClickListener {
            val intent = Intent(this, UserDataInfo::class.java)
            startActivity(intent)
            finish()
        }
    }


    fun download(f_name: String,t_files: String, userid: String) {
        val storageReference = FirebaseStorage.getInstance().reference
        val ref = storageReference.child("Resultados/usuarios/$userid/$f_name.$t_files")
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

    private fun getArchdata(userIdn: String) {
        dbref = FirebaseDatabase.getInstance().getReference("Resultados/$userIdn")
        dbref.addValueEventListener(object : ValueEventListener{


            @SuppressLint("SimpleDateFormat")
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
                        if (cmp < 0){
                            fechaBase = fechaArch
                        }
                    }
                    for(userSnapshot in snapshot.children) {
                        val user2 = userSnapshot.getValue(arch_resultado::class.java)
                        var fechaArch = user2?.date.toString()
                        var fechaArchivo: Date = sdf.parse(fechaArch) as Date
                        var fechaRec: Date = sdf.parse(fechaBase) as Date
                        var cmp2 = fechaRec.compareTo(fechaArchivo)

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
                            var nombreFile = archArrayList.get(position).f_name.toString()
                            val lastIndexOf: Int = nombreFile.lastIndexOf(".")
                            var nomFile = nombreFile.substring(0,lastIndexOf)
                            var tFile = nombreFile.substring(lastIndexOf+1, nombreFile.length)
                            Log.i("Estatus", "tFile $tFile")
                            download(nomFile, tFile, userIdn)
                        }
                    })

                    adapterRes.setOnClickListener(object : resultadosAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            var nombreFile2 = archArrayListRes.get(position).f_name.toString()
                            val lastIndexOf: Int = nombreFile2.lastIndexOf(".")
                            var nomFile = nombreFile2.substring(0,lastIndexOf)
                            var tFile = nombreFile2.substring(lastIndexOf+1, nombreFile2.length)
                            download(nomFile, tFile, userIdn)
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Estatus", "Error de conexion")
            }
        }
        )
    }


}