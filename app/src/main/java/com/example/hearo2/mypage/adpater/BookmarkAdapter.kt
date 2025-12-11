package com.example.hearo2.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hearo2.databinding.ItemActivityBookmarkBinding
import com.example.hearo2.mypage.model.PostBookmark

class BookmarkAdapter(private val items: List<PostBookmark>) :
    RecyclerView.Adapter<BookmarkAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemActivityBookmarkBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PostBookmark) {
            binding.tvCategory.text = item.category
            binding.tvTitle.text = item.title
            binding.tvTime.text = item.time
            binding.tvViews.text = "${item.views} 조회"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemActivityBookmarkBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
}
