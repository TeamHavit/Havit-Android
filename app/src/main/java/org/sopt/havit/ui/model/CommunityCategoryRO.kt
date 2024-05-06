package org.sopt.havit.ui.model

import org.sopt.havit.domain.entity.CommunityCategory

data class CommunityCategoryRO(
    val id: Int,
    val name: String,
    var isSelected: Boolean = false,
)

fun CommunityCategory.toRO() = CommunityCategoryRO(
    id = id,
    name = name,
    isSelected = false,
)