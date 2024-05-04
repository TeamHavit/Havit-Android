package org.sopt.havit.ui.model

data class CommunityCategoryRO(
    val id: Int,
    val name: String,
    var isSelected: Boolean = false,
)