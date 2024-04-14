package com.ifs21023.lostandfound.data.remote.retrofit

import com.ifs21023.lostandfound.data.remote.response.DelcomAddLostfoundResponse
import com.ifs21023.lostandfound.data.remote.response.DelcomLoginResponse
import com.ifs21023.lostandfound.data.remote.response.DelcomLostfoundResponse
import com.ifs21023.lostandfound.data.remote.response.DelcomLostfoundsResponse
import com.ifs21023.lostandfound.data.remote.response.DelcomResponse
import com.ifs21023.lostandfound.data.remote.response.DelcomUserResponse
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface IApiService {
    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): DelcomResponse

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): DelcomLoginResponse

    @GET("users/me")
    suspend fun getMe(): DelcomUserResponse

    @FormUrlEncoded
    @POST("lost-founds")
    suspend fun postLostfound(
        @Field("title") title: String,
        @Field("description") description: String,
        @Field("status") status: String,
    ): DelcomAddLostfoundResponse

    @FormUrlEncoded
    @PUT("lost-founds/{id}")
    suspend fun putLostfound(
        @Path("id") lostFoundId: Int,
        @Field("title") title: String,
        @Field("description") description: String,
        @Field("status") status: String,
        @Field("is_completed") isCompleted: Int,
    ): DelcomResponse

    @GET("lost-founds")
    suspend fun getLostfounds(
        @Query("is_completed") isCompleted: Int?,
        @Query("is_me") userId : Int?,
        @Query("status") status: String,
    ): DelcomLostfoundsResponse

    @GET("lost-founds/{id}")
    suspend fun getLostfound(
        @Path("id") lostFoundId: Int,
    ): DelcomLostfoundResponse

    @DELETE("lost-founds/{id}")
    suspend fun deleteLostfound(
        @Path("id") lostFoundId: Int,
    ): DelcomResponse
}