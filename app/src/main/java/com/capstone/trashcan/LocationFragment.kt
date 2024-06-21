package com.capstone.trashcan

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.trashcan.databinding.FragmentLocationBinding
import com.capstone.trashcan.view.ViewModelFactory
import com.capstone.trashcan.view.wastebank.WasteBankViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import java.util.Locale

class LocationFragment : Fragment() {

    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding!!

    private val viewModelFactory by lazy {
        ViewModelFactory.getInstance(requireContext())
    }

    private val wasteBankViewModel: WasteBankViewModel by viewModels { viewModelFactory }

    private lateinit var wasteBankAdapter: WasteBankAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        wasteBankAdapter = WasteBankAdapter()
        binding.wasteBanksList.layoutManager = LinearLayoutManager(requireContext())
        binding.wasteBanksList.adapter = wasteBankAdapter

        wasteBankViewModel.loadingState.observe(viewLifecycleOwner, Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        wasteBankViewModel.errorMessage.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })

        wasteBankViewModel.nearestWasteBank.observe(viewLifecycleOwner, { nearest ->
            nearest?.let {
                binding.nearestWasteBankName.text = it.name
                binding.nearestWasteBankDistance.text = it.distance.toString()
                binding.nearestWasteBankAddress.text = it.address
            }
        })

        wasteBankViewModel.otherWasteBanks.observe(viewLifecycleOwner, { others ->
            wasteBankAdapter.submitList(others)
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
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                else -> {
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

                    val latLngString = "${location.latitude}" + ", " +"${location.longitude}"
                    wasteBankViewModel.fetchWasteBanks(latLngString)

                    val geocoder = Geocoder(requireContext(), Locale.getDefault())
                    val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    val cityName = addresses?.get(0)?.locality
                    binding.location.text = cityName ?: getString(R.string.location_not_found)
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

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}