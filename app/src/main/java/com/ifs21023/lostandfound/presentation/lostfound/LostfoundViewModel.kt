package com.ifs21023.lostandfound.presentation.lostfound

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ifs21023.lostandfound.data.remote.MyResult
import com.ifs21023.lostandfound.data.remote.response.DataAddLostfoundResponse
import com.ifs21023.lostandfound.data.remote.response.DelcomLostfoundResponse
import com.ifs21023.lostandfound.data.remote.response.DelcomResponse
import com.ifs21023.lostandfound.data.repository.LostfoundRepository
import com.ifs21023.lostandfound.presentation.ViewModelFactory

class LostfoundViewModel(
    private val lostfoundRepository: LostfoundRepository
) : ViewModel() {

    fun getLostfound(lostfoundId: Int): LiveData<MyResult<DelcomLostfoundResponse>> {
        return lostfoundRepository.getLostfound(lostfoundId).asLiveData()
    }

    fun postLostfound(
        title: String,
        description: String,
        status: String,
    ): LiveData<MyResult<DataAddLostfoundResponse>> {
        return lostfoundRepository.postLostfound(
            title,
            description,
            status
        ).asLiveData()
    }

    fun putLostfound(
        lostfoundId: Int,
        title: String,
        description: String,
        isCompleted: Boolean,
    ): LiveData<MyResult<DelcomResponse>> {
        return LostfoundRepository.putLostfound(
            lostfoundId,
            title,
            description,
            isCompleted,
        ).asLiveData()
    }

    fun deleteLostfound(lostfoundId: Int): LiveData<MyResult<DelcomResponse>> {
        return lostfoundRepository.deleteLostfound(lostfoundId).asLiveData()
    }

    companion object {
        @Volatile
        private var INSTANCE: LostfoundViewModel? = null
        fun getInstance(
            lostfoundRepository: LostfoundRepository
        ): LostfoundViewModel {
            synchronized(ViewModelFactory::class.java) {
                INSTANCE = LostfoundViewModel(
                    lostfoundRepository
                )
            }
            return INSTANCE as LostfoundViewModel
        }
    }
}