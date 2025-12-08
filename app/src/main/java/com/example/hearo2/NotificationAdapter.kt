package com.example.hearo2.mypage.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hearo2.R
import com.example.hearo2.databinding.ItemNotificationBinding
import com.example.hearo2.NotificationItem

class NotificationAdapter(
    private var items: List<NotificationItem>
) : RecyclerView.Adapter<NotificationAdapter.NotiViewHolder>() {

    inner class NotiViewHolder(val binding: ItemNotificationBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotiViewHolder {
        val binding = ItemNotificationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NotiViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotiViewHolder, position: Int) {
        val item = items[position]
        val b = holder.binding

        b.tvCategory.text = item.category
        b.tvTitle.text = item.title
        b.tvContent.text = item.content
        b.tvTime.text = item.time

        // 중요 알림 → 빨간 아이콘 표시
        b.imgImportant.visibility = if (item.isImportant) View.VISIBLE else View.GONE

        // 새 알림 → 왼쪽 파란 점 표시
        b.newDot.visibility = if (item.isNew) View.VISIBLE else View.GONE
    }

    override fun getItemCount(): Int = items.size

    fun updateList(newItems: List<NotificationItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}
