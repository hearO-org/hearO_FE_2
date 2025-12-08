package com.example.hearo2.mypage

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hearo2.R
import com.example.hearo2.databinding.FragmentNotificationBinding
import com.example.hearo2.mypage.adapter.NotificationAdapter
import com.example.hearo2.NotificationItem

class NotificationFragment : Fragment() {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: NotificationAdapter
    private lateinit var fullList: List<NotificationItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // ▣ 더미 데이터
        fullList = listOf(
            NotificationItem(
                id = 1,
                type = "중요",
                category = "소리 감지",
                title = "응급 상황 감지",
                content = "화재 경보음이 감지되었습니다. 즉시 대피하세요!",
                time = "5분 전",
                isImportant = true
            ),
            NotificationItem(
                id = 2,
                type = "커뮤니티",
                category = "커뮤니티",
                title = "새 댓글 알림",
                content = "내가 쓴 글에 새로운 댓글이 달렸어요.",
                time = "3시간 전"
            ),
            NotificationItem(
                id = 3,
                type = "시스템",
                category = "시스템 업데이트",
                title = "버전 업데이트 안내",
                content = "hearO 앱의 신규 기능이 업데이트되었습니다!",
                time = "어제"
            )
        )

        adapter = NotificationAdapter(fullList)

        binding.recyclerNotification.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.recyclerNotification.adapter = adapter

        setupFilters()
    }

    private fun setupFilters() {
        binding.filterAll.setOnClickListener {
            selectTab(binding.filterAll)
            adapter.updateList(fullList)
        }

        binding.filterImportant.setOnClickListener {
            selectTab(binding.filterImportant)
            adapter.updateList(fullList.filter { it.isImportant })
        }

        binding.filterSystem.setOnClickListener {
            selectTab(binding.filterSystem)
            adapter.updateList(fullList.filter { it.type == "시스템" })
        }

        binding.filterCommunity.setOnClickListener {
            selectTab(binding.filterCommunity)
            adapter.updateList(fullList.filter { it.type == "커뮤니티" })
        }
    }

    private fun selectTab(selected: View) {
        val allTabs = listOf(
            binding.filterAll,
            binding.filterImportant,
            binding.filterSystem,
            binding.filterCommunity
        )

        allTabs.forEach {
            it.setBackgroundResource(R.drawable.tab_unselected_bg)
            (it as? android.widget.TextView)?.setTextColor(Color.parseColor("#777777"))
        }

        selected.setBackgroundResource(R.drawable.tab_selected_bg)
        (selected as? android.widget.TextView)?.setTextColor(
            ContextCompat.getColor(requireContext(), R.color.purple_500)
        )
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
