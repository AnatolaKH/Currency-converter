package akh.data.db.model

import com.google.gson.annotations.SerializedName

class RateDB(
    @SerializedName("countryCode") val countryCode: String,
    @SerializedName("countryName") val countryName: String,
    @SerializedName("rate") val rate: Double,
    @SerializedName("countryFlag") val countryFlag: Int,
    @SerializedName("exchange") var exchange: String,
    @SerializedName("isBase") var isBase: Boolean = false
)