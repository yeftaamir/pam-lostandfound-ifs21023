package com.ifs21023.lostandfound.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DelcomLostfound(
    val id: Int,
    val title: String,
    val description: String,
    var isCompleted: Boolean,
    var isMe: Int,
    val cover: String?,
    var status: Boolean
) : Parcelable