package com.example.hearo2.custominfo

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hearo2.R
import com.example.hearo2.databinding.FragmentCustomInfoBinding
import androidx.navigation.fragment.findNavController

class CustomInfoFragment : Fragment() {

    private lateinit var binding: FragmentCustomInfoBinding
    private lateinit var adapter: JobAdapter
    private lateinit var fullList: MutableList<JobData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCustomInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupJobList()
        setupSearch()
        setupJobFilter()
    }

    // --------------------------------------------------------------------
    // 1) 구인 리스트 데이터 세팅
    // --------------------------------------------------------------------
    private fun setupJobList() {

        fullList = mutableListOf(
            JobData("사무 보조원(일반사무업무)", "삼성생명보험(주)", "서울특별시 서초구", "무관", "월 124만원", "정규직"),
            JobData("환경 미화원", "국립한국해양대학교", "부산광역시 영도구", "무관", "시급 10,030원", "서비스직"),
            JobData("건물 보수원 및 영선원", "주식회사 사람모아", "전북 전주시", "무관", "시급 10,030원", "생산직")
        )

        adapter = JobAdapter(fullList) { selected ->
            openDetail(selected)
        }

        binding.rvJobs.layoutManager = LinearLayoutManager(requireContext())
        binding.rvJobs.adapter = adapter
    }

    // --------------------------------------------------------------------
    // 2) 상세 페이지로 이동
    // --------------------------------------------------------------------
    private fun openDetail(item: JobData) {

        val action = CustomInfoFragmentDirections
            .actionCustomInfoToJobDetail(
                item.title,
                item.company,
                item.location,
                item.condition,
                item.pay,
                item.type,
                "여기에 상세 내용이 들어갑니다."
            )

        findNavController().navigate(action)
    }

    // --------------------------------------------------------------------
    // 3) 검색 기능
    // --------------------------------------------------------------------
    private fun setupSearch() {
        binding.etJobSearch.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                performSearch()
                true
            } else false
        }
    }

    private fun performSearch() {
        val query = binding.etJobSearch.text.toString().trim()

        val filtered = if (query.isEmpty()) {
            fullList
        } else {
            fullList.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.company.contains(query, ignoreCase = true) ||
                        it.location.contains(query, ignoreCase = true)
            }
        }

        adapter.updateList(filtered)
    }

    // --------------------------------------------------------------------
    // 4) 직종 필터 기능 (전체 / 정규직 / 사무직 / 서비스직 / 생산직)
    // --------------------------------------------------------------------
    private fun setupJobFilter() {

        // 필터 버튼 TextView 연결
        val tabs = listOf(
            binding.tabAll,
            binding.tabRegular,
            binding.tabOffice,
            binding.tabService,
            binding.tabFactory
        )

        var selectedTab: TextView = binding.tabAll // 초기값 = 전체

        // ---- UI 업데이트 함수 ----
        fun updateTabUI(selected: TextView) {
            tabs.forEach { tab ->
                if (tab == selected) {
                    tab.setBackgroundResource(R.drawable.keyword_selected)
                    tab.setTextColor(resources.getColor(android.R.color.white))
                } else {
                    tab.setBackgroundResource(R.drawable.keyword_unselected)
                    tab.setTextColor(resources.getColor(R.color.gray_700))
                }
            }
        }

        // ---- 필터 적용 함수 ----
        fun applyFilter(category: String) {
            val filtered = when (category) {
                "전체" -> fullList
                else -> fullList.filter { it.type.contains(category) }
            }
            adapter.updateList(filtered)
        }

        // ---- 클릭 이벤트 등록 ----
        tabs.forEach { tab ->
            tab.setOnClickListener {
                selectedTab = tab
                updateTabUI(tab)
                applyFilter(tab.text.toString())
            }
        }

        updateTabUI(selectedTab) // 초기 UI 적용
    }
}
