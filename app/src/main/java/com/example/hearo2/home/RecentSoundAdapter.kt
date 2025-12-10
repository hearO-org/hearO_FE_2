package com.example.hearo2.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hearo2.databinding.ItemRecentSoundBinding

class RecentSoundAdapter(
    private val items: MutableList<RecentSoundData>
) : RecyclerView.Adapter<RecentSoundAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemRecentSoundBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RecentSoundData) {
            binding.imgIcon.setImageResource(item.iconRes)
            binding.tvTitle.text = item.title
            binding.tvAccuracy.text = item.accuracy
            binding.tvTime.text = item.time

            if (item.tag != null) {
                binding.tvTag.text = item.tag
                binding.tvTag.visibility = View.VISIBLE
            } else {
                binding.tvTag.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecentSoundBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun addItem(item: RecentSoundData) {
        items.add(0, item)
        notifyItemInserted(0)
    }
}
