package com.example.hearo2.custominfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hearo2.R
import com.example.hearo2.databinding.FragmentJobDetailBinding

class JobDetailFragment : Fragment() {

    private lateinit var binding: FragmentJobDetailBinding
    private var isFavorite = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJobDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadJobData()
        initListeners()
    }

    // -----------------------------
    // 뒤로가기 + 찜 버튼
    // -----------------------------
    private fun initListeners() {

        // 뒤로가기
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // 하트 토글
        binding.btnFavorite.setOnClickListener {
            isFavorite = !isFavorite
            updateFavoriteIcon()
        }
    }

    private fun updateFavoriteIcon() {
        val icon = if (isFavorite)
            R.drawable.ic_heart_filled
        else
            R.drawable.ic_heart_empty

        binding.btnFavorite.setImageResource(icon)
    }

    // -----------------------------
    // 데이터를 UI에 바인딩
    // -----------------------------
    private fun loadJobData() {

        val title = arguments?.getString("title") ?: ""
        val company = arguments?.getString("company") ?: ""
        val location = arguments?.getString("location") ?: ""
        val condition = arguments?.getString("condition") ?: ""
        val pay = arguments?.getString("pay") ?: ""
        val type = arguments?.getString("type") ?: ""
        val content = arguments?.getString("content") ?: "상세 내용이 없습니다."

        // 상단 제목
        binding.tvTitleDetail.text = title

        // include 내부 job_item 레이아웃에 접근
        val include = binding.includeJobCard

        include.tvJobTitle.text = title
        include.tvCompany.text = company
        include.tvLocation.text = location
        include.tvCondition.text = condition
        include.tvPay.text = pay
        include.tvJobType.text = type

        // 상세내용 적용
        binding.tvContent.text = content
    }

    // -----------------------------
    // newInstance (데이터 전달)
    // -----------------------------
    companion object {
        fun newInstance(
            title: String,
            company: String,
            location: String,
            condition: String,
            pay: String,
            type: String,
            content: String
        ): JobDetailFragment {

            val fragment = JobDetailFragment()
            val args = Bundle()

            args.putString("title", title)
            args.putString("company", company)
            args.putString("location", location)
            args.putString("condition", condition)
            args.putString("pay", pay)
            args.putString("type", type)
            args.putString("content", content)

            fragment.arguments = args
            return fragment
        }
    }
}
