package com.codeplay.aintrealname.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.codeplay.aintrealname.R
import com.codeplay.aintrealname.models.Disease


class DiseaseAdapter(val context: Context, val itemClick:(Disease, View) -> Unit) : RecyclerView.Adapter<DiseaseAdapter.DiseaseViewHolder>() {

    var mList = ArrayList<Disease>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiseaseViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.list_disease_item, parent, false)
        return DiseaseViewHolder(itemView, itemClick)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: DiseaseViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    fun swapList(list: List<Disease>){

        mList.clear()
        mList.addAll(list)
        notifyDataSetChanged()
    }

    fun clear(){
        mList.clear()
        notifyDataSetChanged()
    }
    inner class DiseaseViewHolder(itemView: View, private val itemClick: (Disease, View) -> Unit) : RecyclerView.ViewHolder(itemView){
        val nameTextView = itemView.findViewById<TextView>(R.id.prescriptionNameTextView)
        val startDateTextView = itemView.findViewById<TextView>(R.id.startDateTextView)
        val endDateTextView = itemView.findViewById<TextView>(R.id.endDateTextView)

        fun bind(entry: Disease){
            nameTextView.text = entry.name
            startDateTextView.text = entry.startDate
            endDateTextView.text = entry.endDate

            itemView.setOnClickListener {
                itemClick(entry, itemView)
            }
        }
    }
}