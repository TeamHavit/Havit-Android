package org.sopt.havit.ui.community

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.sopt.havit.util.havit_edit_text.EditTextData
import org.sopt.havit.util.havit_edit_text.EditTextInfoType
import javax.inject.Inject

class CreatePostViewModel @Inject constructor() : ViewModel() {

    val urlEditTextData = MutableLiveData(
        EditTextData(
            text = MutableLiveData(""),
            infoStatus = MutableLiveData(EditTextInfoType.NONE),
            maxLength = 1000,
            hint = "소개하고 싶은 링크를 공유해보세요."
        )
    )

}