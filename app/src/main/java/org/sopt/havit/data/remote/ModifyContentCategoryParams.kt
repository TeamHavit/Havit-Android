package org.sopt.havit.data.remote

data class ModifyContentCategoryParams(
    val contentId: Int,
    val newCategoryIds: List<Int>
)
