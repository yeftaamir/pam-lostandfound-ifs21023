package com.ifs21023.lostandfound.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DelcomLostfound(
    val id: Int,
    val userId: Int,
    val title: String,
    val description: String,
    val status: String,
    var isCompleted: Boolean,
) : Parcelable