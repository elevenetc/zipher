package com.elevenetc.zipher.androidApp.records

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.elevenetc.zipher.Record
import com.elevenetc.zipher.androidApp.R

class RecordsAdapter(
    val records: List<Record>,
    val listener: (Record) -> Unit
) : RecyclerView.Adapter<RecordsAdapter.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {

        val textKey = view.findViewById<TextView>(R.id.text_key)
        val textValue = view.findViewById<TextView>(R.id.edit_text_value)

        fun bind(record: Record) {
            textKey.text = record.id
            textValue.text = record.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.rv_item_record, parent, false)



        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val record = records[position]
        holder.bind(record)
        holder.itemView.setOnClickListener {
            listener(record)
        }
    }

    override fun getItemCount(): Int {
        return records.size
    }
}