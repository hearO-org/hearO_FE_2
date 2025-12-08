package com.example.hearo2.dictionary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hearo2.R
import com.example.hearo2.databinding.ItemDictionaryBinding

class DictionaryAdapter(
    private val items: MutableList<DictionaryData>,
    private val onItemClick: (DictionaryData) -> Unit
) : RecyclerView.Adapter<DictionaryAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemDictionaryBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DictionaryData) {
            binding.tvTitle.text = item.title
            binding.tvDescription.text = item.description
            binding.tvCategory.text = item.category
            binding.tvViewCount.text = "${item.viewCount}회"

            // ♥ 즐겨찾기 아이콘 상태
            val heartIcon = if (item.isFavorite) {
                R.drawable.ic_heart_filled
            } else {
                R.drawable.ic_heart_empty
            }
            binding.btnFavorite.setImageResource(heartIcon)

            // 즐겨찾기 토글
            binding.btnFavorite.setOnClickListener {
                item.isFavorite = !item.isFavorite

                // 1) 먼저 아이콘 변경
                val newIcon = if (item.isFavorite) {
                    R.drawable.ic_heart_filled
                } else {
                    R.drawable.ic_heart_empty
                }
                binding.btnFavorite.setImageResource(newIcon)

                // 2) 하트 애니메이션 추가
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

                // 3) 변경된 항목만 업데이트
                notifyItemChanged(adapterPosition)
            }

            // 카드 전체 또는 "상세보기 >" 눌러도 상세로 이동
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

    // 🔄 검색 후 리스트 갱신용
    fun updateList(newItems: List<DictionaryData>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
