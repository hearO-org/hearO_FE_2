package com.example.hearo2.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hearo2.databinding.ItemActivityLikeBinding
import com.example.hearo2.mypage.model.MyActivityItem

class LikeAdapter(
    private val items: List<MyActivityItem>
) : RecyclerView.Adapter<LikeAdapter.LikeViewHolder>() {

    inner class LikeViewHolder(
        private val binding: ItemActivityLikeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MyActivityItem) {
            binding.tvTitle.text = item.title
            binding.tvCategory.text = item.category
            binding.tvLikes.text = "${item.likeCount} ❤️"
            binding.tvTime.text = item.date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikeViewHolder {
        val binding = ItemActivityLikeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LikeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LikeViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
