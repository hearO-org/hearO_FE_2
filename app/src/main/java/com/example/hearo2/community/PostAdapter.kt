package com.example.hearo2.community

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hearo2.databinding.CommunityPostItemBinding

class PostAdapter(
    private val items: List<PostModel>,
    private val onClick: (PostModel) -> Unit
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    inner class PostViewHolder(val binding: CommunityPostItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PostModel) {
            binding.tvCategory.text = "#${item.category}"
            binding.tvTitle.text = item.title
            binding.tvContent.text = item.content
            binding.tvWriter.text = item.writer
            binding.tvViews.text = item.views.toString()
            binding.tvLike.text = item.likes.toString()
            binding.tvCommentCount.text = item.comments.toString()

            binding.root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            CommunityPostItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(items[position])
    }
}
