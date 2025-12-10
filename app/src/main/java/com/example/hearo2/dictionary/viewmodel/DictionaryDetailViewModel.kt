package com.example.hearo2.dictionary.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hearo2.dictionary.model.SignDetailData
import com.example.hearo2.dictionary.repository.SignRepository
import kotlinx.coroutines.launch

class DictionaryDetailViewModel(
    private val repository: SignRepository
) : ViewModel() {

    val detail = MutableLiveData<SignDetailData>()
    val error = MutableLiveData<String>()

    /** 상세 조회 */
    fun loadDetail(id: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getSignDetail(id)
                detail.postValue(response.data)
            } catch (e: Exception) {
                error.postValue(e.message)
            }
        }
    }

    /**
     * ⭐ 상세 화면 즐겨찾기 토글
     * @param id - 수어 ID
     * @param newState - true(추가), false(삭제)
     * @param callback - UI 업데이트 여부 알려주는 콜백
     */
    fun toggleFavorite(id: Int, newState: Boolean, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                if (newState) {
                    repository.addFavorite(id)
                } else {
                    repository.removeFavorite(id)
                }

                // LiveData 데이터 업데이트
                val current = detail.value
                current?.favorite = newState
                detail.postValue(current)

                callback(true)
            } catch (e: Exception) {
                error.postValue(e.message)
                callback(false)
            }
        }
    }
}
