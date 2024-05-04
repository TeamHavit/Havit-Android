package org.sopt.havit.util.havit_edit_text

import androidx.lifecycle.MutableLiveData


data class EditTextData(
    val text: MutableLiveData<String>,
    val maxLength: Int,
    val hint: String,
    var infoStatus: MutableLiveData<EditTextInfoType> = MutableLiveData(EditTextInfoType.NONE),
    val wordCountVisibility: Boolean = true,
)
