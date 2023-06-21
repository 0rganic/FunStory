package com.example.funstory.Ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.funstory.R
import com.example.funstory.data.remote.response.Story

class StoryAdapter(private val listStory: List<Story>, private val onItemClick: (story: Story) -> Unit) : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tv_item_name)
        private val imgStory: ImageView = itemView.findViewById(R.id.item_photo)

        fun bind(story: Story) {
            tvName.text = story.name

            Glide.with(itemView.context)
                .load(story.photoUrl)
                .into(imgStory)

            itemView.setOnClickListener { onItemClick(story) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listStory.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = listStory[position]
        holder.bind(story)
    }
}