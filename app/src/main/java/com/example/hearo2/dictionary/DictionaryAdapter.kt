package com.example.hearo2.dictionary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hearo2.R
import com.example.hearo2.databinding.ItemDictionaryBinding
import com.example.hearo2.dictionary.model.SignItem
import com.example.hearo2.dictionary.viewmodel.DictionaryViewModel

class DictionaryAdapter(
    private val items: MutableList<SignItem>,
    private val viewModel: DictionaryViewModel,   // ⭐ 추가: ViewModel 전달
    private val onItemClick: (SignItem) -> Unit
) : RecyclerView.Adapter<DictionaryAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemDictionaryBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SignItem) {

            // 제목
            binding.tvTitle.text = item.title

            // 설명
            binding.tvDescription.text =
                item.signDescription ?: "설명이 제공되지 않았습니다."

            // 카테고리
            binding.tvCategory.text =
                item.categoryType ?: "수어"

            // 조회수
            binding.tvViewCount.text = "${item.viewCount ?: 0}회"

            // 썸네일 Glide
            Glide.with(binding.root)
                .load(item.thumbnailUrl)
                .placeholder(R.drawable.sample_reference)
                .error(R.drawable.sample_reference)
                .into(binding.imgThumbnail)

            // 즐겨찾기 아이콘
            val icon = if (item.favorite == true)
                R.drawable.ic_heart_filled
            else
                R.drawable.ic_heart_empty

            binding.btnFavorite.setImageResource(icon)

            // 즐겨찾기 클릭 → ViewModel 에 요청
            binding.btnFavorite.setOnClickListener {
                // 서버로 즐겨찾기 추가/삭제 요청
                viewModel.toggleFavoriteFromList(item)

                // 클릭 애니메이션(UI만)
                binding.btnFavorite.animate()
                    .scaleX(1.2f)
                    .scaleY(1.2f)
                    .alpha(0.6f)
                    .setDuration(120)
                    .withEndAction {
                        binding.btnFavorite.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .alpha(1f)
                            .setDuration(120)
                            .start()
                    }
                    .start()
            }

            // 상세보기 이동
            binding.root.setOnClickListener { onItemClick(item) }
            binding.tvDetail.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDictionaryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    // 리스트 갱신
    fun updateList(newItems: List<SignItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
