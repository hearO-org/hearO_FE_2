package com.example.hearo2.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hearo2.R
import com.example.hearo2.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: RecentSoundAdapter

    private var isListening = false
    private var emergencyMode = false
    private var sensitivityValue = 50

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkMicPermission()
        initRecentList()
        initMicButton()
        initEmergencyMode()
        initSensitivity()
    }

    // ------------------------
    // 마이크 권한
    // ------------------------
    private fun checkMicPermission() {
        val permission = Manifest.permission.RECORD_AUDIO
        if (ContextCompat.checkSelfPermission(requireContext(), permission)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(permission),
                1001
            )
        }
    }

    // ------------------------
    // 최근 인식 목록
    // ------------------------
    private fun initRecentList() {
        val sample = mutableListOf(
            RecentSoundData("화재 경보", "정확도 95%", "방금 전", R.drawable.ic_fire, "긴급"),
            RecentSoundData("자동차 경적", "정확도 87%", "2분 전", R.drawable.ic_car),
            RecentSoundData("아기 울음소리", "정확도 92%", "5분 전", R.drawable.ic_baby)
        )

        adapter = RecentSoundAdapter(sample)

        binding.rvRecent.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRecent.adapter = adapter
    }

    // 외부에서 새 인식 결과 받았을 때 추가하는 함수
    fun addNewSoundResult(data: RecentSoundData) {
        adapter.addItem(data)
        binding.rvRecent.scrollToPosition(0)
    }


    // ------------------------
    // 마이크 버튼
    // ------------------------
    private fun initMicButton() {
        binding.btnMic.setOnClickListener {
            isListening = !isListening

            if (isListening) {
                activateMic()
            } else {
                resetMic()
            }
        }
    }

    private fun activateMic() {
        binding.btnMic.setImageResource(R.drawable.ic_mic_idle)
        binding.tvMicState.text = "소리를 분석하고 있어요\nAI가 주변 소리를 실시간으로 인식합니다."

        binding.progressSection.visibility = View.VISIBLE
        startMicAnimation()

        // Progress 예시값 (추후 실제 분석값으로 변경 가능)
        binding.progressAnalyze.progress = 14
        binding.progressAccuracy.progress = 45
        binding.tvAnalyzePercent.text = "분석률 14%"
        binding.tvAccuracyPercent.text = "정확도 45%"
    }

    private fun resetMic() {
        binding.btnMic.setImageResource(R.drawable.ic_mic_idle)
        binding.tvMicState.text = "소리 인식 준비 완료\n버튼을 눌러 주변 소리를 분석해보세요"

        binding.progressSection.visibility = View.GONE

        binding.btnMic.animate().cancel()
        binding.btnMic.scaleX = 1f
        binding.btnMic.scaleY = 1f
    }


    // ------------------------
    // 마이크 애니메이션
    // ------------------------
    private fun startMicAnimation() {
        binding.btnMic.animate()
            .scaleX(1.15f)
            .scaleY(1.15f)
            .setDuration(500)
            .withEndAction {
                binding.btnMic.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(500)
                    .withEndAction { if (isListening) startMicAnimation() }
                    .start()
            }
            .start()
    }


    // ------------------------
    // 긴급모드
    // ------------------------
    private fun initEmergencyMode() {
        binding.btnEmergency.setOnClickListener {
            emergencyMode = !emergencyMode

            if (emergencyMode) {
                binding.btnEmergency.setBackgroundResource(R.drawable.chip_bg)
            } else {
                binding.btnEmergency.setBackgroundResource(R.drawable.chip_bg)
            }
        }
    }


    // ------------------------
    // 감도조절 BottomSheet
    // ------------------------
    private fun initSensitivity() {
        binding.btnSensitivity.setOnClickListener {
            SensitivityBottomSheet(
                defaultValue = sensitivityValue,
                onSave = { newValue ->
                    sensitivityValue = newValue
                    binding.btnSensitivity.text = "감도 ${newValue}%"
                }
            ).show(parentFragmentManager, "SensitivityBottomSheet")
        }
    }
}
