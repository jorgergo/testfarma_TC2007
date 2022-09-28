package com.example.testfarma_app

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class resultado : AppCompatActivity() {

    private lateinit var dbref : DatabaseReference
    private lateinit var resRecyclerView : RecyclerView
    private lateinit var archArrayList: ArrayList<arch_resultado>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado)

        resRecyclerView = findViewById(R.id.recycler_view_res)
        resRecyclerView.layoutManager = GridLayoutManager(this, 2)
        resRecyclerView.setHasFixedSize(true)

        archArrayList = arrayListOf<arch_resultado>()
        getArchdata()

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
                            Toast.makeText(this@resultado, "clicked item no. "+ archArrayList.get(position),Toast.LENGTH_SHORT).show()
                            //add downloader
                            
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