package com.capstone.trashcan.view.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.capstone.trashcan.R
import com.capstone.trashcan.data.entity.ClassificationHistoryEntity
import com.capstone.trashcan.databinding.FragmentHistoryBinding
import com.capstone.trashcan.view.HistoryViewModelFactory
import com.capstone.trashcan.view.ViewModelFactory
import com.capstone.trashcan.view.profile.ProfileViewModel

class HistoryFragment : Fragment() {
    private val viewModel by viewModels<HistoryViewModel> {
        HistoryViewModelFactory.getInstance(requireContext())
    }

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var historyAdapter: HistoryAdapter

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

        setupRecyclerView()

        viewModel.getAllHistory().observe(viewLifecycleOwner) { listHistory ->
            historyAdapter.submitList(listHistory)
        }

//        listHistory.addAll(getListHistory())
    }

//    private fun getListHistory(): ArrayList<History> {
//        val dataResult = resources.getStringArray(R.array.data_result)
//        val dataDate = resources.getStringArray(R.array.data_date)
//        val dataPhoto = resources.obtainTypedArray(R.array.data_photo)
//
//        val listHistory = ArrayList<History>()
//        for (i in dataResult.indices) {
//            val history = History(dataResult[i], dataDate[i], dataPhoto.getResourceId(i, -1))
//            listHistory.add(history)
//        }
//        dataPhoto.recycle()
//        return listHistory
//    }

    private fun setupRecyclerView() {
        historyAdapter = HistoryAdapter()
        binding.rvHistoryItem.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = historyAdapter
        }
    }

    private fun showRecyclerList(historyItems: List<ClassificationHistoryEntity>?) {
//        binding.rvHistoryItem.apply {
//            layoutManager = GridLayoutManager(context, 2)
//            adapter = HistoryAdapter(listHistory)
//        }

//        layoutManager = GridLayoutManager(context, 2)
//        val adapter = HistoryAdapter()
//        adapter.submitList(historyItems)
//        binding.rvHistoryItem.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}