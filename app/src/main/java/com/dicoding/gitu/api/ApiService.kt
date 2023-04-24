package com.dicoding.gitu.api

import com.dicoding.gitu.UserDetailResponse
import com.dicoding.gitu.GithubResponse
import com.dicoding.gitu.Items
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
    @Headers("Authorization: token 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")
    fun getListOfFollows(
        @Path("username") username: String,
        @Path("type") type: String
    ): Call<List<Items>>
}