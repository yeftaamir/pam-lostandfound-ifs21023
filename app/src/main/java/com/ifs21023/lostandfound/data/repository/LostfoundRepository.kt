package com.ifs21023.lostandfound.data.repository

import com.google.gson.Gson
import com.ifs21023.lostandfound.data.remote.MyResult
import com.ifs21023.lostandfound.data.remote.response.DelcomResponse
import com.ifs21023.lostandfound.data.remote.retrofit.IApiService
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class LostfoundRepository private constructor(
    private val apiService: IApiService,
) {
    fun postLostfound(
        title: String,
        description: String,
        status: String
    ) = flow {
        emit(MyResult.Loading)
        try {
            //get success message
            emit(
                MyResult.Success(
                    apiService.postLostfound(title, description, status).data
                )
            )
        } catch (e: HttpException) {
            //get error message
            val jsonInString = e.response()?.errorBody()?.string()
            emit(
                MyResult.Error(
                    Gson()
                        .fromJson(jsonInString, DelcomResponse::class.java)
                        .message
                )
            )
        }
    }

    fun putLostfound(
        lostfoundId: Int,
        title: String,
        description: String,
        isCompleted: Boolean,
    ) = flow {
        emit(MyResult.Loading)
        try {
            //get success message
            emit(
                MyResult.Success(
                    apiService.putLostfound(
                        lostfoundId,
                        title,
                        description,
                        if (isCompleted) 1 else 0
                    )
                )
            )
        } catch (e: HttpException) {
            //get error message
            val jsonInString = e.response()?.errorBody()?.string()
            emit(
                MyResult.Error(
                    Gson()
                        .fromJson(jsonInString, DelcomResponse::class.java)
                        .message
                )
            )
        }
    }

    fun getLostfounds(
        isCompleted: Int?,
        isMe: Int,
        status: Boolean,
    ) = flow {
        emit(MyResult.Loading)
        try {
            //get success message
            emit(MyResult.Success(apiService.getLostfounds(isCompleted, isMe, status)))
        } catch (e: HttpException) {
            //get error message
            val jsonInString = e.response()?.errorBody()?.string()
            emit(
                MyResult.Error(
                    Gson()
                        .fromJson(jsonInString, DelcomResponse::class.java)
                        .message
                )
            )
        }
    }

    fun getLostfound(
        lostfoundId: Int,
    ) = flow {
        emit(MyResult.Loading)
        try {
            //get success message
            emit(MyResult.Success(apiService.getLostfound(lostfoundId)))
        } catch (e: HttpException) {
            //get error message
            val jsonInString = e.response()?.errorBody()?.string()
            emit(
                MyResult.Error(
                    Gson()
                        .fromJson(jsonInString, DelcomResponse::class.java)
                        .message
                )
            )
        }
    }

    fun deleteLostfound(
        lostfoundId: Int,
    ) = flow {
        emit(MyResult.Loading)
        try {
//get success message
            emit(MyResult.Success(apiService.deleteLostfound(lostfoundId)))
        } catch (e: HttpException) {
            //get error message
            val jsonInString = e.response()?.errorBody()?.string()
            emit(
                MyResult.Error(
                    Gson()
                        .fromJson(jsonInString, DelcomResponse::class.java)
                        .message
                )
            )
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: LostfoundRepository? = null
        fun getInstance(
            apiService: IApiService,
        ): LostfoundRepository {
            synchronized(LostfoundRepository::class.java) {
                INSTANCE = LostfoundRepository(
                    apiService
                )
            }
            return INSTANCE as LostfoundRepository
        }
    }
}
