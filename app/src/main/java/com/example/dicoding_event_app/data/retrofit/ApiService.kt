package com.example.dicoding_event_app.data.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import com.example.dicoding_event_app.data.response.EventResponse
import com.example.dicoding_event_app.data.response.DetailEventResponse

interface ApiService {
    @GET("events/{id}")
    fun getEventDetail(
        @Path("id") id: String
    ): Call<DetailEventResponse>

    @GET("events")
    fun getEvent(
        @Query("active") active: Int
    ): Call<EventResponse>

    @GET("events")
    fun searchEvent(
        @Query("active") active: Int,
        @Query("q") query: String
    ): Call<EventResponse>
}