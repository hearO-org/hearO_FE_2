package com.example.hearo2.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hearo2.databinding.ItemPostBinding
import com.example.hearo2.mypage.model.Post

class PostAdapter(
    private var items: List<Post>
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    inner class PostViewHolder(val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val item = items[position]

        holder.binding.tvCategory.text = item.category
        holder.binding.tvTitle.text = item.title
        holder.binding.tvTime.text = item.time
        holder.binding.tvViews.text = item.views.toString()
        holder.binding.tvLikes.text = item.likes.toString()
        holder.binding.tvComments.text = item.comments.toString()
    }

    override fun getItemCount() = items.size
}
