package org.sopt.havit.domain.entity

data class Category(
    val id: Int,
    val title: String,
    val orderIndex: Int,
    val imageId: Int,
    val imageUrl: String,
    val contentNumber: Int,
    var isSelected: Boolean = true
)
