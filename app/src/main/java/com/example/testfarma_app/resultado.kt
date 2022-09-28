package com.example.testfarma_app

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
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
        resRecyclerView.layoutManager = LinearLayoutManager(this)
        resRecyclerView.setHasFixedSize(true)

        archArrayList = arrayListOf<arch_resultado>()
        getArchdata()

    }

    private fun getArchdata() {
        dbref = FirebaseDatabase.getInstance().getReference("Resultados/u2")
        dbref.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for(userSnapshot in snapshot.children){
                        val user = userSnapshot.getValue(arch_resultado::class.java)
                        archArrayList.add(user!!)
                    }
                    resRecyclerView.adapter = resultadosAdapter(archArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Estatus", "No jalo el archdata");
            }
        }
        )
    }


}