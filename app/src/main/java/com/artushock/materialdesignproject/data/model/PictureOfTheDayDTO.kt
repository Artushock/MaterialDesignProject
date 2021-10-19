package com.artushock.materialdesignproject.data.model

import com.google.gson.annotations.SerializedName

data class PictureOfTheDayDTO(
    @field:SerializedName("date") val date: String?,
    @field:SerializedName("explanation") val explanation: String?,
    @field:SerializedName("media_type") val media_type: String?,
    @field:SerializedName("service_version") val service_version: String?,
    @field:SerializedName("title") val title: String?,
    @field:SerializedName("url") val url: String?,
    @field:SerializedName("hdurl") val hdurl: String?,
)