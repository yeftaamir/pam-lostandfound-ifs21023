package com.ifs21023.lostandfound.presentation.lostfound

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ifs21023.lostandfound.data.remote.MyResult
import com.ifs21023.lostandfound.data.remote.response.DataAddLostfoundResponse
import com.ifs21023.lostandfound.data.remote.response.DelcomLostfoundResponse
import com.ifs21023.lostandfound.data.remote.response.DelcomLostfoundsResponse
import com.ifs21023.lostandfound.data.remote.response.DelcomResponse
import com.ifs21023.lostandfound.data.repository.LostfoundRepository
import com.ifs21023.lostandfound.presentation.ViewModelFactory

class LostfoundViewModel(
    private val lostfoundRepository: LostfoundRepository
) : ViewModel() {
    fun getLostfounds(
        isCompleted: Int?,
        userId: Int?,
        status: String,
    ): LiveData<MyResult<DelcomLostfoundsResponse>> {
        return lostfoundRepository.getLostfounds(isCompleted,userId,status, ).asLiveData()
    }
    fun getLostfound(lostFoundId: Int): LiveData<MyResult<DelcomLostfoundResponse>>{
        return lostfoundRepository.getLostfound(lostFoundId).asLiveData()
    }
    fun postLostfound(
        title: String,
        description: String,
        status: String,
    ): LiveData<MyResult<DataAddLostfoundResponse>>{
        return lostfoundRepository.postLostfound(
            title,
            description,
            status
        ).asLiveData()
    }
    fun putLostfound(
        lostFoundId: Int,
        title: String,
        description: String,
        status: String,
        isCompleted: Boolean,
    ): LiveData<MyResult<DelcomResponse>> {
        return lostfoundRepository.putLostfound(
            lostFoundId,
            title,
            description,
            status,
            isCompleted,
        ).asLiveData()
    }
    fun deleteLostfound(lostFoundId: Int): LiveData<MyResult<DelcomResponse>> {
        return lostfoundRepository.deleteLostfound(lostFoundId).asLiveData()
    }
    companion object {
        @Volatile
        private var INSTANCE: LostfoundViewModel? = null
        fun getInstance(
            lostRepository: LostfoundRepository
        ): LostfoundViewModel {
            synchronized(ViewModelFactory::class.java) {
                INSTANCE = LostfoundViewModel(
                    lostRepository
                )
            }
            return INSTANCE as LostfoundViewModel
        }
    }
}

