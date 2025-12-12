package com.example.hearo2.mypage.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hearo2.community.CommentDto
import com.example.hearo2.databinding.ItemCommentBinding

class MyCommentAdapter :
    RecyclerView.Adapter<MyCommentAdapter.ViewHolder>() {

    private val items = mutableListOf<CommentDto>()

    fun submitList(list: List<CommentDto>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val binding: ItemCommentBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CommentDto) {

            // 댓글 내용
            binding.tvComment.text = item.content

            // 시간
            binding.tvTime.text = item.createdAt

            // 좋아요 수
            binding.tvLikeCount.text = item.likeCount.toString()
            binding.btnCommentLike.isSelected = item.liked

            // 🔥 마이페이지에서는 기능 제거
            binding.btnReply.visibility = View.GONE
            binding.btnShowReplies.visibility = View.GONE
            binding.layoutOptions.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemCommentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
}
