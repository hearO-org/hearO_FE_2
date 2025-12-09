package com.example.hearo2.dictionary.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hearo2.dictionary.model.SignItem
import com.example.hearo2.dictionary.repository.SignRepository
import kotlinx.coroutines.launch

class DictionaryViewModel(private val repo: SignRepository) : ViewModel() {

    val signList = MutableLiveData<List<SignItem>>()
    val error = MutableLiveData<String>()

    fun loadAllSigns() {
        viewModelScope.launch {
            try {
                val response = repo.getSigns(0, 100)
                signList.value = response.data.content
            } catch (e: Exception) {
                error.value = e.message
            }
        }
    }

    fun search(keyword: String) {
        viewModelScope.launch {
            try {
                val response = repo.searchSigns(keyword, 0, 100)
                signList.value = response.data.content
            } catch (e: Exception) {
                error.value = e.message
            }
        }
    }
}
