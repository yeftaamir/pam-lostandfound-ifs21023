package com.ifs21023.lostandfound.data.remote.response

import com.google.gson.annotations.SerializedName

data class DelcomAddLostfoundResponse(

	@field:SerializedName("data")
	val data: DataAddLostfoundResponse,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class DataAddLostfoundResponse(

	@field:SerializedName("lost_found_id")
	val lostFoundId: Int
)
