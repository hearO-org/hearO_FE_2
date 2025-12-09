package com.example.hearo2.mypage

import android.content.Context
import androidx.lifecycle.*
import com.example.hearo2.auth.AuthPrefs
import com.example.hearo2.network.api.AuthService
import com.example.hearo2.network.api.RetrofitClient
import com.example.hearo2.network.request.LogoutRequest
import kotlinx.coroutines.launch
import retrofit2.Response

class MyPageViewModel(private val context: Context) : ViewModel() {

    private val authService: AuthService =
        RetrofitClient.getInstance(context).create(AuthService::class.java)

    private val _logoutState = MutableLiveData<Boolean>()
    val logoutState: LiveData<Boolean> = _logoutState

    fun logout() = viewModelScope.launch {
        try {
            val refresh = AuthPrefs.getRefresh(context)
            val request = LogoutRequest(refresh ?: "")

            val response: Response<Unit> = authService.logout(request)

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
