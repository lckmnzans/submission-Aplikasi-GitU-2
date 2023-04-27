package com.dicoding.gitu.api

import com.dicoding.gitu.response.UserDetailResponse
import com.dicoding.gitu.response.GithubResponse
import com.dicoding.gitu.response.Items
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getUser(
        @Query("q") q: String
    ): Call<GithubResponse>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<UserDetailResponse>


    @GET("users/{username}/{type}")
    @Headers("Authorization: token _")
    fun getListOfFollows(
        @Path("username") username: String,
        @Path("type") type: String,
        @Query("per_page") per_page: String,
    ): Call<List<Items>>
}