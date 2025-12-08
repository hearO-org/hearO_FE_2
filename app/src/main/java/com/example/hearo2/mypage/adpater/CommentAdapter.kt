package com.example.hearo2.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hearo2.databinding.ItemActivityCommentBinding
import com.example.hearo2.mypage.model.Comment

class CommentAdapter(
    private val items: List<Comment>
) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    inner class CommentViewHolder(
        private val binding: ItemActivityCommentBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Comment) {
            binding.tvCommentCategory.text = item.category
            binding.tvCommentContent.text = item.content
            binding.tvCommentTime.text = item.time

            // 대댓글 있으면서 텍스트 존재할 때만 표시
            if (!item.reply.isNullOrEmpty()) {
                binding.tvCommentReply.text = item.reply
                binding.tvCommentReply.visibility = android.view.View.VISIBLE
            } else {
                binding.tvCommentReply.visibility = android.view.View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = ItemActivityCommentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
