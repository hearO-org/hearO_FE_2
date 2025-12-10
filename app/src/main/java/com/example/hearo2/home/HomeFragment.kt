package com.example.hearo2.home

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hearo2.R
import com.example.hearo2.databinding.FragmentHomeBinding
import com.example.hearo2.sound.SoundDetectionService
import com.example.hearo2.sound.SoundRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: RecentSoundAdapter
    private var soundReceiver: BroadcastReceiver? = null

    private var isListening = false
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
        initSensitivity()

        // ✅ ⭐⭐⭐ 이게 없어서 안 떴던 거임
        loadSoundHistory()
    }

    override fun onStart() {
        super.onStart()
        registerBroadcastReceiver()
    }

    override fun onStop() {
        super.onStop()
        soundReceiver?.let {
            requireActivity().unregisterReceiver(it)
        }
        soundReceiver = null
    }

    /* ---------------------------------- */

    private fun loadSoundHistory() {
        lifecycleScope.launch {
            val history = SoundRepository.getMySoundHistory()

            history.forEach { item ->

                // ✅ null 방어
                val label = item.label ?: "unknown"

                val icon = when (label) {
                    "car_horn" -> R.drawable.ic_car
                    "fire_alarm", "siren" -> R.drawable.ic_fire
                    "baby_crying" -> R.drawable.ic_baby
                    else -> R.drawable.ic_mic_idle
                }

                val time = item.detectedAt
                    ?.substringAfter("T")
                    ?.substring(0, 8)
                    ?: "--:--:--"

                adapter.addItem(
                    RecentSoundData(
                        title = label,
                        accuracy = "정확도 ${(item.confidence * 100).toInt()}%",
                        time = time,
                        iconRes = icon,
                        tag = if (item.alert) "긴급" else null
                    )
                )
            }
        }
    }


    private fun initRecentList() {
        adapter = RecentSoundAdapter(mutableListOf())
        binding.rvRecent.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRecent.adapter = adapter
    }

    private fun registerBroadcastReceiver() {
        if (soundReceiver != null) return

        soundReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action != SoundDetectionService.ACTION_SOUND_RESULT) return

                val label = intent.getStringExtra(SoundDetectionService.EXTRA_LABEL) ?: return
                val confidence = intent.getDoubleExtra(
                    SoundDetectionService.EXTRA_CONFIDENCE, 0.0
                )
                val alert = intent.getBooleanExtra(
                    SoundDetectionService.EXTRA_ALERT, false
                )

                val now = SimpleDateFormat("HH:mm:ss", Locale.KOREA).format(Date())

                val icon = when (label) {
                    "car_horn" -> R.drawable.ic_car
                    "fire_alarm", "siren" -> R.drawable.ic_fire
                    "baby_crying" -> R.drawable.ic_baby
                    else -> R.drawable.ic_mic_idle
                }

                requireActivity().runOnUiThread {
                    adapter.addItem(
                        RecentSoundData(
                            title = label,
                            accuracy = "정확도 ${(confidence * 100).toInt()}%",
                            time = now,
                            iconRes = icon,
                            tag = if (alert) "긴급" else null
                        )
                    )
                }
            }
        }

        val filter = IntentFilter(SoundDetectionService.ACTION_SOUND_RESULT)

        if (android.os.Build.VERSION.SDK_INT >= 33) {
            requireActivity().registerReceiver(
                soundReceiver,
                filter,
                Context.RECEIVER_NOT_EXPORTED
            )
        } else {
            @Suppress("UnspecifiedRegisterReceiverFlag")
            requireActivity().registerReceiver(soundReceiver, filter)
        }
    }

    /* --------- 마이크 --------- */

    private fun initMicButton() {
        binding.btnMic.setOnClickListener {
            isListening = !isListening

            if (isListening) {
                ContextCompat.startForegroundService(
                    requireContext(),
                    Intent(requireContext(), SoundDetectionService::class.java)
                )
                binding.tvMicState.text = "소리를 분석하고 있어요"
            } else {
                requireContext().stopService(
                    Intent(requireContext(), SoundDetectionService::class.java)
                )
                binding.tvMicState.text = "소리 인식 준비 완료"
            }
        }
    }

    private fun initSensitivity() {
        binding.btnSensitivity.setOnClickListener {
            SensitivityBottomSheet(
                defaultValue = sensitivityValue,
                onSave = {
                    sensitivityValue = it
                    binding.btnSensitivity.text = "감도 ${it}%"
                }
            ).show(parentFragmentManager, "SensitivityBottomSheet")
        }
    }

    private fun checkMicPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.RECORD_AUDIO),
                1001
            )
        }
    }
}
