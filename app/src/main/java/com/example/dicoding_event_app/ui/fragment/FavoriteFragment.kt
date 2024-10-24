package com.example.dicoding_event_app.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dicoding_event_app.databinding.FragmentFavoriteBinding
import com.example.dicoding_event_app.ui.MainViewModel
import com.example.dicoding_event_app.ui.adapter.ScrollEventAdapter
import com.example.dicoding_event_app.helpers.ConnectionHelper

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()
    private lateinit var listrecyclerView: RecyclerView
    private lateinit var listadapter: ScrollEventAdapter
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi RecyclerView dan Adapter
        listadapter = ScrollEventAdapter { detailDataEntity ->
            val intent = Intent(requireContext(), ScrollEventAdapter::class.java)
            intent.putExtra("EXTRA_ENTITY_DETAIL", detailDataEntity)
            startActivity(intent)
        }

        // Setup RecyclerView
        binding.rvFavorite.adapter = listadapter
        binding.rvFavorite.layoutManager = LinearLayoutManager(requireContext())

//        progressBar = binding!!.progressBar
//        listrecyclerView = binding!!.finishedListRecyclerView
//
//        // Observe ViewModel untuk data favorite
//        viewModel.getAllEvents().observe(viewLifecycleOwner) { events ->
//            if (events != null) {
//                adapter.submitList(events)
//                toggleLoading(false)
//            } else {
//                toggleLoading(true)
//            }
//        }

        // Handle error
        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                toggleLoading(false)
            }
        }

        // Cek koneksi Internet
        ConnectionHelper.verifyInternet(requireContext())
    }

    private fun toggleLoading(isLoading: Boolean) {
        if (isLoading) {
            progressBar.visibility = View.VISIBLE
            binding.rvFavorite.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            binding.rvFavorite.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
