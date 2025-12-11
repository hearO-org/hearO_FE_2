package com.example.hearo2.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hearo2.databinding.ItemSignLikeBinding
import com.example.hearo2.dictionary.model.SignItem

class SignLikeAdapter(
    private val onItemClick: (SignItem) -> Unit   // 🔥 클릭 콜백 추가
) : RecyclerView.Adapter<SignLikeAdapter.ViewHolder>() {

    private val items = mutableListOf<SignItem>()

    fun submitList(list: List<SignItem>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemSignLikeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SignItem) {

            binding.tvTitle.text = item.title
            binding.tvCat.text = item.categoryType ?: "기타"

            Glide.with(binding.imgThumb.context)
                .load(item.thumbnailUrl)
                .into(binding.imgThumb)

            // 🔥 클릭 → 콜백 실행
            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSignLikeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(items[position])
}
