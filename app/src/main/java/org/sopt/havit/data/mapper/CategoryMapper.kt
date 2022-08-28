package org.sopt.havit.data.mapper

import org.sopt.havit.domain.entity.CategoryWithSelected
import javax.inject.Inject

class CategoryMapper @Inject constructor() {
    fun toCategoryId(category: CategoryWithSelected) = category.id
}
