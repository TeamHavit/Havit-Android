package org.sopt.havit.data.remote

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ContentsMoreData(
    val id: Int,
    val image: String,
    val title: String,
    val createdAt: String,
    val url: String,
    val isNotified: Boolean,
    val notificationTime: String,
) : Parcelable
