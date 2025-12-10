package com.example.hearo2.dictionary.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hearo2.dictionary.model.SignItem
import com.example.hearo2.dictionary.repository.SignRepository
import kotlinx.coroutines.launch

class DictionaryViewModel(
    private val repo: SignRepository
) : ViewModel() {

    val signList = MutableLiveData<List<SignItem>>()
    val error = MutableLiveData<String>()

    // 즐겨찾기 id 들을 기억하는 Set (목록/상세 동기화)
    private val favoriteIdSet = mutableSetOf<Int>()


    /** 전체 수어 목록 조회 */
    fun loadAllSigns() {
        viewModelScope.launch {
            try {
                val response = repo.getSigns(page = 0, size = 100)
                val list = response.data.content.toMutableList()

                refreshFavorites()          // 내 즐겨찾기 목록 불러오기
                applyFavoriteState(list)    // 즐겨찾기 표시 반영

                signList.value = list
            } catch (e: Exception) {
                error.value = e.message
            }
        }
    }

    /** 검색 */
    fun search(keyword: String) {
        viewModelScope.launch {
            try {
                val response = repo.searchSigns(keyword, page = 0, rows = 100)
                val list = response.data.content.toMutableList()

                applyFavoriteState(list)    // 검색결과에도 즐겨찾기 반영

                signList.value = list
            } catch (e: Exception) {
                error.value = e.message
            }
        }
    }

    /** 목록 화면에서 하트 눌렀을 때 */
    fun toggleFavoriteFromList(item: SignItem) {
        viewModelScope.launch {
            try {
                val nowFav = item.favorite ?: false

                if (nowFav) {
                    repo.removeFavorite(item.id)
                    item.favorite = false
                    favoriteIdSet.remove(item.id)
                } else {
                    repo.addFavorite(item.id)
                    item.favorite = true
                    favoriteIdSet.add(item.id)
                }

                updateItemInList(item.id, item.favorite ?: false)

            } catch (e: Exception) {
                error.value = e.message
            }
        }
    }



    /** 상세 화면에서 변경된 favorite 상태를 목록에 반영 */
    fun updateFavoriteStateFromDetail(signId: Int, isFavorite: Boolean) {
        if (isFavorite) favoriteIdSet.add(signId) else favoriteIdSet.remove(signId)
        updateItemInList(signId, isFavorite)
    }


    // --------------------------------------------------
    // 내부 헬퍼 함수들
    // --------------------------------------------------

    private suspend fun refreshFavorites() {
        try {
            val favResponse = repo.getFavoriteSigns()
            favoriteIdSet.clear()
            favoriteIdSet.addAll(favResponse.data.content.map { it.id })
        } catch (_: Exception) { }
    }

    private fun applyFavoriteState(list: MutableList<SignItem>) {
        list.forEach { item ->
            item.favorite = favoriteIdSet.contains(item.id)
        }
    }

    private fun updateItemInList(signId: Int, isFavorite: Boolean) {
        val current = signList.value?.toMutableList() ?: return
        val index = current.indexOfFirst { it.id == signId }
        if (index != -1) {
            current[index].favorite = isFavorite
            signList.value = current
        }
    }
}
