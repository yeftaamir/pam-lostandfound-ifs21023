package com.ifs21023.lostandfound.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ifs21023.lostandfound.data.pref.UserModel
import com.ifs21023.lostandfound.data.remote.MyResult
import com.ifs21023.lostandfound.data.remote.response.DelcomLostfoundResponse
import com.ifs21023.lostandfound.data.remote.response.DelcomResponse
import com.ifs21023.lostandfound.data.repository.AuthRepository
import com.ifs21023.lostandfound.data.repository.LostfoundRepository
import com.ifs21023.lostandfound.presentation.ViewModelFactory
import kotlinx.coroutines.launch

class MainViewModel(
    private val authRepository: AuthRepository,
    private val lostfoundRepository: LostfoundRepository
) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return authRepository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

    fun getLostfound(lostFoundId: Int): LiveData<MyResult<DelcomLostfoundResponse>> {
        return lostfoundRepository.getLostfound(lostFoundId).asLiveData()
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

    companion object {
        @Volatile
        private var INSTANCE: MainViewModel? = null
        fun getInstance(
            authRepository: AuthRepository,
            lostfoundRepository: LostfoundRepository
        ): MainViewModel {
            synchronized(ViewModelFactory::class.java) {
                INSTANCE = MainViewModel(
                    authRepository,
                    lostfoundRepository
                )
            }
            return INSTANCE as MainViewModel
        }
    }
}