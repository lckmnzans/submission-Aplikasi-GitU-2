package com.dicoding.gitu.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.gitu.api.ApiConfig
import com.dicoding.gitu.response.Items
import com.dicoding.gitu.response.UserDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel: ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _userDetail = MutableLiveData<UserDetailResponse>()
    val userDetail: LiveData<UserDetailResponse> = _userDetail

    private val _userFollowsList = MutableLiveData<List<Items>>()
    val userFollowsList: LiveData<List<Items>> = _userFollowsList

    private val _userFollowers = MutableLiveData<String>()
    val userFollowers: LiveData<String> = _userFollowers

    private val _userFollowing = MutableLiveData<String>()
    val userFollowing: LiveData<String> = _userFollowing

    companion object {
        private const val TAG = "DetailActivity"
        var username = "username"
    }

    private fun getUserDetail(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object: Callback<UserDetailResponse>{
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _userDetail.value = responseBody
                        _userFollowers.value = responseBody.followers.toString()
                        _userFollowing.value = responseBody.following.toString()
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }
            }
            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getUserFollowsDetail(client: Call<List<Items>>) {
        client.enqueue(object: Callback<List<Items>> {
            override fun onResponse(call: Call<List<Items>>, response: Response<List<Items>>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _userFollowsList.value = responseBody
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<Items>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    init {
        getUserDetail(username)
    }
}