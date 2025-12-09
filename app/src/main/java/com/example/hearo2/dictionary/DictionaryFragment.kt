package com.example.hearo2.dictionary

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.fragment.findNavController
import com.example.hearo2.R
import com.example.hearo2.databinding.FragmentDictionaryBinding

class DictionaryFragment : Fragment() {

    private lateinit var binding: FragmentDictionaryBinding
    private lateinit var adapter: DictionaryAdapter

    // 전체 데이터
    private lateinit var fullList: List<DictionaryData>

    // 현재 선택된 카테고리
    private var selectedCategory: String = "전체"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDictionaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycler()
        setupSearch()
        setupCategoryButtons()
    }

    private fun setupRecycler() {

        // ↓ 너가 원래 쓰던 전체 테스트 데이터
        fullList = listOf(
            DictionaryData("삼권 분립 제도", "오른 주먹의 1·2·3지를 펴서...", "정치", 280, R.drawable.sample_reference),
            DictionaryData("상원", "왼손을 펴서 손등을 위로 향하게...", "정치", 235, R.drawable.sample_reference),
            DictionaryData("우리 사주 조합", "오른 손바닥을 가슴에 대고...", "경제", 978, R.drawable.sample_reference),
        )

        adapter = DictionaryAdapter(fullList.toMutableList()) { item ->
            openDetail(item)
        }

        binding.rvDictionary.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDictionary.adapter = adapter
    }

    // ==============================
    // 🔥 카테고리 버튼 클릭 로직
    // ==============================
    private fun setupCategoryButtons() {

        val categoryMap = mapOf(
            "전체" to binding.catAll,
            "정치" to binding.catPolitics,
            "경제" to binding.catEconomy,
            "인간" to binding.catHuman
        )

        // 각 버튼 클릭 시 동작
        categoryMap.forEach { (categoryName, textView) ->
            textView.setOnClickListener {
                selectedCategory = categoryName
                updateCategoryUI(categoryMap)
                filterByCategory()
            }
        }

        // 처음엔 전체 선택된 상태로 UI 업데이트
        updateCategoryUI(categoryMap)
    }

    // ==============================
    // 🔥 선택된 카테고리 UI 업데이트 (보라색/회색 변경)
    // ==============================
    private fun updateCategoryUI(categoryMap: Map<String, TextView>) {

        categoryMap.forEach { (name, tv) ->
            if (name == selectedCategory) {
                // 선택됨 (보라색)
                tv.setBackgroundResource(R.drawable.category_selected_bg)
                tv.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
            } else {
                // 선택 안됨 (회색 테두리)
                tv.setBackgroundResource(R.drawable.category_unselected_bg)
                tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_700))
            }
        }
    }

    // ==============================
    // 🔥 카테고리 필터링 기능
    // ==============================
    private fun filterByCategory() {
        val result = when (selectedCategory) {
            "전체" -> fullList
            else -> fullList.filter { it.category == selectedCategory }
        }
        adapter.updateList(result)
    }

    // ==============================
    // 🔎 검색 기능
    // ==============================
    private fun setupSearch() {

        binding.etSearch.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                performSearch()
                true
            } else false
        }

        binding.btnFilter.setOnClickListener {
            // TODO: 바텀시트 연결 가능
        }
    }

    private fun performSearch() {
        val query = binding.etSearch.text.toString().trim()

        val filtered = if (query.isEmpty()) {
            fullList
        } else {
            fullList.filter { item ->
                item.title.contains(query, ignoreCase = true) ||
                        item.description.contains(query, ignoreCase = true) ||
                        item.category.contains(query, ignoreCase = true)
            }
        }

        adapter.updateList(filtered)
    }

    // 상세 페이지 이동
    private fun openDetail(item: DictionaryData) {
        val action = DictionaryFragmentDirections
            .actionDictionaryFragmentToDictionaryDetailFragment(
                item.title, item.description, item.category, item.viewCount, item.imageRes
            )
        findNavController().navigate(action)
    }
}
