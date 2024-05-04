package org.sopt.havit.util

import android.content.res.ColorStateList
import androidx.core.content.ContextCompat.getColor
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import org.sopt.havit.R
import org.sopt.havit.ui.model.CommunityCategoryRO

class CommunityCategoryChipGroup(
    private val chipGroup: ChipGroup,
    private val category: List<CommunityCategoryRO>,
) {
    private var _selectedCategory = MutableLiveData(mutableListOf<CommunityCategoryRO>())
    val selectedCategory: LiveData<MutableList<CommunityCategoryRO>> = _selectedCategory

    private val states = arrayOf(
        intArrayOf(android.R.attr.state_selected),
        intArrayOf(-android.R.attr.state_selected)
    )

    private val backgroundStateList = getColorStateList(R.color.havit_gray, R.color.white_gray)
    private val textStateList = getColorStateList(R.color.white, R.color.gray_3)

    init {
        setChipGroup()
        setChipSpacing()
    }

    private fun setChipGroup() {
        category.forEach {
            val chip = createChip(it)
            chipGroup.addView(chip)
        }
    }

    private fun setChipSpacing() {
        chipGroup.apply {
            chipSpacingHorizontal = resources.getDimension(R.dimen.chip_spacing_horizontal).toInt()
            chipSpacingVertical = resources.getDimension(R.dimen.chip_spacing_horizontal).toInt()
        }
    }

    private fun createChip(communityCategoryRO: CommunityCategoryRO): Chip {
        return Chip(chipGroup.context).apply {
            setEnsureMinTouchTargetSize(false)
            text = communityCategoryRO.name
            chipStartPadding = resources.getDimension(R.dimen.chip_padding_horizontal)
            chipEndPadding = resources.getDimension(R.dimen.chip_padding_horizontal)
            chipMinHeight = resources.getDimension(R.dimen.chip_min_height)
            chipBackgroundColor = backgroundStateList
            setTextAppearance(R.style.Text12Semibold)
            setTextColor(textStateList) // (순서 중요) textAppearance 후에 배치
            setOnClickListener {
                toggleChipSelected(this)
                updateSelectedCategory(communityCategoryRO, this.isSelected)
                copySelectedCategoryList()
            }
        }
    }

    private fun toggleChipSelected(chip: Chip) {
        chip.isSelected = !chip.isSelected
    }

    private fun updateSelectedCategory(
        communityCategoryRO: CommunityCategoryRO,
        isSelected: Boolean,
    ) {
        if (isSelected) {
            _selectedCategory.value?.add(communityCategoryRO)
        } else {
            _selectedCategory.value?.remove(communityCategoryRO)
        }
    }

    private fun copySelectedCategoryList() {
        _selectedCategory.value = _selectedCategory.value?.toMutableList()
    }

    private fun getColorIntArray(selectedColor: Int, unSelectedColor: Int): IntArray {
        val context = chipGroup.context
        return intArrayOf(getColor(context, selectedColor), getColor(context, unSelectedColor))
    }

    private fun getColorStateList(selectedColor: Int, unSelectedColor: Int): ColorStateList {
        val colorIntArray = getColorIntArray(selectedColor, unSelectedColor)
        return ColorStateList(states, colorIntArray)
    }
}