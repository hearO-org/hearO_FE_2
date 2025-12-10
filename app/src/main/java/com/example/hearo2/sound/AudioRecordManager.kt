package com.example.hearo2.sound

import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * 짧은 구간(예: 2~3초)씩 마이크 소리를 녹음해서
 * WAV 파일로 만들어주는 유틸 클래스.
 *
 * ⚠️ recordOnce()는 블로킹 함수이므로 반드시 백그라운드 스레드에서 실행해야 함.
 */
class AudioRecordManager(
    private val context: Context
) {

    companion object {
        private const val TAG = "AudioRecordManager"

        private const val SAMPLE_RATE = 16000
        private const val CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO
        private const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT

        private const val BITS_PER_SAMPLE = 16
        private const val CHANNEL_COUNT = 1
    }

    /**
     * durationMillis 동안 마이크를 녹음하고
     * WAV 파일을 생성하여 File 형태로 반환.
     */
    fun recordOnce(durationMillis: Long): File? {

        // 🔥 권한 체크 — Android Studio 경고 제거
        if (context.checkSelfPermission(android.Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e(TAG, "RECORD_AUDIO 권한 없음 → 녹음 불가")
            return null
        }

        val minBufferSize = AudioRecord.getMinBufferSize(
            SAMPLE_RATE,
            CHANNEL_CONFIG,
            AUDIO_FORMAT
        )

        if (minBufferSize == AudioRecord.ERROR || minBufferSize == AudioRecord.ERROR_BAD_VALUE) {
            Log.e(TAG, "getMinBufferSize() 실패: $minBufferSize")
            return null
        }

        val audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            SAMPLE_RATE,
            CHANNEL_CONFIG,
            AUDIO_FORMAT,
            minBufferSize
        )

        if (audioRecord.state != AudioRecord.STATE_INITIALIZED) {
            Log.e(TAG, "AudioRecord 초기화 실패")
            return null
        }

        val buffer = ByteArray(minBufferSize)
        val pcmOutputStream = ByteArrayOutputStream()

        try {
            audioRecord.startRecording()
            Log.d(TAG, "recordOnce: 녹음 시작 (duration=${durationMillis}ms)")

            val startTime = System.currentTimeMillis()

            while (System.currentTimeMillis() - startTime < durationMillis) {
                val readBytes = audioRecord.read(buffer, 0, buffer.size)

                if (readBytes > 0) {
                    pcmOutputStream.write(buffer, 0, readBytes)
                }
            }

            audioRecord.stop()
            Log.d(TAG, "recordOnce: 녹음 종료")

            val pcmData = pcmOutputStream.toByteArray()

            return writeWavFile(pcmData)

        } catch (e: Exception) {
            Log.e(TAG, "recordOnce 중 오류: ${e.message}", e)
            return null

        } finally {
            try {
                audioRecord.release()
            } catch (_: Exception) {}

            try {
                pcmOutputStream.close()
            } catch (_: IOException) {}
        }
    }

    /**
     * PCM 데이터를 WAV 파일로 변환하여 저장하는 함수
     */
    private fun writeWavFile(pcmData: ByteArray): File? {
        val wavFile = File(
            context.cacheDir,
            "sound_${System.currentTimeMillis()}.wav"
        )

        return try {
            FileOutputStream(wavFile).use { fos ->

                val totalAudioLen = pcmData.size.toLong()
                val totalDataLen = totalAudioLen + 36
                val byteRate = (BITS_PER_SAMPLE * SAMPLE_RATE * CHANNEL_COUNT / 8).toLong()

                writeWavHeader(
                    fos,
                    totalAudioLen,
                    totalDataLen,
                    SAMPLE_RATE.toLong(),
                    CHANNEL_COUNT,
                    byteRate
                )

                fos.write(pcmData)
            }

            Log.d(TAG, "WAV 파일 생성 성공: ${wavFile.absolutePath}")
            wavFile

        } catch (e: Exception) {
            Log.e(TAG, "WAV 파일 생성 실패: ${e.message}", e)
            null
        }
    }

    /**
     * WAV 파일 헤더 작성
     */
    @Throws(IOException::class)
    private fun writeWavHeader(
        out: FileOutputStream,
        totalAudioLen: Long,
        totalDataLen: Long,
        longSampleRate: Long,
        channels: Int,
        byteRate: Long
    ) {
        val header = ByteArray(44)

        // Chunk ID "RIFF"
        header[0] = 'R'.code.toByte()
        header[1] = 'I'.code.toByte()
        header[2] = 'F'.code.toByte()
        header[3] = 'F'.code.toByte()

        // Chunk Size
        header[4] = (totalDataLen and 0xff).toByte()
        header[5] = ((totalDataLen shr 8) and 0xff).toByte()
        header[6] = ((totalDataLen shr 16) and 0xff).toByte()
        header[7] = ((totalDataLen shr 24) and 0xff).toByte()

        // Format "WAVE"
        header[8] = 'W'.code.toByte()
        header[9] = 'A'.code.toByte()
        header[10] = 'V'.code.toByte()
        header[11] = 'E'.code.toByte()

        // Subchunk1 ID "fmt "
        header[12] = 'f'.code.toByte()
        header[13] = 'm'.code.toByte()
        header[14] = 't'.code.toByte()
        header[15] = ' '.code.toByte()

        // Subchunk1 Size (16 for PCM)
        header[16] = 16
        header[17] = 0
        header[18] = 0
        header[19] = 0

        // AudioFormat (1 = PCM)
        header[20] = 1
        header[21] = 0

        // NumChannels
        header[22] = channels.toByte()
        header[23] = 0

        // SampleRate
        header[24] = (longSampleRate and 0xff).toByte()
        header[25] = ((longSampleRate shr 8) and 0xff).toByte()
        header[26] = ((longSampleRate shr 16) and 0xff).toByte()
        header[27] = ((longSampleRate shr 24) and 0xff).toByte()

        // ByteRate
        header[28] = (byteRate and 0xff).toByte()
        header[29] = ((byteRate shr 8) and 0xff).toByte()
        header[30] = ((byteRate shr 16) and 0xff).toByte()
        header[31] = ((byteRate shr 24) and 0xff).toByte()

        // BlockAlign
        val blockAlign = (channels * BITS_PER_SAMPLE / 8).toShort()
        header[32] = (blockAlign.toInt() and 0xff).toByte()
        header[33] = ((blockAlign.toInt() shr 8) and 0xff).toByte()

        // BitsPerSample
        header[34] = BITS_PER_SAMPLE.toByte()
        header[35] = 0

        // Subchunk2 ID "data"
        header[36] = 'd'.code.toByte()
        header[37] = 'a'.code.toByte()
        header[38] = 't'.code.toByte()
        header[39] = 'a'.code.toByte()

        // Subchunk2 Size
        header[40] = (totalAudioLen and 0xff).toByte()
        header[41] = ((totalAudioLen shr 8) and 0xff).toByte()
        header[42] = ((totalAudioLen shr 16) and 0xff).toByte()
        header[43] = ((totalAudioLen shr 24) and 0xff).toByte()

        out.write(header, 0, 44)
    }
}
