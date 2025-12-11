package com.example.hearo2.mypage

import android.content.Context
import androidx.lifecycle.*
import com.example.hearo2.auth.AuthPrefs
import com.example.hearo2.network.api.AuthService
import com.example.hearo2.network.api.MemberApi
import com.example.hearo2.network.api.RetrofitClient
import com.example.hearo2.network.request.LogoutRequest
import com.example.hearo2.network.request.MemberUpdateRequest
import com.example.hearo2.network.response.MemberInfo
import com.example.hearo2.network.response.MemberInfoResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class MyPageViewModel(private val context: Context) : ViewModel() {

    private val memberApi = RetrofitClient.getInstance(context).create(MemberApi::class.java)
    private val authApi = RetrofitClient.getInstance(context).create(AuthService::class.java)

    // ⭐ 내 정보 LiveData
    private val _userInfo = MutableLiveData<MemberInfo>()
    val userInfo: LiveData<MemberInfo> = _userInfo

    // ⭐ 로그아웃 결과
    private val _logoutState = MutableLiveData<Boolean>()
    val logoutState: LiveData<Boolean> = _logoutState

    // ⭐ 정보 수정 성공 여부
    private val _updateSuccess = MutableLiveData<Boolean>()
    val updateSuccess: LiveData<Boolean> = _updateSuccess

    init {
        loadMyInfo()
    }

    // -----------------------------------------
    // ⭐ 내 정보 조회 API
    // -----------------------------------------
    fun loadMyInfo() = viewModelScope.launch {
        try {
            val response = memberApi.getMyInfo()

            if (response.isSuccessful && response.body() != null) {
                _userInfo.postValue(response.body()!!.data)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    // -----------------------------------------
    // ⭐ 내 정보 수정 API
    // -----------------------------------------
    fun updateMyInfo(request: MemberUpdateRequest) = viewModelScope.launch {
        try {
            val response = memberApi.updateMyInfo(request)
            if (response.isSuccessful && response.body() != null) {
                _userInfo.postValue(response.body()!!.data)
                _updateSuccess.postValue(true)
            } else {
                _updateSuccess.postValue(false)
            }
        } catch (e: Exception) {
            _updateSuccess.postValue(false)
        }
    }

    // -----------------------------------------
    // ⭐ 로그아웃 API
    // -----------------------------------------
    fun logout() = viewModelScope.launch {
        try {
            val refresh = AuthPrefs.getRefresh(context)
            val request = LogoutRequest(refresh ?: "")

            val response: Response<Unit> = authApi.logout(request)

            if (response.isSuccessful) {
                AuthPrefs.clearTokens(context)
                _logoutState.postValue(true)
            } else {
                _logoutState.postValue(false)
            }
        } catch (e: Exception) {
            _logoutState.postValue(false)
        }
    }
}
