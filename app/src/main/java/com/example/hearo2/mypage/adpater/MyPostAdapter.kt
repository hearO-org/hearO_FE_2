package com.example.hearo2.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hearo2.community.PostModel
import com.example.hearo2.databinding.CommunityPostItemBinding

class MyPostAdapter(
    private val onClick: (PostModel) -> Unit
) : RecyclerView.Adapter<MyPostAdapter.ViewHolder>() {

    private val items = mutableListOf<PostModel>()

    fun submitList(list: List<PostModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val binding: CommunityPostItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PostModel) {
            binding.tvTitle.text = item.title
            binding.tvContent.text = item.content
            binding.tvWriter.text = item.authorNickname   // ⭐ 실제 id 확인!
            binding.tvLike.text = item.likeCount.toString()

            binding.root.setOnClickListener {
                onClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            CommunityPostItemBinding.inflate(
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