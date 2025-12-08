//SensitivityBottomSheet
package com.example.hearo2.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hearo2.databinding.BottomSensitivityBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SensitivityBottomSheet(
    private val defaultValue: Int,
    private val onSave: (Int) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSensitivityBinding
    private var currentValue = defaultValue

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSensitivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 초기값 표시
        binding.tvSensitivityValue.text = "현재 감도: $currentValue"
        binding.seekSensitivity.progress = currentValue

        // 슬라이더 움직일 때 감도 텍스트 변경
        binding.seekSensitivity.setOnSeekBarChangeListener(object :
            android.widget.SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: android.widget.SeekBar?, value: Int, fromUser: Boolean) {
                currentValue = value
                binding.tvSensitivityValue.text = "현재 감도: $value"
            }
            override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) {}
        })

        // 저장하기 버튼
        binding.btnSaveSensitivity.setOnClickListener {
            onSave(currentValue)
            dismiss()
        }
    }
}
