package com.codeplay.aintrealname.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.codeplay.aintrealname.R
import com.codeplay.aintrealname.models.Perscription

class PrescriptionAdapter (val context: Context, val itemClick:(Perscription, View) -> Unit) : RecyclerView.Adapter<PrescriptionAdapter.DiseaseViewHolder>() {

    val mList = ArrayList<Perscription>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiseaseViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.list_prescription_item, parent, false)
        return DiseaseViewHolder(itemView, itemClick)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: DiseaseViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    fun swapList(list: List<Perscription>){
        mList.clear()
        mList.addAll(list)
        notifyDataSetChanged()
    }

    inner class DiseaseViewHolder(itemView: View, private val itemClick: (Perscription, View) -> Unit) : RecyclerView.ViewHolder(itemView){
        val nameTextView = itemView.findViewById<TextView>(R.id.prescriptionNameTextView)
        val morning = itemView.findViewById<TextView>(R.id.morningView)
        val afternoon = itemView.findViewById<TextView>(R.id.afternoonView)
        val evening = itemView.findViewById<TextView>(R.id.eveningView)
        val night = itemView.findViewById<TextView>(R.id.nightView)

        fun bind(entry: Perscription){
            nameTextView.text = entry.medicineName

            if(entry.morning > 0){
                morning.text = "Morning " + entry.morning
                morning.visibility = View.VISIBLE
            } else{
                morning.visibility = View.GONE
            }

            if(entry.afternoon > 0){
                afternoon.text = "Afternoon " + entry.afternoon
                afternoon.visibility = View.VISIBLE
            } else{
                afternoon.visibility = View.GONE
            }

            if(entry.evening > 0){
                evening.text = "Evening " + entry.evening
                evening.visibility = View.VISIBLE
            } else{
                evening.visibility = View.GONE
            }

            if(entry.night > 0){
                night.text = "Night " + entry.night
                night.visibility = View.VISIBLE
            } else{
                night.visibility = View.GONE
            }
        }
    }
}