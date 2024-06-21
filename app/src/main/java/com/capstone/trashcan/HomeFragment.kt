package com.capstone.trashcan

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.trashcan.databinding.FragmentHomeBinding
import com.capstone.trashcan.view.HistoryViewModelFactory
import com.capstone.trashcan.view.ViewModelFactory
import com.capstone.trashcan.view.history.HistoryAdapter
import com.capstone.trashcan.view.history.HistoryViewModel
import com.capstone.trashcan.view.wastebank.WasteBankViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModelFactory by lazy {
        ViewModelFactory.getInstance(requireContext())
    }

    private val historyViewModel: HistoryViewModel by viewModels {
        HistoryViewModelFactory.getInstance(requireContext())
    }
    private lateinit var historyAdapter: HistoryAdapter

    private val wasteBankViewModel: WasteBankViewModel by viewModels { viewModelFactory }

    private lateinit var wasteBankAdapter: WasteBankAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        historyAdapter = HistoryAdapter()
        binding.rvHistoryItem.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = historyAdapter
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        wasteBankAdapter = WasteBankAdapter()
        binding.wasteBanksList.layoutManager = LinearLayoutManager(requireContext())
        binding.wasteBanksList.adapter = wasteBankAdapter

        historyViewModel.getAllHistory().observe(viewLifecycleOwner, Observer { listHistory ->
            Log.d("HistoryFragment", "History items count: ${listHistory.size}")
            val totalScansText = "So far you've scanned ${listHistory.size} trash."
            binding.scanCountTotal.text = totalScansText
            val lastTwoHistory = if (listHistory.size >= 2) {
                listHistory.subList(listHistory.size - 2, listHistory.size)
            } else {
                listHistory
            }
            historyAdapter.submitList(lastTwoHistory)
        })

        historyViewModel.getNonOrganicCount().observe(viewLifecycleOwner, Observer { count ->
            binding.nonOrganicCount.text = count.toString()
        })

        historyViewModel.getOrganicCount().observe(viewLifecycleOwner, Observer { count ->
            binding.organicCount.text = count.toString()
        })

        historyViewModel.getToxicCount().observe(viewLifecycleOwner, Observer { count ->
            binding.toxicCount.text = count.toString()
        })

        wasteBankViewModel.loadingState.observe(viewLifecycleOwner, Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        wasteBankViewModel.errorMessage.observe(viewLifecycleOwner, { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        })

        wasteBankViewModel.otherWasteBanks.observe(viewLifecycleOwner, { others ->
            others?.let {
                wasteBankAdapter.submitList(it)
            }
        })

        wasteBankViewModel.filteredWasteBanks.observe(viewLifecycleOwner, Observer { wasteBanks ->
            wasteBanks?.let {
                wasteBankAdapter.submitList(it)
            }
        })

        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            getMyLastLocation()
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLastLocation()
                }
                else -> {
                    // No location access granted.
                    Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    Log.d("LocationFragment", "Latitude: ${location.latitude}, Longitude: ${location.longitude}")

                    val latLngString = "${location.latitude}, ${location.longitude}"
                    wasteBankViewModel.fetchNearbyWasteBanks(latLngString)
                } else {
                    Toast.makeText(requireContext(), "Location is not found. Try Again", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to get location", Toast.LENGTH_SHORT).show()
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
