package com.example.hearo2.community

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hearo2.R
import com.example.hearo2.databinding.ItemCommentBinding

class CommentAdapter(
    private val onEdit: (CommentModel) -> Unit,
    private val onDelete: (CommentModel) -> Unit,
    private val onReply: (CommentModel) -> Unit,
    private val onLike: (CommentModel) -> Unit,
    private val onShowReplies: (CommentModel) -> Unit
) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    private val items = mutableListOf<CommentModel>()

    fun submitList(list: List<CommentModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val binding: ItemCommentBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CommentModel) {

            binding.tvComment.text = item.content
            binding.tvTime.text = item.time

            // 좋아요 수
            binding.tvLikeCount.text = item.likeCount.toString()

            // 좋아요 아이콘
            binding.btnCommentLike.setImageResource(
                if (item.isLiked)
                    R.drawable.ic_heart_filled
                else
                    R.drawable.ic_heart_empty
            )

            binding.btnEditComment.setOnClickListener { onEdit(item) }
            binding.btnDeleteComment.setOnClickListener { onDelete(item) }
            binding.btnReply.setOnClickListener { onReply(item) }
            binding.btnCommentLike.setOnClickListener { onLike(item) }
            binding.btnShowReplies.setOnClickListener { onShowReplies(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCommentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
}
