package com.example.hearo2.mypage

import androidx.lifecycle.*
import com.example.hearo2.dictionary.model.SignItem
import com.example.hearo2.dictionary.repository.SignRepository
import kotlinx.coroutines.launch

class SignLikeViewModel(private val repository: SignRepository) : ViewModel() {

    private val _favoriteSigns = MutableLiveData<List<SignItem>>()
    val favoriteSigns: LiveData<List<SignItem>> get() = _favoriteSigns

    fun loadFavoriteSigns() {
        viewModelScope.launch {
            try {
                val response = repository.getFavoriteSigns()

                _favoriteSigns.value = response.data.content ?: emptyList()
            } catch (e: Exception) {
                _favoriteSigns.value = emptyList()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val repository: SignRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SignLikeViewModel(repository) as T
        }
    }
}
