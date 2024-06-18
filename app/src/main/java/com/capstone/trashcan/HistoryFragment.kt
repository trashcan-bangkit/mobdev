package com.capstone.trashcan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.trashcan.databinding.FragmentHistoryBinding
import com.capstone.trashcan.view.history.History
import com.capstone.trashcan.view.history.HistoryAdapter

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val listHistory = ArrayList<History>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listHistory.addAll(getListHistory())
        showRecyclerList()
    }

    private fun getListHistory(): ArrayList<History> {
        val dataResult = resources.getStringArray(R.array.data_result)
        val dataDate = resources.getStringArray(R.array.data_date)
        val dataPhoto = resources.obtainTypedArray(R.array.data_photo)

        val listHistory = ArrayList<History>()
        for (i in dataResult.indices) {
            val history = History(dataResult[i], dataDate[i], dataPhoto.getResourceId(i, -1))
            listHistory.add(history)
        }
        dataPhoto.recycle()
        return listHistory
    }

    private fun showRecyclerList() {
        binding.rvHistoryItem.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = HistoryAdapter(listHistory)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}