package com.ifs21023.lostandfound.data.remote.response

import com.google.gson.annotations.SerializedName

data class DelcomLostfoundsResponse(

	@field:SerializedName("data")
	val data: DataLostfoundsResponse,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class DataLostfoundsResponse(

	@field:SerializedName("lost_founds")
	val lostFounds: List<LostfoundsItemResponse>
)

data class LostfoundsItemResponse(

	@field:SerializedName("cover")
	val cover: String,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("author")
	val author: AuthorLostfoundsResponse,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("is_completed")
	var isCompleted: Int,

	@field:SerializedName("status")
	val status: String
)

data class AuthorLostfoundsResponse(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("photo")
	val photo: Any
)
