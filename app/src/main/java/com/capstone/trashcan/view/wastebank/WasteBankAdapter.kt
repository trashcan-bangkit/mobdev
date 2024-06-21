package com.capstone.trashcan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.trashcan.databinding.ItemWastebankBinding
import com.capstone.trashcan.data.response.OthersItem

class WasteBankAdapter : RecyclerView.Adapter<WasteBankAdapter.ViewHolder>() {

    private val wasteBanks = mutableListOf<OthersItem?>()

    fun submitList(newWasteBanks: List<OthersItem?>?) {
        wasteBanks.clear()
        if (newWasteBanks != null) {
            wasteBanks.addAll(newWasteBanks)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWastebankBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val wasteBank = wasteBanks[position]
        wasteBank?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = wasteBanks.size

    inner class ViewHolder(private val binding: ItemWastebankBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(wasteBank: OthersItem) {
            binding.tvItemName.text = wasteBank.name
            binding.tvItemDistance.text = wasteBank.distance.toString()
            binding.tvItemAddress.text = wasteBank.address
        }
    }
}
