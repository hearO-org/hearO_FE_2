package com.example.hearo2.custominfo.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hearo2.custominfo.model.JobDetailData
import com.example.hearo2.custominfo.model.JobItem
import com.example.hearo2.custominfo.repository.JobRepository
import kotlinx.coroutines.launch

class JobViewModel : ViewModel() {

    private val repo = JobRepository()

    val jobListState = MutableLiveData<UiState<List<JobItem>>>()
    val jobDetailState = MutableLiveData<UiState<JobDetailData>>()

    fun loadJobList(context: Context) {
        jobListState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val result = repo.getJobList(context)
                jobListState.value = UiState.Success(result)
            } catch (e: Exception) {
                jobListState.value = UiState.Error(e.message ?: "오류 발생")
            }
        }
    }

    fun searchJobs(context: Context, filters: Map<String, String>) {
        jobListState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val result = repo.searchJobs(context, filters)
                jobListState.value = UiState.Success(result)
            } catch (e: Exception) {
                jobListState.value = UiState.Error(e.message ?: "오류 발생")
            }
        }
    }

    fun loadJobDetail(context: Context, rno: String) {
        jobDetailState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val result = repo.getJobDetail(context, rno)
                jobDetailState.value = UiState.Success(result)
            } catch (e: Exception) {
                jobDetailState.value = UiState.Error(e.message ?: "오류 발생")
            }
        }
    }
}
