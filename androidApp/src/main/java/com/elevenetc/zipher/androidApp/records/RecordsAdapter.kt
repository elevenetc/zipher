package com.elevenetc.zipher.androidApp.records

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.elevenetc.zipher.Record
import com.elevenetc.zipher.androidApp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RecordsAdapter(
    val listener: (Record) -> Unit
) : RecyclerView.Adapter<RecordsAdapter.VH>() {

    private val pages = mutableMapOf<Int, List<Record>>()
    private val total = mutableListOf<Record>()
    var allLoaded = false

    class VH(view: View) : RecyclerView.ViewHolder(view) {

        val textKey = view.findViewById<TextView>(R.id.text_key)
        val textCreationTime = view.findViewById<TextView>(R.id.text_creation_time)
        val textUpdateTime = view.findViewById<TextView>(R.id.text_update_time)
        val textValue = view.findViewById<TextView>(R.id.edit_text_value)

        fun bind(record: Record, position: Int) {
            textKey.text = "$position. " + record.id
            textCreationTime.text = record.creation_time
            textUpdateTime.text = record.update_time
            textValue.text = record.name
        }
    }

    fun add(flow: Flow<List<Record>>, page: Int) {

        CoroutineScope(Dispatchers.Main).launch {
            flow.collect {
                appendOrRefresh(it, page)
            }
        }
    }

    private fun appendOrRefresh(records: List<Record>, pageId: Int) {

        if (records.isEmpty()) {
            allLoaded = true
            return
        }

        pages[pageId] = records

        val newData = mutableListOf<Record>()

        pages.keys.sorted().forEach {
            val page = pages[it]!!
            page.forEach { record -> newData.add(record) }
        }

        insertNewDiff(newData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.rv_item_record, parent, false
        )
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val record = total[position]
        holder.bind(record, position)
        holder.itemView.setOnClickListener {
            listener(record)
        }
    }

    override fun getItemCount(): Int {
        return total.size
    }

    private fun insertNewDiff(newData: List<Record>) {

        val diffCallback = DiffCallback(newData, total) { a, b ->
            a.id == b.id
        }
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        total.clear()
        total.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
    }

    private class DiffCallback<T>(
        val newList: List<T>, val oldList: List<T>,
        val compareIds: (a: T, b: T) -> Boolean
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val newItem = newList[newItemPosition]
            val oldItem = oldList[oldItemPosition]

            val contentEqual = newItem == oldItem
            return contentEqual
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val newItem = newList[newItemPosition]
            val oldItem = oldList[oldItemPosition]

            val idsEqual = compareIds(newItem, oldItem)
            return idsEqual
        }

    }
}