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
}
