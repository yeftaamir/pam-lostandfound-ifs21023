package com.ifs21023.lostandfound.data.remote.response

import com.google.gson.annotations.SerializedName

data class DelcomLostfoundResponse(

	@field:SerializedName("data")
	val data: DataLostfoundResponse,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class LostFoundResponse(

	@field:SerializedName("cover")
	val cover: Any,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("author")
	val author: AuthorLostfoundResponse,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("is_completed")
	val isCompleted: Int,

	@field:SerializedName("status")
	val status: String
)

data class AuthorLostfoundResponse(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("photo")
	val photo: Any
)

data class DataLostfoundResponse(

	@field:SerializedName("lost_found")
	val lostFound: LostFoundResponse
)
