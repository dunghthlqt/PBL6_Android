package com.demo.pbl6_android.data.model

import com.google.gson.annotations.SerializedName

data class ProvinceJson(
    @SerializedName("name")
    val name: String,
    @SerializedName("districts")
    val districts: List<DistrictJson>
)

data class DistrictJson(
    @SerializedName("name")
    val name: String,
    @SerializedName("wards")
    val wards: List<WardJson>
)

data class WardJson(
    @SerializedName("name")
    val name: String
)

