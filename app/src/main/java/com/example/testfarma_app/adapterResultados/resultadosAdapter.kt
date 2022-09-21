package com.example.testfarma_app.adapterResultados

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testfarma_app.R
import com.example.testfarma_app.resultadosModel.resultadosArchivos

class resultadosAdapter(
    private val context: Context,
    private val dataset:List<resultadosArchivos>
    ) :RecyclerView.Adapter<resultadosAdapter.resultadosViewHolder>(){

    class resultadosViewHolder(private val view: View): RecyclerView.ViewHolder(view){
        val textView: TextView = view.findViewById(R.id.item_title)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): resultadosViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_resultado, parent, false)

        return resultadosViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: resultadosViewHolder, position: Int) {
        val item = dataset[position]
        holder.textView.text = context.resources.getString(item.stringResourceId)

    }

    override fun getItemCount() = dataset.size
}