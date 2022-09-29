package com.example.testfarma_app

//import kotlinx.coroutines.tasks.await
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage


class resultado : AppCompatActivity() {

    private lateinit var dbref : DatabaseReference
    private lateinit var resRecyclerView : RecyclerView
    private lateinit var archArrayList: ArrayList<arch_resultado>
    val resultadosRef =Firebase.storage.reference.child("Resultados")
    val storageRef = Firebase.storage.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado)

        resRecyclerView = findViewById(R.id.recycler_view_res)
        resRecyclerView.layoutManager = GridLayoutManager(this, 2)
        resRecyclerView.setHasFixedSize(true)

        archArrayList = arrayListOf<arch_resultado>()
        getArchdata()

    }

    /*private fun downloadFiles(f_name: String, t_file: String) = CoroutineScope(Dispatchers.IO).launch {

        val storage = FirebaseStorage.getInstance()
        val ref = storage.reference.child("Resultados/prueba.pdf")
        try {
            val streamSnapshot = Tasks.await(ref.stream)
            withContext(Dispatchers.Main){
                Toast.makeText(this@resultado,"Descargaste"+f_name,Toast.LENGTH_SHORT).show()
            }

            }

        catch (e: Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(this@resultado,e.message,Toast.LENGTH_SHORT).show()
            }
        }
    }*/


    fun download(f_name: String) {
        val storageReference = FirebaseStorage.getInstance().reference
        val ref = storageReference.child("Resultados/$f_name")
        ref.downloadUrl.addOnSuccessListener { uri ->
            val url = uri.toString()
            downloadFiles(this@resultado, "prueba", ".pdf", Environment.DIRECTORY_DOWNLOADS, url)
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
                            var strStringName = nombreFile.split(".").toTypedArray()
                            var nombreDarchivo = strStringName[0].toString()
                            var tipoDarchivo = strStringName[1].toString()
                            //Toast.makeText(this@resultado, "nombre archivo: "+nombreDarchivo+tipoDarchivo, Toast.LENGTH_SHORT).show()
                            //downloadFiles(nombreDarchivo,tipoDarchivo)
                            download(nombreFile)



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