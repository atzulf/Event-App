package com.example.dicoding_event_app.ui.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import com.example.dicoding_event_app.R
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicoding_event_app.data.response.ListEventsItem
import com.example.dicoding_event_app.helpers.DateHelper.format

class ScrollEventAdapter(private val onClick: (Int?) -> Unit) :
    RecyclerView.Adapter<ScrollEventAdapter.EventResponseViewHolder>() {

    private val events = mutableListOf<ListEventsItem>()
    @SuppressLint("NotifyDataSetChanged")
    fun setEvents(newEvents: List<ListEventsItem>) {
        events.clear()
        events.addAll(newEvents)
        notifyDataSetChanged()
    }

    class EventResponseViewHolder(
        itemView: View,
        private val onClick: (Int?) -> Unit,
    ) : RecyclerView.ViewHolder(itemView) {
        private val eventName: TextView = itemView.findViewById(R.id.tv_event_name)
        private val eventOwner: TextView = itemView.findViewById(R.id.tv_event_ownerName)
        private val eventBeginTime: TextView = itemView.findViewById(R.id.tv_event_beginTime)
        private val eventQuota: TextView = itemView.findViewById(R.id.tv_event_quota)
        private val eventImage: ImageView = itemView.findViewById(R.id.iv_image_logo)

        fun bind(item: ListEventsItem) {
            eventName.text = item.name
            eventOwner.text = item.ownerName
            eventBeginTime.text = format(item.beginTime)
            "${item.registrants}/${item.quota}".also { eventQuota.text = it }

            Glide.with(itemView.context)
                .load(item.imageLogo)
                .into(eventImage)

            itemView.setOnClickListener {
                onClick(item.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventResponseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.scroll_event, parent, false)
        return EventResponseViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: EventResponseViewHolder, position: Int) {
        val item = events[position]
        holder.bind(item)
    }

    override fun getItemCount() = events.size
}