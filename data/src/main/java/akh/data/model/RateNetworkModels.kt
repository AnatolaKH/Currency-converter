package akh.data.model

import com.google.gson.annotations.SerializedName

class RatesResponseModel(
    @SerializedName("base") val base: String?,
    @SerializedName("date") val date: String?,
    @SerializedName("rates") val rates: Map<String?, Double?>?
)