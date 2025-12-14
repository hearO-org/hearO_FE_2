package com.example.hearo2.custominfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.hearo2.R
import com.example.hearo2.custominfo.viewmodel.JobViewModel
import com.example.hearo2.custominfo.viewmodel.UiState
import com.example.hearo2.databinding.FragmentJobDetailBinding
import com.example.hearo2.custominfo.model.JobDetailData

class JobDetailFragment : Fragment() {

    private lateinit var binding: FragmentJobDetailBinding

    private val viewModel: JobViewModel by activityViewModels()

    private var rno: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJobDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rno = arguments?.getString("rno") ?: ""

        if (rno.isBlank()) {
            Toast.makeText(requireContext(), "잘못된 접근입니다.", Toast.LENGTH_SHORT).show()
            return
        }

        initObservers()
        initListeners()

        // 🔥 상세 조회 API 호출
        viewModel.loadJobDetail(requireContext(), rno)
    }

    private fun initListeners() {
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }


    }

    private var isFavorite = false

    private fun toggleFavoriteIcon() {
        isFavorite = !isFavorite
        val icon = if (isFavorite)
            R.drawable.ic_heart_filled
        else
            R.drawable.ic_heart_empty

    }

    private fun initObservers() {
        viewModel.jobDetailState.observe(viewLifecycleOwner) { state ->
            when (state) {

                is UiState.Loading -> showLoading(true)

                is UiState.Success -> {
                    showLoading(false)
                    applyJobDetail(state.data)
                }

                is UiState.Error -> {
                    showLoading(false)
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.includeJobCard.root.visibility =
            if (isLoading) View.INVISIBLE else View.VISIBLE

        binding.tvContent.visibility =
            if (isLoading) View.INVISIBLE else View.VISIBLE
    }

    private fun applyJobDetail(detail: JobDetailData) {

        binding.tvTitleDetail.text = detail.jobNm

        val card = binding.includeJobCard
        card.tvJobTitle.text = detail.jobNm
        card.tvCompany.text = detail.busplaName
        card.tvLocation.text = detail.compAddr
        card.tvCondition.text = detail.reqCareer ?: "-"
        card.tvPay.text = formatSalary(detail.salaryType, detail.salary)
        card.tvJobType.text = detail.empType

        val contentText = """
• 회사명: ${detail.busplaName}
• 주소: ${detail.compAddr}
• 고용 형태: ${detail.empType}
• 경력: ${detail.reqCareer ?: "무관"}
• 학력: ${detail.reqEduc ?: "무관"}
• 급여: ${formatSalary(detail.salaryType, detail.salary)}
• 모집 기간: ${detail.termDate ?: "-"}
• 담당 기관: ${detail.regganName ?: "-"}
• 연락처: ${detail.cntctNo ?: "-"}
""".trimIndent()

        binding.tvContent.text = contentText
    }

    private fun formatSalary(type: String?, value: String?): String {
        if (value.isNullOrBlank()) return "-"

        return when (type) {
            "월급" -> "월 $value"
            "시급" -> "시급 $value"
            "연봉" -> "연봉 $value"
            else -> value
        }
    }
}
