package com.example.hearo2.dictionary.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hearo2.dictionary.repository.SignRepository

class DictionaryViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repo = SignRepository(context)

        return when {
            modelClass.isAssignableFrom(DictionaryViewModel::class.java) ->
                DictionaryViewModel(repo) as T

            modelClass.isAssignableFrom(DictionaryDetailViewModel::class.java) ->
                DictionaryDetailViewModel(repo) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
