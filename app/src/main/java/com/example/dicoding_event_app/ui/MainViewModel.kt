package com.example.dicoding_event_app.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicoding_event_app.data.response.EventResponse
import com.example.dicoding_event_app.data.response.ListEventsItem
import com.example.dicoding_event_app.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel: ViewModel() {
    private val _activeEvents = MutableLiveData<List<ListEventsItem>>()
    val activeEvents: LiveData<List<ListEventsItem>> get() = _activeEvents

    private val _inactiveEvents = MutableLiveData<List<ListEventsItem>>()
    val inactiveEvents: LiveData<List<ListEventsItem>> get() = _inactiveEvents

    private val _error = MutableLiveData<Error?>()
    val error: MutableLiveData<Error?> get() = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _searchResults = MutableLiveData<List<ListEventsItem>>()
    val searchResults: LiveData<List<ListEventsItem>> get() = _searchResults

    private var isSearching = false

    fun fetchEvents(activeStatus: Int) {
        isSearching = false
        _isLoading.value = true
        viewModelScope.launch {
            ApiConfig.getApiService().getEvent(activeStatus).enqueue(object : Callback<EventResponse> {
                override fun onResponse(
                    call: Call<EventResponse>,
                    response: Response<EventResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        handleSuccessResponse(activeStatus, response.body()?.listEvents ?: emptyList())
                    } else {
                        handleErrorResponse(response.message())
                    }
                }

                override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                    _isLoading.value = false
                    _error.value = Error("Failure: ${t.message}")
                }
            })
        }
    }

    private fun handleSuccessResponse(activeStatus: Int, events: List<ListEventsItem>) {
        if (activeStatus == 1) {
            _activeEvents.value = events
        } else {
            _inactiveEvents.value = events
        }
    }

    private fun handleErrorResponse(message: String) {
        _error.value = Error("API Error: $message")
    }

    fun searchEvents(active: Int, query: String) {
        isSearching = true
        _isLoading.value = true
        viewModelScope.launch {
            ApiConfig.getApiService().searchEvent(active, query).enqueue(object : Callback<EventResponse> {
                override fun onResponse(
                    call: Call<EventResponse>,
                    response: Response<EventResponse>
                ) {
                    _isLoading.value = false
                    val events = response.body()?.listEvents ?: emptyList()
                    _searchResults.value = events
                    if (events.isEmpty()) {
                        _error.value = Error("Dicoding Event Not Found!")
                    }
                }

                override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                    _isLoading.value = false
                    _error.value = Error("Failure: ${t.message}")
                }
            })
        }
    }

    fun clearError() {
        _error.value = null
        _searchResults.value = emptyList()
        _isLoading.value = false
        isSearching = false
    }
}