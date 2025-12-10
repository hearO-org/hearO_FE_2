package com.example.hearo2.sound

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.hearo2.R
import kotlinx.coroutines.*
import java.io.File

/**
 * 항상 마이크를 감지하는 ForegroundService
 * 3초마다 AudioRecordManager로 녹음 → WAV 파일 생성 → 서버 업로드
 * 분석 결과는 LocalBroadcast 로 HomeFragment 에 전달됨
 */
class SoundDetectionService : Service() {

    companion object {
        private const val TAG = "SoundDetectionService"
        private const val CHANNEL_ID = "sound_detection_channel"
        private const val NOTI_ID = 99999

        // HomeFragment가 받을 Broadcast Key
        const val ACTION_SOUND_RESULT = "ACTION_SOUND_RESULT"
        const val EXTRA_LABEL = "EXTRA_LABEL"
        const val EXTRA_CONFIDENCE = "EXTRA_CONFIDENCE"
        const val EXTRA_ALERT = "EXTRA_ALERT"
    }

    private lateinit var audioManager: AudioRecordManager
    private var serviceJob: Job? = null

    override fun onCreate() {
        super.onCreate()
        audioManager = AudioRecordManager(this)
        createNotificationChannel()
        startForeground(NOTI_ID, createNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "서비스 시작됨")
        startListeningLoop()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "서비스 종료됨")
        serviceJob?.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    /**
     * 3초마다 반복 실행되는 Listening Loop
     */
    private fun startListeningLoop() {
        serviceJob = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                Log.d(TAG, "3초 녹음 시작")

                // 1. AudioRecordManager로 녹음 (3초 = 3000ms)
                val wavFile: File? = audioManager.recordOnce(3000)

                if (wavFile != null) {
                    Log.d(TAG, "WAV 파일 생성됨: ${wavFile.path}")

                    // 2. 서버 업로드 (AI 분석)
                    val result = SoundRepository.detectSound(wavFile)

                    if (result != null) {
                        Log.d(TAG, "AI 분석 완료: ${result.label}")

                        // 3. HomeFragment 에게 결과 전달
                        sendResultBroadcast(result)
                    }
                } else {
                    Log.e(TAG, "녹음 실패")
                }

                delay(500) // 잠깐 쉬고(0.5초) 다시 반복
            }
        }
    }

    private fun sendResultBroadcast(result: SoundResultModel) {
        val intent = Intent(ACTION_SOUND_RESULT).apply {
            putExtra(EXTRA_LABEL, result.label)
            putExtra(EXTRA_CONFIDENCE, result.confidence)
            putExtra(EXTRA_ALERT, result.alert)
        }
        sendBroadcast(intent)
    }

    // ----------------- Foreground Notification -----------------

    private fun createNotification(): Notification {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("소리 감지 실행 중")
            .setContentText("AI가 주변 소리를 분석하고 있습니다.")
            .setSmallIcon(R.drawable.ic_mic_idle)
            .setOngoing(true)

        return builder.build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "소리 감지 서비스",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}
