package org.sopt.havit.domain.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
    val contentNumber: Int,
    val id: Int,
    var imageId: Int,
    @SerializedName("imageUrl")
    var url: String,
    var orderIndex: Int,
    var title: String
) : Parcelable
