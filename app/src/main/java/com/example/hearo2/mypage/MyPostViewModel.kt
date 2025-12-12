package com.example.hearo2.mypage.viewmodel

import androidx.lifecycle.*
import com.example.hearo2.community.PostModel
import com.example.hearo2.network.api.CommunityRetrofitClient
import kotlinx.coroutines.launch

class MyPostViewModel : ViewModel() {

    private val api = CommunityRetrofitClient.communityApi

    private val _posts = MutableLiveData<List<PostModel>>()
    val posts: LiveData<List<PostModel>> = _posts

    // ✅ 내가 작성한 게시글
    fun loadMyPosts() {
        viewModelScope.launch {
            try {
                val response = api.getMyPosts()
                _posts.value = response.data.content
            } catch (e: Exception) {
                _posts.value = emptyList()
            }
        }
    }

    // ✅ 내가 스크랩한 게시글
    fun loadScrappedPosts() {
        viewModelScope.launch {
            try {
                val response = api.getScrappedPosts()
                _posts.value = response.data.content
            } catch (e: Exception) {
                _posts.value = emptyList()
            }
        }
    }
}
