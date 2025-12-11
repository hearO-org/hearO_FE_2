package com.example.hearo2.sound

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.hearo2.MainActivity
import com.example.hearo2.R
import kotlinx.coroutines.*
import java.io.File

/**
 * 항상 마이크를 감지하는 ForegroundService
 * 3초마다 녹음 → AI 분석
 * 사이렌 / 경적 감지 시 알림 → 클릭 시 홈 화면 이동
 */
class SoundDetectionService : Service() {

    companion object {
        private const val TAG = "SoundDetectionService"

        // Foreground Service
        private const val SERVICE_CHANNEL_ID = "sound_detection_channel"
        private const val SERVICE_NOTI_ID = 99999

        // 🚨 위험 알림
        private const val ALERT_CHANNEL_ID = "sound_alert_channel"

        // Broadcast → HomeFragment
        const val ACTION_SOUND_RESULT = "ACTION_SOUND_RESULT"
        const val EXTRA_LABEL = "EXTRA_LABEL"
        const val EXTRA_CONFIDENCE = "EXTRA_CONFIDENCE"
        const val EXTRA_ALERT = "EXTRA_ALERT"
    }

    private lateinit var audioManager: AudioRecordManager
    private var serviceJob: Job? = null

    // ==================================================
    // Lifecycle
    // ==================================================
    override fun onCreate() {
        super.onCreate()
        audioManager = AudioRecordManager(this)

        createServiceChannel()
        createAlertChannel()

        startForeground(SERVICE_NOTI_ID, createServiceNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "서비스 시작됨")
        startListeningLoop()
        return START_STICKY
    }

    override fun onDestroy() {
        Log.d(TAG, "서비스 종료됨")
        serviceJob?.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    // ==================================================
    // 🎧 반복 감지 루프
    // ==================================================
    private fun startListeningLoop() {
        serviceJob = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {

                val wavFile: File? = audioManager.recordOnce(3000)

                if (wavFile != null) {
                    val result = SoundRepository.detectSound(wavFile)

                    if (result != null) {
                        Log.d(TAG, "AI 분석 완료: ${result.label}")

                        // ✅ HomeFragment 결과 전달
                        sendResultBroadcast(result)

                        // ✅ 위험 소리 → 알림
                        if (
                            result.label in listOf("siren", "car_horn") &&
                            result.confidence >= 0.7
                        ) {
                            showDangerNotification(
                                label = result.label,
                                confidence = result.confidence
                            )
                        }
                    }
                }

                delay(500)
            }
        }
    }

    // ==================================================
    // 📡 HomeFragment 전달
    // ==================================================
    private fun sendResultBroadcast(result: SoundResultModel) {
        val intent = Intent(ACTION_SOUND_RESULT).apply {
            putExtra(EXTRA_LABEL, result.label)
            putExtra(EXTRA_CONFIDENCE, result.confidence)
            putExtra(EXTRA_ALERT, result.alert)
        }
        sendBroadcast(intent)
    }

    // ==================================================
    // 🔔 위험 알림 (클릭 시 홈 이동)
    // ==================================================
    private fun showDangerNotification(label: String, confidence: Double) {

        val title = when (label) {
            "siren" -> "🚨 사이렌 감지"
            "car_horn" -> "🚗 경적 감지"
            else -> "⚠️ 위험 소리 감지"
        }

        val message =
            "주변에서 위험한 소리가 감지되었습니다.\n정확도 ${(confidence * 100).toInt()}%"

        // ✅ 알림 클릭 → MainActivity → HomeFragment
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            System.currentTimeMillis().toInt(), // ✅ 중복 방지
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, ALERT_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_warning_red)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(0, 500, 300, 500))
            .setContentIntent(pendingIntent)
            .build()

        val manager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        manager.notify(
            System.currentTimeMillis().toInt(),
            notification
        )
    }

    // ==================================================
    // 🔔 Notification Channels
    // ==================================================
    private fun createServiceChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                SERVICE_CHANNEL_ID,
                "소리 감지 서비스",
                NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }
    }

    private fun createAlertChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                ALERT_CHANNEL_ID,
                "위험 소리 알림",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableVibration(true)
            }
            getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }
    }

    // ==================================================
    // 🎧 Foreground 알림
    // ==================================================
    private fun createServiceNotification(): Notification {
        return NotificationCompat.Builder(this, SERVICE_CHANNEL_ID)
            .setContentTitle("HearO 실행 중")
            .setContentText("주변 소리를 감지하고 있습니다")
            .setSmallIcon(R.drawable.ic_mic_idle)
            .setOngoing(true)
            .build()
    }
}
