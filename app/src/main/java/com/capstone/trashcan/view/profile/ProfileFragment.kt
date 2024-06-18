package com.capstone.trashcan.view.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.capstone.trashcan.databinding.FragmentProfileBinding
import com.capstone.trashcan.view.ViewModelFactory
import com.capstone.trashcan.view.main.MainViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class ProfileFragment : Fragment() {
    private val profileViewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

//    private lateinit var profileViewModel: ProfileViewModel

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        profileViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(ProfileViewModel::class.java)

        profileViewModel.profile.observe(viewLifecycleOwner) { profile ->
//            binding.tvName.text = profile.user. name
            binding.tvEmail.text = profile.user?.email
        }

        profileViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // Show or hide a loading indicator based on the value of isLoading
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

//        profileViewModel.error.observe(viewLifecycleOwner) { error ->
//            // Show error message
//            if (error != null) {
//                // Handle error
//            }
//        }

        fetchUserProfile()
        setupAction()
    }

    private fun fetchUserProfile() {
        profileViewModel.getProfile()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupAction() {
        binding.btnLogout.setOnClickListener {
            signOut()
        }

    }

    private fun signOut() {
        profileViewModel.logout()

    }
}