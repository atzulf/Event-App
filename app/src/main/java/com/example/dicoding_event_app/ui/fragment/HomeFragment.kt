package com.example.dicoding_event_app.ui.fragment

import android.view.View
import android.os.Bundle
import android.widget.Toast
import android.view.ViewGroup
import android.widget.ProgressBar
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.navigation.fragment.findNavController
import com.example.dicoding_event_app.ui.MainViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicoding_event_app.databinding.FragmentHomeBinding
import com.example.dicoding_event_app.helpers.ConnectionHelper
import com.example.dicoding_event_app.ui.adapter.GeserEventAdapter
import com.example.dicoding_event_app.ui.adapter.ScrollEventAdapter

class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    private lateinit var gridRecyclerView: RecyclerView
    private lateinit var listRecyclerView: RecyclerView
    private lateinit var gridAdapter: GeserEventAdapter
    private lateinit var listAdapter: ScrollEventAdapter
    private lateinit var progressBar: ProgressBar
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gridRecyclerView = binding!!.gridRecyclerView
        listRecyclerView = binding!!.listRecyclerView
        progressBar = binding!!.progressBar

        gridAdapter = GeserEventAdapter(
            onClick = {
                    id -> navigateToDetail(id)
            }
        )
        listAdapter = ScrollEventAdapter(
            onClick = {
                    id -> navigateToDetail(id)
            }
        )

        gridRecyclerView.adapter = gridAdapter
        listRecyclerView.adapter = listAdapter

        val gridLayoutManager = GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
        gridRecyclerView.layoutManager = gridLayoutManager
        listRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.activeEvents.observe(viewLifecycleOwner) { events ->
            gridAdapter.setEvents(events)
        }
        viewModel.inactiveEvents.observe(viewLifecycleOwner) { events ->
            listAdapter.setEvents(events)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                progressBar.visibility = View.VISIBLE
                gridRecyclerView.visibility = View.GONE
                listRecyclerView.visibility = View.GONE
            } else {
                progressBar.visibility = View.GONE
                gridRecyclerView.visibility = View.VISIBLE
                listRecyclerView.visibility = View.VISIBLE
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.fetchEvents(1)
        viewModel.fetchEvents(0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onResume() {
        super.onResume()
        ConnectionHelper.verifyInternet(requireContext())
    }

    private fun navigateToDetail(eventId: Int?) {
        eventId?.let {
            val action = HomeFragmentDirections.actionNavigationHomeToDetailFragment(it)
            findNavController().navigate(action)
        }
    }
}