package com.example.hearo2.dictionary

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hearo2.R
import com.example.hearo2.databinding.FragmentDictionaryBinding
import com.example.hearo2.dictionary.model.SignItem
import com.example.hearo2.dictionary.viewmodel.DictionaryViewModel
import com.example.hearo2.dictionary.viewmodel.DictionaryViewModelFactory

class DictionaryFragment : Fragment() {

    private lateinit var binding: FragmentDictionaryBinding
    private lateinit var adapter: DictionaryAdapter

    private val viewModel: DictionaryViewModel by viewModels {
        DictionaryViewModelFactory(requireContext())
    }

    private var selectedCategory: String = "전체"

    /** ✅ UI에서 사용할 카테고리 (최종) */
    private val categories = listOf(
        "전체",
        "경제",
        "사회생활",
        "정치",
        "인간",
        "식생활",
        "교육",
        "종교"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDictionaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycler()
        observeViewModel()
        setupSearch()
        setupCategoryUI()

        viewModel.loadAllSigns()
    }

    // ----------------------------------------------------
    // RecyclerView
    // ----------------------------------------------------
    private fun setupRecycler() {
        adapter = DictionaryAdapter(
            mutableListOf(),
            viewModel
        ) { item ->
            openDetail(item)
        }

        binding.rvDictionary.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDictionary.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.signList.observe(viewLifecycleOwner) { list ->
            applyCategoryFilter(list)
        }

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    // ----------------------------------------------------
    // 🔥 카테고리 UI 생성
    // ----------------------------------------------------
    private fun setupCategoryUI() {
        binding.categoryContainer.removeAllViews()

        categories.forEach { category ->
            val tv = LayoutInflater.from(requireContext())
                .inflate(
                    R.layout.item_category_chip,
                    binding.categoryContainer,
                    false
                ) as TextView

            tv.text = category
            updateCategoryStyle(tv, category == selectedCategory)

            tv.setOnClickListener {
                selectedCategory = category
                updateAllCategoryStyles()
                applyCategoryFilter(viewModel.signList.value ?: emptyList())
            }

            binding.categoryContainer.addView(tv)
        }
    }

    private fun updateAllCategoryStyles() {
        for (i in 0 until binding.categoryContainer.childCount) {
            val tv = binding.categoryContainer.getChildAt(i) as TextView
            updateCategoryStyle(tv, tv.text.toString() == selectedCategory)
        }
    }

    private fun updateCategoryStyle(tv: TextView, selected: Boolean) {
        if (selected) {
            tv.setBackgroundResource(R.drawable.category_selected_bg)
            tv.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
        } else {
            tv.setBackgroundResource(R.drawable.category_unselected_bg)
            tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_700))
        }
    }

    // ----------------------------------------------------
    // 🔍 카테고리 필터 (핵심 수정)
    // ----------------------------------------------------
    private fun applyCategoryFilter(list: List<SignItem>) {

        if (selectedCategory == "전체") {
            adapter.updateList(list)
            return
        }

        val filtered = list.filter { item ->
            val serverCategory = item.categoryType ?: return@filter false

            when (selectedCategory) {
                "경제" ->
                    serverCategory.contains("경제")

                "사회생활" ->
                    serverCategory.contains("사회")

                "정치" ->
                    serverCategory.contains("정치")

                "인간" ->
                    serverCategory.contains("인간")

                "식생활" ->
                    serverCategory.contains("식생활")

                "교육" ->
                    serverCategory.contains("교육")

                "종교" ->
                    serverCategory.contains("종교")

                else -> false
            }
        }

        adapter.updateList(filtered)
    }

    // ----------------------------------------------------
    // 🔍 검색
    // ----------------------------------------------------
    private fun setupSearch() {
        binding.etSearch.setOnEditorActionListener { _, actionId, event ->
            if (
                actionId == EditorInfo.IME_ACTION_SEARCH ||
                (event?.keyCode == KeyEvent.KEYCODE_ENTER &&
                        event.action == KeyEvent.ACTION_DOWN)
            ) {
                val query = binding.etSearch.text.toString().trim()
                if (query.isNotEmpty()) {
                    viewModel.search(query)
                } else {
                    viewModel.loadAllSigns()
                }
                true
            } else {
                false
            }
        }
    }

    // ----------------------------------------------------
    // 상세 이동
    // ----------------------------------------------------
    private fun openDetail(item: SignItem) {
        val action =
            DictionaryFragmentDirections
                .actionDictionaryToDictionaryDetail(item.id)

        findNavController().navigate(action)
    }
}
