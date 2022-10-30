package org.sopt.havit.data.remote

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.sopt.havit.domain.entity.CategoryWithSelected

data class CategoryResponse(
    val data: List<CategoryWithSelected>,
    val message: String,
    val status: Int,
    val success: Boolean
) {
    @Parcelize
    data class AllCategoryData(
        val contentNumber: Int,
        val id: Int,
        var imageId: Int,
        @SerializedName("imageUrl")
        var url: String,
        val orderIndex: Int,
        var title: String
    ) : Parcelable
}
