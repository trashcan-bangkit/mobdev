package com.capstone.trashcan.view.history

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.capstone.trashcan.R
import com.capstone.trashcan.data.entity.ClassificationHistoryEntity
import com.capstone.trashcan.databinding.ItemHistoryBinding
import com.capstone.trashcan.view.classify.ResultActivity

class HistoryAdapter : ListAdapter<ClassificationHistoryEntity, HistoryAdapter.MyViewHolder>(DIFF_CALLBACK)  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, HistoryDetailActivity::class.java)
            intent.putExtra(HistoryDetailActivity.EXTRA_IMAGE_URI, history.photo)
            intent.putExtra(HistoryDetailActivity.EXTRA_MAIN_CATEGORY, history.mainCategory)
            intent.putExtra(HistoryDetailActivity.EXTRA_SUB_CATEGORY, history.subCategory)
            intent.putExtra(HistoryDetailActivity.EXTRA_DESCRIPTION, history.description)
            intent.putExtra(HistoryDetailActivity.EXTRA_DATE, history.date)
            val nonNullRecommendations = history.recommendations?.filterNotNull() ?: emptyList()
            intent.putStringArrayListExtra(HistoryDetailActivity.EXTRA_RECOMMENDATIONS, ArrayList(nonNullRecommendations))
            holder.itemView.context.startActivity(intent)
        }
    }

    class MyViewHolder(val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(history: ClassificationHistoryEntity){
            binding.tvLabel.text = history.mainCategory
            binding.tvSublabel.text = history.subCategory
            binding.tvTimestamp.text = history.date
            Glide.with(binding.root)
                .load(history.photo)
                .into(binding.imgCenter)
        }

    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ClassificationHistoryEntity>() {
            override fun areItemsTheSame(oldItem: ClassificationHistoryEntity, newItem: ClassificationHistoryEntity): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ClassificationHistoryEntity, newItem: ClassificationHistoryEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}

//class HistoryAdapter(private val listHistory: ArrayList<History>) : RecyclerView.Adapter<HistoryAdapter.ListViewHolder>() {
//    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val imgPhoto: ImageView = itemView.findViewById(R.id.img_center)
//        val tvResult: TextView = itemView.findViewById(R.id.tv_label)
//        val tvTimestamp: TextView = itemView.findViewById(R.id.tv_timestamp)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
//        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
//        return ListViewHolder(view)
//    }
//
//    override fun getItemCount(): Int = listHistory.size
//
//
//    override fun onBindViewHolder(holder: HistoryAdapter.ListViewHolder, position: Int) {
//        val (result, timestamp, photo) = listHistory[position]
//        holder.imgPhoto.setImageResource(photo)
//        holder.tvResult.text = result
//        holder.tvTimestamp.text = timestamp
//
////        holder.itemView.setOnClickListener {
////            val intentDetail = Intent(holder.itemView.context, IdolDetail::class.java)
////            intentDetail.putExtra(IdolDetail.EXTRA_IDOL, listIdol[holder.adapterPosition])
////            holder.itemView.context.startActivity(intentDetail)
////        }
//
//    }
//}