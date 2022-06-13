package org.sopt.havit.data.remote

import com.google.gson.annotations.SerializedName

data class ModifyTitleParams(
    @SerializedName("newTitle")
    val newTitle: String
)
