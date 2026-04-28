package com.intuit.catapp.data

import com.google.gson.annotations.SerializedName

data class Breed(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("reference_image_id") val referenceImageId: String?,
    @SerializedName("description") val description: String

) {
    fun getImageUrl(): String {
        return referenceImageId?.let { "https://cdn2.thecatapi.com/images/$it.jpg" } ?: ""
    }
}
