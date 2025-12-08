package com.example.hearo2.dictionary

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hearo2.R
import com.example.hearo2.databinding.FragmentDictionaryBinding
import androidx.navigation.fragment.findNavController

class DictionaryFragment : Fragment() {

    private lateinit var binding: FragmentDictionaryBinding
    private lateinit var adapter: DictionaryAdapter

    // 전체 데이터(검색의 기준)
    private lateinit var fullList: List<DictionaryData>

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
    }

    private fun setupRecycler() {
        fullList = listOf(
            DictionaryData(
                title = "삼권 분립 제도",
                description = "오른 주먹의 1·2·3지를 펴서 바닥이 밖으로 향하게 세우고…",
                category = "전문용어/수어",
                viewCount = 280,
                imageRes = R.drawable.sample_reference
            ),
            DictionaryData(
                title = "상원",
                description = "왼손을 펴서 손등을 위로 향하게 하고 움직이는 동작입니다.",
                category = "전문용어/수어",
                viewCount = 235,
                imageRes = R.drawable.sample_reference
            ),
            DictionaryData(
                title = "우리 사주 조합",
                description = "오른 손바닥을 가슴에 대고 이렇게 저렇게 하는 동작입니다.",
                category = "전문용어/수어",
                viewCount = 978,
                imageRes = R.drawable.sample_reference
            )
        )

        adapter = DictionaryAdapter(fullList.toMutableList()) { item ->
            openDetail(item)
        }

        binding.rvDictionary.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDictionary.adapter = adapter
    }

    // 상세 페이지 이동
    private fun openDetail(item: DictionaryData) {
        val action = DictionaryFragmentDirections
            .actionDictionaryFragmentToDictionaryDetailFragment(
                item.title,
                item.description,
                item.category,
                item.viewCount,
                item.imageRes
            )

        findNavController().navigate(action)
    }

    private fun setupSearch() {

        // 키보드 검색/엔터 누를 때 검색
        binding.etSearch.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                performSearch()
                true
            } else {
                false
            }
        }

        // 필터 버튼 (나중에 BottomSheet 연결 가능)
        binding.btnFilter.setOnClickListener {
            // TODO: 카테고리/즐겨찾기 필터 바텀시트
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
}
