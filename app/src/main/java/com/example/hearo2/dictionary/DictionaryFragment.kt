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
import com.example.hearo2.dictionary.viewmodel.DictionaryViewModel
import com.example.hearo2.dictionary.viewmodel.DictionaryViewModelFactory
import com.example.hearo2.dictionary.model.SignItem

class DictionaryFragment : Fragment() {

    private lateinit var binding: FragmentDictionaryBinding
    private lateinit var adapter: DictionaryAdapter

    // ⭐ 반드시 Factory 적용해야 정상 동작
    private val viewModel: DictionaryViewModel by viewModels {
        DictionaryViewModelFactory(requireContext())
    }

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
        observeViewModel()
        viewModel.loadAllSigns()

        setupSearch()
        setupCategoryButtons()
    }


    // 🔥🔥 수정된 부분: Adapter 에 viewModel 전달 추가
    private fun setupRecycler() {
        adapter = DictionaryAdapter(
            mutableListOf(),
            viewModel,              // ⭐ 추가됨! (즐겨찾기 서버 반영 위해 필요)
        ) { item ->
            openDetail(item)
        }

        binding.rvDictionary.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDictionary.adapter = adapter
    }


    private fun observeViewModel() {
        viewModel.signList.observe(viewLifecycleOwner) { list ->
            adapter.updateList(list)
        }

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "오류 발생: $it", Toast.LENGTH_SHORT).show()
        }
    }


    private fun setupCategoryButtons() {

        val categoryMap = mapOf(
            "전체" to binding.catAll,
            "정치" to binding.catPolitics,
            "경제" to binding.catEconomy,
            "인간" to binding.catHuman
        )

        categoryMap.forEach { (categoryName, textView) ->
            textView.setOnClickListener {
                selectedCategory = categoryName
                updateCategoryUI(categoryMap)
                applyCategoryFilter()
            }
        }

        updateCategoryUI(categoryMap)
    }

    private fun updateCategoryUI(categoryMap: Map<String, TextView>) {
        categoryMap.forEach { (name, tv) ->
            if (name == selectedCategory) {
                tv.setBackgroundResource(R.drawable.category_selected_bg)
                tv.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
            } else {
                tv.setBackgroundResource(R.drawable.category_unselected_bg)
                tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_700))
            }
        }
    }

    private fun applyCategoryFilter() {
        val currentList = viewModel.signList.value ?: emptyList()

        val filtered = when (selectedCategory) {
            "전체" -> currentList
            else -> currentList.filter { it.categoryType == selectedCategory }
        }

        adapter.updateList(filtered)
    }


    private fun setupSearch() {
        binding.etSearch.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                val query = binding.etSearch.text.toString().trim()
                if (query.isNotEmpty()) viewModel.search(query)
                else viewModel.loadAllSigns()
                true
            } else false
        }
    }


    private fun openDetail(item: SignItem) {
        val action = DictionaryFragmentDirections
            .actionDictionaryFragmentToDictionaryDetailFragment(item.id)
        findNavController().navigate(action)
    }
}
