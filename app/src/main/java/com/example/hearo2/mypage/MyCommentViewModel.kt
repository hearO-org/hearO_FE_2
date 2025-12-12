package com.example.hearo2.mypage

import androidx.lifecycle.*
import com.example.hearo2.community.CommentDto
import com.example.hearo2.network.api.CommunityRetrofitClient
import kotlinx.coroutines.launch

class MyCommentViewModel : ViewModel() {

    private val api = CommunityRetrofitClient.communityApi

    private val _comments = MutableLiveData<List<CommentDto>>()
    val comments: LiveData<List<CommentDto>> = _comments

    fun loadMyComments() {
        viewModelScope.launch {
            runCatching {
                api.getMyComments()
            }.onSuccess {
                _comments.value = it.data.content
            }.onFailure {
                _comments.value = emptyList()
            }
        }
    }
}