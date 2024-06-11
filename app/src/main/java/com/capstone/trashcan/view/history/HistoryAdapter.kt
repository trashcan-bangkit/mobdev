package com.capstone.trashcan.view.history

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.trashcan.R

class HistoryAdapter(private val listHistory: ArrayList<History>) : RecyclerView.Adapter<HistoryAdapter.ListViewHolder>() {
    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.img_center)
        val tvResult: TextView = itemView.findViewById(R.id.tv_label)
        val tvTimestamp: TextView = itemView.findViewById(R.id.tv_timestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listHistory.size


    override fun onBindViewHolder(holder: HistoryAdapter.ListViewHolder, position: Int) {
        val (result, timestamp, photo) = listHistory[position]
        holder.imgPhoto.setImageResource(photo)
        holder.tvResult.text = result
        holder.tvTimestamp.text = timestamp

//        holder.itemView.setOnClickListener {
//            val intentDetail = Intent(holder.itemView.context, IdolDetail::class.java)
//            intentDetail.putExtra(IdolDetail.EXTRA_IDOL, listIdol[holder.adapterPosition])
//            holder.itemView.context.startActivity(intentDetail)
//        }

    }
}