package com.example.hearo2.custominfo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hearo2.R
import com.example.hearo2.databinding.JobItemBinding

class JobAdapter(
    private var items: List<JobData>,
    private val onClick: (JobData) -> Unit
) : RecyclerView.Adapter<JobAdapter.JobViewHolder>() {

    inner class JobViewHolder(val binding: JobItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: JobData) = with(binding) {

            tvJobTitle.text = item.title
            tvCompany.text = item.company
            tvLocation.text = item.location
            tvCondition.text = item.condition
            tvPay.text = item.pay
            tvJobType.text = item.type

            // 하트 색/아이콘 설정
            btnWish.setImageResource(
                if (item.isLiked) R.drawable.ic_heart_filled else R.drawable.ic_heart_empty
            )

            // 하트 클릭 → 찜 토글
            btnWish.setOnClickListener {
                item.isLiked = !item.isLiked
                notifyItemChanged(adapterPosition)
            }

            // 카드 전체 클릭 → 상세 보기 이동
            root.setOnClickListener {
                onClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val binding = JobItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return JobViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    // 🔥 검색/필터 적용 시 리스트 갱신
    fun updateList(newList: List<JobData>) {
        items = newList
        notifyDataSetChanged()
    }
}
