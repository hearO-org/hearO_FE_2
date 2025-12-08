package com.example.hearo2.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hearo2.databinding.ItemActivityBookmarkBinding
import com.example.hearo2.databinding.ItemActivityCommentBinding
import com.example.hearo2.databinding.ItemActivityLikeBinding
import com.example.hearo2.mypage.model.MyActivityItem

class MyActivityAdapter(
    private val items: List<MyActivityItem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_BOOKMARK = 0
        private const val TYPE_LIKE = 1
        private const val TYPE_COMMENT = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position].type) {
            MyActivityItem.Type.BOOKMARK -> TYPE_BOOKMARK
            MyActivityItem.Type.LIKE -> TYPE_LIKE
            MyActivityItem.Type.COMMENT -> TYPE_COMMENT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {

            TYPE_BOOKMARK -> {
                val binding = ItemActivityBookmarkBinding.inflate(inflater, parent, false)
                BookmarkViewHolder(binding)
            }

            TYPE_LIKE -> {
                val binding = ItemActivityLikeBinding.inflate(inflater, parent, false)
                LikeViewHolder(binding)
            }

            else -> { // TYPE_COMMENT
                val binding = ItemActivityCommentBinding.inflate(inflater, parent, false)
                CommentViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        when (holder) {
            is BookmarkViewHolder -> holder.bind(item)
            is LikeViewHolder -> holder.bind(item)
            is CommentViewHolder -> holder.bind(item)
        }
    }

    override fun getItemCount(): Int = items.size


    /** ----------------------
     *  ViewHolder 정의
     * ---------------------- */

    class BookmarkViewHolder(private val binding: ItemActivityBookmarkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MyActivityItem) {
            binding.tvTitle.text = item.title
            binding.tvCategory.text = item.category

        }
    }

    class LikeViewHolder(private val binding: ItemActivityLikeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MyActivityItem) {
            binding.tvTitle.text = item.title
            binding.tvLikes.text = "${item.likeCount} ❤️"
        }
    }

    class CommentViewHolder(private val binding: ItemActivityCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MyActivityItem) {

        }
    }
}
