package com.example.hearo2.community

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hearo2.R
import com.example.hearo2.databinding.CommunityPostItemBinding

class PostAdapter(
    private val onPostClick: (PostModel) -> Unit,
    private val onScrapClick: (PostModel) -> Unit
) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    private val items = mutableListOf<PostModel>()

    val currentList: List<PostModel>
        get() = items

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
            binding.tvWriter.text = item.authorNickname
            binding.tvCommentCount.text = "-"
            binding.tvCategory.text = when (item.category) {
                "GENERAL" -> "일반"
                "QUESTION" -> "질문"
                "REVIEW" -> "후기"
                "SIGN_INFO" -> "수어학습정보"
                "SAFETY" -> "안전/대처"
                "POLICY" -> "정책/지원"
                "JOB" -> "취업/교육"
                else -> "기타"
            }

            updateScrapIcon(item.scrapped)

            binding.btnScrap.setOnClickListener {
                onScrapClick(item)
            }

            binding.root.setOnClickListener {
                onPostClick(item)
            }
        }

        private fun updateScrapIcon(isScrapped: Boolean) {
            binding.btnScrap.setColorFilter(
                if (isScrapped)
                    binding.root.context.getColor(R.color.black)
                else
                    binding.root.context.getColor(R.color.gray_700)
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CommunityPostItemBinding.inflate(
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
