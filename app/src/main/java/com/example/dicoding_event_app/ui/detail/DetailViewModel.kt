package com.example.dicoding_event_app.ui.detail

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicoding_event_app.data.retrofit.ApiConfig
import com.example.dicoding_event_app.data.response.ListEventsItem
import com.example.dicoding_event_app.data.response.DetailEventResponse

class DetailViewModel: ViewModel() {
    private val _event = MutableLiveData<ListEventsItem>()
    val event: LiveData<ListEventsItem> get() = _event

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchEvent(id: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getEventDetail(id)
        client.enqueue(object: Callback<DetailEventResponse> {
            override fun onResponse(
                call: Call<DetailEventResponse>,
                response: Response<DetailEventResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val body = response.body()
                    _event.value = body?.event
                } else {
                    _error.value = "Error: ${response.message()}"
                }
            }
            override fun onFailure(call: Call<DetailEventResponse>, t: Throwable) {
                _isLoading.value = false
                _error.value = "Error: ${t.message}"
            }
        })
    }
}