package com.example.dicoding_event_app.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dicoding_event_app.ui.adapter.ScrollEventAdapter
import com.example.dicoding_event_app.databinding.FragmentUpcomingBinding
import com.example.dicoding_event_app.helpers.ConnectionHelper
import com.example.dicoding_event_app.ui.MainViewModel

class UpcomingFragment : Fragment() {

    private var binding: FragmentUpcomingBinding? = null
    private lateinit var listrecyclerView: RecyclerView
    private lateinit var listAdapter: ScrollEventAdapter
    private lateinit var progressBar: ProgressBar
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = binding!!.progressBar
        listrecyclerView = binding!!.upcomingListRecyclerView

        listAdapter = ScrollEventAdapter(
            onClick = { id ->
                navigateToDetail(id)
            }
        )
        listrecyclerView.adapter = listAdapter
        listrecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.activeEvents.observe(viewLifecycleOwner) { events ->
            listAdapter.setEvents(events)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                progressBar.visibility = View.VISIBLE
                listrecyclerView.visibility = View.GONE
            } else {
                progressBar.visibility = View.GONE
                listrecyclerView.visibility = View.VISIBLE
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.fetchEvents(1)
    }

    override fun onResume() {
        super.onResume()
        ConnectionHelper.verifyInternet(requireContext())
        viewModel.fetchEvents(1)
    }

    private fun navigateToDetail(eventId: Int?) {
        eventId?.let {
            val action = UpcomingFragmentDirections.actionNavigationUpcomingToDetailFragment(it)
            findNavController().navigate(action)
        }
    }
}
