package com.example.hearo2.custominfo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hearo2.databinding.JobItemBinding
import com.example.hearo2.custominfo.model.JobItem

class JobAdapter(
    private var items: List<JobItem>,
    private val onClick: (JobItem) -> Unit
) : RecyclerView.Adapter<JobAdapter.JobViewHolder>() {

    inner class JobViewHolder(val binding: JobItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: JobItem) = with(binding) {

            // ---- 텍스트 적용 ----
            tvJobTitle.text = item.jobNm
            tvCompany.text = item.busplaName
            tvLocation.text = item.compAddr
            tvCondition.text = item.reqCareer ?: "무관"
            tvPay.text = item.salary
            tvJobType.text = item.empType

            // ---- 상세 이동 ----
            root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val binding =
            JobItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun updateList(newList: List<JobItem>) {
        items = newList
        notifyDataSetChanged()
    }
}
