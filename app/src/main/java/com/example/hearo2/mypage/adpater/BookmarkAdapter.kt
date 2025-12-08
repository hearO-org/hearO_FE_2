package com.example.hearo2.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hearo2.databinding.ItemActivityBookmarkBinding
import com.example.hearo2.mypage.model.MyActivityItem

class BookmarkAdapter(
    private val items: List<MyActivityItem>
) : RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder>() {

    inner class BookmarkViewHolder(
        private val binding: ItemActivityBookmarkBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MyActivityItem) {
            binding.tvTitle.text = item.title
            binding.tvCategory.text = item.category
            binding.tvTime.text = item.date
            binding.tvViews.text = "${item.viewCount} 조회"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val binding = ItemActivityBookmarkBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BookmarkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
