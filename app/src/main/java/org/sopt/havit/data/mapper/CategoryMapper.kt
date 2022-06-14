package org.sopt.havit.data.mapper

import org.sopt.havit.domain.entity.Category
import javax.inject.Inject

class CategoryMapper @Inject constructor() {
    fun toCategoryId(category: Category) = category.id
}
